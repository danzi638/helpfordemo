package com.example.jiayin.helpfordemo.helpfor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.MainActivity;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.help.activity.PayForSuccessActivity;
import com.example.jiayin.helpfordemo.utils.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TakeOrderActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("成功接单");
        initListener();
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakeOrderActivity.this, MainActivity.class);
                startActivity(intent);
                TakeOrderActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}