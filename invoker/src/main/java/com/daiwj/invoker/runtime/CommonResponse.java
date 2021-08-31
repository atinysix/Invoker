package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:25
 */
public interface CommonResponse {

    int getHttpCode();

    String getHttpMessage();

    String getHttpHeader(String name);

}
