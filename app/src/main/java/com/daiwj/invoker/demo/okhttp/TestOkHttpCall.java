package com.daiwj.invoker.demo.okhttp;

import com.daiwj.invoker.call.okhttp3.OkHttpCall;
import com.daiwj.invoker.runtime.CallException;
import com.daiwj.invoker.runtime.Callback;
import com.daiwj.invoker.runtime.Caller;
import com.daiwj.invoker.runtime.FailureResult;
import com.daiwj.invoker.runtime.Result;
import com.daiwj.invoker.runtime.SuccessResult;
import com.daiwj.invoker.runtime.SourceConverter;

import okhttp3.OkHttpClient;

/**
 * author: daiwj on 2020/12/4 18:33
 */
public class TestOkHttpCall<Data> extends OkHttpCall<Data> {

    private SourceConverter<TestSource> mSourceConverter;
    private TestFailureFactory mFailureFactory;

    public TestOkHttpCall(Caller<Data> caller, OkHttpClient client) {
        super(caller, client);

        mSourceConverter = (SourceConverter<TestSource>) getSourceConverter();
        mFailureFactory = (TestFailureFactory) getFailureFactory();
    }

    @Override
    protected final void parseSuccess(Callback<Data, ?> c, Result result) {
        final TestSource source = mSourceConverter.convert(result.getResponse().getContent());
        if (source.isSuccessful()) {
            final SuccessResult<Data> success = new SuccessResult<>(result);
            success.setData(parseData(source.data()));
            executeSuccess(c, success);
        } else {
            final FailureResult<TestFailure> failure = new FailureResult<>(result);
            failure.setFailure(mFailureFactory.create(source));
            executeFailure(c, failure);
        }
    }

    @Override
    protected final void parseFailure(Callback<Data, ?> c, Result result, Exception e) {
        final FailureResult<TestFailure> failure = new FailureResult<>(result);
        failure.setFailure(mFailureFactory.create(e));
        executeFailure(c, failure);
    }

    @Override
    protected final SuccessResult<Data> parseSuccessSync(Result result) throws CallException {
        final TestSource source = mSourceConverter.convert(result.getResponse().getContent());
        if (source.isSuccessful()) {
            final SuccessResult<Data> success = new SuccessResult<>(result);
            success.setData(parseData(source.data()));
            return success;
        } else {
            final FailureResult<TestFailure> failure = new FailureResult<>(result);
            failure.setFailure(mFailureFactory.create(source));
            throw new CallException(failure);
        }
    }

    @Override
    protected final FailureResult<?> parseFailureSync(Result result, Exception e) {
        final FailureResult<TestFailure> failure = new FailureResult<>(result);
        failure.setFailure(mFailureFactory.create(e));
        return failure;
    }

    protected Data parseData(String source) {
        return getDataParser().parse(source, getMethodVisitor().getDataType());
    }

}
