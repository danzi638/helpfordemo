package com.example.jiayin.helpfordemo.my.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseFragment;
import com.example.jiayin.helpfordemo.my.adapter.MyOrderAdapter;
import com.hyphenate.chat.EMClient;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hyphenate.chat.EMGCMListenerService.TAG;

/**
 * Created by jiayin on 2017/9/7.
 */

public class HelpForUnFinishOrderFragment extends BaseFragment {
    @Bind(R.id.rv_allorder)
    RecyclerView rvAllorder;
    @Bind(R.id.refresh)
    MaterialRefreshLayout refresh;

    private MyOrderAdapter myOrderAdapter;
    private List<AVObject> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_all_allorder, null);
        ButterKnife.bind(this, view);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext,1);
        rvAllorder.setLayoutManager(layoutManager);
        myOrderAdapter = new MyOrderAdapter(mList,mContext);
        rvAllorder.setAdapter(myOrderAdapter);

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
        final AVQuery<AVObject> order_message = new AVQuery<>("order_message");
        order_message.whereEqualTo("handuserid", EMClient.getInstance().getCurrentUser());
        final AVQuery<AVObject> order_message1 = new AVQuery<>("order_message");
        order_message1.whereEqualTo("status",4);
        final AVQuery<AVObject> order_message2 = new AVQuery<>("order_message");
        order_message2.whereEqualTo("status",5);
        final AVQuery<AVObject> order_message3 = new AVQuery<>("order_message");
        order_message3.whereEqualTo("status",6);
        AVQuery<AVObject> query = AVQuery.or(Arrays.asList(order_message1, order_message2,order_message3));
        AVQuery<AVObject> query1 = AVQuery.and(Arrays.asList(query,order_message));
        query1.orderByAscending("status");

        query1.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.e(TAG, "done: " + list.size());
                    mList.addAll(list);
                    Log.e(TAG, "done: " + mList.size());

                    myOrderAdapter.changeStatus();
                    myOrderAdapter.notifyDataSetChanged();
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
