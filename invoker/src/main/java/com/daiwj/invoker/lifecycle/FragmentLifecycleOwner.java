package com.daiwj.invoker.lifecycle;

import androidx.fragment.app.Fragment;

/**
 * author: daiwj on 1/3/21 21:32
 */
public final class FragmentLifecycleOwner extends AbstractLifecycleOwner {

    private ILifecycle mLifecycle;

    public FragmentLifecycleOwner(String name, Fragment fragment) {
        super(name);

        mLifecycle = new AndroidLifecycle(fragment);
    }

    @Override
    public ILifecycle onCreateLifecycle() {
        return mLifecycle;
    }
}
