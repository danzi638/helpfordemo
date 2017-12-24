package com.example.jiayin.helpfordemo.my.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.community.activity.PublishCommunityActivity;
import com.example.jiayin.helpfordemo.community.adapter.ImagePickerAdapter;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.GlideImageLoader;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.listener.OnOperItemClickL;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.widget.ActionSheetDialog;
import com.example.jiayin.helpfordemo.utils.view.CircleImageView;
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
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.jiayin.helpfordemo.community.activity.PublishCommunityActivity.REQUEST_CODE_SELECT;

public class PersonInfoActivity extends BaseActivity {

    private static final String TAG = "PersonInfoActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_atatar)
    CircleImageView ivAtatar;
    @Bind(R.id.fl_avatar)
    FrameLayout flAvatar;
    @Bind(R.id.tv_nick)
    TextView tvNick;
    @Bind(R.id.fl_nick)
    FrameLayout flNick;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.fl_phone)
    FrameLayout flPhone;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.fl_sex)
    FrameLayout flSex;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.fl_birthday)
    FrameLayout flBirthday;
    private String objectid;
    private TimeSelector timeSelector;
    String birthday;

    private byte[] mImageBytes = null;
    ArrayList<ImageItem> images = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                commitTime(time);
            }
        }, "1965-1-1 00:00", getLocalTime()
        );
        timeSelector.setMode(TimeSelector.MODE.YMD);

        checkUser();

        initImagePicker();

        initListener();

    }

    private void commitTime(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDateTime = df.parse(time);
            birthday = df.format(startDateTime);
//                    tvBirthday.setText(birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AVObject userDetail = AVObject.createWithoutData("UserDetail", objectid);
        userDetail.put("birthday", birthday);
        userDetail.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    tvBirthday.setText(birthday);
                    TastyToast.makeText(getApplicationContext(), "生日修改成功", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                } else {
                    TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);

                }
            }
        });
    }

    private void initListener() {
        flAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "done: " + objectid);
                ActionSheetDialogNoTitle();
            }
        });
        flNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "done: " + objectid);
                Intent intent = new Intent(PersonInfoActivity.this, EditNickActivity.class);
                intent.putExtra(Constant.OBJECT_ID, objectid);
                startActivity(intent);

            }
        });
        flPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(getApplicationContext(), "暂不支付修改电话", Toast.LENGTH_SHORT, TastyToast.WARNING);

            }
        });
        flSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(getApplicationContext(), "暂不支付修改性别", Toast.LENGTH_SHORT, TastyToast.WARNING);

            }
        });
        flBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelector.show();
                Log.e(TAG, "done: " + objectid);
            }
        });
    }

    private void commit() {
        AVObject userDetail = AVObject.createWithoutData("UserDetail", objectid);
        userDetail.put("image", new AVFile("productPic", mImageBytes));
        userDetail.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
//                    tvBirthday.setText(birthday);
                    checkUser();
                    TastyToast.makeText(getApplicationContext(), "头像修改成功", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                } else {
                    TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
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


    private void checkUser() {
        AVQuery<AVObject> userDetail = new AVQuery<>("UserDetail");
        userDetail.whereEqualTo("userId", EMClient.getInstance().getCurrentUser());

        userDetail.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    objectid = avObject.getObjectId();
                    Log.e(TAG, "done: " + objectid);
                    tvNick.setText(avObject.get("nick").toString());
                    tvPhone.setText(avObject.get("userId").toString());
                    String sex;
                    if (avObject.get("sex").toString() == "true") {
                        sex = "男";
                    } else {
                        sex = "女";
                    }
                    tvSex.setText(sex);

                    tvBirthday.setText(String.valueOf(avObject.get("birthday")));
                    if (tvBirthday.getText().equals("null")) {
                        tvBirthday.setText("还未填写该信息，快填写吧~");
                    }
                    String path = avObject.getAVFile("image").getUrl();
                    if (!TextUtil.isEmpty(path)) {
                        Glide.with(PersonInfoActivity.this).load(path).into(ivAtatar);
                    } else {
                        TastyToast.makeText(getApplicationContext(), "照片有问题" , Toast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                    Log.e(TAG, "checkUser: " + path);
                } else {
                    TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
    }

    private void initImagePicker(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
        imagePicker.setShowCamera(true);
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.e(TAG, "onActivityResult: " + images.get(0).path);
                ivAtatar.setImageBitmap(BitmapFactory.decodeFile(images.get(0).path));
                mImageBytes = OftenUtils.getBytes(images.get(0).path);
                commit();
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"拍照", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(PersonInfoActivity.this, stringItems, ivAtatar);
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
                        Intent intent = new Intent(PersonInfoActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, 100);
                        break;
                    case 1:
                        Intent intent1 = new Intent(PersonInfoActivity.this, ImageGridActivity.class);
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
