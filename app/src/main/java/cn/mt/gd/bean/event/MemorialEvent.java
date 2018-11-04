package cn.mt.gd.bean.event;

import android.os.Parcel;

import cn.mt.gd.bean.event.base.BaseEvent;
import cn.mt.gd.bean.event.constants.EventType;

import static cn.mt.gd.bean.event.constants.EventType.TYPE_MEMORIAL;

public class MemorialEvent extends BaseEvent {

    protected boolean mShowNotification = true;
    protected EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(TYPE_MEMORIAL);

    public MemorialEvent(){ }

    public MemorialEvent(Parcel in){
        super(in);
        mShowNotification = in.readByte() == 1;
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

    public static final Creator<MemorialEvent> CREATOR = new Creator<MemorialEvent>() {
        @Override
        public MemorialEvent createFromParcel(Parcel source) {
            return new MemorialEvent(source);
        }

        @Override
        public MemorialEvent[] newArray(int size) {
            return new MemorialEvent[0];
        }
    };

}
