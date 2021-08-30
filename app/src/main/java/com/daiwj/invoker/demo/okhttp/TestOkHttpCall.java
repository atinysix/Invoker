package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.call.okhttp3.OkHttpCall;
import com.daiwj.invoker.demo.Test;
import com.daiwj.invoker.runtime.Caller;
import com.daiwj.invoker.runtime.FailureResult;
import com.daiwj.invoker.runtime.ISource;
import com.daiwj.invoker.runtime.Result;

import okhttp3.OkHttpClient;

/**
 * desc:
 * author: daiwj
 */
public class TestOkHttpCall extends OkHttpCall<Test> {

    public TestOkHttpCall(Caller<Test> caller, OkHttpClient client) {
        super(caller, client);
    }

    @Override
    public Test parseData(ISource source) {
        return super.parseData(source);
    }

    @Override
    public FailureResult<?> parseFailure(Result origin, ISource source) {
        return super.parseFailure(origin, source);
    }

    @Override
    protected FailureResult<?> parseFailure(Result origin, Exception e) {
        return super.parseFailure(origin, e);
    }
}