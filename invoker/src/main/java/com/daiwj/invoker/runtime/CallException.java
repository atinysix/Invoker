package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/7 20:44
 */
public final class CallException extends Exception {

    private static final long serialVersionUID = 3232880574229389304L;

    private FailureResult<?> mFailure;

    public CallException(FailureResult<?> failure) {
        super();
        mFailure = failure;
    }

    public FailureResult<?> getFailure() {
        return mFailure;
    }
}
