package cn.mt.gd.bean.event;

import cn.mt.gd.bean.event.base.DefaultEvent;
import cn.mt.gd.bean.event.constants.EventType;

import static cn.mt.gd.bean.event.constants.EventType.TYPE_MEETING;

public class MeetingEvent extends DefaultEvent {

    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_MEETING);

    @Override
    public EventType getEventType() {
        return mEventType;
    }
}
