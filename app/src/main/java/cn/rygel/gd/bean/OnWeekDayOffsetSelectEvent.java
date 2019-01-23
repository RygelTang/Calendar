package cn.rygel.gd.bean;

public class OnWeekDayOffsetSelectEvent {

    private int mOffset;

    public OnWeekDayOffsetSelectEvent(int offset) {
        mOffset = offset;
    }

    public int getOffset() {
        return mOffset;
    }
}
