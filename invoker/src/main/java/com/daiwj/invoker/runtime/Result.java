package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:23
 */
public class Result implements ResponseAccess {
    private Caller<?> mCaller;
    private ResponseAccess mResponse;

    public Result(Caller<?> caller) {
        mCaller = caller;
    }

    public Result(Caller<?> caller, ResponseAccess response) {
        mCaller = caller;
        mResponse = response;
    }

    public Result(Result origin) {
        mCaller = origin.mCaller;
        mResponse = origin.mResponse;
    }

    public Caller<?> getCaller() {
        return mCaller;
    }

    @Override
    public int getHttpCode() {
        return mResponse != null ? mResponse.getHttpCode() : -1;
    }

    @Override
    public String getHttpMessage() {
        return mResponse != null ? mResponse.getHttpMessage() : "";
    }

    @Override
    public String getHttpHeader(String name) {
        return mResponse != null ? mResponse.getHttpHeader(name) : "";
    }
}
