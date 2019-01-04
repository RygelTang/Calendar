package cn.rygel.gd.ui.index.fragment.calendar.impl;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.widget.timeline.TimeLineView;
import cn.rygel.gd.bean.TimeLineItem;
import cn.rygel.gd.ui.event.impl.AddEventActivity;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.impl.CalendarView;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;
import rygel.cn.uilibrary.mvp.BaseFragment;
import rygel.cn.uilibrary.utils.UIUtils;

public class CalendarFragment extends BaseFragment<CalendarPresenter> implements ICalendarView {

    @BindView(R.id.tb_main)
    Toolbar mToolbar;

    @BindView(R.id.cv_calendar)
    CalendarView mCalendarView;

    @BindView(R.id.tl_calendar)
    TimeLineView mTimeLine;

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
                mTimeLine.setDate(date);
            }

            @Override
            public void onDateLongClick(LunarUtils.Solar date) {
                mTimeLine.setDate(date);
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
        mTimeLine.setDateSelectListener(new TimeLineView.IDateSelectListener() {
            @Override
            public void onDateSelect(LunarUtils.Solar date) {
                mCalendarView.setSelect(date);
            }
        });
        mTimeLine.setLoadMoreListener(new TimeLineView.ILoadMoreListener() {
            @Override
            public void onLoadMore(LunarUtils.Solar start, int interval, boolean isStart) {
                getPresenter().loadEventItemsInRange(start, interval, isStart);
            }
        });
    }

    @OnClick(R.id.fab_add_event)
    protected void addEvent(){
        AddEventActivity.start(getContext(),null,null,null);
    }

    @Override
    protected void loadData() {
        LunarUtils.Solar today = CalendarUtils.today();
        LunarUtils.Solar start = CalendarUtils.clone(today);
        start.solarDay = 1;
        getPresenter().loadEventItemsInRange(start,CalendarUtils.getMonthDay(start.solarYear,start.solarMonth) - 1,true);
    }

    @Override
    public void showEvents(List<TimeLineItem> items, boolean isStart) {
        if(isStart) {
            mTimeLine.addEvents(0,items);
        } else {
            mTimeLine.addEvents(items);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_calendar;
    }

    @Override
    public void refresh() {
        Logger.d("do not support refresh action!");
    }

    private void onMonthChanged(int year,int month){
        Logger.i("on month changed : year : " + year + " month : " + month);
        mToolbar.setTitle(UIUtils.getString(getContext(),R.string.yyyy_MM,year,month));
    }

    private void onDateSelect(LunarUtils.Solar solar){
        Logger.i("select date : " +  solar);

    }

}
