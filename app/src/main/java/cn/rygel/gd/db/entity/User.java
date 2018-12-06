package cn.rygel.gd.db.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;

@Entity
public class User {

    @Id
    long mId;
    @Index
    String mUserName;

    ToMany<Event> mEvents;

}
