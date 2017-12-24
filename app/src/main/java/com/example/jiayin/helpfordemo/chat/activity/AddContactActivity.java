package com.example.jiayin.helpfordemo.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.chat.model.Model;
import com.example.jiayin.helpfordemo.chat.model.bean.UserInfo;
import com.example.jiayin.helpfordemo.utils.view.CircleImageView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class AddContactActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_add_name)
    EditText et_add_name;
    @Bind(R.id.tv_add_name)
    TextView tv_add_name;
    @Bind(R.id.bt_add_add)
    Button bt_add_add;
    @Bind(R.id.rl_add)
    RelativeLayout rl_add;
    @Bind(R.id.iv_add_photo)
    CircleImageView ivAddPhoto;

    private String name;
    private List<UserInfo> userInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        toolbar.setTitle("添加好友");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initListener();
    }


    private void initListener() {

        // 添加按钮的点击事件处理
        bt_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    // 查找按钮的处理
    private void find() {
        // 获取输入的用户名称
        name = et_add_name.getText().toString();
        // 校验输入的名称
        if (TextUtils.isEmpty(name)) {
            TastyToast.makeText(getApplicationContext(),"输入的用户名称不能为空",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
            return;
        }
        AVQuery is_name = new AVQuery("UserDetail");
        is_name.whereEqualTo("userId", name);
        is_name.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0){
                        rl_add.setVisibility(View.VISIBLE);
                        tv_add_name.setText(name);
                        Glide.with(AddContactActivity.this).load(list.get(0).getAVFile("image").getUrl()).into(ivAddPhoto);
                    }else {
                        rl_add.setVisibility(View.GONE);
                        TastyToast.makeText(getApplicationContext(),"对不起，查无此人",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    // 添加按钮处理
    private void add() {
        userInfos = Model.getInstance().getDbManager().getContactTableDao().getContacts();
        Log.e(TAG, "add: " + userInfos);
        for (int i = 0; i<userInfos.size();i++){
            if (name.equals(userInfos.get(i).getHxid())){
                TastyToast.makeText(getApplicationContext(),"此用户已是您的好友",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                return;
            }

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 去环信服务器添加好友
                    try {
                        EMClient.getInstance().contactManager().addContact(name, "添加好友");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TastyToast.makeText(getApplicationContext(),"发送添加好友邀请成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TastyToast.makeText(getApplicationContext(),"发送添加好友邀请失败",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.search) {
            find();
        }
        return super.onOptionsItemSelected(item);
    }
}
