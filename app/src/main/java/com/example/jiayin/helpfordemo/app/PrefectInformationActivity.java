package com.example.jiayin.helpfordemo.app;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.chat.model.Model;
import com.example.jiayin.helpfordemo.chat.model.bean.UserInfo;
import com.example.jiayin.helpfordemo.my.activity.PersonInfoActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.GlideImageLoader;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.example.jiayin.helpfordemo.utils.view.LoadingDialog;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.listener.OnOperItemClickL;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.widget.ActionSheetDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PrefectInformationActivity extends BaseActivity {
    private final String TAG = "PrefectInfomationActivity:";

//imageView

    @Bind(R.id.et_nick)
    EditText etNick;
    @Bind(R.id.rb_man)
    RadioButton rbMan;
    @Bind(R.id.rb_woman)
    RadioButton rbWoman;
    @Bind(R.id.btn_send_pre_information)
    Button btnSendPreInformation;
    @Bind(R.id.view_part)
    LinearLayout viewPart;
    @Bind(R.id.image_head)
    CircleImageView imageHead;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private LoadingDialog dialog;
    private Boolean sex = false;
    private String username;
    private String password;

    private byte[] mImageBytes = null;
    ArrayList<ImageItem> images = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefect_information);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.prefectinformation_title));

        username = getIntent().getStringExtra(Constant.USER_NAME);
        password = getIntent().getStringExtra(Constant.USER_PAS);

        initImagePicker();

        initListener();

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

    private void initListener() {

        imageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialogNoTitle();
            }
        });
        rbMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex = false;
            }
        });
        rbWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex = true;
            }
        });
    }

    private void commitPrefectInformation() {
        Log.e(TAG, "commitPrefectInformation: " + username);
        String nick = etNick.getText().toString().trim();
        if (TextUtils.isEmpty(nick)) {
            Toast.makeText(PrefectInformationActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImageBytes == null) {
            Toast.makeText(PrefectInformationActivity.this, "请选择一张照片", Toast.LENGTH_SHORT).show();
            return;
        }
//        dialog.show();
//        dialog.setMessage("正在注册");
        AVObject user_detail = new AVObject("UserDetail");
        user_detail.put("userId", username);
        user_detail.put("nick", nick);
        user_detail.put("sex", sex);
        user_detail.put("owner", AVUser.getCurrentUser());
        user_detail.put("image", new AVFile("productPic", mImageBytes));

        user_detail.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
//                    easeUIregister();
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                // 去环信服务器注册账号
                                EMClient.getInstance().createAccount(username, password);

                                // 更新页面显示
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TastyToast.makeText(getApplicationContext(), "注册成功" , Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                                        easeUiLogin();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TastyToast.makeText(getApplicationContext(), "注册失败" + e.toString() , Toast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                });
                            }
                        }
                    });
//                    easeUiLogin();

                    Intent intent = new Intent(PrefectInformationActivity.this, MainActivity.class);
                    intent.putExtra(Constant.USER_NAME, username);
                    startActivity(intent);
                    PrefectInformationActivity.this.finish();
                } else {
                    TastyToast.makeText(getApplicationContext(), "出错了"+e.toString() , Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });

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
                                TastyToast.makeText(getApplicationContext(), "登陆失败" + s , Toast.LENGTH_SHORT, TastyToast.ERROR);
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

    private void easeUIregister() {
        // 3 去服务器注册账号
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // 去环信服务器注册账号
                    EMClient.getInstance().createAccount(username, password);

                    // 更新页面显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TastyToast.makeText(getApplicationContext(), "注册成功" , Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TastyToast.makeText(getApplicationContext(), "注册失败" + e.toString() , Toast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.e(TAG, "onActivityResult: " + images.get(0).path);
                imageHead.setImageBitmap(BitmapFactory.decodeFile(images.get(0).path));
                mImageBytes = OftenUtils.getBytes(images.get(0).path);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"拍照", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(PrefectInformationActivity.this, stringItems, imageHead);
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
                        Intent intent = new Intent(PrefectInformationActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, 100);
                        break;
                    case 1:
                        Intent intent1 = new Intent(PrefectInformationActivity.this, ImageGridActivity.class);
                        intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                        //ImagePicker.getInstance().setSelectedImages(images);
                        startActivityForResult(intent1, 100);
                        break;
                }

                TastyToast.makeText(getApplicationContext(), stringItems[position] + position , Toast.LENGTH_SHORT, TastyToast.DEFAULT);

                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_prefectinformation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.backup) {
            commitPrefectInformation();
            TastyToast.makeText(getApplicationContext(), "提交成功" , Toast.LENGTH_SHORT, TastyToast.SUCCESS);
        }
        return super.onOptionsItemSelected(item);
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
