package cn.rygel.gd.db.entity;

import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.db.converter.EventTypeConverter;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Event {

    @Id
    long mId;

    ToMany<User> mUser;

    ToOne<Description> mDescription;
    @Backlink(to = "mEvent")
    ToOne<Time> mTime;
    ToOne<Location> mLocation;

    @Convert(converter = EventTypeConverter.class,dbType = Integer.class)
    EventType mEventType;

    String mName;

    boolean mShowNotification = true;

    public long getId() {
        return mId;
    }

    public Event setId(long id) {
        mId = id;
        return this;
    }

    public ToMany<User> getUser() {
        return mUser;
    }

    public Event setUser(ToMany<User> user) {
        mUser = user;
        return this;
    }

    public ToOne<Description> getDescription() {
        return mDescription;
    }

    public void setDescription(ToOne<Description> description) {
        mDescription = description;
    }

    public ToOne<Time> getTime() {
        return mTime;
    }

    public void setTime(ToOne<Time> time) {
        mTime = time;
    }

    public ToOne<Location> getLocation() {
        return mLocation;
    }

    public void setLocation(ToOne<Location> location) {
        mLocation = location;
    }

    public EventType getEventType() {
        return mEventType;
    }

    public Event setEventType(EventType eventType) {
        mEventType = eventType;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Event setName(String name) {
        mName = name;
        return this;
    }

    public boolean isShowNotification() {
        return mShowNotification;
    }

    public Event setShowNotification(boolean showNotification) {
        mShowNotification = showNotification;
        return this;
    }
}
