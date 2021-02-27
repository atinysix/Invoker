package com.daiwj.invoker;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.daiwj.invoker.annotation.Creator;
import com.daiwj.invoker.call.okhttp3.OkHttpCallFactory;
import com.daiwj.invoker.call.okhttp3.OkHttpFailureFactory;
import com.daiwj.invoker.lifecycle.LifecycleOwnerManager;
import com.daiwj.invoker.parser.GsonParserFactory;
import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.ResultExecutor;
import com.daiwj.invoker.runtime.DataParser;
import com.daiwj.invoker.runtime.InvokerCreator;
import com.daiwj.invoker.runtime.InvokerLog;
import com.daiwj.invoker.runtime.InvokerUtil;
import com.daiwj.invoker.runtime.IFailure;
import com.daiwj.invoker.runtime.MethodVisitor;
import com.daiwj.invoker.runtime.Parser;
import com.daiwj.invoker.runtime.ISource;
import com.daiwj.invoker.runtime.SourceFactory;
import com.daiwj.invoker.runtime.StringParser;
import com.daiwj.invoker.runtime.UiThreadResultExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: daiwj on 2020/12/2 21:43
 */
@SuppressWarnings("unchecked")
public final class Invoker {

    private boolean mDebug;

    private final Map<Method, MethodVisitor<?>> mMethodCache = new ConcurrentHashMap<>();

    private String mBaseUrl;
    private Call.CallFactory mCallFactory;
    private SourceFactory<? extends ISource> mSourceFactory;
    private Parser.Factory mParserFactory;
    private DataParser mDataParser;
    private StringParser mStringParser;
    private IFailure.Factory mFailureFactory;

    private ResultExecutor mResultExecutor = new UiThreadResultExecutor();

    private LifecycleOwnerManager mOwnerManager = new LifecycleOwnerManager();

    public static <T> T invoke(Class<T> c) {
        if (!c.isInterface()) {
            InvokerUtil.error("cannot provide invoker api for a class type: " + c.getName());
        }
        return (T) ApiProvider.provide(c);
    }

    private <T> T create(Class<T> c) {
        return (T) Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[]{c}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
                MethodVisitor<?> visitor = createMethodVisitor(method, args);
                return visitor.createCaller();
            }

        });
    }

    private Invoker(Builder builder) {
        mDebug = builder.mDebug;
        mBaseUrl = builder.mBaseUrl;
        mCallFactory = builder.mCallFactory;
        mSourceFactory = builder.mSourceFactory;
        mParserFactory = builder.mParserFactory;
        mDataParser = builder.mParserFactory.dataParser();
        mStringParser = builder.mParserFactory.stringParser();
        mFailureFactory = builder.mFailureFactory;

        InvokerLog.setDebug(mDebug);
    }

    public boolean isDebug() {
        return mDebug;
    }

    public String getBaseUrl() {
        return mBaseUrl == null ? "" : mBaseUrl;
    }

    public Call.CallFactory getCallFactory() {
        return mCallFactory;
    }

    public SourceFactory<? extends ISource> getSourceFactory() {
        return mSourceFactory;
    }

    public Parser.Factory getParserFactory() {
        return mParserFactory;
    }

    public DataParser getDataParser() {
        return mDataParser;
    }

    public StringParser getStringParser() {
        return mStringParser;
    }

    public IFailure.Factory getFailureFactory() {
        return mFailureFactory;
    }

    public ResultExecutor getResultExecutor() {
        return mResultExecutor;
    }

    public LifecycleOwnerManager getLifecycleOwnerManager() {
        return mOwnerManager;
    }

    public static final class Builder {
        private boolean mDebug;
        private String mBaseUrl;
        private Call.CallFactory mCallFactory = new OkHttpCallFactory();
        private SourceFactory<? extends ISource> mSourceFactory;
        private Parser.Factory mParserFactory = new GsonParserFactory();
        private IFailure.Factory mFailureFactory = OkHttpFailureFactory.DEFAULT;

        public Builder debug(boolean debug) {
            mDebug = debug;
            return this;
        }

        public Builder baseUrl(String url) {
            mBaseUrl = url;
            return this;
        }

        public Builder callFactory(Call.CallFactory callFactory) {
            mCallFactory = callFactory;
            return this;
        }

        public Builder sourceFactory(SourceFactory<? extends ISource> factory) {
            mSourceFactory = factory;
            return this;
        }

        public Builder parserFactory(Parser.Factory factory) {
            mParserFactory = factory;
            return this;
        }

        public Builder failureFactory(IFailure.Factory factory) {
            mFailureFactory = factory;
            return this;
        }

        public Invoker build() {
            return new Invoker(this);
        }
    }

    private MethodVisitor<?> createMethodVisitor(Method method, Object[] args) {
        MethodVisitor<?> visitor = mMethodCache.get(method);
        if (visitor == null) {
            visitor = new MethodVisitor.Builder<>(this, method, args).build();
            mMethodCache.put(method, visitor);
        } else {
            visitor.setArgs(args);
        }
        return visitor;
    }

    private static class ApiProvider {

        final static Map<String, Invoker> mFactoryMap = new HashMap<>();
        final static Map<String, Object> mApiMap = new HashMap<>();

        static <Api> Api provide(Class<Api> c) {
            final Creator creator = c.getAnnotation(Creator.class);
            InvokerUtil.checkNull(creator, "@Creator not found on class: " + c.getName());

            final Class<? extends InvokerCreator> creatorClass = creator.value();

            String apiName = creator.name();
            if (TextUtils.isEmpty(apiName)) {
                apiName = c.getName();
            }
            Object api = mApiMap.get(apiName);
            if (api == null) {
                api = create(c, creatorClass);
                mApiMap.put(apiName, api);
            }
            return (Api) api;
        }

        static <Api> Api create(Class<Api> c, Class<? extends InvokerCreator> factoryClass) {
            try {
                final String factoryClassName = factoryClass.getName();
                Invoker client = mFactoryMap.get(factoryClassName);
                if (client == null) {
                    InvokerCreator factory = factoryClass.newInstance();
                    client = factory.create();
                    mFactoryMap.put(factoryClassName, client);
                }
                return client.create(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
