package cn.rygel.gd.widget.calendar.impl;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.bean.CalendarOptions;
import cn.rygel.gd.widget.calendar.helper.CalendarDataHelper;
import cn.rygel.gd.widget.calendar.impl.helper.DefaultCalendarDataHelper;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;

public class CalendarPageAdapter extends PagerAdapter {

    private static final int MONTH_COUNT = 12 * 199;

    private SparseArray<RealCalendarView> mCachedCalendarViews = new SparseArray<>();

    private CalendarDataHelper mCalendarDataHelper = new DefaultCalendarDataHelper();

    private CalendarOptions mOptions = new CalendarOptions();

    private OnDateSelectedListener mOnDateSelectedListener = null;

    private OnMonthChangedListener mOnMonthChangedListener = null;

    private ViewPager mCalendarPager = null;

    private LunarUtils.Solar mTargetToSelect = null;

    private int mLastItem = -1;

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
                mLastItem = i;
                if(i == getIndexByDate(mTargetToSelect)) {
                    RealCalendarView calendar = mCachedCalendarViews.get(i);
                    if(calendar != null){
                        calendar.setSelectIndex(mTargetToSelect.solarDay - 1);
                    }
                }
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
        RealCalendarView calendar = mCachedCalendarViews.get(position);
        if(calendar == null){
            calendar = new RealCalendarView(container.getContext());
            calendar.setCalendarOptions(mOptions);
            calendar.setOnDateSelectedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelect(LunarUtils.Solar date) {
                    mTargetToSelect = date;
                    if(mOnDateSelectedListener != null) {
                        mOnDateSelectedListener.onDateSelect(date);
                    }
                }

                @Override
                public void onDateLongClick(LunarUtils.Solar date) {
                    mTargetToSelect = date;
                    if(mOnDateSelectedListener != null) {
                        mOnDateSelectedListener.onDateLongClick(date);
                    }
                }
            });
            mCachedCalendarViews.put(position,calendar);
        }
        calendar.setCalendarData(mCalendarDataHelper.getCalendarData(getYearByPosition(position), getMonthByPosition(position)));
        if(position == mCalendarPager.getCurrentItem() && position == getIndexByDate(mTargetToSelect)) {
            if(calendar.getSelectIndex() != mTargetToSelect.solarDay - 1){
                calendar.setSelectIndex(mTargetToSelect.solarDay - 1);
            }
        }
        container.addView(calendar);
        return calendar;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        removeParent((View) object);
    }

    private void removeParent(View view){
        if(view.getParent() != null && view.getParent() instanceof ViewGroup){
            ((ViewGroup) view.getParent()).removeView(view);
        }
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
        if(targetIndex == mCalendarPager.getCurrentItem()){
            RealCalendarView calendar = mCachedCalendarViews.get(targetIndex);
            if(calendar != null){
                calendar.setSelectIndex(solar.solarDay - 1);
            }
        } else {
            mCalendarPager.setCurrentItem(targetIndex,true);
        }
    }

    protected LunarUtils.Solar getSelectDate(){
        int currentItem = mCalendarPager.getCurrentItem();
        int selectIndex = mCachedCalendarViews.get(currentItem).getSelectIndex();
        if(selectIndex < 0) {
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
        if(calendarDataHelper != null && mOptions != null){
            calendarDataHelper.setDateOffset(mOptions.getDateOffset());
        }
        mCalendarDataHelper = calendarDataHelper;
    }

    protected void setCalendarOptions(CalendarOptions options) {
        mOptions = options;
        if(mCalendarDataHelper != null && mOptions != null){
            mCalendarDataHelper.setDateOffset(mOptions.getDateOffset());
            mCalendarPager.invalidate();
        }
        mCachedCalendarViews.clear();
    }

    protected void removeSelectItem(){
        if(mLastItem >= 0){
            RealCalendarView calendar = mCachedCalendarViews.get(mLastItem);
            if(calendar != null){
                calendar.setSelectIndex(-1);
            }
        }
    }

}
