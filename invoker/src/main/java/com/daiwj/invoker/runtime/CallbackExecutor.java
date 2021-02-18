package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/4 16:01
 */
public interface CallbackExecutor {

    void execute(Runnable r);

    void executeSuccess(Callback c, SuccessResult<?> success);

    void executeFailure(Callback c, FailureResult<?> failure);

}
