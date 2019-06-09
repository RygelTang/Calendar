package cn.rygel.gd.bean.event;

import cn.rygel.gd.bean.event.base.LocationEvent;
import cn.rygel.gd.bean.event.constants.EventType;

import static cn.rygel.gd.bean.event.constants.EventType.TYPE_APPOINTMENT;

public class AppointmentEvent extends LocationEvent {

    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_APPOINTMENT);

    @Override
    public EventType getEventType() {
        return mEventType;
    }

}
