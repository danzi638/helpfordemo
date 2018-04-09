package com.example.jiayin.helpfordemo.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.community.activity.CommunityDetailActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Date;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by jiayin on 2017/9/10.
 */

public class ComunityListAdapter extends RecyclerView.Adapter<ComunityListAdapter.CommunityListHolder> {


    private Context mContext;
    private List<AVObject> mList;

    public ComunityListAdapter(Context mContext, List<AVObject> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CommunityListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityListHolder(LayoutInflater.from(mContext).inflate(R.layout.community_list,parent,false));
    }

    @Override
    public void onBindViewHolder(CommunityListHolder holder, final int position) {
        final String objectid = mList.get(position).getObjectId() == null ? "" : mList.get(position).getObjectId();
        final String userid = mList.get(position).get("userid") == null ? "" : (String) mList.get(position).get("userid");
        final String title = mList.get(position).get("title") == null ? "" : (String) mList.get(position).get("title");
        final String content = mList.get(position).get("content") == null ? "" : (String) mList.get(position).get("content");
        final String createAt = OftenUtils.formatTimeYYYY_MM_DD(mList.get(position).getCreatedAt()) == null ? "" : OftenUtils.formatTimeYYYY_MM_DD(mList.get(position).getCreatedAt());
        final String createAt_Detail = OftenUtils.formatTime(mList.get(position).getCreatedAt()) == null ? "" : OftenUtils.formatTime(mList.get(position).getCreatedAt());
        final String image1 = mList.get(position).getAVFile("image1") == null ? "" : (String) mList.get(position).getAVFile("image1").getUrl();
        final String image2 = mList.get(position).getAVFile("image2") == null ? "" : (String) mList.get(position).getAVFile("image2").getUrl();
        final String image3 = mList.get(position).getAVFile("image3") == null ? "" : (String) mList.get(position).getAVFile("image3").getUrl();
        final Integer comment_count = (Integer) mList.get(position).get("comment_count");

        holder.mTv_title.setText(title);
        holder.mTv_author.setText(userid);
        holder.mTv_createtime.setText(createAt);
        holder.mTv_comment.setText(comment_count.toString() + "评");
        if (!image1.equals("")) {
            Glide.with(mContext).load(image1).into(holder.mIv_image1);
        }else {
            Glide.with(mContext).load(R.drawable.icon_default_page).into(holder.mIv_image1);
        }

        holder.mLl_news_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommunityDetailActivity.class);
                intent.putExtra(Constant.OBJECT_ID,objectid);
                intent.putExtra(Constant.CREATE_AT,createAt_Detail);
                intent.putExtra(Constant.USER_ID,userid);
                intent.putExtra(Constant.TITLE,title);
                intent.putExtra(Constant.CONTENT,content);
                intent.putExtra(Constant.IMAGE_PATH_1,image1);
                intent.putExtra(Constant.IMAGE_PATH_2,image2);
                intent.putExtra(Constant.IMAGE_PATH_3,image3);
                intent.putExtra(Constant.COMMENT_COUNT,comment_count.toString());
                mContext.startActivity(intent);
//                TastyToast.makeText(mContext,"点击了"+position,TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class CommunityListHolder extends RecyclerView.ViewHolder{
        private LinearLayout mLl_news_list;
        private ImageView mIv_image1;
        private TextView mTv_title;
        private TextView mTv_createtime;
        private TextView mTv_author;
        private TextView mTv_comment;//总评论数


        public CommunityListHolder(View itemView) {
            super(itemView);
            mLl_news_list = (LinearLayout) itemView.findViewById(R.id.ll_news_list);
            mIv_image1 = (ImageView) itemView.findViewById(R.id.iv_image1);
            mTv_title = (TextView) itemView.findViewById(R.id.tv_title);
            mTv_createtime = (TextView) itemView.findViewById(R.id.tv_createtime);
            mTv_author = (TextView) itemView.findViewById(R.id.tv_author);
            mTv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
        }
    }
}