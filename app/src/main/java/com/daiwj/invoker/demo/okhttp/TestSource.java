package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.runtime.ISource;

/**
 * author: daiwj on 2020/12/3 19:05
 */
public class TestSource implements ISource {
    private static final int SUCCESS = 1;

    private int status;
    private String code;
    private String data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data == null ? "" : data;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return status == SUCCESS;
    }

    @Override
    public String data() {
        return getData();
    }

}
