package com.example.jiayin.helpfordemo.community;

import android.content.Intent;
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
import com.example.jiayin.helpfordemo.community.activity.PublishCommunityActivity;
import com.example.jiayin.helpfordemo.community.adapter.ComunityListAdapter;
import com.example.jiayin.helpfordemo.helpfor.adapter.HelpForListAdapter;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by jiayin on 2017/9/2.
 */

public class CommunityFragment extends BaseFragment {
    @Bind(R.id.rv_community_list)
    RecyclerView rvCommunityList;
    @Bind(R.id.refresh)
    MaterialRefreshLayout refresh;
    @Bind(R.id.filter_menu)
    FilterMenuLayout filterMenu;

    private ComunityListAdapter comunityListAdapter;
    private List<AVObject> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_community, null);
        ButterKnife.bind(this, view);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        rvCommunityList.setLayoutManager(layoutManager);
        comunityListAdapter = new ComunityListAdapter(mContext, mList);
        rvCommunityList.setAdapter(comunityListAdapter);

        initView();
        initListener();

        return view;
    }

    private void initView() {
        attachMenu3(filterMenu);
    }

    private FilterMenu attachMenu3(FilterMenuLayout layout) {
        return new FilterMenu.Builder(mContext)
                .addItem(R.drawable.ic_action_add)
                .addItem(R.drawable.ic_action_clock)
                .attach(layout)
                .withListener(listener)
                .build();
    }

    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            switch (position){
                case 0:
                    TastyToast.makeText(mContext, "点击了发布新闻" + position, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    mContext.startActivity(new Intent(mContext, PublishCommunityActivity.class));
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };

    private void initListener() {
        refresh.setSunStyle(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                queryCommunityMessage();
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onfinish() {
                TastyToast.makeText(mContext, "刷新成功", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
        Log.e(TAG, "initData: " + mList.size());
    }

    @Override
    protected void initData() {
        super.initData();
        queryCommunityMessage();
        Log.e(TAG, "initData: " + mList.size());
    }


    private void queryCommunityMessage() {
        mList.clear();
        AVQuery<AVObject> order_message = new AVQuery<>("community_message");
        order_message.orderByDescending("createdAt");
        order_message.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.e(TAG, "done: " + list.size());
                    mList.addAll(list);
                    Log.e(TAG, "done: " + mList.size());
//                    helpForListAdapter.changeStatus();
                    comunityListAdapter.notifyDataSetChanged();
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
