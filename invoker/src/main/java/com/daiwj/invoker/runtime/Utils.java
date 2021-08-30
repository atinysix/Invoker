package com.daiwj.invoker.runtime;

import com.daiwj.invoker.exception.InvokerError;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author: daiwj on 2020/12/4 11:27
 */
public class Utils {

    public static <T> void checkNull(T o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }

    public static void error(String message) {
        throw new InvokerError(message);
    }

    public static Type getActualGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actual = parameterizedType.getActualTypeArguments();
            return actual[0];
        } else if (type instanceof Class<?>) {
            return getActualGenericType(((Class<?>) type).getGenericSuperclass());
        }
        return type;
    }

}
