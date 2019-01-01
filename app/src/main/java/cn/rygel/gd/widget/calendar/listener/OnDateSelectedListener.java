package cn.rygel.gd.widget.calendar.listener;

import cn.rygel.gd.utils.calendar.LunarUtils;

public interface OnDateSelectedListener {

    void onDateSelect(LunarUtils.Solar date);

    void onDateLongClick(LunarUtils.Solar date);

}
