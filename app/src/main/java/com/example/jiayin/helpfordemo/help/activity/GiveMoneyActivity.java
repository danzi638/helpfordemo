package com.example.jiayin.helpfordemo.help.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.PrefectInformationActivity;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.GlideImageLoader;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.listener.OnOperItemClickL;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.widget.ActionSheetDialog;
import com.hyphenate.chat.EMClient;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.sdsmdg.tastytoast.TastyToast;

import org.feezu.liuli.timeselector.TimeSelector;
import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GiveMoneyActivity extends BaseActivity {


    private static final String TAG = "GiveMoneyActivity";
    @Bind(R.id.you_want_thing)
    EditText youWantThing;
    @Bind(R.id.start_location)
    EditText startLocation;
    @Bind(R.id.start_location_detail)
    EditText startLocationDetail;
    @Bind(R.id.end_location)
    EditText endLocation;
    @Bind(R.id.end_location_detail)
    EditText endLocationDetail;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.end_date)
    TextView endDate;
    @Bind(R.id.seek_bar)
    SeekBar seekBar;
    @Bind(R.id.text_money)
    TextView textMoney;
    @Bind(R.id.et_good_detail)
    EditText etGoodDetail;
    @Bind(R.id.imageAction)
    ImageView imageAction;
    @Bind(R.id.image_show)
    ImageView imageShow;
    @Bind(R.id.btn_publish_reward)
    Button btnPublishReward;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private TimeSelector timeSelector;
    private String endTime;
    private String you_want_thing;
    private String start_location;
    private String start_location_detail;
    private String end_location;
    private String end_location_detail;
    private String phone_number;
    private int money;
    private String thing_detail;
    private double mStartCurrentLat;
    private double mStartCurrentLng;
    private double mEndCurrentLat;
    private double mEndCurrentLng;
    private String truename;
    private String idcard;
    private int ispass;

    private byte[] mImageBytes = null;
    ArrayList<ImageItem> images = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_money);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.give_money);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getTrueName();

        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                endTime = time;
                endDate.setText(" 期限  " + endTime);

            }
        }, getLocalTime(), "2020-12-31 00:00"
        );
        timeSelector.setMode(TimeSelector.MODE.YMDHM);
        initImagePicker();
        initData();
        initListener();
    }

    private void initData() {
        money = getIntent().getIntExtra(Constant.MONEY, money);
        Log.e(TAG, "initData: " + money);
        start_location = getIntent().getStringExtra(Constant.START_LOCATION);
        end_location = getIntent().getStringExtra(Constant.END_LOCATION);
        mStartCurrentLat = getIntent().getDoubleExtra(Constant.START_CURRENT_LAT, mStartCurrentLat);
        mStartCurrentLng = getIntent().getDoubleExtra(Constant.START_CURRENT_LNG, mStartCurrentLng);
        mEndCurrentLat = getIntent().getDoubleExtra(Constant.END_CURRENT_LAT, mEndCurrentLat);
        mEndCurrentLng = getIntent().getDoubleExtra(Constant.END_CURRENT_LNG, mEndCurrentLng);

        textMoney.setText("￥" + money + "元");
        seekBar.setProgress(money);
        startLocation.setText(start_location);
        endLocation.setText(end_location);
        phone.setText(EMClient.getInstance().getCurrentUser());


    }

       private String getLocalTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return year + "-" + month + "-" + day + " " + hour + ":" + minute;
    }


    private void initListener() {
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelector.show();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textMoney.setText("￥" + progress + "元");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imageAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialogNoTitle();
            }
        });
        btnPublishReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (truename.equals("")||idcard.equals("")||ispass==2||ispass==0){
                    TastyToast.makeText(getApplication(),"实名认证后才能发布订单哦",TastyToast.LENGTH_LONG,TastyToast.WARNING);
                }else {
                    commit();
                }
            }
        });
    }

    /**
     * 数据存贮：
     * 你想要的东西：you_want_thing:youWantThing
     * 起始位置：start_location:startLocation
     * 起始位置详情：start_location_detail:startLocationDetail
     * 结束位置：end_location:endLocation
     * 结束位置详情：end_location_detail:endLocationDetail
     * 电话号码：phone_number:phone
     * 期限：纯日期：endTime---TextView:endDate
     * 悬赏金额：String:moneys---int:money
     * 你想要的东西详情：thing_detail：etGoodDetail
     */
    private void commit() {
        you_want_thing = youWantThing.getText().toString().trim();
        start_location = startLocation.getText().toString().trim();
        start_location_detail = startLocationDetail.getText().toString().trim();
        end_location = endLocation.getText().toString().trim();
        end_location_detail = endLocationDetail.getText().toString().trim();
        phone_number = phone.getText().toString().trim();
        String moneys = String.valueOf(seekBar.getProgress());
        thing_detail = etGoodDetail.getText().toString().trim();

        Log.e(TAG, "commit: " + you_want_thing);
        Log.e(TAG, "commit: " + start_location);
        Log.e(TAG, "commit: " + start_location_detail);
        Log.e(TAG, "commit: " + end_location);
        Log.e(TAG, "commit: " + end_location_detail);
        Log.e(TAG, "commit: " + phone_number);
        Log.e(TAG, "commit: " + moneys);
        Log.e(TAG, "commit: " + endTime);
        Log.e(TAG, "commit: " + thing_detail);
        Log.e(TAG, "commit: " + mStartCurrentLat);
        Log.e(TAG, "commit: " + mStartCurrentLng);
        Log.e(TAG, "commit: " + mEndCurrentLat);
        Log.e(TAG, "commit: " + mEndCurrentLng);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(you_want_thing)) {
            youWantThing.setError(getString(R.string.empty_you_want_thing));
            focusView = youWantThing;
            cancel = true;
        }
        if (TextUtil.isEmpty(start_location)) {
            startLocation.setError(getString(R.string.empty_start_location));
            focusView = startLocation;
            cancel = true;
        }
        if (TextUtil.isEmpty(start_location_detail)) {
            startLocationDetail.setError(getString(R.string.empty_start_location_detail));
            focusView = startLocationDetail;
            cancel = true;
        }
        if (TextUtil.isEmpty(end_location)) {
            endLocation.setError(getString(R.string.empty_end_location));
            focusView = endLocation;
            cancel = true;
        }
//        if (TextUtil.isEmpty(end_location_detail)) {
//            endLocationDetail.setError(getString(R.string.empty_end_location_detail));
//            focusView = endLocationDetail;
//            cancel = true;
//        }
        if (TextUtil.isEmpty(phone_number)) {
            phone.setError(getString(R.string.empty_phone_number));
            focusView = phone;
            cancel = true;
        }
        if (TextUtil.isEmpty(endTime)) {
            endDate.setError(getString(R.string.empty_end_date));
            focusView = endDate;
            cancel = true;
        }

        if (TextUtil.isEmpty(thing_detail)) {
            etGoodDetail.setError(getString(R.string.empty_thing_detail));
            focusView = etGoodDetail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            final AVObject order_message = new AVObject("order_message");
            order_message.put("userId", EMClient.getInstance().getCurrentUser());
            order_message.put("you_want_thing", you_want_thing);
            order_message.put("startLocation", start_location);
            order_message.put("startLocationDetail", start_location_detail);
            order_message.put("endLocation", end_location);
            order_message.put("endLocationDetail", end_location_detail);
            order_message.put("phone", phone_number);
            order_message.put("money", seekBar.getProgress());
            order_message.put("endDate", endTime);
            order_message.put("goodDetail", thing_detail);
            order_message.put("truename",truename);
            order_message.put("mStartCurrentLat", mStartCurrentLat);
            order_message.put("mStartCurrentLng", mStartCurrentLng);
            order_message.put("mEndCurrentLat", mEndCurrentLat);
            order_message.put("mEndCurrentLng", mEndCurrentLng);
            order_message.put("goodDetailImg", new AVFile("goodPic", mImageBytes));
            order_message.put("status",0);
            order_message.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Intent intent = new Intent(GiveMoneyActivity.this, PayForActivity.class);
                        intent.putExtra(Constant.OBJECT_ID,order_message.getObjectId());
                        Log.e(TAG, "done: " + order_message.getObjectId());
                        intent.putExtra(Constant.MONEY, seekBar.getProgress());
                        startActivity(intent);
                        TastyToast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                        GiveMoneyActivity.this.finish();
                    } else {
                        TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }
            });
            TastyToast.makeText(getApplicationContext(), "点击了发布悬赏", Toast.LENGTH_SHORT, TastyToast.SUCCESS);

        }
    }
    private void getTrueName(){
        AVQuery<AVObject> userDetail = new AVQuery<>("UserDetail");
        userDetail.whereEqualTo("userId", EMClient.getInstance().getCurrentUser());

        userDetail.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    truename = (String) avObject.get("truename") == null ? "" : (String) avObject.get("truename");
                    idcard = (String) avObject.get("idCard") == null ? "" : (String) avObject.get("idCard");
                    ispass = (int) avObject.get("ispass");
                    Log.e(TAG, "done: " + truename + idcard );
                }
            }
        });

    }

    private void initImagePicker(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
//        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
        imagePicker.setShowCamera(true);
        imagePicker.setCrop(false);
        imagePicker.setSaveRectangle(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.e(TAG, "onActivityResult: " + images.get(0).path);
                imageShow.setImageBitmap(BitmapFactory.decodeFile(images.get(0).path));
                mImageBytes = OftenUtils.getBytes(images.get(0).path);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"拍照", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(GiveMoneyActivity.this, stringItems, imageShow);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 直接调起相机
                        /**
                         * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                         *
                         * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                         *
                         * 如果实在有所需要，请直接下载源码引用。
                         */
                        //打开选择,本次允许选择的数量
                        Intent intent = new Intent(GiveMoneyActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, 100);
                        break;
                    case 1:
                        Intent intent1 = new Intent(GiveMoneyActivity.this, ImageGridActivity.class);
                        intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                        //ImagePicker.getInstance().setSelectedImages(images);
                        startActivityForResult(intent1, 100);
                        break;
                }
                TastyToast.makeText(getApplicationContext(), stringItems[position] + position, Toast.LENGTH_SHORT, TastyToast.DEFAULT);
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
