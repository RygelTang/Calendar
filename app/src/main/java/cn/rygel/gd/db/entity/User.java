package cn.rygel.gd.db.entity;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.IndexType;
import io.objectbox.annotation.Unique;
import io.objectbox.relation.ToMany;

@Entity
public class User {

    @Id
    long mId;
    @Unique
    @Index(type = IndexType.HASH64)
    String mUserName;

    @Backlink(to = "mUser")
    ToMany<Event> mEvents;

    public ToMany<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(ToMany<Event> events) {
        mEvents = events;
    }

    public long getId() {
        return mId;
    }

    public User setId(long id) {
        mId = id;
        return this;
    }

    public String getUserName() {
        return mUserName;
    }

    public User setUserName(String userName) {
        mUserName = userName;
        return this;
    }

}
