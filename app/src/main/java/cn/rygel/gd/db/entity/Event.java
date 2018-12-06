package cn.rygel.gd.db.entity;

import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.db.converter.EventTypeConverter;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Event {

    @Id
    long mId;
    @Backlink(to = "mEvents")
    ToOne<User> mUser;

    ToOne<Alert> mAlert;
    ToOne<Description> mDescription;
    ToOne<Repeat> mRepeat;
    ToOne<Time> mTime;

    @Convert(converter = EventTypeConverter.class,dbType = Integer.class)
    EventType mEventType;

    String mName;

    boolean mIsLunarEvent;
    boolean mShowNotificaion = true;

}
