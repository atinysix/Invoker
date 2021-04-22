package com.daiwj.invoker.runtime;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.daiwj.invoker.Invoker;

import java.util.HashMap;
import java.util.Map;

/**
 * author: daiwj on 2/1/21 15:14
 */
public class DynamicCaller<Data> implements IDynamicCaller<Data> {

    private Caller<Data> mOrigin;
    private MethodVisitor<Data> mMethodVisitor;

    public DynamicCaller(Caller<Data> origin) {
        mOrigin = origin;
        mMethodVisitor = origin.getMethodVisitor().copy();
    }

    public DynamicCaller<Data> setHttpMethod(String method) {
        mMethodVisitor.setHttpMethod(method);
        return this;
    }

    public DynamicCaller<Data> setRelativeUrl(String url) {
        mMethodVisitor.setRelativeUrl(url);
        return this;
    }

    public DynamicCaller<Data> addParam(String name, String value) {
        return addParam(name, value, false);
    }

    public DynamicCaller<Data> addParam(String name, String value, boolean encode) {
        final RequestParam param = new RequestParam(name, value, encode);
        mMethodVisitor.getRequestParams().add(param);
        return this;
    }

    public DynamicCaller<Data> addParam(HashMap<String, Object> map) {
        return addParam(map, false);
    }

    public DynamicCaller<Data> addParam(Map<String, Object> map, boolean encode) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final String name = entry.getKey();
            final String value = entry.getValue().toString();
            final RequestParam param = new RequestParam(name, value, encode);
            mMethodVisitor.getRequestParams().add(param);
        }
        return this;
    }

    public DynamicCaller<Data> addHeader(String name, String value) {
        mMethodVisitor.getHeaders().put(name, value);
        return this;
    }

    public DynamicCaller<Data> addHeader(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            mMethodVisitor.getHeaders().put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public DynamicCaller<Data> setSourceType(Class<? extends ISource> sourceType) {
        mMethodVisitor.setSourceType(sourceType);
        return this;
    }

    public DynamicCaller<Data> setCallFactory(Call.CallFactory factory) {
        mMethodVisitor.setCallFactory(factory);
        return this;
    }

    @Override
    public Invoker getInvoker() {
        return mOrigin.getInvoker();
    }

    @Override
    public MethodVisitor<Data> getMethodVisitor() {
        return mMethodVisitor;
    }

    @Override
    public DynamicCaller<Data> asDynamic() {
        return this;
    }

    @Override
    public DynamicCaller<Data> mock(String content) {
        mOrigin.mock(content);
        return this;
    }

    @Override
    public Mocker getMocker() {
        return mOrigin.getMocker();
    }

    @Override
    public Call<Data> newCall() {
        return mOrigin.newCall();
    }

    @Override
    public <F extends IFailure> void call(Callback<Data, F> callback) {
        mOrigin.call(callback);
    }

    @Override
    public <F extends IFailure> void call(Context context, Callback<Data, F> callback) {
        mOrigin.call(context, callback);
    }

    @Override
    public <F extends IFailure> void call(Fragment fragment, Callback<Data, F> callback) {
        mOrigin.call(fragment, callback);
    }

    @Override
    public SuccessResult<Data> callSync() throws CallException {
        return mOrigin.callSync();
    }

    @Override
    public void cancel() {
        mOrigin.cancel();
    }

    @Override
    public boolean isCanceled() {
        return mOrigin.isCanceled();
    }

    @Override
    public void finish() {
        mOrigin.finish();
    }

    @Override
    public String getTag() {
        return mOrigin.getTag();
    }

}
