package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.demo.okhttp.extra.TestHttpCodeInterceptor;
import com.daiwj.invoker.runtime.Callback;
import com.daiwj.invoker.runtime.Failure;
import com.daiwj.invoker.runtime.Success;

/**
 * author: daiwj on 2020/12/5 15:23
 */
public class TestCallback<Data> implements Callback<Data, TestFailure> {

    private TestHttpCodeInterceptor mInterceptor = new TestHttpCodeInterceptor();

    @Override
    public void onSuccess(Success<Data> result) {
        onSuccessful(result.getData());
    }

    @Override
    public final void onFailure(Failure<TestFailure> result) {
        final TestFailure failure = result.getFailure();
        if (failure.isError()) {
            onError((TestError) failure);
        } else {
            if ("302".equals(result.getFailure().getCode())) {
                mInterceptor.intercept(this, result);
            } else {
                onFail(failure);
            }
        }
    }

    public void onSuccessful(Data data) {

    }

    public void onUnsuccessful() {

    }

    /**
     * 业务失败
     */
    public void onFail(TestFailure failure) {
    }

    /**
     * 发生错误
     */
    public void onError(TestError error) {

    }
}
