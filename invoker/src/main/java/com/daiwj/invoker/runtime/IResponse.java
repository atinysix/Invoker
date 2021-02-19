package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:25
 */
public interface IResponse {

    int getCode();

    String getMessage();

    String getHeader(String name);

    String getContent();

}
