package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 21:15
 */
public interface Call<Data> {

    Caller<Data> getCaller();

    MethodVisitor<Data> getMethodVisitor();

    void call(Callback<Data, ?> callback);

    Success<Data> callSync() throws CallException;

    void cancel();

    boolean isCanceled();

    void finish();

    SourceConverter<?> getSourceConverter();

    DataParser getDataParser();

    StringParser getJsonStringParser();

    IFailure.Factory getFailureFactory();

    interface CallFactory {

        Call<?> newCall(Caller<?> caller);

    }

}
