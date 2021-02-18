package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/7 20:44
 */
public final class CallException extends Exception {

    private static final long serialVersionUID = 3232880574229389304L;

    private Failure<?> mFailure;

    public CallException(Failure<?> failure) {
        super();
        mFailure = failure;
    }

    public Failure<?> getFailure() {
        return mFailure;
    }
}
