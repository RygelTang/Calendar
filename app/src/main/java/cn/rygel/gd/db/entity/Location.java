package cn.rygel.gd.db.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Location {

    @Id
    long mId;

    String mLocation;

    public long getId() {
        return mId;
    }

    public Location setId(long id) {
        mId = id;
        return this;
    }

    public String getLocation() {
        return mLocation;
    }

    public Location setLocation(String location) {
        mLocation = location;
        return this;
    }
}
