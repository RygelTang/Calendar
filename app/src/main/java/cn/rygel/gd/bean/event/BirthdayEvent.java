package cn.rygel.gd.bean.event;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.constants.EventType;

import static cn.rygel.gd.bean.event.constants.EventType.TYPE_BIRTHDAY;

public class BirthdayEvent extends BaseEvent {

    protected boolean mIsLunarEvent = true;
    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_BIRTHDAY);

    @Override
    public EventType getEventType() {
        return mEventType;
    }
}
