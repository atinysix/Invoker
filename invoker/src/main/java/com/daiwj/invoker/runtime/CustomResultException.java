package com.daiwj.invoker.runtime;

/**
 *
 * author: daiwj on 2020/12/7 20:44
 */
public final class CustomResultException extends Exception {

    private static final long serialVersionUID = 6327226300655588374L;

    private Result mResult;

    public CustomResultException(Result result) {
        super();
        mResult = result;
    }

    public Result getResult() {
        return mResult;
    }
}
