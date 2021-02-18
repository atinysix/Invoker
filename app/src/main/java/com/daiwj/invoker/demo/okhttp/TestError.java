package com.daiwj.invoker.demo.okhttp;


import com.daiwj.invoker.runtime.IError;

/**
 * author: daiwj on 2020/12/5 15:28
 */
public class TestError extends TestFailure implements IError {

    public static final int TYPE_SERVER_ERROR = 404;
    public static final int TYPE_TIMEOUT = 500;

    private int mType;

    @Override
    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
