package com.example.jiayin.helpfordemo.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.community.adapter.ImagePickerAdapter;
import com.example.jiayin.helpfordemo.utils.GlideImageLoader;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.listener.OnOperItemClickL;
import com.example.jiayin.helpfordemo.utils.pointDialog.dialog.widget.ActionSheetDialog;
import com.hyphenate.chat.EMClient;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.sdsmdg.tastytoast.TastyToast;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PublishCommunityActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    private static final String TAG = "PublishCommunityActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private String title;
    private String content;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 3;               //允许选择图片最大数
    ArrayList<ImageItem> images = null;
    private byte[] mImageBytes_0 = null;
    private byte[] mImageBytes_1 = null;
    private byte[] mImageBytes_2 = null;
    private String imageItem_0;
    private String imageItem_1;
    private String imageItem_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_community);
        ButterKnife.bind(this);
        toolbar.setTitle("发布文章");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initImagePicker();
        initWidget();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {

    }
    private void commitContent(){
        title = etTitle.getText().toString().trim();
        content = etContent.getText().toString().trim();
        if (selImageList.size() >=1){
            imageItem_0 = selImageList.get(0).path;
            Log.e(TAG, "initData: " + imageItem_0);
            mImageBytes_0 =getBytes(imageItem_0);

        }
        if (selImageList.size() >=2){
            imageItem_1 = selImageList.get(1).path;
            Log.e(TAG, "initData: " + imageItem_1);
            mImageBytes_1 =getBytes(imageItem_1);

        }
        if (selImageList.size() >=3){
            imageItem_2 = selImageList.get(2).path;
            Log.e(TAG, "initData: " + imageItem_2);
            mImageBytes_2 =getBytes(imageItem_2);

        }



        if (TextUtil.isEmpty(title)){
            TastyToast.makeText(getApplicationContext(),"请输入标题",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
        }
        if (TextUtil.isEmpty(content)){
            TastyToast.makeText(getApplicationContext(),"请输入内容",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
        }
        AVObject community_message = new AVObject("community_message");
        community_message.put("userid", EMClient.getInstance().getCurrentUser());
        community_message.put("title", title);
        community_message.put("content", content);
        if (mImageBytes_0 != null){
            community_message.put("image1", new AVFile("image1", mImageBytes_0));
        }
        if (mImageBytes_1 != null){
            community_message.put("image2", new AVFile("image2", mImageBytes_1));
        }
        if (mImageBytes_2 != null){
            community_message.put("image3", new AVFile("image3", mImageBytes_2));
        }
        community_message.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    TastyToast.makeText(getApplicationContext(),"保存成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    PublishCommunityActivity.this.finish();
                }else {
                    TastyToast.makeText(getApplicationContext(),"出错了" + e.toString(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }
            }
        });

    }
    public static byte[] getBytes(String filePath){
        File file = new File(filePath);
        ByteArrayOutputStream out = null;
        try {
            FileInputStream in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) != -1) {

                out.write(b, 0, b.length);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] s = out.toByteArray();
        return s;

    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"拍照", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(PublishCommunityActivity.this, stringItems, recyclerView);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        /**
                         * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                         *
                         * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                         *
                         * 如果实在有所需要，请直接下载源码引用。
                         */
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent = new Intent(PublishCommunityActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent1 = new Intent(PublishCommunityActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        break;

                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_prefectinformation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.backup) {
            commitContent();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position){
            case IMAGE_ITEM_ADD:
                ActionSheetDialogNoTitle();
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(PublishCommunityActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}