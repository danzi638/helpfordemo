package com.example.jiayin.helpfordemo.my.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.MainActivity;
import com.example.jiayin.helpfordemo.helpfor.activity.TakeOrderActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommitSureActivity extends AppCompatActivity {

    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_sure);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("申请成功");
        initListener();
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommitSureActivity.this, MainActivity.class);
                startActivity(intent);
                CommitSureActivity.this.finish();
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