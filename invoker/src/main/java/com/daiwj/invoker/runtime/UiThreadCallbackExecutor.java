package com.daiwj.invoker.runtime;

import android.os.Handler;
import android.os.Looper;

/**
 * author: daiwj on 2020/12/4 15:51
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class UiThreadCallbackExecutor implements CallbackExecutor {

    private final Handler mPoster = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable r) {
        mPoster.post(r);
    }

    @Override
    public void executeSuccess(Callback c, Success<?> success) {
        mPoster.post(new Runnable() {
            @Override
            public void run() {
                c.onSuccess(success);
            }
        });
    }

    @Override
    public void executeFailure(Callback c, Failure<?> failure) {
        mPoster.post(new Runnable() {
            @Override
            public void run() {
                c.onFailure(failure);
            }
        });
    }
}
