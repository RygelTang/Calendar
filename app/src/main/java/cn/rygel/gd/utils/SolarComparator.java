package cn.rygel.gd.utils;

import java.util.Comparator;

import rygel.cn.calendar.bean.Solar;

public class SolarComparator implements Comparator<Solar> {

    @Override
    public int compare(Solar o1, Solar o2) {
        return solarToInt(o1) - solarToInt(o2);
    }

    private static int solarToInt(Solar solar) {
        return (solar.solarYear << 9) |
                (solar.solarMonth << 5) |
                solar.solarDay;
    }
}
