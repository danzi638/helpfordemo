package com.example.jiayin.helpfordemo.community.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.community.adapter.CommentListAdapter;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class CommunityDetailActivity extends BaseActivity {

    private static final String TAG = "CommunityDetailActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_createtime)
    TextView tvCreatetime;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.iv_image1)
    ImageView ivImage1;
    @Bind(R.id.iv_image2)
    ImageView ivImage2;
    @Bind(R.id.iv_image3)
    ImageView ivImage3;
    @Bind(R.id.rv_comment)
    RecyclerView rvComment;
    @Bind(R.id.iv_say)
    ImageView ivSay;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    @Bind(R.id.sv_main)
    ScrollView svMain;
    @Bind(R.id.iv_comment_up)
    ImageView ivCommentUp;
    @Bind(R.id.iv_comment_down)
    ImageView ivCommentDown;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.tv_comment_shafa)
    TextView tvCommentShafa;

    private String objectid;
    private String userid;
    private String create_at;
    private String title;
    private String content;
    private String image1;
    private String image2;
    private String image3;
    private String comment;

    private CommentListAdapter commentListAdapter;
    private List<AVObject> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        ButterKnife.bind(this);
        toolbar.setTitle("文章");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridLayoutManager layoutManager = new GridLayoutManager(CommunityDetailActivity.this, 1);
        rvComment.setLayoutManager(layoutManager);
        commentListAdapter = new CommentListAdapter(CommunityDetailActivity.this, mList);
        rvComment.setAdapter(commentListAdapter);

        initData();
        initView();
        initListener();
    }


    private void initData() {
        objectid = getIntent().getStringExtra(Constant.OBJECT_ID);
        userid = getIntent().getStringExtra(Constant.USER_ID);
        create_at = getIntent().getStringExtra(Constant.CREATE_AT);
        title = getIntent().getStringExtra(Constant.TITLE);
        content = getIntent().getStringExtra(Constant.CONTENT);
        image1 = getIntent().getStringExtra(Constant.IMAGE_PATH_1);
        image2 = getIntent().getStringExtra(Constant.IMAGE_PATH_2);
        image3 = getIntent().getStringExtra(Constant.IMAGE_PATH_3);
        refresh();

    }

    private void initView() {
        tvTitle.setText(title);
        tvCreatetime.setText(create_at);
        tvAuthor.setText(userid);
        tvContent.setText("\u3000\u3000" + content);
        if (!image1.equals("")) {
            Glide.with(CommunityDetailActivity.this).load(image1).into(ivImage1);
        }
        if (!image2.equals("")) {
            Glide.with(CommunityDetailActivity.this).load(image2).into(ivImage2);
        }
        if (!image3.equals("")) {
            Glide.with(CommunityDetailActivity.this).load(image3).into(ivImage3);
        }
    }

    private void initListener() {
        ivSay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdit();
            }
        });
        ivCommentDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintEdit();
            }
        });
        ivCommentUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdit();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = etComment.getText().toString().trim();
                Log.e(TAG, "initData: " + comment);
                if (comment.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "请输入评论的内容", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {
                    AVObject comment_message = new AVObject("comment_message");
                    comment_message.put("community_objectid", objectid);
                    comment_message.put("userid", EMClient.getInstance().getCurrentUser());
                    comment_message.put("content", comment);
                    comment_message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                refresh();
                                TastyToast.makeText(getApplicationContext(), "评论成功", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            } else {
                                TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                        }
                    });
                }
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
    }


    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我正在使用随手快递，大家快来");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("http://i4.bvimg.com/612636/3dfd856b9950bbb0.png");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我正在使用随手快递，大家快来");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    private void refresh() {
        mList.clear();
        AVQuery<AVObject> comment_message = new AVQuery<>("comment_message");
        comment_message.whereEqualTo("community_objectid", objectid);
        comment_message.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.e(TAG, "done: " + list.size());
                    mList.addAll(list);
                    commentListAdapter.notifyDataSetChanged();
                } else {
                    TastyToast.makeText(getApplicationContext(), "出错了" + e.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
    }

    private void hintEdit() {
        ivSay.setVisibility(View.VISIBLE);
        ivShare.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.GONE);
        etComment.setVisibility(View.GONE);
        ivCommentUp.setVisibility(View.VISIBLE);
        ivCommentDown.setVisibility(View.GONE);
    }

    private void showEdit() {
        ivSay.setVisibility(View.GONE);
        ivShare.setVisibility(View.GONE);
        btnCommit.setVisibility(View.VISIBLE);
        etComment.setVisibility(View.VISIBLE);
        ivCommentUp.setVisibility(View.GONE);
        ivCommentDown.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}