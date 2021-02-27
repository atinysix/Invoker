package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.call.okhttp3.OkHttpCall;
import com.daiwj.invoker.call.okhttp3.OkHttpCallFactory;
import com.daiwj.invoker.demo.BuildConfig;
import com.daiwj.invoker.demo.okhttp.extra.TestLogInterceptor;
import com.daiwj.invoker.demo.okhttp.extra.TestRequestInterceptor;
import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.Caller;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * author: daiwj on 2/27/21 21:17
 */
public class TestCallFactory extends OkHttpCallFactory {

    @Override
    protected OkHttpClient createClient() {
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

        return builder.build();
    }

    @Override
    public Call<?> newCall(Caller<?> caller, OkHttpClient client) {
        return new TestCall<>(caller, client);
    }

    public static final class TestCall<Data> extends OkHttpCall<Data> {

        public TestCall(Caller<Data> caller, OkHttpClient client) {
            super(caller, client);
        }
    }
}
