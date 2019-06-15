package cn.rygel.gd.ui.index.fragment.calendar;

import java.util.List;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.widget.timeline.bean.TimeLineItem;
import rygel.cn.uilibrary.mvp.IView;

public interface ICalendarView extends IView {

    void showEvents(List<BaseEvent> events);

}
