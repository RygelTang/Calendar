package cn.mt.gd.widget.calendar;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

// TODO: 2018/11/4 CalendarData计算工具类的实现
public class CalendarPageAdapter extends PagerAdapter {

    private static final int MONTH_COUNT = 12 * 200;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return MONTH_COUNT;
    }
}
