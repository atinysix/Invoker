package com.daiwj.invoker.demo.okhttp.extra;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TestRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder();

        builder.addHeader("Connection", "close");

        return chain.proceed(builder.build());
    }

}
