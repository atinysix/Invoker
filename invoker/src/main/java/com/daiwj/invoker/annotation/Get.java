package com.daiwj.invoker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: daiwj on 2020/12/2 21:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Get {

    /**
     * api path like: service/api
     *
     * @return
     */
    String value();

}
