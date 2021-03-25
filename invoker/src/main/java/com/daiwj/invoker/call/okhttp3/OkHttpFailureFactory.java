package com.daiwj.invoker.call.okhttp3;

import com.daiwj.invoker.runtime.IFailure;
import com.daiwj.invoker.runtime.ISource;

/**
 * author: daiwj on 1/1/21 22:06
 */
public interface OkHttpFailureFactory extends IFailure.Factory {

    OkHttpFailureFactory DEFAULT = new OkHttpFailureFactory() {

        @Override
        public IFailure create(Exception e) {
            return new IFailure() {

                @Override
                public String getMessage() {
                    return e.getMessage();
                }
            };
        }

        @Override
        public IFailure create(String message) {
            return new IFailure() {

                @Override
                public String getMessage() {
                    return message;
                }
            };
        }

        @Override
        public IFailure create(ISource source) {
            return new IFailure() {

                @Override
                public String getMessage() {
                    return "please custom your failure by implementing OkHttpFailureFactory";
                }
            };
        }
    };

    IFailure create(Exception e);

}
