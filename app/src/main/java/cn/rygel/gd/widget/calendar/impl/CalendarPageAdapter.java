package cn.rygel.gd.widget.calendar.impl;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.rygel.gd.utils.LunarUtils;
import cn.rygel.gd.widget.calendar.bean.ParamBean;
import cn.rygel.gd.widget.calendar.helper.CalendarDataHelper;
import cn.rygel.gd.widget.calendar.impl.helper.DefaultCalendarDataHelper;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;

public class CalendarPageAdapter extends PagerAdapter {

    private static final int MONTH_COUNT = 12 * 199;

    private int mCacheSize = 7;

    private List<RealCalendarView> mCachedCalendarViews = new ArrayList<>();

    private CalendarDataHelper mCalendarDataHelper = new DefaultCalendarDataHelper();

    private ParamBean mParam = new ParamBean();

    private OnDateSelectedListener mOnDateSelectedListener = null;

    private OnMonthChangedListener mOnMonthChangedListener = null;

    private ViewPager mCalendarPager = null;

    private LunarUtils.Solar mTargetToSelect = null;

    public CalendarPageAdapter(ViewPager calendarPager) {
        mCalendarPager = calendarPager;
        mCalendarPager.setOffscreenPageLimit(0);
        mCalendarPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(mOnMonthChangedListener != null){
                    mOnMonthChangedListener.onMonthChanged(getYearByPosition(i),getMonthByPosition(i));
                }
                removeSelectItem();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if(position < 0 ){
            return super.instantiateItem(container, position);
        }
        while (mCachedCalendarViews.size() < mCacheSize){
            RealCalendarView cache = new RealCalendarView(container.getContext());
            cache.setParam(mParam);
            cache.setOnDateSelectedListener(mOnDateSelectedListener);
            mCachedCalendarViews.add(cache);
        }
        RealCalendarView calendar = mCachedCalendarViews.get(position % mCacheSize);
        if(position == getIndexByDate(mTargetToSelect)) {
            calendar.setSelectIndex(mTargetToSelect.solarDay - 1);
        }
        calendar.setCalendarData(mCalendarDataHelper.getCalendarData(getYearByPosition(position), getMonthByPosition(position)));
        container.addView(calendar);
        return calendar;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return MONTH_COUNT;
    }

    private int getIndexByDate(LunarUtils.Solar solar){
        if(solar == null) {
            return -1;
        }
        return (solar.solarYear - 1901) * 12 + solar.solarMonth - 1;
    }

    protected void select(LunarUtils.Solar solar){
        mTargetToSelect = solar;
        int targetIndex = getIndexByDate(solar);
        mCalendarPager.setCurrentItem(targetIndex,true);
    }

    protected LunarUtils.Solar getSelectDate(){
        int currentItem = mCalendarPager.getCurrentItem();
        int selectIndex = mCachedCalendarViews.get(currentItem % mCacheSize).getSelectIndex();
        if(selectIndex < 0){
            return null;
        }
        return new LunarUtils.Solar(getYearByPosition(currentItem),getMonthByPosition(currentItem),selectIndex + 1);
    }

    protected int getYearByPosition(int position){
        return position / 12 + 1901;
    }

    protected int getMonthByPosition(int position){
        return position % 12 + 1;
    }

    protected void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
        mCachedCalendarViews.clear();
    }

    protected void setOnMonthChangedListener(OnMonthChangedListener onMonthChangedListener) {
        mOnMonthChangedListener = onMonthChangedListener;
    }

    protected void setCalendarDataHelper(CalendarDataHelper calendarDataHelper) {
        mCalendarDataHelper = calendarDataHelper;
    }

    protected void setCacheSize(int cacheSize){
        mCacheSize = cacheSize;
        if(mCacheSize <= 7){
            mCacheSize = 7;
        }
        mCachedCalendarViews.clear();
    }

    protected void setParam(ParamBean param) {
        mParam = param;
        mCachedCalendarViews.clear();
    }

    protected void removeSelectItem(){
        for(RealCalendarView calendar : mCachedCalendarViews){
            calendar.setSelectIndex(-1);
        }
    }

}
