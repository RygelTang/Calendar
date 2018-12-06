package cn.rygel.gd.db.entity;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Description {

    @Id
    long mId;
    @Backlink(to = "mDescription")
    ToMany<Event> mEvent;

    String mDescription;

}
