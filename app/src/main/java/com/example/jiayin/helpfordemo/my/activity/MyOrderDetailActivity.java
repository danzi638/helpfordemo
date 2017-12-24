package com.example.jiayin.helpfordemo.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.MainActivity;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.help.activity.PayForActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.listener.OnBtnClickL;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.widget.NormalDialog;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyOrderDetailActivity extends BaseActivity {

    private static final String TAG = "MyOrderDetailActivity";
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
    @Bind(R.id.fl_pic)
    FrameLayout flPic;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.btn_common)
    Button btnCommon;
    @Bind(R.id.tv_chufa)
    TextView tvChufa;
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
    private int status;

    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_detail);
        ButterKnife.bind(this);
        initData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initListener();

    }

    private void initListener() {

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
        switch (status) {
            case 0:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setText("重新支付");
                btnCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyOrderDetailActivity.this, PayForActivity.class);
                        intent.putExtra(Constant.MONEY, moeny);
                        intent.putExtra(Constant.OBJECT_ID, objectid);
                        startActivity(intent);
                    }
                });
                break;
            case 1:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setText("耐心等待");
                btnCommon.setClickable(false);
                break;
            case 2:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setText("已经送达");
                btnCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogCommitSure();
                    }
                });
                break;
            case 3:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setText("再来一单");
                btnCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MyOrderDetailActivity.this, MainActivity.class));
                        MyOrderDetailActivity.this.finish();
                    }
                });
                break;
            case 4:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setText("再来一单");
                btnCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MyOrderDetailActivity.this, MainActivity.class));
                        MyOrderDetailActivity.this.finish();
                    }
                });
                break;
            case 5:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setText("再来一单");
                btnCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MyOrderDetailActivity.this, MainActivity.class));
                        MyOrderDetailActivity.this.finish();
                    }
                });
                break;
            case 6:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setVisibility(View.GONE);
                tvChufa.setVisibility(View.VISIBLE);
                break;
            case 7:
                tvStatus.setText("订单状态：" + OftenUtils.setStatuText(status));
                btnCommon.setText("确认");
                btnCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSure();
                    }
                });
                break;
        }
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
        status = getIntent().getIntExtra(Constant.STATUS, status);
    }

    private void dialogCommitSure() {
        final NormalDialog dialog = new NormalDialog(MyOrderDetailActivity.this);
        dialog.content("确认物品已经送到求助者手上？")//
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
                        AVObject order_message = AVObject.createWithoutData("order_message", objectid);
                        order_message.put("status", 7);
                        order_message.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    startActivity(new Intent(MyOrderDetailActivity.this, CommitSureActivity.class));
                                } else {
                                    TastyToast.makeText(MyOrderDetailActivity.this, "出错了" + e.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });
    }

    private void dialogSure() {
        final NormalDialog dialog = new NormalDialog(MyOrderDetailActivity.this);
        dialog.content("请确认物品是否已经到达您的手上，且满足您的要求")//
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
                        AVObject order_message = AVObject.createWithoutData("order_message", objectid);
                        order_message.put("status", 3);
                        order_message.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    startActivity(new Intent(MyOrderDetailActivity.this, OrderFinishActivity.class));
                                } else {
                                    TastyToast.makeText(MyOrderDetailActivity.this, "出错了" + e.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            }
                        });
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
