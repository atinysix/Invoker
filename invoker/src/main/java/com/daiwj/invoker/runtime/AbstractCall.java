package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/4 16:29
 */
public abstract class AbstractCall<Data> implements Call<Data> {

    private Caller<Data> mCaller;

    public AbstractCall(Caller<Data> caller) {
        mCaller = caller;
    }

    @Override
    public Caller<Data> getCaller() {
        return mCaller;
    }

    @Override
    public MethodVisitor<Data> getMethodVisitor() {
        return mCaller.getMethodVisitor();
    }

    @Override
    public SourceConverter<? extends ISource> getSourceConverter() {
        return mCaller.getClient().getSourceConverter();
    }

    @Override
    public DataParser getDataParser() {
        return mCaller.getClient().getDataParser();
    }

    @Override
    public StringParser getJsonStringParser() {
        return mCaller.getClient().getStringParser();
    }

    @Override
    public IFailure.Factory getFailureFactory() {
        return mCaller.getClient().getFailureFactory();
    }

    protected final void execute(Runnable r) {
        mCaller.getClient().getCallbackExecutor().execute(r);
    }

    protected final void executeSuccess(Callback<?, ?> c, Success<?> success) {
        mCaller.getClient().getCallbackExecutor().executeSuccess(c, success);
    }

    protected final void executeFailure(Callback<?, ?> c, Failure<?> failure) {
        mCaller.getClient().getCallbackExecutor().executeFailure(c, failure);
    }

}
