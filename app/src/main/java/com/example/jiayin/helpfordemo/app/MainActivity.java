package com.example.jiayin.helpfordemo.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.chat.ChatFragment;
import com.example.jiayin.helpfordemo.community.CommunityFragment;
import com.example.jiayin.helpfordemo.help.HelpFragment;
import com.example.jiayin.helpfordemo.helpfor.HelpForFragment;
import com.example.jiayin.helpfordemo.my.MyFragment;
import com.example.jiayin.helpfordemo.utils.view.tab.BarEntity;
import com.example.jiayin.helpfordemo.utils.view.tab.BottomTabBar;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements BottomTabBar.OnSelectListener {

    private static final String TAG = "MainActivity:";

    private BottomTabBar tb;
    private List<BarEntity> bars;
    private ChatFragment chatFragment;
    private HelpFragment helpFragment;
    private HelpForFragment helpForFragment;
    private CommunityFragment communityFragment;
    private MyFragment myFragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        String username = getIntent().getStringExtra(Constant.USER_NAME);
        Log.e(TAG, "onCreate: " + EMClient.getInstance().getCurrentUser());
//        Log.e(TAG, "onCreate: " + AVUser.getCurrentUser());
        Log.e(TAG, "onCreate: ");
        initView();
    }

    private void initView() {
        manager = getSupportFragmentManager();
        tb = (BottomTabBar) findViewById(R.id.tb);
        tb.setManager(manager);
        tb.setOnSelectListener(this);
        bars = new ArrayList<>();
        bars.add(new BarEntity("求助", R.drawable.icon_help_select, R.drawable.icon_help));
        bars.add(new BarEntity("帮忙", R.drawable.icon_help_for_select, R.drawable.icon_help_for));
        bars.add(new BarEntity("圈子", R.drawable.icon_community_select,R.drawable.icon_community));
        bars.add(new BarEntity("会话", R.drawable.icon_chat_select, R.drawable.icon_chat));
        bars.add(new BarEntity("个人", R.drawable.icon_my_select,R.drawable.icon_my));
        tb.setBars(bars);
    }

    @Override
    public void onSelect(int position) {
        switch (position) {
            case 0:
                if (helpFragment == null) {
                    helpFragment = new HelpFragment();
                }
                tb.switchContent(helpFragment);
                break;
            case 1:
                if (helpForFragment == null) {
                    helpForFragment = new HelpForFragment();
                }
                tb.switchContent(helpForFragment);
                break;
            case 2:
                if (communityFragment == null){
                    communityFragment = new CommunityFragment();
                }
                tb.switchContent(communityFragment);
                break;
            case 3:
                if (chatFragment == null) {
                    chatFragment = new ChatFragment();
                }
                tb.switchContent(chatFragment);
                break;
            case 4:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                }
                tb.switchContent(myFragment);
                break;
            default:
                break;
        }
    }
}
