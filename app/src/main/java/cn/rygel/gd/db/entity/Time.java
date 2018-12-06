package cn.rygel.gd.db.entity;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Time {

    @Id
    long mId;
    @Backlink(to = "mTime")
    ToMany<Event> mEvent;

    int mLunarYear;
    int mLunarMonth;
    int mLunarDay;

    int mSolarYear;
    int mSolarMonth;
    int mSolarDay;

    int mStart;
    int mDuration;

    int mTimeZone;

}
