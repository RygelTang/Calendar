package cn.rygel.gd.ui.index.fragment.calendar;

import java.util.List;

import cn.rygel.gd.bean.event.base.BaseEvent;
import rygel.cn.uilibrary.mvp.IView;

public interface ICalendarView extends IView {

    void showEvents(List<BaseEvent> events);

}
