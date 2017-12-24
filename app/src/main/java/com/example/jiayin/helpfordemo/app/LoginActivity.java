package com.example.jiayin.helpfordemo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.chat.model.Model;
import com.example.jiayin.helpfordemo.chat.model.bean.UserInfo;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.view.LoadingDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_progress)
    ProgressBar mProgressView;
    @Bind(R.id.username)
    AutoCompleteTextView mUsernameView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.username_login_button)
    Button mUsernameLoginButton;
    @Bind(R.id.username_register_button)
    Button mUsernameRegisterButton;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private LoadingDialog dialog;

    final String TAG = "LoginActivity";
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        enableSwipe(false);
        initListener();
        initView();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(R.string.login);
        setSupportActionBar(toolbar);


//        if (AVUser.getCurrentUser() != null) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            LoginActivity.this.finish();
//        }
    }

    private void initView() {
//        dialog = new LoadingDialog(this);
//        dialog.setCanceledOnTouchOutside(false);
    }

    private void initListener() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mUsernameLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mUsernameRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    private void attemptLogin() {

        mUsernameView.setError(null);
        mPasswordView.setError(null);

        username = mUsernameView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
//            dialog.show();
//            dialog.setMessage("正在登陆");
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        easeUiLogin();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(Constant.USER_NAME, username);
                        Log.e(TAG, "login: " + "username:" + username + " password:" + password);
                        TastyToast.makeText(getApplicationContext(), "登陆成功" , Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                        LoginActivity.this.finish();
                        startActivity(intent);

                    } else {
//                        showProgress(false);
                        Log.e(TAG, "login: " + "username:" + username + " password:" + password);
                        TastyToast.makeText(getApplicationContext(), "出错了" + e.toString() , Toast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }
            });
        }
    }

    private void easeUiLogin() {
        // 3 登录逻辑处理
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器登录
                EMClient.getInstance().login(username, password, new EMCallBack() {
                    // 登录成功后的处理
                    @Override
                    public void onSuccess() {
                        // 对模型层数据的处理
                        Model.getInstance().loginSuccess(new UserInfo(username));
                        // 保存用户账号信息到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(username));
                    }

                    // 登录失败的处理
                    @Override
                    public void onError(int i, final String s) {
                        // 提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    // 登录过程中的处理
                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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
