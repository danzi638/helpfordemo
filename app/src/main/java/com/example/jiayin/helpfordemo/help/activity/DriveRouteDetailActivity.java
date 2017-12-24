package com.example.jiayin.helpfordemo.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.TMC;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseActivity;
import com.example.jiayin.helpfordemo.help.adapter.DriveSegmentListAdapter;
import com.example.jiayin.helpfordemo.help.util.amap.AMapUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 驾车路线详情
 */
public class DriveRouteDetailActivity extends BaseActivity {

    @Bind(R.id.firstline)
    TextView mTitleDriveRoute;
    @Bind(R.id.secondline)
    TextView mDesDriveRoute;
    @Bind(R.id.bus_segment_list)
    ListView mDriveSegmentList;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private DrivePath mDrivePath;
    private DriveRouteResult mDriveRouteResult;
    private DriveSegmentListAdapter mDriveSegmentListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.detail_drive);

        getIntentData();
        init();
    }

    private void init() {
//		mTitle.setText("驾车路线详情");
        String dur = AMapUtil.getFriendlyTime((int) mDrivePath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) mDrivePath
                .getDistance());
        mTitleDriveRoute.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mDriveRouteResult.getTaxiCost();
        mDesDriveRoute.setText("打车约" + taxiCost + "元");
        mDesDriveRoute.setVisibility(View.VISIBLE);
        configureListView();
    }

    private void configureListView() {
        mDriveSegmentListAdapter = new DriveSegmentListAdapter(
                this.getApplicationContext(), mDrivePath.getSteps());
        mDriveSegmentList.setAdapter(mDriveSegmentListAdapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mDrivePath = intent.getParcelableExtra("drive_path");
        mDriveRouteResult = intent.getParcelableExtra("drive_result");
        for (int i = 0; i < mDrivePath.getSteps().size(); i++) {
            DriveStep step = mDrivePath.getSteps().get(i);
            List<TMC> tmclist = step.getTMCs();
            for (int j = 0; j < tmclist.size(); j++) {
                String s = "" + tmclist.get(j).getPolyline().size();
                Log.i("MY", s + tmclist.get(j).getStatus()
                        + tmclist.get(j).getDistance()
                        + tmclist.get(j).getPolyline().toString());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
