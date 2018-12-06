package cn.rygel.gd.db.entity;

import cn.rygel.gd.bean.event.constants.RepeatType;
import cn.rygel.gd.db.converter.RepeatTypeConverter;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Repeat {

    @Id
    long mId;
    @Backlink(to = "mRepeat")
    ToMany<Event> mEvent;

    @Convert(converter = RepeatTypeConverter.class,dbType = Integer.class)
    RepeatType mRepeatType;

}
