package com.daiwj.invoker.runtime;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.lifecycle.ILifecycleOwner;

/**
 * author: daiwj on 2020/12/3 19:13
 */
public abstract class StaticCaller<Data> implements Caller<Data> {

    private MethodVisitor<Data> mVisitor;
    private Mocker mMocker;
    private Call<Data> mCall;
    private boolean mCanceled;

    private ILifecycleOwner mLifecycleOwner;

    public StaticCaller(MethodVisitor<Data> visitor) {
        mVisitor = visitor;
    }

    @Override
    public Invoker getClient() {
        return mVisitor.getClient();
    }

    @Override
    public MethodVisitor<Data> getMethodVisitor() {
        return mVisitor;
    }

    @Override
    public Caller<Data> copy() {
        mVisitor = mVisitor.copy();
        return this;
    }

    @Override
    public DynamicCaller<Data> asDynamic() {
        return new DynamicCaller<Data>(this);
    }

    @Override
    public Caller<Data> mock(String content) {
        mMocker = new Mocker(content);
        return this;
    }

    @Override
    public Mocker getMocker() {
        return mMocker;
    }

    @Override
    public final <F extends IFailure> void call(Callback<Data, F> callback) {
        mCall = (Call<Data>) getClient().getCallFactory().newCall(this);
        mCall.call(callback);
    }

    @Override
    public final <F extends IFailure> void call(Context context, Callback<Data, F> callback) {
        if (context != null && callback != null) {
            mLifecycleOwner = getClient().getLifecycleOwnerManager().findOrCreate(context);
            if (mLifecycleOwner != null) {
                mLifecycleOwner.bind(this);
            }
        }
        mCall = (Call<Data>) getClient().getCallFactory().newCall(this);
        mCall.call(callback);
    }

    @Override
    public final <F extends IFailure> void call(Fragment fragment, Callback<Data, F> callback) {
        if (fragment != null && callback != null) {
            mLifecycleOwner = getClient().getLifecycleOwnerManager().findOrCreate(fragment);
            if (mLifecycleOwner != null) {
                mLifecycleOwner.bind(this);
            }
        }
        mCall = (Call<Data>) getClient().getCallFactory().newCall(this);
        mCall.call(callback);
    }

    @Override
    public final SuccessResult<Data> callSync() throws CallException {
        mCall = (Call<Data>) getClient().getCallFactory().newCall(this);
        return mCall.callSync();
    }

    @Override
    public void cancel() {
        mCanceled = true;
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        return mCanceled;
    }

    @Override
    public void finish() {
        if (mLifecycleOwner != null) {
            mLifecycleOwner.unbind(this);
            mLifecycleOwner = null;
        }
    }

    @Override
    public String getTag() {
        final MethodVisitor<?> visitor = getMethodVisitor();
        return visitor.getApiName() + "." + visitor.getMethod().getName();
    }

}
