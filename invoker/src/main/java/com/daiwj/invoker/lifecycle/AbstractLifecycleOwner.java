package com.daiwj.invoker.lifecycle;

import androidx.annotation.CallSuper;

import com.daiwj.invoker.runtime.Caller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * author: daiwj on 1/3/21 20:55
 */
public abstract class AbstractLifecycleOwner implements ILifecycleOwner {

    private String mName;
    private List<Caller<?>> mCallers;
    private LifecycleOwnerManager mLifecycleOwnerManager;

    public AbstractLifecycleOwner(String name) {
        mName = name;
        mCallers = new LinkedList<>();
    }

    public String getName() {
        return mName == null ? "" : mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public synchronized void bind(Caller<?> caller) {
        if (!mCallers.contains(caller)) {
            mCallers.add(caller);
        }
    }

    @Override
    public synchronized void unbind(Caller<?> caller) {
        mCallers.remove(caller);
    }

    @Override
    public void unbindAll() {
        synchronized(this) {
            final Iterator<Caller<?>> iterator = mCallers.iterator();
            while (iterator.hasNext()) {
                Caller<?> caller = iterator.next();
                caller.cancel();
                iterator.remove();
            }
        }
    }

    @CallSuper
    @Override
    public void onCreate(LifecycleOwnerManager manager) {
        final ILifecycle lifecycle = onCreateLifecycle();
        lifecycle.setLifecycleOwner(this);

        mLifecycleOwnerManager = manager;
        mLifecycleOwnerManager.add(this);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        unbindAll();
        mLifecycleOwnerManager.remove(this);
    }

}
