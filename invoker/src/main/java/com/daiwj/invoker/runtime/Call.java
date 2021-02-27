package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 21:15
 */
public interface Call<Data> {

    Caller<Data> getCaller();

    MethodVisitor<Data> getMethodVisitor();

    void call(Callback<Data, ?> callback);

    SuccessResult<Data> callSync() throws CallException, CustomResultException;

    void cancel();

    boolean isCanceled();

    void finish();

    SourceFactory<?> getSourceFactory();

    DataParser getDataParser();

    StringParser getJsonStringParser();

    IFailure.Factory getFailureFactory();

    Result parseSuccess(Result origin, ISource source);

    FailureResult<?> parseFailure(Result origin, ISource source);

    ISource parseSource(String content);

    Data parseData(ISource source);

    interface CallFactory {

        Call<?> newCall(Caller<?> caller);

    }

}
