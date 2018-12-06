package cn.rygel.gd.db.converter;

import cn.rygel.gd.bean.event.constants.EventType;
import io.objectbox.converter.PropertyConverter;

import static cn.rygel.gd.bean.event.constants.EventType.EVENT_TYPE_SUPPORT;

public class EventTypeConverter implements PropertyConverter<EventType,Integer> {

    @Override
    public EventType convertToEntityProperty(Integer databaseValue) {
        return EVENT_TYPE_SUPPORT.get(databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(EventType entityProperty) {
        return EventType.EVENT_TYPE_SUPPORT.indexOf(entityProperty);
    }
}
