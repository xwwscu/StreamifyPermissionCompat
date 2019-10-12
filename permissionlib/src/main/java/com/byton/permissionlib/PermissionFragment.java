package com.byton.permissionlib;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by xwwscu on 2019/10/11.
 */
public class PermissionFragment extends Fragment {
    private static final int TYPE_PERMISSION_REQUEST_RX = 1;
    private static final int TYPE_PERMISSION_REQUEST_VM = 2;
    private static final int TYPE_PERMISSION_REQUEST_CB = 3;

    private SparseArray<PublishSubject<PermissionResult>> requestRxSubjects;
    private SparseArray<MutableLiveData<PermissionResult>> requestLiveDatas;
    private SparseArray<PermissionRequestCb> requestCallbacks;

    public PermissionFragment() {
        requestRxSubjects = new SparseArray<>();
        requestLiveDatas = new SparseArray<>();
        requestCallbacks = new SparseArray<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    Observable<PermissionResult> rxRequestPermissions(final Intent intent, final int requestCode) {
        checkRequestConflicts(requestCode, TYPE_PERMISSION_REQUEST_RX);
        PublishSubject<PermissionResult> subject = PublishSubject.create();
        requestRxSubjects.put(requestCode, subject);
        return subject.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) {
                startActivityForResult(intent, requestCode);
            }
        });
    }

    Observable<PermissionResult> rxRequestPermissions(@NonNull final String[] permissions, final int requestCode) {
        checkRequestConflicts(requestCode, TYPE_PERMISSION_REQUEST_RX);
        PublishSubject<PermissionResult> subject = PublishSubject.create();
        requestRxSubjects.put(requestCode, subject);
        return subject.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) {
                requestPermissions(permissions, requestCode);
            }
        });
    }

    private void checkRequestConflicts(int requestCode, int requestType) {
        if (requestType == TYPE_PERMISSION_REQUEST_RX) {
            PublishSubject origin = requestRxSubjects.get(requestCode);
            if (origin != null) {
                throw new RuntimeException("rx permission requestCode conflicts!");
            }
        } else if (requestType == TYPE_PERMISSION_REQUEST_VM) {
            LiveData origin = requestLiveDatas.get(requestCode);
            if (origin != null) {
                throw new RuntimeException("vm permission requestCode conflicts!");
            }
        } else if (requestType == TYPE_PERMISSION_REQUEST_CB) {
            PermissionRequestCb cb = requestCallbacks.get(requestCode);
            if (cb != null) {
                throw new RuntimeException("cb permission requestCode conflicts!");
            }
        }
    }

    LiveData<PermissionResult> vmRequestPermissions(Intent intent, int requestCode) {
        checkRequestConflicts(requestCode, TYPE_PERMISSION_REQUEST_VM);
        MutableLiveData<PermissionResult> liveData = new MutableLiveData<>();
        requestLiveDatas.put(requestCode, liveData);
        startActivityForResult(intent, requestCode);
        Log.i("xww-op", "vmRequestPermissions intent execute ---> 1111");
        return liveData;
    }

    LiveData<PermissionResult> vmRequestPermissions(@NonNull String[] permissions, int requestCode) {
        checkRequestConflicts(requestCode, TYPE_PERMISSION_REQUEST_VM);
        MutableLiveData<PermissionResult> liveData = new MutableLiveData<>();
        requestLiveDatas.put(requestCode, liveData);
        requestPermissions(permissions, requestCode);
        return liveData;
    }

    void cbRequestPermissions(@NonNull String[] permissions, int requestCode, @NonNull PermissionRequestCb callback) {
        checkRequestConflicts(requestCode, TYPE_PERMISSION_REQUEST_CB);
        requestCallbacks.put(requestCode, callback);
        requestPermissions(permissions, requestCode);
    }

    private void onRequestPermissionsResult(PermissionResult result) {
        int requestCode = result.requestCode;
        PublishSubject<PermissionResult> subject = requestRxSubjects.get(requestCode);
        if (subject != null) {
            subject.onNext(result);
            subject.onComplete();
            requestRxSubjects.remove(requestCode);
        }
        MutableLiveData<PermissionResult> liveData = requestLiveDatas.get(requestCode);
        if (liveData != null) {
            liveData.setValue(result);
            requestLiveDatas.remove(requestCode);
        }
        PermissionRequestCb callback = requestCallbacks.get(requestCode);
        if (callback != null) {
            callback.onRequestPermissionsResult(result);
            requestCallbacks.remove(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionResult opResult = new PermissionResult(requestCode, permissions, grantResults);
        this.onRequestPermissionsResult(opResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // request permissions by intent work?
        /*PermissionResult opResult = new PermissionResult(requestCode, permissions, grantResults);
        this.onRequestPermissionsResult(opResult);*/
    }
}
