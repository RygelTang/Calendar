package rygel.cn.uilibrary.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

import com.orhanobut.logger.Logger;

public class SmoothRecyclerScroller extends LinearSmoothScroller {

    RecyclerView mRecyclerView = null;

    public SmoothRecyclerScroller(Context context) {
        super(context);
    }

    public SmoothRecyclerScroller bind(@NonNull RecyclerView recyclerView){
        mRecyclerView = recyclerView;
        return this;
    }

    public void smoothMoveToPosition(int position){
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if(!(manager instanceof LinearLayoutManager)){
            Logger.e("only support recycler view with LinearLayoutManager");
            return;
        }
        setTargetPosition(position);
        manager.startSmoothScroll(this);
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }

}
