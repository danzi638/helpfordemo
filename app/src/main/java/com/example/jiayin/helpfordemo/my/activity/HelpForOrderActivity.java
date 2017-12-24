package com.example.jiayin.helpfordemo.my.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.app.base.BaseFragmentActivity;
import com.example.jiayin.helpfordemo.chat.adapter.HomeFragmentPageAdapter;
import com.example.jiayin.helpfordemo.my.fragment.AllAllOrderFragment;
import com.example.jiayin.helpfordemo.my.fragment.AllFinishFragment;
import com.example.jiayin.helpfordemo.my.fragment.AllUnfinishFragment;
import com.example.jiayin.helpfordemo.my.fragment.HelpForAllOrderFragment;
import com.example.jiayin.helpfordemo.my.fragment.HelpForFinishOrderFragment;
import com.example.jiayin.helpfordemo.my.fragment.HelpForUnFinishOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpForOrderActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rb_allorder)
    RadioButton rbAllorder;
    @Bind(R.id.rb_finishorder)
    RadioButton rbFinishorder;
    @Bind(R.id.rb_unfinishorder)
    RadioButton rbUnfinishorder;
    @Bind(R.id.rg_main)
    RadioGroup rgMain;
    @Bind(R.id.vp_content)
    ViewPager vpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_for_order);
        ButterKnife.bind(this);
        toolbar.setTitle("帮忙订单");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        rbFinishorder.setText("已接订单");
        rbFinishorder.isChecked();
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.rb_allorder:
                        vpContent.setCurrentItem(0);
                        break;
                    case R.id.rb_finishorder:
                        vpContent.setCurrentItem(1);
                        break;
                    case R.id.rb_unfinishorder:
                        vpContent.setCurrentItem(2);
                        break;
                }
            }
        });
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new HelpForAllOrderFragment());
        mFragmentList.add(new HelpForFinishOrderFragment());
        mFragmentList.add(new HelpForUnFinishOrderFragment());
        vpContent.setAdapter(new HomeFragmentPageAdapter(getSupportFragmentManager(),mFragmentList));
        vpContent.setCurrentItem(1);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rgMain.check(R.id.rb_allorder);
                        break;
                    case 1:
                        rgMain.check(R.id.rb_finishorder);
                        break;
                    case 2:
                        rgMain.check(R.id.rb_unfinishorder);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
