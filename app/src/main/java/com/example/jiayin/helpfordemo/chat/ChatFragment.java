package com.example.jiayin.helpfordemo.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseFragment;
import com.example.jiayin.helpfordemo.chat.adapter.HomeFragmentPageAdapter;
import com.example.jiayin.helpfordemo.chat.fragment.ChatListFragment;
import com.example.jiayin.helpfordemo.chat.fragment.ContactListFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiayin on 2017/8/17.
 */

public class ChatFragment extends BaseFragment {


    @Bind(R.id.tab_gank)
    TabLayout tabGank;
    @Bind(R.id.vp_gank)
    ViewPager vpGank;
    private ArrayList<String> mTitleList = new ArrayList<>(2);
    private ArrayList<Fragment> mFragments = new ArrayList<>(2);
    private HomeFragmentPageAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_chat);
        ButterKnife.bind(this, getContentView());
        initView();

        return getContentView();
    }

    private void initView() {
        initFragmentList();
        myAdapter = new HomeFragmentPageAdapter(getChildFragmentManager(), mFragments, mTitleList);
        vpGank.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        tabGank.setTabMode(TabLayout.MODE_FIXED);
        tabGank.setupWithViewPager(vpGank);
    }

//    private Fragment current;
//
//    /**
//     * 切换当前显示的fragment
//     */
//    public void switchContent(Fragment to) {
//        if (current != to) {
//            FragmentTransaction transaction = manager.beginTransaction();
//
//            if (current != null) {
//                transaction.hide(current);
//            }
//            if (!to.isAdded()) {
//                transaction.add(R.id.f_content, to).commit();
//            } else {
//                transaction.show(to).commit();
//            }
//            current = to;
//        }
//    }

private void initFragmentList() {
    if (mTitleList.size() != 0) {
        return;
    }
    mTitleList.add("会话");
    mTitleList.add("联系人");
    mFragments.add(new ChatListFragment());
    mFragments.add(new ContactListFragment());
}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
