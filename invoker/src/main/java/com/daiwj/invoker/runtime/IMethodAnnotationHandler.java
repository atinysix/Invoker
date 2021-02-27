package com.daiwj.invoker.runtime;

import android.text.TextUtils;

import com.daiwj.invoker.annotation.Host;
import com.daiwj.invoker.annotation.Get;
import com.daiwj.invoker.annotation.Header;
import com.daiwj.invoker.annotation.Post;
import com.daiwj.invoker.annotation.Source;

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
            visitor.mHttpMethod = "POST";
            visitor.mRelativeUrl = target.value();
            visitor.mJsonBody = target.isJsonBody();
        }
    };

    IMethodAnnotationHandler HOST = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            Host target = (Host) source;
            String baseUrl = target.value();
            if (!TextUtils.isEmpty(baseUrl)) {
                visitor.mBaseUrl = baseUrl;
            }
        }
    };

    IMethodAnnotationHandler GET = new IMethodAnnotationHandler() {

        @Override
        public void handle(Annotation source, MethodVisitor<?> visitor) {
            Get target = (Get) source;
            visitor.mHttpMethod = "GET";
            visitor.mRelativeUrl = target.value();
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
            Source target = (Source) source;
            visitor.mSourceType = target.value();
        }
    };
}
