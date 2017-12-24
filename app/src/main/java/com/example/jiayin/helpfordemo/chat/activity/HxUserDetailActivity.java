package com.example.jiayin.helpfordemo.chat.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.hyphenate.easeui.EaseConstant;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HxUserDetailActivity extends BaseActivity {

    private static final String TAG = "HxUserDetailActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hx_user_detail);
        ButterKnife.bind(this);
        toolbar.setTitle("关于我们");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initLinstener();

    }

    private void initLinstener() {

    }

    private void initData() {
        userid = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        Log.e(TAG, "initData: " + userid );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
