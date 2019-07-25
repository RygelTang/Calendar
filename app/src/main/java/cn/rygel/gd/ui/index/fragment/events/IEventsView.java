package cn.rygel.gd.ui.index.fragment.events;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import rygel.cn.uilibrary.mvp.IView;

public interface IEventsView extends IView {

    void onAdapterGenerated(List<BaseQuickAdapter> adapters);

}
