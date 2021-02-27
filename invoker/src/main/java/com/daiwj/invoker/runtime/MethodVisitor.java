package com.daiwj.invoker.runtime;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.annotation.FileParam;
import com.daiwj.invoker.annotation.Get;
import com.daiwj.invoker.annotation.Header;
import com.daiwj.invoker.annotation.Host;
import com.daiwj.invoker.annotation.Param;
import com.daiwj.invoker.annotation.ParamMap;
import com.daiwj.invoker.annotation.Post;
import com.daiwj.invoker.annotation.Source;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * author: daiwj on 2020/12/3 10:44
 */
public final class MethodVisitor<Data> {

    private Invoker mClient;

    protected Method mMethod;
    protected String mApiName;
    protected Class<?> mCallerType;
    protected Class<? extends ISource> mSourceType;
    protected Type mDataType;

    protected String mHttpMethod = "GET";
    protected String mBaseUrl;
    protected String mRelativeUrl;
    protected boolean mJsonBody;
    protected Map<String, String> mHeaders;
    protected Annotation[] mMethodAnnotations;
    protected Annotation[][] mParamsAnnotations;
    protected Object[] mArgs;
    private List<RequestParam> mRequestParams;
    private List<FilePart> mFileParts;

    public Invoker getClient() {
        return mClient;
    }

    public Method getMethod() {
        return mMethod;
    }

    private void setMethod(Method method) {
        mMethod = method;

        final Host host = method.getDeclaringClass().getAnnotation(Host.class);
        if (host != null) {
            IMethodAnnotationHandler.HOST.handle(host, this);
        }

        final Source source = method.getDeclaringClass().getAnnotation(Source.class);
        if (source != null) {
            IMethodAnnotationHandler.SOURCE.handle(source, this);
        }

        mApiName = method.getDeclaringClass().getSimpleName();

        mMethodAnnotations = method.getAnnotations();
        mParamsAnnotations = method.getParameterAnnotations();

        for (Annotation a : mMethodAnnotations) {
            if (a instanceof Host) {
                IMethodAnnotationHandler.HOST.handle(a, this);
            } else if (a instanceof Source) {
                IMethodAnnotationHandler.SOURCE.handle(a, this);
            } else if (a instanceof Get) {
                IMethodAnnotationHandler.GET.handle(a, this);
            } else if (a instanceof Post) {
                IMethodAnnotationHandler.POST.handle(a, this);
            } else if (a instanceof Header) {
                IMethodAnnotationHandler.HEADER.handle(a, this);
            }
        }

        mCallerType = method.getReturnType();
        mDataType = InvokerUtil.getActualGenericType(method.getGenericReturnType());
    }

    public String getApiName() {
        return mApiName == null ? "" : mApiName;
    }

    public String getHttpMethod() {
        return mHttpMethod == null ? "" : mHttpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        mHttpMethod = httpMethod;
    }

    public String getBaseUrl() {
        return mBaseUrl == null ? "" : mBaseUrl;
    }

    public String getRelativeUrl() {
        return mRelativeUrl == null ? "" : mRelativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        mRelativeUrl = relativeUrl;
    }

    public boolean isJsonBody() {
        return mJsonBody;
    }

    public Map<String, String> getHeaders() {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        return mHeaders;
    }

    public Class<? extends ISource> getSourceType() {
        return mSourceType;
    }

    public Type getDataType() {
        return mDataType;
    }

    public void setArgs(Object[] args) {
        mArgs = args;

        mRequestParams = new LinkedList<>();
        mFileParts = new LinkedList<>();

        final int paramsCount = mParamsAnnotations.length;
        for (int i = 0; i < paramsCount; i++) {
            boolean containsParam = false;
            final Annotation[] annotations = mParamsAnnotations[i];
            for (Annotation a : annotations) {
                if (a instanceof Param) {
                    IParamAnnotationHandler.PARAM.handle(a, args[i], mRequestParams);
                    containsParam = true;
                } else if (a instanceof ParamMap) {
                    IParamAnnotationHandler.PARAM_MAP.handle(a, args[i], mRequestParams);
                    containsParam = true;
                } else if (a instanceof FileParam) {
                    if ("GET".equals(mHttpMethod)) {
                        InvokerUtil.error("@File is not supported by @Get in method: " + mMethod.getName());
                    }
                    IParamAnnotationHandler.FILE.handle(a, args[i], mFileParts);
                    containsParam = true;
                }
            }
            if (!containsParam) {
                InvokerUtil.error("no parameter annotation was found in method: " + mMethod.getName());
            }
        }
    }

    public List<RequestParam> getRequestParams() {
        return mRequestParams;
    }

    public List<FilePart> getFileParts() {
        return mFileParts;
    }

    public Caller<Data> createCaller() throws Exception {
        Class<?> c = Class.forName(mCallerType.getName());
        if (Modifier.isInterface(c.getModifiers())) {
            InvokerUtil.error("Caller cannot be a interface: " + c.getName());
        }
        if (Modifier.isAbstract(c.getModifiers())) {
            InvokerUtil.error("Caller cannot be a abstract class: " + c.getName());
        }
        Constructor<?> constructor = c.getConstructor(MethodVisitor.class);
        return (Caller<Data>) constructor.newInstance(this);
    }

    protected <T> MethodVisitor<T> copy() {
        return new Copy(this).copy();
    }

    protected <T> MethodVisitor<T> convert(Class<?> type) {
        return new Copy(this, type).copy();
    }

    public static class Builder<Data> {
        private Invoker mClient;
        private Method mMethod;
        private Object[] mArgs;

        public Builder(Invoker client, Method method, Object[] args) {
            mClient = client;
            mMethod = method;
            mArgs = args;
        }

        public MethodVisitor<Data> build() {
            final MethodVisitor<Data> visitor = new MethodVisitor<>();

            visitor.mClient = mClient;

            visitor.setMethod(mMethod);
            visitor.setArgs(mArgs);

            return visitor;
        }

    }

    private static class Copy {
        private MethodVisitor<?> mOrigin;
        private Class<?> mTargetClassType;
        private Type mTargetDataType;

        public Copy(MethodVisitor<?> origin) {
            mOrigin = origin;
            mTargetClassType = origin.mCallerType;
            mTargetDataType = origin.mDataType;
        }

        public Copy(MethodVisitor<?> origin, Class<?> type) {
            mOrigin = origin;
            mTargetClassType = type;
            mTargetDataType = InvokerUtil.getActualGenericType(type);
        }

        public <T> MethodVisitor<T> copy() {
            final MethodVisitor<T> visitor = new MethodVisitor<>();

            visitor.mClient = mOrigin.mClient;

            visitor.mMethod = mOrigin.mMethod;
            visitor.mApiName = mOrigin.mApiName;
            visitor.mCallerType = mTargetClassType;
            visitor.mSourceType = mOrigin.mSourceType;
            visitor.mDataType = mTargetDataType;

            visitor.mHttpMethod = mOrigin.mHttpMethod;
            visitor.mBaseUrl = mOrigin.mBaseUrl;
            visitor.mRelativeUrl = mOrigin.mRelativeUrl;
            visitor.mJsonBody = mOrigin.mJsonBody;
            visitor.mHeaders = mOrigin.mHeaders;
            visitor.mMethodAnnotations = mOrigin.mMethodAnnotations;
            visitor.mParamsAnnotations = mOrigin.mParamsAnnotations;
            visitor.mArgs = mOrigin.mArgs;
            visitor.mRequestParams = mOrigin.mRequestParams;
            visitor.mFileParts = mOrigin.mFileParts;

            return visitor;
        }
    }

}
