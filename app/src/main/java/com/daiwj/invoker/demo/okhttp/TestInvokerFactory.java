package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.call.okhttp3.OkHttpCallFactory;
import com.daiwj.invoker.call.okhttp3.OkHttpFailureFactory;
import com.daiwj.invoker.demo.BuildConfig;
import com.daiwj.invoker.demo.okhttp.extra.TestLogInterceptor;
import com.daiwj.invoker.demo.okhttp.extra.TestRequestInterceptor;
import com.daiwj.invoker.parser.FastJsonParserFactory;
import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.IFailure;
import com.daiwj.invoker.runtime.ISource;
import com.daiwj.invoker.runtime.InvokerFactory;
import com.daiwj.invoker.runtime.SourceFactory;

import java.net.SocketTimeoutException;

import okhttp3.OkHttpClient;

/**
 * author: daiwj on 2020/12/4 10:50
 */
public class TestInvokerFactory implements InvokerFactory {

    @Override
    public Invoker create() {
        final Call.CallFactory callFactory = new OkHttpCallFactory() {

            @Override
            protected OkHttpClient createClient() {
                final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .addInterceptor(new TestRequestInterceptor())
                        .addInterceptor(new TestLogInterceptor()) //设置log拦截器
                        .retryOnConnectionFailure(false);//错误重连
                return builder.build();
            }
        };

        final SourceFactory<TestSource> sourceFactory = new SourceFactory<TestSource>() {

            @Override
            public Class<TestSource> create() {
                return TestSource.class;
            }
        };

        final OkHttpFailureFactory failureFactory = new OkHttpFailureFactory() {

            @Override
            public IFailure create(String message) {
                TestFailure failure = new TestFailure();
                failure.setMessage(message);
                return failure;
            }

            @Override
            public IFailure create(ISource source) {
                TestSource testSource = (TestSource) source;
                TestFailure failure = new TestFailure();
                failure.setCode(testSource.getCode());
                failure.setMessage(testSource.getMessage());
                return failure;
            }

            @Override
            public TestError create(Exception e) {
                TestError error = new TestError();
                if (e instanceof SocketTimeoutException) {
                    error.setType(TestError.TYPE_TIMEOUT);
                    error.setMessage("请求超时，请检查网络连接");
                } else {
                    error.setType(TestError.TYPE_SERVER_ERROR);
                    error.setMessage("服务器开小差，请稍后再试");
                }
                return error;
            }
        };

        return new Invoker.Builder()
                .baseUrl("https://www.baidu.com/")
                .callFactory(callFactory) // call实体
                .sourceFactory(sourceFactory) // response层数据
                .parserFactory(new FastJsonParserFactory()) // data层数据
                .failureFactory(failureFactory) // failure数据
                .debug(BuildConfig.DEBUG)
                .build();
    }

}
