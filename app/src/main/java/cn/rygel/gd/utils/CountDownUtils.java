package cn.rygel.gd.utils;

import cn.rygel.gd.bean.event.base.BaseEvent;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;

public class CountDownUtils {

    public static int getInterval(BaseEvent event, Solar solar) {

        if (event == null || solar == null) {
            throw new IllegalArgumentException("event or solar should not be null");
        }

        final boolean isLunar = event.isLunarEvent();
        if (isLunar) {
            return getLunarInterval(event, solar);
        } else {
            return getSolarInterval(event, solar);
        }

    }

    private static int getSolarInterval(BaseEvent event, Solar solar) {
        switch (event.getEventType().mRepeatType) {
            case NO_REPEAT:
                return SolarUtils.getIntervalDays(event.getEventSolarDate(), solar);
            case EVERY_DAY:
                return 0;
            case EVERY_WEEK:
                return SolarUtils.getIntervalDays(event.getEventSolarDate(), solar);
            case EVERY_MONTH:
                // TODO: 2019/7/22 no implement
                return Solar.CONTENTS_FILE_DESCRIPTOR;



        }
        return 0;
    }

    private static int getLunarInterval(BaseEvent event, Solar solar) {
        return 0;
    }
}
