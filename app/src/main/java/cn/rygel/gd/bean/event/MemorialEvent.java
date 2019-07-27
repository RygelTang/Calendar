package cn.rygel.gd.bean.event;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.bean.event.constants.RepeatType;

import static cn.rygel.gd.bean.event.constants.EventType.TYPE_MEMORIAL;

public class MemorialEvent extends BaseEvent {

    public MemorialEvent() {
        mRepeatType = RepeatType.EVERY_YEAR;
    }

    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_MEMORIAL);

    @Override
    public EventType getEventType() {
        return mEventType;
    }

}
