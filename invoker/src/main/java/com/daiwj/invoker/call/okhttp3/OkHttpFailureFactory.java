package com.daiwj.invoker.call.okhttp3;

import com.daiwj.invoker.runtime.IFailure;

/**
 * author: daiwj on 1/1/21 22:06
 */
public interface OkHttpFailureFactory extends IFailure.Factory {

    IFailure create(Exception e);

}
