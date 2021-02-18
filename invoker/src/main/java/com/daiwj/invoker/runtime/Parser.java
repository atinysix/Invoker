package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 19:59
 */
public interface Parser<F, T> {

    T parse(F from);

    interface Factory {

        DataParser dataParser();

        StringParser stringParser();

    }

}
