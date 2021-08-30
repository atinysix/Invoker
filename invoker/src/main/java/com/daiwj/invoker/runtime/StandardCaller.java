package com.daiwj.invoker.runtime;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.lifecycle.ILifecycleOwner;

/**
 * author: daiwj on 2020/12/3 19:13
 */
public class StandardCaller<Data> implements Caller<Data> {

    private MethodVisitor<Data> mVisitor;
    private Mocker mMocker;
    private Call<Data> mCall;
    private boolean mCanceled;

    private ILifecycleOwner mLifecycleOwner;

    public StandardCaller(MethodVisitor<Data> visitor) {
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
    public IDynamicCaller<Data> asDynamic() {
        return new DynamicCaller<>(this);
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

    private Call<Data> newCall() {
        if (mCall == null) {
            Call.CallFactory factory = getMethodVisitor().getCallFactory();
            if (factory == null) {
                factory = getClient().getCallFactory();
            }
            mCall = (Call<Data>) factory.newCall(this);
        }
        return mCall;
    }

    @Override
    public final <F extends IFailure> void call(Callback<Data, F> callback) {
        newCall().call(callback);
    }

    @Override
    public final <F extends IFailure> void call(Context context, Callback<Data, F> callback) {
        if (context != null && callback != null) {
            mLifecycleOwner = getClient().getLifecycleOwnerManager().findOrCreate(context);
            if (mLifecycleOwner != null) {
                mLifecycleOwner.bind(this);
            }
        }
        newCall().call(callback);
    }

    @Override
    public final <F extends IFailure> void call(Fragment fragment, Callback<Data, F> callback) {
        if (fragment != null && callback != null) {
            mLifecycleOwner = getClient().getLifecycleOwnerManager().findOrCreate(fragment);
            if (mLifecycleOwner != null) {
                mLifecycleOwner.bind(this);
            }
        }
        newCall().call(callback);
    }

    @Override
    public final SuccessResult<Data> callSync() throws CallException {
        return newCall().callSync();
    }

    @CallSuper
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

    @CallSuper
    @Override
    public void finish() {
        if (mLifecycleOwner != null) {
            mLifecycleOwner.unbind(this);
            mLifecycleOwner = null;
        }
    }

    @Override
    public final String getTag() {
        final MethodVisitor<?> visitor = getMethodVisitor();
        return visitor.getApiName() + "." + visitor.getMethod().getName();
    }

}
