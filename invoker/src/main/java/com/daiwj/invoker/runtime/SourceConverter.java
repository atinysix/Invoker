package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 21:53
 */
public interface SourceConverter<S extends ISource> {

    S convert(String content);

}
