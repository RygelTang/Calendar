package cn.rygel.gd.widget.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Comparator;
import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
import rygel.cn.calendar.bean.Lunar;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;

public class EventListAdapter extends EventAdapter {

    private Solar start = null;
    private Solar end = null;

    public EventListAdapter(List<BaseEvent> events, Solar start, Solar end) {
        super(events);
        if (start == null) {
            throw new IllegalArgumentException("start date should not be null!");
        }
        if (end == null) {
            end = SolarUtils.getDayByInterval(start, 365);
        }
        this.start = start;
        this.end = end;
    }

    @Override
    protected void convert(BaseViewHolder helper, final BaseEvent item) {
        super.convert(helper, item);
        int duration = getEventDuration(item, start, end);
        if(duration > 0) {
            helper.setText(R.id.tv_duration, duration + "天后");
        }
    }

    private static int getEventDuration(BaseEvent event, Solar start, Solar end) {
        int interval = 0;
        switch (event.getEventType().mRepeatType) {
            case NO_REPEAT :
                return SolarUtils.getIntervalDays(start, event.getEventSolarDate());
            case EVERY_DAY:
                return 0;
            case EVERY_WEEK:
                return SolarUtils.getIntervalDays(start, event.getEventSolarDate()) % 7;
            case EVERY_MONTH:
                interval = SolarUtils.getIntervalDays(start, new Solar(start.solarYear, start.solarMonth, event.getEventSolarDate().solarDay));
                return interval < 0 ? interval + SolarUtils.getMonthDay(start.solarYear, start.solarMonth) : interval;
            case EVERY_YEAR:
                if (event.isLunarEvent()) {
                    Lunar date = event.getEventLunarDate();
                    interval = SolarUtils.getIntervalDays(start, new Lunar(false, start.toLunar().lunarYear, date.lunarMonth, date.lunarDay).toSolar());
                    return interval < 0 ? SolarUtils.getIntervalDays(start, new Lunar(false, start.toLunar().lunarYear + 1, date.lunarMonth, date.lunarDay).toSolar()) : interval;
                } else {
                    Solar date = event.getEventSolarDate();
                    interval = SolarUtils.getIntervalDays(start, new Solar(start.solarYear, date.solarMonth, date.solarDay));
                    return interval < 0 ? SolarUtils.getIntervalDays(start, new Solar(start.solarYear + 1, date.solarMonth, date.solarDay)) : interval;
                }
            default:
                return -1;
        }
    }

    /**
     * 二分法查找公历日期
     * @param solar
     * @param start
     * @param end
     * @return
     */
    private static Solar binarySearchSolar (Solar solar, Solar start, Solar end) {
        if (solar == null || start == null || end == null) {
            return null;
        }
        CustomSolarComparator comparator = new CustomSolarComparator();
        Solar mid = SolarUtils.getDayByInterval(start, SolarUtils.getIntervalDays(start, end) / 2);
        if (comparator.compare(mid, solar) < 0) {
            mid.solarDay += 1;
            return binarySearchSolar(solar, mid, end);
        } else if(comparator.compare(mid, solar) > 0) {
            mid.solarDay -= 1;
            return binarySearchSolar(solar, start, mid);
        } else {
            return mid;
        }
    }

    /**
     * 二分法查找农历日期
     * @param lunar
     * @param start
     * @param end
     * @return
     */
    private static Solar binarySearchLunar (Lunar lunar, Solar start, Solar end) {
        if (lunar == null || start == null || end == null) {
            return null;
        }
        CustomLunarComparator comparator = new CustomLunarComparator();
        Solar mid = SolarUtils.getDayByInterval(start, SolarUtils.getIntervalDays(start, end) / 2);
        Lunar midLunar = mid.toLunar();
        if (comparator.compare(midLunar, lunar) < 0) {
            mid.solarDay += 1;
            return binarySearchLunar(lunar, mid, end);
        } else if(comparator.compare(midLunar, lunar) > 0) {
            mid.solarDay -= 1;
            return binarySearchLunar(lunar, start, mid);
        } else {
            return mid;
        }
    }

    private static class CustomSolarComparator implements Comparator<Solar> {

        @Override
        public int compare(Solar o1, Solar o2) {
            return solarToInt(o1) - solarToInt(o2);
        }

        private int solarToInt(Solar solar) {
            return (solar.solarMonth << 6) |
                    (solar.solarDay << 1);
        }
    }

    private static class CustomLunarComparator implements Comparator<Lunar> {

        @Override
        public int compare(Lunar o1, Lunar o2) {
            return lunarToInt(o1) - lunarToInt(o2);
        }

        private int lunarToInt(Lunar lunar) {
            return (lunar.lunarMonth << 6) |
                    (lunar.lunarDay << 1);
        }
    }

}
