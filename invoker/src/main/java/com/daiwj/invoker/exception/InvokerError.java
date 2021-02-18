package com.daiwj.invoker.exception;

/**
 * author: daiwj on 2020/12/7 20:44
 */
public class InvokerError extends RuntimeException {

    private static final long serialVersionUID = 3751204215034499923L;

    public InvokerError(String message) {
        super(message);
    }

}
