package cn.rygel.gd.bean.event.base;

import android.os.Parcel;

public class LocationEvent extends DefaultEvent {

    protected String mLocation = "";

    public LocationEvent(){ }

    public LocationEvent(Parcel in){
        super(in);
        mLocation = in.readString();
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    /********************************************** 支持Parcelable ********************************************/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mLocation);
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
