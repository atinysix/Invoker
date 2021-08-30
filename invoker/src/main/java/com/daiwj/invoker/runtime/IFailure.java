package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/4 17:08
 */
public interface IFailure {

    String getMessage();

    interface Factory {

        IFailure create(ISource source);

    }

}
