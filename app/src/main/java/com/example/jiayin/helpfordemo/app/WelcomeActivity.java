package com.example.jiayin.helpfordemo.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.chat.model.Model;
import com.example.jiayin.helpfordemo.chat.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isFinishing()) {
                return;
            }
            toMainOrLogin();
        }
    };
// 判断进入主页面还是登录页面

    private void toMainOrLogin() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
//                 判断当前账号是否已经登录过
                if (EMClient.getInstance().isLoggedInBefore()) {// 登录过
//                     获取到当前登录用户的信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                    if (account == null) {
                        // 跳转到登录页面
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
//                         登录成功后的方法
                        Model.getInstance().loginSuccess(account);

//                         跳转到主页面
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {// 没登录过
//                     跳转到登录页面
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                // 结束当前页面
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        enableSwipe(true);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_welcome);
        handler.sendMessageDelayed(Message.obtain(), 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
