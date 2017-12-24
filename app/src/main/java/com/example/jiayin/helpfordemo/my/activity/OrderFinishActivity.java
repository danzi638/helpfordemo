package com.example.jiayin.helpfordemo.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.MainActivity;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderFinishActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.btn_again)
    Button btnAgain;
    @Bind(R.id.btn_share)
    Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finish);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("订单完成");
        initListener();
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderFinishActivity.this, MainActivity.class);
                startActivity(intent);
                OrderFinishActivity.this.finish();
            }
        });
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(getApplicationContext(),"不好意思，再来一单尚未开发",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(getApplicationContext(),"不好意思，再来一单尚未开发",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
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