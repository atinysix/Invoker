package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.call.okhttp3.OkHttpCall;
import com.daiwj.invoker.call.okhttp3.OkHttpCallFactory;
import com.daiwj.invoker.demo.BuildConfig;
import com.daiwj.invoker.demo.okhttp.extra.TestLogInterceptor;
import com.daiwj.invoker.demo.okhttp.extra.TestRequestInterceptor;
import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.Caller;
import com.daiwj.invoker.runtime.InvokerProvider;
import com.daiwj.invoker.runtime.SourceFactory;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * author: daiwj on 2020/12/4 10:50
 */
public class TestInvokerProvider implements InvokerProvider {

    @Override
    public Invoker provide() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new TestRequestInterceptor())
                .addInterceptor(new TestLogInterceptor()) //设置log拦截器
                .connectTimeout(5, TimeUnit.SECONDS) //设置超时
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false);//错误重连

        //测试环境下，开放证书校验，方便开发 测试抓包
        if (BuildConfig.DEBUG) {
            try {
                //构造自己的SSLContext
                builder.hostnameVerifier((hostname, session) -> true);
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] x509Certificates,
                            String s) throws java.security.cert.CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] x509Certificates,
                            String s) throws java.security.cert.CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }};
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                builder.sslSocketFactory(sc.getSocketFactory());
                builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        final Call.CallFactory mCallFactory1 = new Call.CallFactory() {
//
//            @Override
//            public Call<?> newCall(Caller<?> caller) {
//                return new OkHttpCall<>(caller, builder.build());
//            }
//        };
//
//        final Call.CallFactory mCallFactory2 = new OkHttpCallFactory() {
//
//            @Override
//            protected OkHttpClient createClient() {
//                return builder.build();
//            }
//
//            @Override
//            public Call<?> newCall(Caller<?> caller, OkHttpClient client) {
//                return new OkHttpCall<>(caller, client);
//            }
//        };
//
//        final Call.CallFactory mCallFactory3 = new OkHttpCallFactory(builder.build());

        final SourceFactory<TestSource> mSourceFactory = new SourceFactory<TestSource>() {

            @Override
            public Class<TestSource> create() {
                return TestSource.class;
            }
        };

        return new Invoker.Builder()
                .baseUrl("https://www.baidu.com/")
//                .callFactory(mCallFactory) // call实体
                .sourceFactory(mSourceFactory) // response层数据
//                .parserFactory(new FastJsonParserFactory()) // data层数据
//                .failureFactory(new TestFailureFactory()) // failure数据
                .debug(BuildConfig.DEBUG)
                .build();
    }

}
