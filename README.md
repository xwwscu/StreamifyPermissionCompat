# StreamifyPermissionCompat
streamify android permission request(rx, livedata, callback)

# rx-way request permissions
PermissionMgr.getInstance(this)
             .rxRequestPermissions(RX_MULTI_PERMISSIONS, 100)
             .subscribe(new Consumer<PermissionResult>() {
                    @Override
                    public void accept(PermissionResult permissionResult) {
                        logRequestResult(permissionResult);
                    }
             });

# viewmodel-way request permissions
PermissionMgr.getInstance(this)
             .vmRequestPermissions(VM_MULTI_PERMISSIONS, 101)
             .observe(this, new Observer<PermissionResult>() {
                    @Override
                    public void onChanged(@Nullable PermissionResult permissionResult) {
                        logRequestResult(permissionResult);
                    }
             });

# callback-way request permissions
PermissionMgr.getInstance(MainActivity.this)
             .cbRequestPermissions(CB_MULTI_PERMISSIONS, 102, new PermissionRequestCb() {
                    @Override
                    public void onRequestPermissionsResult(PermissionResult permissionResult) {
                         logRequestResult(permissionResult);
                    }
             });