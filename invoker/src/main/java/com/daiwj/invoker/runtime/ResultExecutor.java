package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/4 16:01
 */
public interface ResultExecutor {

    void executeSuccess(Callback c, SuccessResult<?> success);

    void executeFailure(Callback c, FailureResult<?> failure);

}
