package cn.rygel.gd.utils;

import java.util.Comparator;

import rygel.cn.calendar.bean.Lunar;

public class LunarComparator implements Comparator<Lunar> {

    @Override
    public int compare(Lunar o1, Lunar o2) {
        return lunarToInt(o1) - lunarToInt(o2);
    }

    private static int lunarToInt(Lunar lunar) {
        return (lunar.lunarYear << 10) |
                (lunar.lunarMonth << 6) |
                (lunar.lunarDay << 1) |
                (lunar.isLeap ? 1 : 0);
    }
}
