package com.example.jiayin.helpfordemo.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;

import static com.hyphenate.chat.EMClient.TAG;

/**
 * Created by jiayin on 2017/9/20.
 */

public class AVObjectUtils {

    public static void getImageByUserId(final Context context, String username, final ImageView imageView){
        Log.e(TAG, "username: " + username );
        final AVQuery avatar = new AVQuery("UserDetail");
        avatar.whereEqualTo("userId",username);
        avatar.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    String path = avObject.getAVFile("image") == null ? "null" : (String) avObject.getAVFile("image").getUrl();
                    if (path.equals("null")){
                        Glide.with(context).load(com.hyphenate.easeui.R.drawable.ease_default_avatar).into(imageView);
                    }else {
                        Glide.with(context).load(path).into(imageView);
                    }
                } else {
                    Toast.makeText(context,"出错了",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    public static void getUserId
}
