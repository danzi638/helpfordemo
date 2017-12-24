package com.example.jiayin.helpfordemo.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.chat.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewGroupActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_newgroup_name)
    EditText et_newgroup_name;
    @Bind(R.id.et_newgroup_desc)
    EditText et_newgroup_desc;
    @Bind(R.id.cb_newgroup_public)
    CheckBox cb_newgroup_public;
    @Bind(R.id.cb_newgroup_invite)
    CheckBox cb_newgroup_invite;
    @Bind(R.id.bt_newgroup_create)
    Button bt_newgroup_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_group);
        ButterKnife.bind(this);
        toolbar.setTitle("新建群组");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initListener();
    }

    private void initListener() {
        // 创建按钮的点击事件处理
        bt_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到选择联系人页面
                Intent intent = new Intent(NewGroupActivity.this, PickContactActivity.class);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 成功获取到联系人
        if (resultCode == RESULT_OK) {
            // 创建群
            createGroup(data.getStringArrayExtra("members"));
        }
    }

    // 创建群
    private void createGroup(final String[] memberses) {
        // 群名称
        final String groupName = et_newgroup_name.getText().toString();
        // 群描述
        final String groupDesc = et_newgroup_desc.getText().toString();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器创建群
                // 参数一：群名称；参数二：群描述；参数三：群成员；参数四：原因；参数五：参数设置
                EMGroupOptions option = new EMGroupOptions();
                option.maxUsers = 200;
                EMGroupManager.EMGroupStyle groupStyle = null;

                if (cb_newgroup_public.isChecked()) {//公开
                    if (cb_newgroup_invite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                } else {
                    if (cb_newgroup_invite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }

                option.style = groupStyle; // 创建群的类型

                try {
                    EMClient.getInstance().groupManager().createGroup(groupName, groupDesc, memberses, "申请加入群", option);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群成功", Toast.LENGTH_SHORT).show();

                            // 结束当前页面
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
