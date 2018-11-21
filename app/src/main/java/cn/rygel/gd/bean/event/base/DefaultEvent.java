package cn.rygel.gd.bean.event.base;

import android.os.Parcel;

import cn.rygel.gd.bean.event.constants.EventType;

import static cn.rygel.gd.bean.event.constants.EventType.TYPE_DEFAULT;

public class DefaultEvent extends BaseEvent {

    protected long mDuration = 30 * 60 * 1000;
    protected boolean mShowStartNotification = true;
    protected boolean mShowEndNotification = false;
    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_DEFAULT);

    public DefaultEvent(){ }

    public DefaultEvent(Parcel in){
        super(in);
        mDuration = in.readLong();
        mShowStartNotification = in.readByte() == 1;
        mShowEndNotification = in.readByte() == 1;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public boolean isShowStartNotification() {
        return mShowStartNotification;
    }

    public void setShowStartNotification(boolean showStartNotification) {
        mShowStartNotification = showStartNotification;
    }

    public boolean isShowEndNotification() {
        return mShowEndNotification;
    }

    public void setShowEndNotification(boolean showEndNotification) {
        mShowEndNotification = showEndNotification;
    }

    /********************************************** 支持Parcelable ********************************************/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(mDuration);
        dest.writeByte((byte) (mShowStartNotification ? 1 : 0));
        dest.writeByte((byte) (mShowEndNotification ? 1 : 0));
    }

    public static final Creator<DefaultEvent> CREATOR = new Creator<DefaultEvent>() {
        @Override
        public DefaultEvent createFromParcel(Parcel source) {
            return new DefaultEvent(source);
        }

        @Override
        public DefaultEvent[] newArray(int size) {
            return new DefaultEvent[size];
        }
    };

}
