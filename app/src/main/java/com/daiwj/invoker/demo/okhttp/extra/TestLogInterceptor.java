package com.daiwj.invoker.demo.okhttp.extra;

import android.text.TextUtils;
import android.util.Log;

import com.daiwj.invoker.demo.BuildConfig;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class TestLogInterceptor implements Interceptor {
    private static final String TAG = TestLogInterceptor.class.getSimpleName();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!BuildConfig.DEBUG) {
            return chain.proceed(request);
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Log.d(TAG, "<-- HTTP FAILED: " + e);
            throw e;
        }

        final StringBuilder sb = new StringBuilder();

        if (TextUtils.equals("GET", request.method())) {
            printGetRequestMessage(chain, sb);
        } else if (TextUtils.equals("POST", request.method())) {
            printPostRequestMessage(chain, sb);
        }

        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer responseBuffer = source.getBuffer();
        long contentLength = responseBody.contentLength();

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        sb.append("\n");
        if (contentLength != 0) {
            sb.append("----------RESPONSE----------")
                    .append("\n");
            sb.append("result: ").append(response.code() + " " + response.message())
                    .append("\n");
            sb.append("time: ").append(tookMs + "ms")
                    .append("\n\n");
            sb.append(responseBuffer.clone().readString(UTF8))
                    .append("\n");
        }

        Log.d(TAG, sb.toString());

        return response;
    }

    /**
     * 打印GET请求信息
     *
     * @param chain
     * @return
     */
    private void printGetRequestMessage(Chain chain, StringBuilder sb) {
        Request request = chain.request();
        sb.append(request.method()).append(' ').append(request.url()).append("\n");
        sb.append("header>").append("\n").append(request.headers().toString()).append("\n");
    }

    /**
     * 打印POST请求信息
     *
     * @param chain chain
     * @return
     * @throws IOException exception
     */
    private void printPostRequestMessage(Chain chain, StringBuilder sb) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        if (isPlaintext(buffer)) {
            sb.append(request.method()).append(' ').append(request.url()).append("\n");
            sb.append("header>").append("\n")
                    .append(request.headers().toString())
                    .append("\n");
            sb.append("body>").append("\n")
                    .append(URLDecoder.decode(buffer.readString(charset), "UTF-8"))
                    .append("\n");
        }
    }

}
