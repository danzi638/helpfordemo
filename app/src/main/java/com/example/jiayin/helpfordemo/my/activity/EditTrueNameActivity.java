package com.example.jiayin.helpfordemo.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditTrueNameActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    @Bind(R.id.et_truename)
    EditText etTruename;
    private String objectid;
    private String truename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_true_name);
        ButterKnife.bind(this);
        toolbar.setTitle("修改真实姓名");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        objectid = getIntent().getStringExtra(Constant.OBJECT_ID);
        initListener();
    }

    private void initListener() {
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                truename = etTruename.getText().toString().trim();
                if (truename.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "真实姓名不能为空", Toast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    commitTime();
                }
            }
        });
    }

    private void commitTime() {
        AVObject userDetail = AVObject.createWithoutData("UserDetail", objectid);
        userDetail.put("truename", truename);
        userDetail.put("ispass",2);
        userDetail.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
//                    startActivity(new Intent(EditTrueNameActivity.this, EditorActivity.class));
                    EditTrueNameActivity.this.finish();
                    TastyToast.makeText(getApplicationContext(), "真实姓名修改成功", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                } else {
                    TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);

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