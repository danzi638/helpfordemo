package com.hyphenate.easeui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

import static com.hyphenate.chat.EMClient.TAG;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
//    public static void setUserAvatar(Context context, String username, ImageView imageView){
//    	EaseUser user = getUserInfo(username);
//        if(user != null && user.getAvatar() != null){
//            try {
////                int avatarResId = Integer.parseInt(user.getAvatar());
//                //修改的地方图片的地址
//                Glide.with(context).load(R.drawable.ease_default_image).into(imageView);
//            } catch (Exception e) {
//                //use default avatar
//                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
//            }
//        }else{
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//        }
//    }
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(final Context context, String username, final ImageView imageView){
//        EaseUser user = getUserInfo(username);
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Log.e(TAG, "setUserAvatar: "  + AVUser.getCurrentUser());
//                Log.e(TAG, "user: " + user);
                Log.e(TAG, "username: " + username );
                final AVQuery avatar = new AVQuery("UserDetail");
                avatar.whereEqualTo("userId",username);
                avatar.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        String path = avObject.getAVFile("image") == null ? "" : avObject.getAVFile("image").getUrl();
                        if (path.equals("")){
                            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
                        }else {
                            Glide.with(context).load(path).into(imageView);
                        }
                    } else {
                        Toast.makeText(context,"出错了",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}
