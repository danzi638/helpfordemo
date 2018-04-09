package com.example.jiayin.helpfordemo.app.base;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.utils.permission.PermissionsManager;
import com.example.jiayin.helpfordemo.utils.permission.PermissionsResultAction;


/**
 * every project should have one BaseActivity,so here we are
 */
public class BaseActivity extends SwipeBackAppCompatActivity {
    @Override
    public void setContentView(int layoutResID) {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//              Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
        super.setContentView(layoutResID);

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
    public void hideKeyboard() {
        View viewFocus = this.getCurrentFocus();
        if (viewFocus != null) {
            InputMethodManager imManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }
    }
}
