package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.call.okhttp3.OkHttpFailureFactory;
import com.daiwj.invoker.runtime.IFailure;
import com.daiwj.invoker.runtime.ISource;

import java.net.SocketTimeoutException;

/**
 * author: daiwj on 2020/12/5 15:28
 */
public class TestFailureFactory implements OkHttpFailureFactory {

    @Override
    public IFailure create(String message) {
        TestFailure failure = new TestFailure();
        failure.setMessage(message);
        return failure;
    }

    @Override
    public IFailure create(ISource source) {
        TestSource testSource = (TestSource) source;
        TestFailure failure = new TestFailure();
        failure.setCode(testSource.getCode());
        failure.setMessage(testSource.getMessage());
        return failure;
    }

    @Override
    public TestError create(Exception e) {
        TestError error = new TestError();
        if (e instanceof SocketTimeoutException) {
            error.setType(TestError.TYPE_TIMEOUT);
            error.setMessage("请求超时，请检查网络连接");
        } else {
            error.setType(TestError.TYPE_SERVER_ERROR);
            error.setMessage("服务器开小差，请稍后再试");
        }
        return error;
    }

}
