package com.example.jiayin.helpfordemo.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.MainActivity;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.utils.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayForSuccessActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.tv_show)
    TextView tvShow;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_success);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.pay_for_success);
        money = getIntent().getIntExtra(Constant.MONEY, money);
        tvShow.setText("共支付：￥" + money + "元");
        initListener();
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayForSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                PayForSuccessActivity.this.finish();
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