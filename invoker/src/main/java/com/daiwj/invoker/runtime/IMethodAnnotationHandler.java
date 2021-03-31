package com.daiwj.invoker.runtime;

import android.text.TextUtils;

import com.daiwj.invoker.annotation.CallProvider;
import com.daiwj.invoker.annotation.BaseUrl;
import com.daiwj.invoker.annotation.Get;
import com.daiwj.invoker.annotation.Header;
import com.daiwj.invoker.annotation.Post;
import com.daiwj.invoker.annotation.SourceProvider;

import java.lang.annotation.Annotation;

/**
 * author: daiwj on 2020/12/15 20:43
 */
public interface IMethodAnnotationHandler {

    void handle(Annotation source, MethodVisitor<?> visitor);

    IMethodAnnotationHandler POST = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            Post target = (Post) source;
            visitor.setHttpMethod("POST");
            visitor.setRelativeUrl(target.value());
            visitor.setJsonBody(target.isJsonBody());
        }
    };

    IMethodAnnotationHandler BASE_URL = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            BaseUrl target = (BaseUrl) source;
            String baseUrl = target.value();
            if (!TextUtils.isEmpty(baseUrl)) {
                visitor.setBaseUrl(baseUrl);
            }
        }
    };

    IMethodAnnotationHandler GET = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            Get target = (Get) source;
            visitor.setHttpMethod("GET");
            visitor.setRelativeUrl(target.value());
        }
    };

    IMethodAnnotationHandler HEADER = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            Header target = (Header) source;
            final String name = target.name();
            final String value = target.value();
            visitor.getHeaders().put(name, value);
        }
    };

    IMethodAnnotationHandler SOURCE = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            SourceProvider target = (SourceProvider) source;
            visitor.setSourceType(target.value());
        }
    };

    IMethodAnnotationHandler CALL = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            CallProvider target = (CallProvider) source;
            try {
                visitor.setCallFactory(target.value().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
