package cn.mt.gd.bean.event;

import cn.mt.gd.bean.event.base.DefaultEvent;
import cn.mt.gd.bean.event.constants.EventType;

public class CustomEvent extends DefaultEvent {

    protected EventType mEventType = null;

    public CustomEvent(EventType eventType){
        mEventType = eventType;
    }

    @Override
    public EventType getEventType() {
        return mEventType;
    }
}
