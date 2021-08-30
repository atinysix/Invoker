package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:53
 */
public class FailureResult<F extends IFailure> extends Result {

    private F mFailure;

    public FailureResult(Result result, F failure) {
        super(result);
        mFailure = failure;
    }

    public F getFailure() {
        return mFailure;
    }

}
