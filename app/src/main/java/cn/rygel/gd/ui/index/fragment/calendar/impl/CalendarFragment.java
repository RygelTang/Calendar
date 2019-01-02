package cn.rygel.gd.ui.index.fragment.calendar.impl;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.adapter.TimeLineAdapter;
import cn.rygel.gd.bean.TimeLineItem;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.impl.CalendarView;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;
import rygel.cn.uilibrary.mvp.BaseFragment;
import rygel.cn.uilibrary.utils.UIUtils;

public class CalendarFragment extends BaseFragment<CalendarPresenter> implements ICalendarView {

    private static final String TAG = "CalendarFragment";

    @BindView(R.id.tb_main)
    Toolbar mToolbar;

    @BindView(R.id.cv_calendar)
    CalendarView mCalendarView;

    @BindView(R.id.rv_time_line)
    RecyclerView mTimeLine;

    LunarUtils.Solar mEventLoadedStart = CalendarUtils.today();
    LunarUtils.Solar mEventLoadedEnd = CalendarUtils.today();

    TimeLineAdapter mTimeLineAdapter = new TimeLineAdapter(new ArrayList<>());

    @Override
    protected CalendarPresenter createPresenter() {
        return new CalendarPresenter();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this,view);
        initCalendarView();
        initTimeLine();
    }

    private void initCalendarView(){
        mCalendarView.setOnDateSelectListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelect(LunarUtils.Solar date) {
                CalendarFragment.this.onDateSelect(date);
            }

            @Override
            public void onDateLongClick(LunarUtils.Solar date) {

            }
        });
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(int year, int month) {
                CalendarFragment.this.onMonthChanged(year, month);
            }
        });
        mCalendarView.setSelect(CalendarUtils.today());
    }

    private void initTimeLine(){
        mTimeLine.setAdapter(mTimeLineAdapter);
    }

    @Override
    protected void loadData() {
        mEventLoadedStart.solarDay = 1;
        mEventLoadedEnd.solarDay = CalendarUtils.getMonthDay(mEventLoadedEnd.solarYear,mEventLoadedEnd.solarMonth);
        getPresenter().loadEventItemsInRange(mEventLoadedStart,mEventLoadedEnd);
    }

    @Override
    public void showEvents(List<TimeLineItem> items) {
        if(mTimeLineAdapter != null){
            if(items.size() > 0){
                if(mTimeLineAdapter.getItemCount() > 0 &&
                        CalendarUtils.compare(mTimeLineAdapter.getItem(0).getDate(), items.get(items.size() - 1).getDate())){
                    mTimeLineAdapter.addData(0, items);
                } else {
                    mTimeLineAdapter.addData(items);
                }
            }
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_calendar;
    }

    @Override
    public void refresh() {
        Logger.d(TAG,"do not support refresh action!");
    }

    private void onMonthChanged(int year,int month){
        Logger.i(TAG,"on month changed : year : " + year + " month : " + month);
        mToolbar.setTitle(UIUtils.getString(getContext(),R.string.yyyy_MM,year,month));
    }

    private void onDateLongClick(LunarUtils.Solar solar){
        Logger.i(TAG,"on long click date : " +  solar);
    }

    private void onDateSelect(LunarUtils.Solar solar){
        Logger.i(TAG,"select date : " +  solar);
    }
}
