package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 19:59
 */
public interface ParserFactory {

    DataParser dataParser();

    StringParser stringParser();

}
