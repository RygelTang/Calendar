package cn.rygel.gd.ui.index.fragment.events;

import java.util.List;

import cn.rygel.gd.widget.adapter.EventListAdapter;
import rygel.cn.uilibrary.mvp.IView;

public interface IEventsView extends IView {

    void onAdapterGenerated(List<EventListAdapter> adapters);

}
