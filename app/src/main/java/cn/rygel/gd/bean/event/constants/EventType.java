package cn.rygel.gd.bean.event.constants;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.app.APP;
import rygel.cn.uilibrary.utils.UIUtils;

import static cn.rygel.gd.bean.event.constants.RepeatType.EVERY_YEAR;
import static cn.rygel.gd.bean.event.constants.RepeatType.NO_REPEAT;

public class EventType implements Parcelable {

    public static final List<EventType> EVENT_TYPE_SUPPORT = new ArrayList<>();

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_BIRTHDAY = 1;
    public static final int TYPE_MEETING = 2;
    public static final int TYPE_APPOINTMENT = 3;
    public static final int TYPE_MEMORIAL = 4;

    static {
        EVENT_TYPE_SUPPORT.add(new EventType(UIUtils.getString(APP.getInstance(), R.string.type_default),NO_REPEAT));
        EVENT_TYPE_SUPPORT.add(new EventType(UIUtils.getString(APP.getInstance(), R.string.type_birthday),EVERY_YEAR));
        EVENT_TYPE_SUPPORT.add(new EventType(UIUtils.getString(APP.getInstance(), R.string.type_meeting),NO_REPEAT));
        EVENT_TYPE_SUPPORT.add(new EventType(UIUtils.getString(APP.getInstance(), R.string.type_appointment),NO_REPEAT));
        EVENT_TYPE_SUPPORT.add(new EventType(UIUtils.getString(APP.getInstance(), R.string.type_memorial),EVERY_YEAR));
    }

    private String mDescription = "";
    public RepeatType mRepeatType = NO_REPEAT;

    public EventType(String description, RepeatType repeatType){
        mDescription = description;
        mRepeatType = repeatType;
    }

    public String getDescription() {
        return mDescription;
    }

    /************************************************* 支持Parcelable *************************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(EVENT_TYPE_SUPPORT.indexOf(this));
    }

    public static final Creator<EventType> CREATOR = new Creator<EventType>() {
        @Override
        public EventType createFromParcel(Parcel source) {
            return EVENT_TYPE_SUPPORT.get(source.readInt());
        }

        @Override
        public EventType[] newArray(int size) {
            return null;
        }
    };
}