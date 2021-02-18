package com.daiwj.invoker.runtime;

import androidx.annotation.Nullable;

/**
 * author: daiwj on 1/16/21 21:53
 */
public class Failure<F extends IFailure> extends Result {
    private F mFailure;

    public Failure(Caller<?> caller) {
        super(caller);
    }

    public Failure(Result result) {
        super(result);
    }

    @Nullable
    @Override
    public Response getResponse() {
        return super.getResponse();
    }

    public F getFailure() {
        return mFailure;
    }

    public void setFailure(F failure) {
        mFailure = failure;
    }

}
