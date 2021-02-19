package com.daiwj.invoker.call.okhttp3;

import com.daiwj.invoker.runtime.IResponse;
import com.daiwj.invoker.runtime.Mocker;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * author: daiwj on 2/3/21 14:23
 */
public class OkHttpResponse implements IResponse {
    private okhttp3.Response mResponse;
    private String mContent;

    public OkHttpResponse(okhttp3.Response response) {
        this(response, null);
    }

    public OkHttpResponse(okhttp3.Response response, Mocker mocker) {
        mResponse = response;

        if (mocker != null) {
            mContent = mocker.getContent();
        } else {
            ResponseBody body = null;
            try {
                body = mResponse.body();
                mContent = body.string();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (body != null) {
                    body.close();
                }
            }
        }
    }

    @Override
    public int getCode() {
        return mResponse.code();
    }

    @Override
    public String getMessage() {
        return mResponse.message();
    }

    @Override
    public String getHeader(String name) {
        return mResponse.headers().get(name);
    }

    @Override
    public String getContent() {
        return mContent == null ? "" : mContent;
    }

}
