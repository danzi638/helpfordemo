package com.example.jiayin.helpfordemo.app.base;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jiayin.helpfordemo.utils.SwipeBackLayout;


/**
 * Created by Eric on 15/3/3.
 */
public class SwipeBackFragmentActivity extends FragmentActivity implements SwipeBackLayout.SwipeBackListener {

    private SwipeBackLayout swipeBackLayout;

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(getContainer());
        swipeBackLayout.addView(view);
    }

    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.setOnSwipeBackListener(this);
        container.addView(swipeBackLayout);
        return container;
    }

    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        if (null != swipeBackLayout)
            swipeBackLayout.setDragEdge(dragEdge);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    protected void enableSwipe(boolean enable) {
        if (null != swipeBackLayout) swipeBackLayout.setEnablePullToBack(enable);
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
    }

}

