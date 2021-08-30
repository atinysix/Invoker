package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:25
 */
public interface ResponseAccess {

    int getHttpCode();

    String getHttpMessage();

    String getHttpHeader(String name);

}
