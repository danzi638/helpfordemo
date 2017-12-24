package com.example.jiayin.helpfordemo.helpfor.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.chat.activity.ChatActivity;
import com.example.jiayin.helpfordemo.helpfor.activity.OrderDetailActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.hyphenate.chat.EMClient;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;


import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.jiayin.helpfordemo.utils.OftenUtils.formatTime;

/**
 * Created by jiayin on 2017/9/4.
 */

public class HelpForListAdapter extends RecyclerView.Adapter<HelpForListAdapter.HelpForListHolder> {


    private Context mContext;
    private List<AVObject> mList;

    public HelpForListAdapter(List<AVObject> list, Context context) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public HelpForListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HelpForListHolder(LayoutInflater.from(mContext).inflate(R.layout.help_for_list, parent, false));
    }


    @Override
    public void onBindViewHolder(HelpForListHolder holder, final int position) {

        final String objectid = (String) mList.get(position).getObjectId();
        final String userid = (String) mList.get(position).get("userId") == null ? "" : (String) mList.get(position).get("userId");
        final String you_want_thing = (String) mList.get(position).get("you_want_thing") == null ? "" : (String) mList.get(position).get("you_want_thing");
        final String endLocation = (String) mList.get(position).get("endLocation") == null ? "" : (String) mList.get(position).get("endLocation");
        final String endLocationDetail = (String) mList.get(position).get("endLocationDetail") == null ? "" : (String) mList.get(position).get("endLocationDetail");
        final String startLocation = (String) mList.get(position).get("startLocation") == null ? "" : (String) mList.get(position).get("startLocation");
        final String startLocationDetail = (String) mList.get(position).get("startLocationDetail") == null ? "" : (String) mList.get(position).get("startLocationDetail");
        final String truename = (String) mList.get(position).get("truename") == null ? "" : (String) mList.get(position).get("truename");
        final String phone = (String) mList.get(position).get("phone") == null ? "" : (String) mList.get(position).get("phone");
        final int money = (int) mList.get(position).get("money") == 0 ? 0 : (int) mList.get(position).get("money");
        final String endDate = (String) mList.get(position).get("endDate") == null ? "" : (String) mList.get(position).get("endDate");
        final String path = mList.get(position).getAVFile("goodDetailImg").getUrl() == null ? "" : mList.get(position).getAVFile("goodDetailImg").getUrl();

        final int status = (int) mList.get(position).get("status") == 0 ? 0 : (int) mList.get(position).get("status");
        Log.e(TAG, "onBindViewHolder: " + status);

        final double mStartCurrentLat = Double.parseDouble(String.valueOf(mList.get(position).get("mStartCurrentLat")) == null ? "0.0" : String.valueOf(mList.get(position).get("mStartCurrentLat")));
        final double mStartCurrentLng = Double.parseDouble(String.valueOf(mList.get(position).get("mStartCurrentLng")) == null ? "0.0" : String.valueOf(mList.get(position).get("mStartCurrentLng")));
        final double mEndCurrentLat = Double.parseDouble(String.valueOf(mList.get(position).get("mEndCurrentLat")) == null ? "0.0" : String.valueOf(mList.get(position).get("mEndCurrentLat")));
        final double mEndCurrentLng = Double.parseDouble(String.valueOf(mList.get(position).get("mEndCurrentLng")) == null ? "0.0" : String.valueOf(mList.get(position).get("mEndCurrentLng")));

        holder.mTv_you_want_thing.setText(you_want_thing == null ? "请求物品:" : "请求物品:" + you_want_thing);
        holder.mTv_endlocation.setText(endLocation == null ? "物品所在地:" : "物品所在地:" + endLocation);
        holder.mTv_startlocation.setText(startLocation == null ? "收货地点:" : "收货地点:" + startLocation);
        holder.mTv_money.setText(money == 0 ? "￥:" : "￥:" + money);
        holder.mTv_truename.setText(truename == null ? "联系人:" : "联系人:" + truename);
        holder.mTv_createtime.setText(formatTime(mList.get(position).getCreatedAt()) == null ? "" : "" + formatTime(mList.get(position).getCreatedAt()));
        holder.mIv_statu_image.setImageResource(OftenUtils.setStatuImage(status));
        holder.mTv_enddate.setText("期限:" + endDate);

        holder.mItem_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userid.equals(EMClient.getInstance().getCurrentUser())) {
                    Log.e(TAG, "onBindViewHolder: " + userid +":"+ phone);

                    TastyToast.makeText(mContext, "无法完成自己发布的订单", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(Constant.OBJECT_ID, objectid);
                    intent.putExtra(Constant.USER_ID, userid);
                    intent.putExtra(Constant.YOU_WANT_THING, you_want_thing);
                    intent.putExtra(Constant.START_LOCATION, startLocation);
                    intent.putExtra(Constant.START_LOCATION_DETAIL, startLocationDetail);
                    intent.putExtra(Constant.END_LOCATION, endLocation);
                    intent.putExtra(Constant.END_LOCATION_DETAIL, endLocationDetail);
                    intent.putExtra(Constant.TRUE_NAME, truename);
                    intent.putExtra(Constant.PHONE, phone);
                    intent.putExtra(Constant.MONEY, money);
                    intent.putExtra(Constant.END_DATE, endDate);
                    intent.putExtra(Constant.PATH, path);
                    intent.putExtra(Constant.START_CURRENT_LAT, mStartCurrentLat);
                    intent.putExtra(Constant.START_CURRENT_LNG, mStartCurrentLng);
                    intent.putExtra(Constant.END_CURRENT_LAT, mEndCurrentLat);
                    intent.putExtra(Constant.END_CURRENT_LNG, mEndCurrentLng);
                    mContext.startActivity(intent);
                }

            }
        });
        holder.mBtn_jiedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userid.equals(EMClient.getInstance().getCurrentUser())) {
                    TastyToast.makeText(mContext, "无法完成自己发布的订单", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                } else {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(Constant.OBJECT_ID, objectid);
                    intent.putExtra(Constant.USER_ID, userid);
                    intent.putExtra(Constant.YOU_WANT_THING, you_want_thing);
                    intent.putExtra(Constant.START_LOCATION, startLocation);
                    intent.putExtra(Constant.START_LOCATION_DETAIL, startLocationDetail);
                    intent.putExtra(Constant.END_LOCATION, endLocation);
                    intent.putExtra(Constant.END_LOCATION_DETAIL, endLocationDetail);
                    intent.putExtra(Constant.TRUE_NAME, truename);
                    intent.putExtra(Constant.PHONE, phone);
                    intent.putExtra(Constant.MONEY, money);
                    intent.putExtra(Constant.END_DATE, endDate);
                    intent.putExtra(Constant.PATH, path);
                    mContext.startActivity(intent);
                }

            }
        });
        holder.mBtn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userid.equals(EMClient.getInstance().getCurrentUser())) {
                    TastyToast.makeText(mContext, "无法与自己聊天", TastyToast.LENGTH_SHORT, TastyToast.WARNING);


                } else {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra(Constant.USER_ID, userid);
                    mContext.startActivity(intent);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void changeStatus() {
        for (int i = 0; i<mList.size() -1;i++){
            String objectId = mList.get(i).getObjectId();
            int status = (int) mList.get(i).get("status");
            Log.e(TAG, "removeItem: " + objectId );
            if (status == 0) {
                if (OftenUtils.timeValue((String) mList.get(i).get("endDate")) <= 0) {
                    changeStatus_4(objectId);
                }
            }else if (status == 1){
                if (OftenUtils.timeValue((String) mList.get(i).get("endDate")) <= 0) {
                    changeStatus_5(objectId);
                }
            }else if (status == 2){
                if (OftenUtils.timeValue((String) mList.get(i).get("endDate")) <= 0) {
                    changeStatus_6(objectId);
                }
            }
        }

//        for (int i = 0; i <= mList.size() - 1; i++) {
//            int status = (int) mList.get(i).get("status");
//            if (status != 1) {
//                this.mList.remove(i);
//                i = i - 1;
//            }
//        }
    }
    public void changeStatus_4(String objectId){
        AVObject order_message = AVObject.createWithoutData("order_message", objectId);
        order_message.put("status", 4);
        order_message.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {

                } else {
                    TastyToast.makeText(mContext,"出错了" + e.toString(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }
            }
        });
    }
    public void changeStatus_5(String objectId){
        AVObject order_message = AVObject.createWithoutData("order_message", objectId);
        order_message.put("status", 5);
        order_message.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {

                } else {
                    TastyToast.makeText(mContext,"出错了" + e.toString(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }
            }
        });
    }
    public void changeStatus_6(String objectId){
        AVObject order_message = AVObject.createWithoutData("order_message", objectId);
        order_message.put("status", 6);
        order_message.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {

                } else {
                    TastyToast.makeText(mContext,"出错了" + e.toString(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }
            }
        });
    }

    class HelpForListHolder extends RecyclerView.ViewHolder {
        private CardView mItem_main;
        private TextView mTv_you_want_thing;
        private TextView mTv_startlocation;
        private TextView mTv_endlocation;
        private TextView mTv_money;
        private TextView mTv_truename;
        private Button mBtn_chat;
        private Button mBtn_jiedan;
        private TextView mTv_createtime;
        private TextView mTv_enddate;
        private ImageView mIv_statu_image;

        public HelpForListHolder(View itemView) {
            super(itemView);
            mItem_main = (CardView) itemView.findViewById(R.id.item_main);
            mTv_you_want_thing = (TextView) itemView.findViewById(R.id.tv_you_want_thing);
            mTv_startlocation = (TextView) itemView.findViewById(R.id.tv_startlocation);
            mTv_endlocation = (TextView) itemView.findViewById(R.id.tv_endlocation);
            mTv_money = (TextView) itemView.findViewById(R.id.tv_money);
            mTv_truename = (TextView) itemView.findViewById(R.id.tv_truename);
            mBtn_chat = (Button) itemView.findViewById(R.id.btn_chat);
            mIv_statu_image = itemView.findViewById(R.id.iv_statu_image);
            mTv_createtime = (TextView) itemView.findViewById(R.id.tv_createtime);
            mBtn_jiedan = itemView.findViewById(R.id.btn_jiedan);
            mTv_enddate = itemView.findViewById(R.id.tv_enddate);
        }
    }
}