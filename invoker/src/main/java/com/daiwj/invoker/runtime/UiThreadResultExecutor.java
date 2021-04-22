package com.daiwj.invoker.runtime;

import android.os.Handler;
import android.os.Looper;

/**
 * author: daiwj on 2020/12/4 15:51
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class UiThreadResultExecutor implements ResultExecutor {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void executeSuccess(Callback c, SuccessResult<?> success) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                c.onSuccess(success);
            }
        });
    }

    @Override
    public void executeFailure(Callback c, FailureResult<?> failure) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                c.onFailure(failure);
            }
        });
    }
}
