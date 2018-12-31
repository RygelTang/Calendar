package cn.rygel.gd.db.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Description {

    @Id
    long mId;

    String mDescription;

    public long getId() {
        return mId;
    }

    public Description setId(long id) {
        mId = id;
        return this;
    }

    public String getDescription() {
        return mDescription;
    }

    public Description setDescription(String description) {
        mDescription = description;
        return this;
    }
}
