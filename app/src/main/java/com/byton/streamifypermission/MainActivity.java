package com.byton.streamifypermission;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.byton.permissionlib.PermissionMgr;
import com.byton.permissionlib.PermissionRequestCb;
import com.byton.permissionlib.PermissionResult;

import io.reactivex.functions.Consumer;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "xwwscu-op";
    private static final String[] RX_MULTI_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String[] VM_MULTI_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS
    };
    private static final String[] CB_MULTI_PERMISSIONS = {
            Manifest.permission.READ_SMS, Manifest.permission.READ_CALENDAR
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Button rxMultiOpBtn = findViewById(R.id.test_rx_multi_op_btn);
        Button vmMultiOpBtn = findViewById(R.id.test_vm_multi_op_btn);
        Button cbMultiOpBtn = findViewById(R.id.test_cb_multi_op_btn);
        rxMultiOpBtn.setOnClickListener(this);
        vmMultiOpBtn.setOnClickListener(this);
        cbMultiOpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.test_rx_multi_op_btn) {
            PermissionMgr.getInstance(this)
                    .rxRequestPermissions(RX_MULTI_PERMISSIONS, 100)
                    .subscribe(new Consumer<PermissionResult>() {
                        @Override
                        public void accept(PermissionResult permissionResult) {
                            logRequestResult(permissionResult);
                        }
                    });
        } else if (resId == R.id.test_vm_multi_op_btn) {
            PermissionMgr.getInstance(this)
                    .vmRequestPermissions(VM_MULTI_PERMISSIONS, 101)
                    .observe(this, new Observer<PermissionResult>() {
                        @Override
                        public void onChanged(@Nullable PermissionResult permissionResult) {
                            logRequestResult(permissionResult);
                        }
                    });
            /*Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + MainActivity.this.getPackageName()));
            PermissionMgr.getInstance(this)
                    .rxRequestPermissions(intent, 102)
                    .subscribe(new Consumer<PermissionResult>() {
                        @Override
                        public void accept(PermissionResult permissionResult) throws Exception {
                            logRequestResult(permissionResult);
                        }
                    });
            PermissionMgr.getInstance(this)
                    .vmRequestPermissions(intent, 103)
                    .observe(this, new Observer<PermissionResult>() {
                        @Override
                        public void onChanged(PermissionResult permissionResult) {
                            logRequestResult(permissionResult);
                        }
                    });*/
        } else if (resId == R.id.test_cb_multi_op_btn) {
            PermissionMgr.getInstance(MainActivity.this)
                    .cbRequestPermissions(CB_MULTI_PERMISSIONS, 102, new PermissionRequestCb() {
                        @Override
                        public void onRequestPermissionsResult(PermissionResult permissionResult) {
                            logRequestResult(permissionResult);
                        }
                    });
        }
    }

    private void logRequestResult(PermissionResult permissionResult) {
        if (permissionResult == null) {
            Log.e(TAG, "Permission result null!!!");
            return;
        }
        StringBuilder sb = new StringBuilder("requestCode: " + permissionResult.requestCode + ",");
        for (int indx = 0; indx < permissionResult.permissions.length; indx++) {
            sb.append(permissionResult.permissions[indx]);
            sb.append(": " + permissionResult.grantResults[indx] + ",");
        }
        Log.i(TAG, sb.toString());
    }
}
