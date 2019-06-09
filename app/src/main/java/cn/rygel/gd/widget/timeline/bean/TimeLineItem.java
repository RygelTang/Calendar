package cn.rygel.gd.widget.timeline.bean;

import java.util.List;

import cn.rygel.gd.bean.event.base.BaseEvent;
import rygel.cn.calendar.bean.Solar;

public class TimeLineItem {

    private Solar mDate;
    private List<BaseEvent> mEvents;

    public TimeLineItem() { }

    public TimeLineItem(Solar date, List<BaseEvent> events) {
        mDate = date;
        mEvents = events;
    }

    public Solar getDate() {
        return mDate;
    }

    public void setDate(Solar date) {
        mDate = date;
    }

    public List<BaseEvent> getEvents() {
        return mEvents;
    }

    public void setEvents(List<BaseEvent> events) {
        mEvents = events;
    }
}
