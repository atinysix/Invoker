package com.daiwj.invoker;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.daiwj.invoker.annotation.Factory;
import com.daiwj.invoker.lifecycle.LifecycleOwnerManager;
import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.CallbackExecutor;
import com.daiwj.invoker.runtime.DataParser;
import com.daiwj.invoker.runtime.InvokerFactory;
import com.daiwj.invoker.runtime.InvokerLog;
import com.daiwj.invoker.runtime.InvokerUtil;
import com.daiwj.invoker.runtime.IFailure;
import com.daiwj.invoker.runtime.MethodVisitor;
import com.daiwj.invoker.runtime.Parser;
import com.daiwj.invoker.runtime.ISource;
import com.daiwj.invoker.runtime.SourceConverter;
import com.daiwj.invoker.runtime.StringParser;
import com.daiwj.invoker.runtime.UiThreadCallbackExecutor;

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
    private SourceConverter<? extends ISource> mSourceConverter;
    private Parser.Factory mParserFactory;
    private DataParser mDataParser;
    private StringParser mStringParser;
    private IFailure.Factory mFailureFactory;

    private CallbackExecutor mCallbackExecutor = new UiThreadCallbackExecutor();

    private LifecycleOwnerManager mOwnerManager = new LifecycleOwnerManager();

    public static <T> T provide(Class<T> c) {
        if (!c.isInterface()) {
            InvokerUtil.error("cannot provide invoker api for a class type: " + c.getCanonicalName());
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
        mSourceConverter = builder.mSourceConverter;
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

    public SourceConverter<? extends ISource> getSourceConverter() {
        return mSourceConverter;
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

    public CallbackExecutor getCallbackExecutor() {
        return mCallbackExecutor;
    }

    public LifecycleOwnerManager getLifecycleOwnerManager() {
        return mOwnerManager;
    }

    public static final class Builder {
        private boolean mDebug;
        private String mBaseUrl;
        private Call.CallFactory mCallFactory;
        private SourceConverter<? extends ISource> mSourceConverter;
        private Parser.Factory mParserFactory;
        private IFailure.Factory mFailureFactory;

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

        public Builder sourceConverter(SourceConverter<? extends ISource> provider) {
            mSourceConverter = provider;
            return this;
        }

        public Builder parserFactory(Parser.Factory factory) {
            mParserFactory = factory;
            return this;
        }

        public Builder failureFactory(IFailure.Factory failureFactory) {
            mFailureFactory = failureFactory;
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
            final Factory factory = c.getAnnotation(Factory.class);
            InvokerUtil.checkNull(factory, "@Factory: must be added on class: " + c.getCanonicalName());

            Class<? extends InvokerFactory> factoryClass = factory.value();
            InvokerUtil.checkNull(factory, "@Factory: factory cannot be null on class: " + c.getCanonicalName());

            String apiName = factory.name();
            if (TextUtils.isEmpty(apiName)) {
                apiName = c.getCanonicalName();
            }
            Object api = mApiMap.get(apiName);
            if (api == null) {
                api = create(c, factoryClass);
                mApiMap.put(apiName, api);
            }
            return (Api) api;
        }

        static <Api> Api create(Class<Api> c, Class<? extends InvokerFactory> factoryClass) {
            try {
                final String factoryClassName = factoryClass.getCanonicalName();
                Invoker client = mFactoryMap.get(factoryClassName);
                if (client == null) {
                    InvokerFactory factory = factoryClass.newInstance();
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
