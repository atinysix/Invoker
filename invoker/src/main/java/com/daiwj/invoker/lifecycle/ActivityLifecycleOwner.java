package com.daiwj.invoker.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

/**
 * author: daiwj on 1/3/21 20:38
 */
@Deprecated
@SuppressWarnings("deprecation")
public final class ActivityLifecycleOwner extends AbstractLifecycleOwner {

    private ILifecycle mLifecycle;

    public ActivityLifecycleOwner(String name, Activity activity) {
        super(name);

        mLifecycle = new ActivityLifecycle(activity);
    }

    @Override
    public ILifecycle onCreateLifecycle() {
        return mLifecycle;
    }

    public static final class ActivityLifecycle implements ILifecycle {

        private ILifecycleOwner mLifecycleOwner;

        public ActivityLifecycle(Activity activity) {
            final LifecycleFragment fragment = new LifecycleFragment();
            fragment.setLifecycle(this);

            final FragmentManager fm = activity.getFragmentManager();
            fm.beginTransaction()
                    .add(fragment, LifecycleFragment.TAG)
                    .commitAllowingStateLoss();
            fm.executePendingTransactions();
        }

        @Override
        public void setLifecycleOwner(ILifecycleOwner owner) {
            mLifecycleOwner = owner;
        }

        @Override
        public void onDestroy() {
            mLifecycleOwner.onDestroy();
            mLifecycleOwner = null;
        }

        public static class LifecycleFragment extends Fragment {

            protected static final String TAG = "Invoker-LifecycleFragment";

            private ILifecycle mLifecycle;

            public void setLifecycle(ILifecycle lifecycle) {
                mLifecycle = lifecycle;
            }

            @Override
            public void onDestroy() {
                super.onDestroy();

                mLifecycle.onDestroy();
            }
        }
    }

}
