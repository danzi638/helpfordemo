package com.example.jiayin.helpfordemo.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayForActivity extends BaseActivity {

    private static final String TAG = "PayForActivity";
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.et_beizhu)
    EditText etBeizhu;
    @Bind(R.id.btn_pay)
    Button btnPay;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private int money;
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.pay_for);
        money = getIntent().getIntExtra(Constant.MONEY, money);
        objectId = getIntent().getStringExtra(Constant.OBJECT_ID);
        Log.e(TAG, "onCreate: " + objectId);
        initListener();
    }

    private void initListener() {
        tvMoney.setText("￥:" + money + "元");

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVObject order_message = AVObject.createWithoutData("order_message", objectId);
                order_message.put("status", 1);
                order_message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Intent intent = new Intent(PayForActivity.this, PayForSuccessActivity.class);
                            intent.putExtra(Constant.MONEY, money);
                            startActivity(intent);
                            PayForActivity.this.finish();
                        } else {
                            TastyToast.makeText(getApplicationContext(), "出错了" +e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);
                            Intent intent = new Intent(PayForActivity.this, PayForErrorActivity.class);
                            intent.putExtra("error",e.getMessage());
                            startActivity(intent);
                            PayForActivity.this.finish();

                        }
                    }
                });
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
