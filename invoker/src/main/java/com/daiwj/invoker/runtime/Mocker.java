package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/2/21 11:52
 */
public class Mocker {

    private String mContent;

    public Mocker(String content) {
        mContent = content;
    }

    public String getContent() {
        return mContent == null ? "" : mContent;
    }
}
