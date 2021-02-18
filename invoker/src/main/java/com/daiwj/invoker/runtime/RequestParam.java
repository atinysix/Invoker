package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 10:53
 */
public class RequestParam {
    private String mName;
    private String mValue;
    private boolean mEncode;

    public RequestParam(String name, String value, boolean encode) {
        mName = name;
        mValue = value;
        mEncode = encode;
    }

    public String getName() {
        return mName == null ? "" : mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getValue() {
        return mValue == null ? "" : mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public boolean isEncode() {
        return mEncode;
    }

    public void setEncode(boolean encode) {
        this.mEncode = encode;
    }
}
