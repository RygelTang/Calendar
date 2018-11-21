package cn.rygel.gd.bean.event;

import android.os.Parcel;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.constants.EventType;

import static cn.rygel.gd.bean.event.constants.EventType.TYPE_BIRTHDAY;

public class BirthdayEvent extends BaseEvent {

    protected boolean mIsLunarEvent = true;
    protected boolean mShowNotification = true;
    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_BIRTHDAY);

    public BirthdayEvent(){ }

    public BirthdayEvent(Parcel in){
        super(in);
        mShowNotification = in.readByte() == 1;
    }

    @Override
    public boolean isLunarEvent() {
        return mIsLunarEvent;
    }

    @Override
    public void setLunarEvent(boolean lunarEvent) {
        mIsLunarEvent = lunarEvent;
    }

    public boolean isShowNotification() {
        return mShowNotification;
    }

    public void setShowNotification(boolean showNotification) {
        mShowNotification = showNotification;
    }

    @Override
    public EventType getEventType() {
        return mEventType;
    }

    /************************************************** 支持Parcelable **************************************************/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (mShowNotification ? 1 : 0));
    }

    public static final Creator<BirthdayEvent> CREATOR = new Creator<BirthdayEvent>() {
        @Override
        public BirthdayEvent createFromParcel(Parcel source) {
            return new BirthdayEvent(source);
        }

        @Override
        public BirthdayEvent[] newArray(int size) {
            return new BirthdayEvent[0];
        }
    };

}
