package cn.rygel.gd.bean;

import java.util.List;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.utils.calendar.LunarUtils;

public class TimeLineItem {

    private LunarUtils.Solar mDate;
    private List<BaseEvent> mEvents;

    public TimeLineItem() { }

    public TimeLineItem(LunarUtils.Solar date, List<BaseEvent> events) {
        mDate = date;
        mEvents = events;
    }

    public LunarUtils.Solar getDate() {
        return mDate;
    }

    public void setDate(LunarUtils.Solar date) {
        mDate = date;
    }

    public List<BaseEvent> getEvents() {
        return mEvents;
    }

    public void setEvents(List<BaseEvent> events) {
        mEvents = events;
    }
}
