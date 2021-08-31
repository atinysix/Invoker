package com.daiwj.invoker.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.daiwj.invoker.runtime.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * author: daiwj on 1/3/21 20:18
 */
public final class LifecycleOwnerManager {

    public static final String TAG = LifecycleOwnerManager.class.getSimpleName();

    private final List<ILifecycleOwner> mOwners = new LinkedList<>();

    public ILifecycleOwner find(String ownerName) {
        for (ILifecycleOwner owner : mOwners) {
            if (TextUtils.equals(owner.getName(), ownerName)) {
                return owner;
            }
        }
        return null;
    }

    public void add(ILifecycleOwner owner) {
        if (owner != null) {
            mOwners.add(owner);
        }
    }

    public void remove(ILifecycleOwner owner) {
        mOwners.remove(owner);
    }

    public ILifecycleOwner findOrCreate(Context context) {
        if (context instanceof FragmentActivity) {
            return findOrCreate((FragmentActivity) context);
        } else if (context instanceof Activity) {
            return findOrCreate((Activity) context);
        } else if (context instanceof ContextWrapper) {
            return findOrCreate(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public ILifecycleOwner findOrCreate(FragmentActivity activity) {
        if (activity == null) {
            return null;
        }
        final String ownerName = ILifecycleOwner.name(activity);
        ILifecycleOwner owner = find(ownerName);
        if (owner == null) {
            owner = new FragmentActivityLifecycleOwner(ownerName, activity);
            owner.onCreate(this);
        }
        return owner;
    }

    @Deprecated
    public ILifecycleOwner findOrCreate(Activity activity) {
        Logger.e(TAG, "Activity is deprecated, please use FragmentActivity instead!");
        if (activity == null) {
            return null;
        }
        final String ownerName = ILifecycleOwner.name(activity);
        ILifecycleOwner owner = find(ownerName);
        if (owner == null) {
            owner = new ActivityLifecycleOwner(ownerName, activity);
            owner.onCreate(this);
        }
        return owner;
    }

    public ILifecycleOwner findOrCreate(Fragment fragment) {
        if (fragment == null) {
            return null;
        }
        final String ownerName = ILifecycleOwner.name(fragment);
        ILifecycleOwner owner = find(ownerName);
        if (owner == null) {
            owner = new FragmentLifecycleOwner(ownerName, fragment);
            owner.onCreate(this);
        }
        return owner;
    }

}
