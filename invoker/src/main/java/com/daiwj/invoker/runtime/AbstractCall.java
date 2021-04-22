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
    public final boolean isCanceled() {
        return mCaller.isCanceled();
    }

    @Override
    public final void finish() {
        mCaller.finish();
    }

    @Override
    public SourceFactory<? extends ISource> getSourceFactory() {
        return mCaller.getInvoker().getSourceFactory();
    }

    @Override
    public DataParser getDataParser() {
        return mCaller.getInvoker().getDataParser();
    }

    @Override
    public StringParser getJsonStringParser() {
        return mCaller.getInvoker().getStringParser();
    }

    @Override
    public IFailure.Factory getFailureFactory() {
        return mCaller.getInvoker().getFailureFactory();
    }

    @Override
    public ISource parseSource(IResponse response) {
        Class<?> sourceType = getMethodVisitor().getSourceType();
        if (sourceType == null) {
            sourceType = getSourceFactory().create();
        }
        return getDataParser().parse(response.getContent(), sourceType);
    }

    @Override
    public Data parseData(ISource source) {
        return getDataParser().parse(source.data(), getMethodVisitor().getDataType());
    }

    @Override
    public final Result parseSuccess(Result origin, ISource source) {
        if (source.isSuccessful()) {
            return new SuccessResult<>(origin, parseData(source));
        } else {
            return parseFailure(origin, source);
        }
    }

    @Override
    public FailureResult<?> parseFailure(Result origin, ISource source) {
        return new FailureResult<>(origin, getFailureFactory().create(source));
    }

    protected final void executeSuccess(Callback<?, ?> c, SuccessResult<?> result) {
        mCaller.getInvoker().getResultExecutor().executeSuccess(c, result);
    }

    protected final void executeFailure(Callback<?, ?> c, FailureResult<?> result) {
        mCaller.getInvoker().getResultExecutor().executeFailure(c, result);
    }
}
