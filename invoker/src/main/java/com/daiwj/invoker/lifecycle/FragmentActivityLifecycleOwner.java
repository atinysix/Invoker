package com.daiwj.invoker.lifecycle;

import androidx.fragment.app.FragmentActivity;

/**
 * author: daiwj on 1/3/21 20:38
 */
public final class FragmentActivityLifecycleOwner extends AbstractLifecycleOwner {

    private ILifecycle mLifecycle;

    public FragmentActivityLifecycleOwner(String name, FragmentActivity activity) {
        super(name);

        mLifecycle = new AndroidLifecycle(activity);
    }

    @Override
    public ILifecycle onCreateLifecycle() {
        return mLifecycle;
    }
}
