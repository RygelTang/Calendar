package cn.rygel.gd.widget.calendar.listener;

import rygel.cn.calendardemo.utils.LunarUtils;

public interface OnDateSelectedListener {

    void onDateSelect(LunarUtils.Solar date);

    void onDateLongClick(LunarUtils.Solar date);

}
