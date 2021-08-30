package com.daiwj.invoker.call.okhttp3;

import com.daiwj.invoker.runtime.ContentResponse;
import com.daiwj.invoker.runtime.Mocker;

import okhttp3.ResponseBody;

/**
 * author: daiwj on 2/3/21 14:23
 */
class OkContentResponse implements ContentResponse {

    private okhttp3.Response mResponse;
    private String mContent;

    public OkContentResponse(okhttp3.Response response) {
        this(response, null);
    }

    public OkContentResponse(okhttp3.Response response, Mocker mocker) {
        mResponse = response;

        if (mocker != null) {
            mContent = mocker.getContent();
        } else {
            try (ResponseBody body = mResponse.body()) {
                mContent = body != null ? body.string() : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getHttpCode() {
        return mResponse.code();
    }

    @Override
    public String getHttpMessage() {
        return mResponse.message();
    }

    @Override
    public String getHttpHeader(String name) {
        return mResponse.headers().get(name);
    }

    public String getHttpContent() {
        return mContent == null ? "" : mContent;
    }

}
