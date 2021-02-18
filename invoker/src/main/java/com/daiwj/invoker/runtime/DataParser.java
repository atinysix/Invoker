package com.daiwj.invoker.runtime;

import java.lang.reflect.Type;

/**
 * author: daiwj on 2020/12/6 11:27
 */
public interface DataParser {

    <Data> Data parse(String from, Type c);

}
