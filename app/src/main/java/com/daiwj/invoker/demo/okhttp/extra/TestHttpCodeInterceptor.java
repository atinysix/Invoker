package com.daiwj.invoker.demo.okhttp.extra;

import com.daiwj.invoker.demo.okhttp.TestCallback;
import com.daiwj.invoker.demo.okhttp.TestFailure;
import com.daiwj.invoker.runtime.Caller;
import com.daiwj.invoker.runtime.FailureResult;

/**
 * author: daiwj on 2020/12/9 23:08
 */
public class TestHttpCodeInterceptor {

    public <Data> void intercept(TestCallback<Data> callback, FailureResult<TestFailure> result) {
        final Caller<Data> caller = (Caller<Data>) result.getCaller();
        caller.call(callback);
    }

}
