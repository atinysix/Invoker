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
        mClient = createClient();
        if (mClient == null) {
            mClient = defaultClient();
        }
    }

    public OkHttpCallFactory(OkHttpClient client) {
        mClient = client;
    }

    protected OkHttpClient createClient() {
        return defaultClient();
    }

    private OkHttpClient defaultClient() {
        return new OkHttpClient.Builder().build();
    }

    @Override
    public final Call<?> newCall(Caller<?> caller) {
        return newCall(caller, mClient);
    }

    public Call<?> newCall(Caller<?> caller, OkHttpClient client) {
        return new OkHttpCall<>(caller, client);
    }

}
