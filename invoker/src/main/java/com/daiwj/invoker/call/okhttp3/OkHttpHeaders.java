package com.daiwj.invoker.call.okhttp3;

import com.daiwj.invoker.runtime.ResponseHeaders;

import okhttp3.Headers;

/**
 * author: daiwj on 1/16/21 21:35
 */
public class OkHttpHeaders implements ResponseHeaders {

    private Headers mHeaders;

    public OkHttpHeaders(Headers headers) {
        mHeaders = headers;
    }

    @Override
    public String get(String name) {
        return mHeaders != null ? mHeaders.get(name) : "";
    }

}
