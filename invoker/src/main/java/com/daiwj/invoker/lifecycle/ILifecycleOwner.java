package com.daiwj.invoker.lifecycle;

import com.daiwj.invoker.runtime.Caller;

/**
 * author: daiwj on 1/3/21 20:20
 */
public interface ILifecycleOwner {

    String getName();

    void bind(Caller<?> caller);

    void unbind(Caller<?> caller);

    void unbindAll();

    ILifecycle onCreateLifecycle();

    void onCreate(LifecycleOwnerManager manager);

    void onDestroy();

    static <Owner> String name(Owner owner) {
        return owner.getClass().getCanonicalName() + "." + owner.hashCode();
    }

}
