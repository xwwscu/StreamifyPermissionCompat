package com.byton.permissionlib;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import io.reactivex.Observable;

/**
 * Created by wenwu.xie on 2019/10/11.
 */
public class PermissionMgr {
    private static final String TAG = "byton.permission";
    private static PermissionMgr sInstance;
    private PermissionFragment permissionFragment;

    public static PermissionMgr getInstance(@NonNull FragmentActivity activity) {
        if (sInstance == null) {
            sInstance = new PermissionMgr(activity);
        }
        return sInstance;
    }

    public Observable<PermissionResult> rxRequestPermissions(Intent intent, int requestCode) {
        return permissionFragment.rxRequestPermissions(intent, requestCode);
    }

    public Observable<PermissionResult> rxRequestPermissions(@NonNull String[] permissions, int requestCode) {
        return permissionFragment.rxRequestPermissions(permissions, requestCode);
    }

    public LiveData<PermissionResult> vmRequestPermissions(Intent intent, int requestCode) {
        return permissionFragment.vmRequestPermissions(intent, requestCode);
    }

    public LiveData<PermissionResult> vmRequestPermissions(@NonNull String[] permissions, int requestCode) {
        return permissionFragment.vmRequestPermissions(permissions, requestCode);
    }

    public void cbRequestPermissions(@NonNull String[] permissions, int requestCode, @NonNull PermissionRequestCb callback) {
        permissionFragment.cbRequestPermissions(permissions, requestCode, callback);
    }

    private PermissionMgr(@NonNull FragmentActivity activity) {
        permissionFragment = getFragment(activity);
    }

    private PermissionFragment getFragment(@NonNull FragmentActivity activity) {
        FragmentManager fragmentMgr = activity.getSupportFragmentManager();
        PermissionFragment fragment = (PermissionFragment) fragmentMgr.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PermissionFragment();
            fragmentMgr.beginTransaction()
                        .add(fragment, TAG)
                        .commitAllowingStateLoss();
            fragmentMgr.executePendingTransactions();
        }
        return fragment;
    }
}
