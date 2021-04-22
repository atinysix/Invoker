package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 21:15
 */
public interface Call<Data> {

    Caller<Data> getCaller();

    MethodVisitor<Data> getMethodVisitor();

    void call(Callback<Data, ?> callback);

    SuccessResult<Data> callSync() throws CallException;

    void cancel();

    boolean isCanceled();

    void finish();

    SourceFactory<?> getSourceFactory();

    DataParser getDataParser();

    StringParser getJsonStringParser();

    IFailure.Factory getFailureFactory();

    ISource parseSource(IResponse response);

    Data parseData(ISource source);

    Result parseSuccess(Result origin, ISource source);

    FailureResult<?> parseFailure(Result origin, ISource source);

    interface CallFactory {

        Call<?> newCall(Caller<?> caller);

    }

}
