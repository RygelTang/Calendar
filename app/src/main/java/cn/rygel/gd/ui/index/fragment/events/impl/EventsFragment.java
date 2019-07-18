package cn.rygel.gd.ui.index.fragment.events.impl;

import android.view.View;

import cn.rygel.gd.ui.index.fragment.events.IEventsView;
import rygel.cn.uilibrary.mvp.BaseFragment;

public class EventsFragment extends BaseFragment<EventsPresenter> implements IEventsView {
    @Override
    protected EventsPresenter createPresenter() {
        return new EventsPresenter();
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResID() {
        return 0;
    }

    @Override
    public void refresh() {

    }
}
