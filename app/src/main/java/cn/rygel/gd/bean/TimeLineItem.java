package cn.rygel.gd.bean;

import java.util.Set;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.utils.LunarUtils;

public class TimeLineItem {

    private LunarUtils.Solar mDate;
    private Set<BaseEvent> mEvents;

    public LunarUtils.Solar getDate() {
        return mDate;
    }

    public void setDate(LunarUtils.Solar date) {
        mDate = date;
    }

    public Set<BaseEvent> getEvents() {
        return mEvents;
    }

    public void setEvents(Set<BaseEvent> events) {
        mEvents = events;
    }
}
