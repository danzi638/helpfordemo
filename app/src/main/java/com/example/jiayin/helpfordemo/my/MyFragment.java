package com.example.jiayin.helpfordemo.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.LoginActivity;
import com.example.jiayin.helpfordemo.app.base.BaseFragment;
import com.example.jiayin.helpfordemo.chat.model.Model;
import com.example.jiayin.helpfordemo.my.activity.AllOrderActivity;
import com.example.jiayin.helpfordemo.my.activity.EditorActivity;
import com.example.jiayin.helpfordemo.my.activity.HelpForOrderActivity;
import com.example.jiayin.helpfordemo.my.activity.HelpOrderActivity;
import com.example.jiayin.helpfordemo.my.activity.PersonInfoActivity;
import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.listener.OnBtnClickL;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.widget.MaterialDialog;
import com.example.jiayin.helpfordemo.utils.view.CircleImageView;
import com.example.jiayin.helpfordemo.utils.view.ObservableScrollView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.sdsmdg.tastytoast.TastyToast;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiayin on 2017/9/2.
 */

public class MyFragment extends BaseFragment {
    private static final String TAG = "MyFragment";
    @Bind(R.id.nameView)
    TextView nameView;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.login_out)
    TextView loginOut;
    @Bind(R.id.tv_person)
    TextView tvPerson;
    @Bind(R.id.fl_person)
    FrameLayout flPerson;
    @Bind(R.id.tv_credit)
    TextView tvCredit;
    @Bind(R.id.fl_credit)
    FrameLayout flCredit;
    @Bind(R.id.tv_suggest)
    TextView tvSuggest;
    @Bind(R.id.tv_about)
    TextView tvAbout;
    @Bind(R.id.contentView)
    ObservableScrollView contentView;
    @Bind(R.id.ll_order_all)
    LinearLayout llOrderAll;
    @Bind(R.id.ll_order_help)
    LinearLayout llOrderHelp;
    @Bind(R.id.ll_order_help_for)
    LinearLayout llOrderHelpFor;

    private String userId;
    private BaseAnimatorSet bas_in;
    private BaseAnimatorSet bas_out;


    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_my, null);
        ButterKnife.bind(this, view);

        initListner();

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void initListner() {
        checkUser();
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });
        flPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(mContext, "点击个人信息", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                Intent intent = new Intent(mContext, PersonInfoActivity.class);
                startActivity(intent);
            }
        });
        flCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(mContext, "点击随手信用", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                Intent intent = new Intent(mContext, EditorActivity.class);
                startActivity(intent);
            }
        });
        tvSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(mContext, "抱歉，建议页面还未开发", Toast.LENGTH_SHORT, TastyToast.WARNING);

            }
        });
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(mContext, "抱歉，关于我们页面还未开发", Toast.LENGTH_SHORT, TastyToast.WARNING);

            }
        });
        llOrderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TastyToast.makeText(mContext, "抱歉，全部订单页面还未开发", Toast.LENGTH_SHORT, TastyToast.WARNING);
                Intent intent = new Intent(mContext, AllOrderActivity.class);
                startActivity(intent);
            }
        });
        llOrderHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TastyToast.makeText(mContext, "抱歉，求助订单页面还未开发", Toast.LENGTH_SHORT, TastyToast.WARNING);
                Intent intent = new Intent(mContext, HelpOrderActivity.class);
                startActivity(intent);
            }
        });
        llOrderHelpFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TastyToast.makeText(mContext, "抱歉，帮忙订单页面还未开发", Toast.LENGTH_SHORT, TastyToast.WARNING);
                Intent intent = new Intent(mContext, HelpForOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void EaseUILogout() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
//                        TastyToast.makeText(mContext, "退出成功", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                        // 回到登录页面
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
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
                    userId = (String) avObject.get("nick") == null ? "" : (String) avObject.get("nick");
                    nameView.setText(userId);
                    String path = avObject.getAVFile("image").getUrl() == null ? "" : avObject.getAVFile("image").getUrl();
                    if (!TextUtil.isEmpty(path)) {
                        Glide.with(mContext).load(path).into(avatar);
                    } else {
                        TastyToast.makeText(mContext, "照片有问题", Toast.LENGTH_SHORT, TastyToast.WARNING);
                    }
                    Log.e(TAG, "checkUser: " + path);
                } else {
                    TastyToast.makeText(mContext, "出错了" + e.toString(), Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
    }


    private void logoutDialog() {
        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.content(
                "确定要退出吗")//
                .btnText("取消", "确定")//
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        TastyToast.makeText(mContext, "登出成功", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                        AVUser.getCurrentUser().logOut();
                        EaseUILogout();
                        dialog.dismiss();
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
