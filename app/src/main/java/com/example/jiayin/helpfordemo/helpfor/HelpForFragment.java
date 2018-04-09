package com.example.jiayin.helpfordemo.helpfor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseFragment;
import com.example.jiayin.helpfordemo.helpfor.adapter.HelpForListAdapter;
import com.example.jiayin.helpfordemo.utils.OftenUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by jiayin on 2017/8/17.
 */

public class HelpForFragment extends BaseFragment {
    @Bind(R.id.rv_help_for)
    RecyclerView rvHelpFor;
    @Bind(R.id.refresh)
    MaterialRefreshLayout refresh;

    private HelpForListAdapter helpForListAdapter;
    private List<AVObject> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_help_for, null);
        ButterKnife.bind(this, view);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        rvHelpFor.setLayoutManager(layoutManager);
        helpForListAdapter = new HelpForListAdapter(mList, mContext);
        rvHelpFor.setAdapter(helpForListAdapter);

        initListener();

        return view;
    }

    private void initListener() {
        refresh.setSunStyle(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                queryOrderMessage();
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.finishRefresh();
                    }
                }, 3000);
            }
            @Override
            public void onfinish() {
                TastyToast.makeText(mContext,"刷新成功",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
            }
        });
        Log.e(TAG, "initData: " + mList.size());

    }

    @Override
    protected void initData() {
        super.initData();
        queryOrderMessage();
        Log.e(TAG, "initData: " + mList.size());

    }


    private void queryOrderMessage() {
        mList.clear();
        AVQuery<AVObject> order_message = new AVQuery<>("order_message");
        order_message.whereEqualTo("status",1);
        order_message.orderByDescending("createdAt");
        order_message.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
//                    Log.e(TAG, "done: " + list.size());
                    for (AVObject avobjcet : list) {
                        String endDate = avobjcet.get("endDate").toString();
                        if (OftenUtils.timeValue(endDate) <= 0) {
                            AVObject order_detail = AVObject.createWithoutData("order_message", avobjcet.getObjectId());
                            // 修改 content
                            order_detail.put("status",5);
                            // 保存到云端
                            order_detail.saveInBackground();
                        }
                    }
                    mList.addAll(list);
//                    Log.e(TAG, "done: " + mList.size());


                    helpForListAdapter.changeStatus();
                    helpForListAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
