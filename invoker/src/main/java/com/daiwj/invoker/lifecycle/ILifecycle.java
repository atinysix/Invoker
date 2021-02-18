package com.daiwj.invoker.lifecycle;

/**
 * author: daiwj on 1/12/21 15:58
 */
public interface ILifecycle {

    void setLifecycleOwner(ILifecycleOwner owner);

    void onDestroy();

}
