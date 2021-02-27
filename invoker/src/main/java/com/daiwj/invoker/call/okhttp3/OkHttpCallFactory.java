package com.daiwj.invoker.call.okhttp3;

import com.daiwj.invoker.runtime.Call;
import com.daiwj.invoker.runtime.Caller;

import okhttp3.OkHttpClient;

/**
 * author: daiwj on 2/27/21 14:57
 */
public class OkHttpCallFactory implements Call.CallFactory {

    private OkHttpClient mClient;

    public OkHttpCallFactory() {
        mClient =  new OkHttpClient.Builder().build();
    }

    public OkHttpCallFactory(OkHttpClient client) {
        mClient = client;
    }

    @Override
    public Call<?> newCall(Caller<?> caller) {
        return new OkHttpCall<>(caller, mClient);
    }
}
