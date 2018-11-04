package cn.mt.gd.bean.event;

import cn.mt.gd.bean.event.base.DefaultEvent;
import cn.mt.gd.bean.event.constants.EventType;

import static cn.mt.gd.bean.event.constants.EventType.TYPE_APPOINTMENT;

public class AppointmentEvent extends DefaultEvent {

    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_APPOINTMENT);

    @Override
    public EventType getEventType() {
        return mEventType;
    }

}
