package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:53
 */
public class SuccessResult<Data> extends Result {

    private Data mData;

    public SuccessResult(Caller<?> caller) {
        super(caller);
    }

    public SuccessResult(Result result, Data data) {
        super(result);
        mData = data;
    }

    public Data getData() {
        return mData;
    }

}
