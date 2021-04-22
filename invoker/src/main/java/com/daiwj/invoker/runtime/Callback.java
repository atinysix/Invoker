package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 14:00
 */
public interface Callback<Data, F extends IFailure> {

    /**
     * for successful result
     *
     * @param result
     */
    void onSuccess(SuccessResult<Data> result);

    /**
     * for fail result
     *
     * @param result
     */
    void onFailure(FailureResult<F> result);

}