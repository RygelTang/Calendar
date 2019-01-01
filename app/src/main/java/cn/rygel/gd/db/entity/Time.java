package cn.rygel.gd.db.entity;

import cn.rygel.gd.bean.event.constants.RepeatType;
import cn.rygel.gd.db.converter.LunarConverter;
import cn.rygel.gd.db.converter.RepeatTypeConverter;
import cn.rygel.gd.db.converter.SolarConverter;
import cn.rygel.gd.utils.calendar.LunarUtils;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Time {

    @Id
    long mId;

    @Backlink(to = "mTime")
    ToOne<Event> mEvent;

    @Convert(converter = RepeatTypeConverter.class,dbType = Integer.class)
    RepeatType mRepeatType;

    @Convert(converter = LunarConverter.class,dbType = Integer.class)
    LunarUtils.Lunar mLunar;

    @Convert(converter = SolarConverter.class,dbType = Integer.class)
    LunarUtils.Solar mSolar;

    ToOne<Alert> mAlert;

    boolean mIsLunar;

    long mStart;
    long mDuration;

    int mTimeZone;

    public long getId() {
        return mId;
    }

    public Time setId(long id) {
        mId = id;
        return this;
    }

    public ToOne<Event> getEvent() {
        return mEvent;
    }

    public Time setEvent(ToOne<Event> event) {
        mEvent = event;
        return this;
    }

    public RepeatType getRepeatType() {
        return mRepeatType;
    }

    public Time setRepeatType(RepeatType repeatType) {
        mRepeatType = repeatType;
        return this;
    }

    public LunarUtils.Lunar getLunar() {
        return mLunar;
    }

    public Time setLunar(LunarUtils.Lunar lunar) {
        mLunar = lunar;
        return this;
    }

    public LunarUtils.Solar getSolar() {
        return mSolar;
    }

    public Time setSolar(LunarUtils.Solar solar) {
        mSolar = solar;
        return this;
    }

    public long getStart() {
        return mStart;
    }

    public Time setStart(long start) {
        mStart = start;
        return this;
    }

    public boolean isLunar() {
        return mIsLunar;
    }

    public Time setLunar(boolean lunar) {
        mIsLunar = lunar;
        return this;
    }

    public long getDuration() {
        return mDuration;
    }

    public Time setDuration(long duration) {
        mDuration = duration;
        return this;
    }

    public int getTimeZone() {
        return mTimeZone;
    }

    public Time setTimeZone(int timeZone) {
        mTimeZone = timeZone;
        return this;
    }

    public ToOne<Alert> getAlert() {
        return mAlert;
    }

    public Time setAlert(ToOne<Alert> alert) {
        mAlert = alert;
        return this;
    }
}
