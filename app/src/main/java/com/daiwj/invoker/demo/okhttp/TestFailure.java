package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.runtime.IError;
import com.daiwj.invoker.runtime.IFailure;

/**
 * author: daiwj on 2020/12/5 15:28
 */
public class TestFailure implements IFailure {

    private String mCode;
    private String mMessage;

    public String getCode() {
        return mCode == null ? "" : mCode;
    }

    public void setCode(String code) {
        this.mCode = code;
    }

    public String getMessage() {
        return mMessage == null ? "" : mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public boolean isError() {
        return this instanceof IError;
    }
}
