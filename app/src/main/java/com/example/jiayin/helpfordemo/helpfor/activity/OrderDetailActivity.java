package com.example.jiayin.helpfordemo.helpfor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.chat.activity.ChatActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.listener.OnBtnClickL;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.widget.NormalDialog;
import com.hyphenate.chat.EMClient;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderDetailActivity extends BaseActivity {


    private static final String TAG = "OrderDetailActivity";
    @Bind(R.id.order_image_view)
    ImageView orderImageView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.tv_you_want_thing)
    TextView tvYouWantThing;
    @Bind(R.id.tv_truename)
    TextView tvTruename;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_endlocation)
    TextView tvEndlocation;
    @Bind(R.id.tv_endlocation_detail)
    TextView tvEndlocationDetail;
    @Bind(R.id.tv_startlocation)
    TextView tvStartlocation;
    @Bind(R.id.tv_startlocation_detail)
    TextView tvStartlocationDetail;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_enddate)
    TextView tvEnddate;
    @Bind(R.id.btn_chat)
    FloatingActionButton btnChat;
    @Bind(R.id.btn_commit)
    FloatingActionButton btnCommit;
    @Bind(R.id.fl_pic)
    FrameLayout flPic;
    private String objectid;
    private String userid;
    private String you_want_thing;
    private String startLocation;
    private String startLocationDetail;
    private String endLocation;
    private String endLocationDetail;
    private String truename;
    private String phone;
    private int moeny;
    private String endDate;
    private String path;


    private double mStartCurrentLat;
    private double mStartCurrentLng;
    private double mEndCurrentLat;
    private double mEndCurrentLng;

    private BaseAnimatorSet bas_in;
    private BaseAnimatorSet bas_out;

    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initListener();

    }

    private void initListener() {
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, ChatActivity.class);
                intent.putExtra(Constant.USER_ID, userid);
                startActivity(intent);
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NormalDialogStyleTwo();
            }
        });
    }

    private void initView() {
        collapsingToolbar.setTitle(you_want_thing);
        Glide.with(this).load(path).into(orderImageView);
        tvYouWantThing.setText("商品名称：" + you_want_thing);
        tvTruename.setText("联系人：" + truename);
        tvPhone.setText("联系方式：" + phone);
        tvEndlocation.setText("物品所在地：" + endLocation);
        tvEndlocationDetail.setText("物品所在地详情：" + endLocationDetail);
        tvStartlocation.setText("收货地点：" + startLocation);
        tvStartlocationDetail.setText("收货地点详情：" + startLocationDetail);
        tvMoney.setText("悬赏金额：" + moeny + "元");
        tvEnddate.setText("期限：" + endDate);
    }

    private void initData() {
        objectid = getIntent().getStringExtra(Constant.OBJECT_ID);
        userid = getIntent().getStringExtra(Constant.USER_ID);
        you_want_thing = getIntent().getStringExtra(Constant.YOU_WANT_THING);
        startLocation = getIntent().getStringExtra(Constant.START_LOCATION);
        startLocationDetail = getIntent().getStringExtra(Constant.START_LOCATION_DETAIL);
        endLocation = getIntent().getStringExtra(Constant.END_LOCATION);
        endLocationDetail = getIntent().getStringExtra(Constant.END_LOCATION_DETAIL);
        truename = getIntent().getStringExtra(Constant.TRUE_NAME);
        phone = getIntent().getStringExtra(Constant.PHONE);
        moeny = getIntent().getIntExtra(Constant.MONEY, moeny);
        endDate = getIntent().getStringExtra(Constant.END_DATE);
        path = getIntent().getStringExtra(Constant.PATH);
        mStartCurrentLat = getIntent().getDoubleExtra(Constant.START_CURRENT_LAT, mStartCurrentLat);
        mStartCurrentLng = getIntent().getDoubleExtra(Constant.START_CURRENT_LNG, mStartCurrentLng);
        mEndCurrentLat = getIntent().getDoubleExtra(Constant.END_CURRENT_LAT, mEndCurrentLat);
        mEndCurrentLng = getIntent().getDoubleExtra(Constant.END_CURRENT_LNG, mEndCurrentLng);
    }

    private void NormalDialogStyleTwo() {
        final NormalDialog dialog = new NormalDialog(OrderDetailActivity.this);
        dialog.content("接单后无法撤回,未在期限内完成扣除信用分")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        Log.e(TAG, "onBtnClick: " + "取消");
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        Log.e(TAG, "onBtnClick: " + OftenUtils.timeValue(endDate) + ":" + OftenUtils.getLocalTime() + ":" + endDate);
                        if (OftenUtils.timeValue(endDate) <= 0) {
                            TastyToast.makeText(OrderDetailActivity.this, "订单已过期", Toast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            AVObject order_message = AVObject.createWithoutData("order_message", objectid);
                            Log.e(TAG, "onBtnClick: " + objectid);
                            order_message.put("status", 2);
                            order_message.put("handuserid", EMClient.getInstance().getCurrentUser());
                            order_message.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        Intent intent = new Intent(OrderDetailActivity.this, TakeOrderActivity.class);
                                        intent.putExtra(Constant.USER_ID, EMClient.getInstance().getCurrentUser());
                                        startActivity(intent);
                                    } else {
                                        TastyToast.makeText(OrderDetailActivity.this, "出错了" + e.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                }
                            });
                        }
                        dialog.dismiss();
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
