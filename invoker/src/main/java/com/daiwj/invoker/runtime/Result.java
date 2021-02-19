package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:23
 */
public class Result {
    private Caller<?> mCaller;
    private IResponse mResponse;

    public Result(Caller<?> caller) {
        mCaller = caller;
    }

    public Result(Result source) {
        mCaller = source.mCaller;
        mResponse = source.mResponse;
    }

    public <Data> Caller<Data> getCaller() {
        return (Caller<Data>) mCaller;
    }

    public IResponse getResponse() {
        return mResponse;
    }

    public void setResponse(IResponse response) {
        mResponse = response;
    }
}
