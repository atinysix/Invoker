package com.daiwj.invoker.lifecycle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * author: daiwj on 1/12/21 17:04
 */
public class AndroidLifecycle implements ILifecycle {

    private ILifecycleOwner mLifecycleOwner;
    private Lifecycle mLifecycle;

    public AndroidLifecycle(FragmentActivity activity) {
        mLifecycle = activity.getLifecycle();
        mLifecycle.addObserver(SYSTEM);
    }

    public AndroidLifecycle(Fragment fragment) {
        mLifecycle = fragment.getLifecycle();
        mLifecycle.addObserver(SYSTEM);
    }

    @Override
    public void setLifecycleOwner(ILifecycleOwner owner) {
        mLifecycleOwner = owner;
    }

    @Override
    public void onDestroy() {
        mLifecycle.removeObserver(SYSTEM);
        mLifecycle = null;

        mLifecycleOwner.onDestroy();
        mLifecycleOwner = null;
    }

    private LifecycleEventObserver SYSTEM = new LifecycleEventObserver() {

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                onDestroy();
            }
        }
    };

}
