package com.example.jiayin.helpfordemo;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.example.jiayin.helpfordemo.chat.model.Model;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.mob.MobSDK;

/**
 * Created by jiayin on 2017/7/22.
 */

public class AllApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化leanColud
        AVOSCloud.initialize(this,"wFgBy3i7XEev6RPWK061OwKp-gzGzoHsz", "2tGiiKmC2NPrh70oLNv4NFeU");
        AVOSCloud.setDebugLogEnabled(true);
        AVAnalytics.enableCrashReport(this, true);
        //初始化短信验证
        MobSDK.init(this, "1f1583adef930", "2c49434b2a8c9c959f851f7d967f569d"); // 初始化 SDK 单例，可以多次调用
        // 初始化EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);// 设置需要同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);// 设置需要同意后才能接受群邀请

        EaseUI.getInstance().init(this,options);

        // 初始化数据模型层类
        Model.getInstance().init(this);

        // 初始化全局上下文对象
        mContext = this;
    }

    // 获取全局上下文对象
    public static Context getGlobalApplication(){
        return mContext;
    }
}
