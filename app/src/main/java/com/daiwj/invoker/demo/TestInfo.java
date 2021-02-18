package com.daiwj.invoker.demo;

/**
 * author: daiwj on 2020/12/3 15:05
 */
public class TestInfo {

    private String text;

    public String getText() {
        return text == null ? "" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
