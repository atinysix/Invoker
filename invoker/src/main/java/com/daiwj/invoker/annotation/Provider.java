package com.daiwj.invoker.annotation;

import com.daiwj.invoker.runtime.InvokerProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: daiwj on 2020/12/4 10:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Provider {

    String name() default "";

    Class<? extends InvokerProvider> value();

}
