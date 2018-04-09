package com.example.jiayin.helpfordemo.my.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.view.RoundIndicatorView;
import com.hyphenate.chat.EMClient;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditorActivity extends BaseActivity {

    private static final String TAG = "EditorActivity";
    @Bind(R.id.riv_credit)
    RoundIndicatorView rivCredit;
    @Bind(R.id.tv_editor)
    TextView tvEditor;
    @Bind(R.id.fl_editor)
    FrameLayout flEditor;
    @Bind(R.id.tv_truename)
    TextView tvTruename;
    @Bind(R.id.fl_truename)
    FrameLayout flTruename;
    @Bind(R.id.tv_idcard)
    TextView tvIdcard;
    @Bind(R.id.fl_idcard)
    FrameLayout flIdcard;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.refresh)
    MaterialRefreshLayout refresh;

    private String objectId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        checkUser();

        initListner();

    }

    private void initListner() {
        flEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(getApplicationContext(), "此信息不能修改哦", Toast.LENGTH_SHORT, TastyToast.WARNING);

            }
        });
        flTruename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " + objectId);
                Intent intent = new Intent(EditorActivity.this, EditTrueNameActivity.class);
                intent.putExtra(Constant.OBJECT_ID, objectId);
                startActivity(intent);
            }
        });
        flIdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " + objectId);
                if (tvTruename.getText().toString().trim().equals("还未填写该信息，快填写吧~")) {
                    TastyToast.makeText(getApplicationContext(), "请输入姓名再输入身份证", Toast.LENGTH_SHORT, TastyToast.WARNING);
                } else {
                    Intent intent = new Intent(EditorActivity.this, EditIdCardActivity.class);
                    intent.putExtra(Constant.OBJECT_ID, objectId);
                    startActivity(intent);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        refresh.setSunStyle(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkUser();
                    }
                });
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onfinish() {
                TastyToast.makeText(EditorActivity.this, "刷新成功", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    private void checkUser() {
        AVQuery<AVObject> userDetail = new AVQuery<>("UserDetail");
        userDetail.whereEqualTo("userId", EMClient.getInstance().getCurrentUser());

        userDetail.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    objectId = avObject.getObjectId();

                    tvTruename.setText(String.valueOf(avObject.get("truename")));
                    tvIdcard.setText(String.valueOf(avObject.get("idCard")));

                    isPass((int) avObject.get("ispass"));

                    if ("".equals(String.valueOf(avObject.get("truename"))) || "null".equals(String.valueOf(avObject.get("truename")))
                    ||String.valueOf(avObject.get("truename"))== null) {
                        tvTruename.setText("还未填写该信息，快填写吧~");
                        tvTruename.setTextColor(R.color.font_color);
                    }
                    if ("".equals(String.valueOf(avObject.get("idCard"))) || "null".equals(String.valueOf(avObject.get("idCard")))
                            ||String.valueOf(avObject.get("idCard"))== null) {
                        tvIdcard.setText("还未填写该信息，快填写吧~");
                        tvIdcard.setTextColor(R.color.font_color);
                    }

                    editornum();

                    int a = Integer.parseInt(avObject.get("creditNum").toString());
                    rivCredit.setCurrentNumAnim(a);

                    tvEditor.setText(a + "分  ");
                    tvEditor.append(editor(a));

                } else {
                    TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
    }

    private void isPass(int i) {
        switch (i) {
            case 0:
                tvIdcard.setText(R.string.nopass);
                tvTruename.setText(R.string.nopass);
                tvIdcard.setTextColor(R.color.red);
                tvTruename.setTextColor(R.color.red);
                break;
            case 1:
                break;
            case 2:
                tvIdcard.setText(R.string.ispassing);
                tvTruename.setText(R.string.ispassing);
                tvIdcard.setTextColor(R.color.main);
                tvTruename.setTextColor(R.color.main);
                break;
            default:
                break;
        }
    }

    private void editornum() {
        int editorNum;
        String truename = tvTruename.getText().toString().trim();
        String idcard = tvIdcard.getText().toString().trim();
        if (truename.equals("审核中") || truename.equals("未通过审核") ||
                truename.equals("还未填写该信息，快填写吧~") || truename.equals("") || truename.equals("null")) {
            editorNum = 50;
        } else if (idcard.equals("审核中") || idcard.equals("未通过审核") ||
                idcard.equals("还未填写该信息，快填写吧~") || idcard.equals("") || truename.equals("null")) {
            editorNum = 100;
        } else {
            editorNum = 250;
        }
        AVObject userDetail = AVObject.createWithoutData("UserDetail", objectId);
        userDetail.put("creditNum", String.valueOf(editorNum));
        userDetail.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {

                } else {

                }
            }
        });
    }

    private String editor(int a) {
        if (a > 0 && a <= 50) {
            return "信用较差";
        } else if (a > 50 && a <= 150) {
            return "信用中等";
        } else if (a > 150 && a <= 250) {
            return "信用良好";
        } else if (a > 250 && a <= 350) {
            return "信用优秀";
        } else if (a > 350 && a <= 500) {
            return "信用极好";
        } else {
            a = 500;
            editor(a);
        }
        return null;
    }

}