package com.example.jiayin.helpfordemo.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.chat.model.bean.PickContactInfo;
import com.example.jiayin.helpfordemo.utils.AVObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/25.
 */
// 选择联系人的页面适配器
public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mPicks = new ArrayList<>();
    private List<String> mExistMembers = new ArrayList<>();// 保存群中已经存在的成员集合

    public PickContactAdapter(Context context, List<PickContactInfo> picks, List<String> existMembers) {
        mContext = context;

        if (picks != null && picks.size() >= 0) {
            mPicks.clear();
            mPicks.addAll(picks);
        }

        // 加载已经存在的成员集合
        mExistMembers.clear();
        mExistMembers.addAll(existMembers);
    }

    @Override
    public int getCount() {
        return mPicks == null ? 0 : mPicks.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 创建或获取viewHolder
        ViewHolder holder  = null;

        if(convertView == null) {
            holder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.item_pick, null);

            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_pick);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_pick_name);
            holder.iv_avatar = convertView.findViewById(R.id.iv_atatar);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取当前item数据
        PickContactInfo pickContactInfo = mPicks.get(position);

        // 显示数据
        holder.tv_name.setText(pickContactInfo.getUser().getName());
        AVObjectUtils.getImageByUserId(mContext,pickContactInfo.getUser().getName(),holder.iv_avatar);
        holder.cb.setChecked(pickContactInfo.isChecked());

        // 判断
        if(mExistMembers.contains(pickContactInfo.getUser().getHxid())) {
            holder.cb.setChecked(true);
            pickContactInfo.setIsChecked(true);
        }

        return convertView;
    }

    // 获取选择的联系人
    public List<String> getPickContacts() {

        List<String> picks = new ArrayList<>();

        for (PickContactInfo pick: mPicks){

            // 判断是否选中
            if(pick.isChecked()) {
                picks.add(pick.getUser().getName());
            }
        }

        return picks;
    }

    private class  ViewHolder{
        private CheckBox cb;
        private TextView tv_name;
        private ImageView iv_avatar;
    }
}
