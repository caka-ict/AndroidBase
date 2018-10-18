package com.caka.base.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.caka.base.utils.AppUtils;
import com.caka.base.utils.KeyboardUtils;
import com.caka.base.widget.progress.ProgressBarCaka;

public abstract class GlobalActivity extends AppCompatActivity implements GlobalFragment.Callback {


    private ProgressBarCaka mProgressDialog;
    public final int REQUEST_PERMISSIONS = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            onPermissionsGranted(requestCode);
        } else {
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions, final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                ActivityCompat.requestPermissions(GlobalActivity.this, requestedPermissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        }
    }

    public abstract void openLoginActivity();

    public abstract void handlerCheckAuthent(boolean isAuthented);

    public void showLoading(int logoLoading) {
        hideLoading();
        mProgressDialog = AppUtils.showLoading(this, logoLoading);
    }

    public void showLoading(int logoLoading, String labelLoading) {
        hideLoading();
        mProgressDialog = AppUtils.showLoading(this, logoLoading, labelLoading);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void hideKeyboard(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    KeyboardUtils.hideSoftInput(GlobalActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboard(innerView);
            }
        }
    }

    public boolean isNetworkConnected() {
        return AppUtils.isNetworkAvailable();
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }
}
