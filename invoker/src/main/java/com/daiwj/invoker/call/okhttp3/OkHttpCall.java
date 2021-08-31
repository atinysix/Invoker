package com.daiwj.invoker.call.okhttp3;

import com.daiwj.invoker.runtime.AbstractCall;
import com.daiwj.invoker.runtime.CallException;
import com.daiwj.invoker.runtime.Callback;
import com.daiwj.invoker.runtime.Caller;
import com.daiwj.invoker.runtime.FailureResult;
import com.daiwj.invoker.runtime.FilePart;
import com.daiwj.invoker.runtime.ISource;
import com.daiwj.invoker.runtime.Logger;
import com.daiwj.invoker.runtime.Utils;
import com.daiwj.invoker.runtime.MethodVisitor;
import com.daiwj.invoker.runtime.RequestParam;
import com.daiwj.invoker.runtime.Result;
import com.daiwj.invoker.runtime.SourceCaller;
import com.daiwj.invoker.runtime.SuccessResult;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author: daiwj on 2020/12/3 16:26
 */
public class OkHttpCall<Data> extends AbstractCall<Data> {

    private OkHttpClient mOkHttpClient;
    private Call mCall;

    public OkHttpCall(Caller<Data> caller, OkHttpClient client) {
        super(caller);
        mOkHttpClient = client;
    }

    @Override
    public final void call(Callback<Data, ?> callback) {
        final Caller<Data> caller = getCaller();

        final Request request = onRequestCreated(createRequest());
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                finish();

                if (callback == null) return;

                if (isCanceled()) {
                    Logger.w("OkHttpCall", "call " + getCaller().getTag() + " is canceled");
                    return;
                }

                executeFailure(callback, parseFailure(new Result(caller), e));
            }

            @Override
            public void onResponse(Call call, Response response) {
                finish();

                if (callback == null) return;

                if (isCanceled()) {
                    Logger.w("OkHttpCall", "call " + getCaller().getTag() + " is canceled");
                    return;
                }

                final Result origin = new Result(caller);
                final OkHttpResponse okHttpResponse;
                if (caller.getClient().isDebug()) {
                    okHttpResponse = new OkHttpResponse(response, caller.getMocker());
                } else {
                    okHttpResponse = new OkHttpResponse(response);
                }

                final ISource source = parseSource(okHttpResponse);

                if (caller instanceof SourceCaller) {
                    final String data;
                    final SourceCaller sourceCaller = (SourceCaller) getCaller();
                    if (sourceCaller.isDataOnly()) {
                        data = source.data();
                    } else {
                        data = okHttpResponse.getHttpContent();
                    }
                    executeSuccess(callback, new SuccessResult<>(origin, data));
                } else {
                    final Result result = parseSuccess(origin, source);
                    if (result instanceof SuccessResult) {
                        executeSuccess(callback, (SuccessResult<?>) result);
                    } else {
                        executeFailure(callback, (FailureResult<?>) result);
                    }
                }
            }
        });
    }

    @Override
    public final SuccessResult<Data> callSync() throws CallException {
        final Caller<Data> caller = getCaller();

        try {
            final Request resolved = onRequestCreated(createRequest());
            mCall = mOkHttpClient.newCall(resolved);
            final Response response = mCall.execute();

            final OkHttpResponse okHttpResponse;
            if (caller.getClient().isDebug()) {
                okHttpResponse = new OkHttpResponse(response, caller.getMocker());
            } else {
                okHttpResponse = new OkHttpResponse(response);
            }
            final Result origin = new Result(caller, okHttpResponse);

            final ISource source = parseSource(okHttpResponse);
            if (caller instanceof SourceCaller) {
                final String data;
                final SourceCaller sourceCaller = (SourceCaller) caller;
                if (sourceCaller.isDataOnly()) {
                    data = source.data();
                } else {
                    data = okHttpResponse.getHttpContent();
                }
                return new SuccessResult<Data>(origin, (Data) data);
            } else {
                final Result result = parseSuccess(origin, source);
                if (result instanceof SuccessResult) {
                    return (SuccessResult<Data>) result;
                } else {
                    throw new CallException((FailureResult<?>) result);
                }
            }
        } catch (CallException e) {
            throw e;
        } catch (Exception e) {
            throw new CallException(parseFailure(new Result(caller), e));
        }
    }

    private Request createRequest() {
        final MethodVisitor<Data> visitor = getMethodVisitor();
        final String httpMethod = visitor.getHttpMethod();
        final boolean isJsonBody = visitor.isJsonBody();
        final Map<String, String> headers = visitor.getHeaders();
        final List<RequestParam> params = visitor.getRequestParams();

        final Request.Builder requestBuilder = new Request.Builder();

        String clientBaseUrl = visitor.getClient().getBaseUrl();
        String methodBaseUrl = visitor.getBaseUrl();
        String relativeUrl = visitor.getRelativeUrl();

        String baseUrl = methodBaseUrl;
        HttpUrl url = HttpUrl.parse(baseUrl);
        if (url == null) {
            baseUrl = clientBaseUrl;
            url = HttpUrl.parse(baseUrl);
        }
        if (url == null) {
            Utils.error("Cannot resolve the base url: " + baseUrl);
        }

        HttpUrl.Builder urlBuilder = url.newBuilder(relativeUrl);
        if (urlBuilder == null) {
            Utils.error("Cannot resolve the relative url: " + relativeUrl);
        }

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if ("GET".equalsIgnoreCase(httpMethod)) {
            for (RequestParam param : params) {
                final String name = param.getName();
                final String value = param.getValue();
                boolean encode = param.isEncode();
                if (encode) {
                    urlBuilder.addEncodedQueryParameter(name, value);
                } else {
                    urlBuilder.addQueryParameter(name, value);
                }
            }
            requestBuilder.get();
        } else if ("POST".equalsIgnoreCase(httpMethod)) {
            RequestBody requestBody;
            final List<FilePart> parts = visitor.getFileParts();
            if (parts.isEmpty()) {
                if (isJsonBody) {
                    final Map<String, String> map = new HashMap<>();
                    for (RequestParam param : params) {
                        map.put(param.getName(), param.getValue());
                    }
                    String json = getJsonStringParser().parse(map);

                    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                    requestBody = RequestBody.create(mediaType, json);
                } else {
                    FormBody.Builder builder = new FormBody.Builder();
                    for (RequestParam param : params) {
                        boolean encode = param.isEncode();
                        if (encode) {
                            builder.addEncoded(param.getName(), param.getValue());
                        } else {
                            builder.add(param.getName(), param.getValue());
                        }
                    }
                    requestBody = builder.build();
                }
            } else {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                for (FilePart part : parts) {
                    File file = new File(part.getFilePath());
                    if (file.exists()) {
                        RequestBody partBody = RequestBody.create(null, new File(part.getFilePath()));
                        builder.addFormDataPart(part.getName(), part.getFileName(), partBody);
                    }
                }
                for (RequestParam param : params) {
                    builder.addFormDataPart(param.getName(), param.getValue());
                }
                requestBody = builder.build();
            }

            requestBuilder.post(requestBody);
        }

        return requestBuilder
                .url(urlBuilder.build())
                .build();
    }

    protected Request onRequestCreated(Request origin) {
        return origin;
    }

    @Override
    public final void cancel() {
        mCall.cancel();
    }

    @Override
    public OkHttpFailureFactory getFailureFactory() {
        return (OkHttpFailureFactory) super.getFailureFactory();
    }

    protected FailureResult<?> parseFailure(Result origin, Exception e) {
        return new FailureResult<>(origin, getFailureFactory().create(e));
    }
}
