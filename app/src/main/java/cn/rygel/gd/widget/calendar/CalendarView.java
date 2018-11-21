package cn.rygel.gd.widget.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

// TODO: 2018/11/4 对外暴露的属性以及方法的设计，以及接口的设计
public class CalendarView extends ViewPager {

    /**
     * 构造方法
     * @param context
     */
    public CalendarView(Context context){
        this(context,null);
    }

    /**
     * 构造方法
     * @param context
     * @param attrs
     */
    public CalendarView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

}
