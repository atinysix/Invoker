package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 14:00
 */
public interface Callback<Data, F extends IFailure> {

    void onSuccess(Success<Data> result);

    void onFailure(Failure<F> result);

}