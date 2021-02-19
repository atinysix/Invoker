package com.daiwj.invoker.demo.okhttp;

import com.alibaba.fastjson.JSON;
import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.demo.BuildConfig;
import com.daiwj.invoker.demo.okhttp.extra.TestLogInterceptor;
import com.daiwj.invoker.demo.okhttp.extra.TestRequestInterceptor;
import com.daiwj.invoker.parser.FastJsonParserFactory;
import com.daiwj.invoker.parser.GsonParserFactory;
import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.Caller;
import com.daiwj.invoker.runtime.InvokerFactory;
import com.daiwj.invoker.runtime.SourceConverter;
import com.google.gson.Gson;

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
public class TestInvokerFactory implements InvokerFactory {

    @Override
    public Invoker create() {
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
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
                clientBuilder.hostnameVerifier((hostname, session) -> true);
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
                clientBuilder.sslSocketFactory(sc.getSocketFactory());
                clientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final OkHttpClient client = clientBuilder.build();

        final Call.CallFactory mCallFactory = new Call.CallFactory() {

            @Override
            public Call<?> newCall(Caller<?> caller) {
                return new TestOkHttpCall<>(caller, client);
            }

        };

        final SourceConverter<TestSource> mSourceConverter = new SourceConverter<TestSource>() {

            @Override
            public TestSource convert(String from) {
                try {
                    return JSON.parseObject(from, TestSource.class);
                } catch (Exception e) {
                    return new TestSource();
                }
            }
        };

        return new Invoker.Builder()
                .baseUrl("https://www.baidu.com/")
                .callFactory(mCallFactory) // call实体
                .sourceConverter(mSourceConverter) // response层数据
                .parserFactory(new GsonParserFactory()) // data层数据
                .failureFactory(new TestFailureFactory()) // failure数据
                .debug(BuildConfig.DEBUG)
                .build();
    }

}
