package cn.rygel.gd.widget.calendar;

import cn.rygel.gd.utils.LunarUtils;

public interface OnDateSelectedListener {

    void onDateSelect(LunarUtils.Solar date);

    void onDateLongClick(LunarUtils.Solar date);

}
