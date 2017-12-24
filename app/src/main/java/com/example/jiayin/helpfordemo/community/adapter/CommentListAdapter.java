package com.example.jiayin.helpfordemo.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.utils.OftenUtils;

import java.util.List;

/**
 * Created by jiayin on 2017/9/18.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentListHolder> {

    private Context mContext;
    private List<AVObject> mList;

    public CommentListAdapter(Context mContext, List<AVObject> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    @Override
    public CommentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentListHolder(LayoutInflater.from(mContext).inflate(R.layout.comment_list,parent,false));
    }

    @Override
    public void onBindViewHolder(final CommentListHolder holder, int position) {
        String userid = mList.get(position).get("userid") == null ? "" : (String) mList.get(position).get("userid");

        AVQuery<AVObject> user_detail = new AVQuery<>("UserDetail");
        user_detail.whereEqualTo("userId",userid);
        user_detail.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                Glide.with(mContext).load(avObject.getAVFile("image").getUrl()).into(holder.mCiv_attavr);
                holder.mTv_author.setText(avObject.get("nick").toString());
            }
        });

        String create_at = OftenUtils.formatTime(mList.get(position).getCreatedAt()) == null ? "" : OftenUtils.formatTime(mList.get(position).getCreatedAt());
        String content = mList.get(position).get("content") == null ? "" : (String) mList.get(position).get("content");
        holder.mTv_comment.setText(content);
        holder.mTv_createtime.setText(create_at);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CommentListHolder extends RecyclerView.ViewHolder{
        private com.example.jiayin.helpfordemo.utils.view.CircleImageView mCiv_attavr;
        private TextView mTv_author;
        private TextView mTv_comment;
        private TextView mTv_createtime;
        public CommentListHolder(View itemView) {
            super(itemView);
            mCiv_attavr = (com.example.jiayin.helpfordemo.utils.view.CircleImageView) itemView.findViewById(R.id.civ_attavr);
            mTv_author = (TextView) itemView.findViewById(R.id.tv_author);
            mTv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            mTv_createtime = (TextView) itemView.findViewById(R.id.tv_createtime);
        }
    }
}


