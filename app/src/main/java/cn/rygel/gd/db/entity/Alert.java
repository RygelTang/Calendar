package cn.rygel.gd.db.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Alert {

    @Id
    long mId;

    long mAdvanceTime;
    long mDelayTime;

    public long getId() {
        return mId;
    }

    public Alert setId(long id) {
        mId = id;
        return this;
    }

    public long getAdvanceTime() {
        return mAdvanceTime;
    }

    public Alert setAdvanceTime(long advanceTime) {
        mAdvanceTime = advanceTime;
        return this;
    }

    public long getDelayTime() {
        return mDelayTime;
    }

    public Alert setDelayTime(long delayTime) {
        mDelayTime = delayTime;
        return this;
    }
}
