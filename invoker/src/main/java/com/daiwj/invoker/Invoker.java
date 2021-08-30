package com.daiwj.invoker;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.daiwj.invoker.annotation.InvokerProvider;
import com.daiwj.invoker.call.okhttp3.OkHttpCallFactory;
import com.daiwj.invoker.call.okhttp3.OkHttpFailureFactory;
import com.daiwj.invoker.lifecycle.LifecycleOwnerManager;
import com.daiwj.invoker.parser.GsonParserFactory;
import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.DataParser;
import com.daiwj.invoker.runtime.IFailure;
import com.daiwj.invoker.runtime.ISource;
import com.daiwj.invoker.runtime.InvokerFactory;
import com.daiwj.invoker.runtime.Utils;
import com.daiwj.invoker.runtime.Logger;
import com.daiwj.invoker.runtime.MethodVisitor;
import com.daiwj.invoker.runtime.ParserFactory;
import com.daiwj.invoker.runtime.ResultExecutor;
import com.daiwj.invoker.runtime.SourceFactory;
import com.daiwj.invoker.runtime.StringParser;
import com.daiwj.invoker.runtime.UiThreadResultExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * author: daiwj on 2020/12/2 21:43
 */
@SuppressWarnings("unchecked")
public final class Invoker {

    private boolean mDebug;

    private final Map<Method, MethodVisitor<?>> mMethodCache = new HashMap<>();

    private String mBaseUrl;
    private Call.CallFactory mCallFactory;
    private SourceFactory<? extends ISource> mSourceFactory;
    private ParserFactory mParserFactory;
    private DataParser mDataParser;
    private StringParser mStringParser;
    private IFailure.Factory mFailureFactory;

    private ResultExecutor mResultExecutor = new UiThreadResultExecutor();

    private LifecycleOwnerManager mOwnerManager = new LifecycleOwnerManager();

    public static <T> T invoke(Class<T> c) {
        if (!c.isInterface()) {
            Utils.error("Cannot provide invoker api for a class type: " + c.getName());
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

        Logger.setDebug(mDebug);
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

    public ParserFactory getParserFactory() {
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
        private ParserFactory mParserFactory = new GsonParserFactory();
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

        public Builder parserFactory(ParserFactory factory) {
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

    private static class ApiProvider<Api> {

        final static Map<String, Invoker> mFactoryMap = new HashMap<>();
        final static Map<String, Object> mApiMap = new HashMap<>();

        static <Api> Api provide(Class<Api> c) {
            final InvokerProvider provider = c.getAnnotation(InvokerProvider.class);
            Utils.checkNull(provider, "@Provider not found on class: " + c.getName());

            final Class<? extends InvokerFactory> factoryClass = provider.value();

            String apiName = provider.name();
            if (TextUtils.isEmpty(apiName)) {
                apiName = c.getName();
            }
            Object api = mApiMap.get(apiName);
            if (api == null) {
                api = create(c, factoryClass);
                if (api != null) {
                    mApiMap.put(apiName, api);
                }
            }
            return (Api) api;
        }

        static <Api> Api create(Class<Api> c, Class<? extends InvokerFactory> factoryClass) {
            try {
                final String className = factoryClass.getName();
                Invoker client = mFactoryMap.get(className);
                if (client == null) {
                    InvokerFactory factory = factoryClass.newInstance();
                    client = factory.create();
                    mFactoryMap.put(className, client);
                }
                return client.create(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
