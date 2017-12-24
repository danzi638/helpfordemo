package com.example.jiayin.helpfordemo.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    @Bind(R.id.register_progress)
    ProgressBar registerProgress;
    @Bind(R.id.username)
    AutoCompleteTextView username;
    @Bind(R.id.verification)
    EditText verification;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.password_again)
    EditText passwordAgain;
    @Bind(R.id.username_register_button)
    Button usernameRegisterButton;
    @Bind(R.id.email_register_form)
    LinearLayout emailRegisterForm;
    @Bind(R.id.btn_send_verification)
    Button btnSendVerification;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private boolean flag;  // 操作是否成功
    //按钮样式改变
    //控制按钮样式是否改变
    private boolean tag = true;
    //每次验证请求需要间隔60S
    private int i = 30;
    private String usernames;
    private String verifications;
    private String passwords;
    private String passwordAagain;
    private ArrayList<AVObject> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.register);
        initListener();

        EventHandler eventHandler = new EventHandler() {    // 操作回调
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);   // 注册回调接口

    }

    private void initListener() {

        btnSendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(username.getText())) {
                    if (username.getText().length() == 11) {
                        final String phoneNumber = username.getText().toString();
                        SMSSDK.getVerificationCode("86", phoneNumber); // 发送验证码给号码的 phoneNumber 的手机
                        username.requestFocus();
                    } else {
                        TastyToast.makeText(getApplicationContext(), "请输入完整电话号码" , Toast.LENGTH_SHORT, TastyToast.WARNING);
                        username.requestFocus();
                    }
                } else {
                    TastyToast.makeText(getApplicationContext(), "请输入电话号码" , Toast.LENGTH_SHORT, TastyToast.WARNING);
                    username.requestFocus();
                }
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();

                    return true;
                }
                return false;
            }
        });
        usernameRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        username.setError(null);
        password.setError(null);
        verification.setError(null);
        passwordAgain.setError(null);

        usernames = username.getText().toString().trim();
        verifications = verification.getText().toString().trim();
        passwords = password.getText().toString().trim();
        passwordAagain = passwordAgain.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;
        //判断电话合法性
        if (!TextUtils.isEmpty(usernames) && !isPhoneValid(usernames)) {
            username.setError(getString(R.string.error_invalid_phone));
            focusView = username;
            cancel = true;
        }
        //判断密码合法性
        if (!TextUtils.isEmpty(passwords) && !isPasswordValid(passwords)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }
        //判断验证码合法性
        if (!TextUtils.isEmpty(verifications) && !isVerificationsValid(verifications)) {
            verification.setError(getString(R.string.error_invalid_verifications));
            focusView = verification;
            cancel = true;
        }
        //判断再次输入密码合法性
        if (!TextUtils.isEmpty(passwordAagain) && !isPasswordValid(passwordAagain)) {
            passwordAgain.setError(getString(R.string.error_invalid_password));
            focusView = passwordAgain;
            cancel = true;
        }
        //判断两次密码是否一致
        if (!passwordAagain.equals(passwords)) {
            passwordAgain.setError(getString(R.string.error_password_again));
            focusView = passwordAgain;
            cancel = true;
        }

//        if ()
        if (TextUtils.isEmpty(usernames)) {
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            //执行内容
            SMSSDK.submitVerificationCode("86", usernames, verifications);
            flag = false;
        }
    }

    private void AddToLeanCloud() {
        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(usernames);// 设置用户名
        user.setMobilePhoneNumber(usernames);//设置手机号码
        user.setPassword(passwords);// 设置密码

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    Intent intent = new Intent(RegisterActivity.this, PrefectInformationActivity.class);
                    intent.putExtra(Constant.USER_NAME, usernames);
                    intent.putExtra(Constant.USER_PAS, passwords);
                    Log.e(TAG, "done: " + usernames);
                    Log.e(TAG, "done: " + passwords);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    TastyToast.makeText(getApplicationContext(), "出错了" +e.toString() , Toast.LENGTH_SHORT, TastyToast.ERROR);
                    return;
                }
            }
        });
    }

    //判断验证码合法性的逻辑
    private boolean isVerificationsValid(String verifications) {
        return verifications.length() == 4;
    }

    //判断电话合法性的逻辑
    private boolean isPhoneValid(String usernames) {
        return usernames.length() == 11 && (isNumeric(usernames));
    }

    //判断是否为数字的逻辑
    public static boolean isNumeric(String usernames) {
        for (int i = 0; i < usernames.length(); i++) {
            System.out.println(usernames.charAt(i));
            if (!Character.isDigit(usernames.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //判断密码合法性的逻辑
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    //判断验证码是否正确的逻辑
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if (result == SMSSDK.RESULT_COMPLETE) {
                // 如果操作成功
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 校验验证码，返回校验的手机和国家代码
                    TastyToast.makeText(getApplicationContext(), "验证成功" , Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                    AddToLeanCloud();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功，true为智能验证，false为普通下发短信
                    changeBtnGetCode();
                    TastyToast.makeText(getApplicationContext(), "验证码已发送" , Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                // 如果操作失败
                if (flag) {
                    TastyToast.makeText(getApplicationContext(), "验证码获取失败，请重新获取" , Toast.LENGTH_SHORT, TastyToast.ERROR);
                    username.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "验证码错误" , Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        }
    };

    /**
     * 改变按钮样式
     */
    private void changeBtnGetCode() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (RegisterActivity.this == null) {
                            break;
                        }
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSendVerification.setText("重新发送(" + i + "s" + ")");
                                btnSendVerification.setClickable(false);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 30;
                tag = true;

                if (RegisterActivity.this != null) {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnSendVerification.setText("获取验证码");
                            btnSendVerification.setClickable(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler(); // 注销回调接口
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }
}
