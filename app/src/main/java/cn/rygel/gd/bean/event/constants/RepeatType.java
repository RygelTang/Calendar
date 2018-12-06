package cn.rygel.gd.bean.event.constants;

public enum RepeatType {
    NO_REPEAT(0),
    EVERY_DAY(1),
    EVERY_WEEK(2),
    EVERY_MONTH(3),
    EVERY_YEAR(4);

    public int mIndex = -1;

    RepeatType(int index){
        mIndex = index;
    }
}