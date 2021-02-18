package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 1/16/21 21:53
 */
public class Success<Data> extends Result {
    private Data mData;

    public Success(Caller<?> caller) {
        super(caller);
    }

    public Success(Result result) {
        super(result);
    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

}
