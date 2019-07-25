package cn.rygel.gd.ui.index.fragment.calendar.impl;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnDateEventDeleteAllEvent;
import cn.rygel.gd.bean.OnDrawerStateChangeEvent;
import cn.rygel.gd.bean.OnEventAddedEvent;
import cn.rygel.gd.bean.OnWeekDayOffsetSelectEvent;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.setting.Settings;
import cn.rygel.gd.widget.adapter.EventAdapter;
import cn.rygel.gd.ui.event.impl.AddEventActivity;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.calendarview.CalendarView;
import rygel.cn.calendarview.listener.OnDateSelectedListener;
import rygel.cn.calendarview.listener.OnMonthChangedListener;
import rygel.cn.uilibrary.mvp.BaseFragment;

public class CalendarFragment extends BaseFragment<CalendarPresenter> implements ICalendarView {

    @BindView(R.id.tb_main)
    Toolbar mToolbar;

    @BindView(R.id.cv_calendar)
    CalendarView mCalendarView;

    @BindView(R.id.rv_events)
    RecyclerView mEvents;

    @BindView(R.id.fab_to_today)
    FloatingActionButton mFabBackToToday;

    EventAdapter mEventAdapter = null;
    Solar mSelected = SolarUtils.today();

    @Override
    protected CalendarPresenter createPresenter() {
        return new CalendarPresenter();
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this,view);
        mToolbar.setNavigationOnClickListener(l -> {
            EventBus.getDefault().post(new OnDrawerStateChangeEvent(true));
        });
        initEventList();
        initCalendarView();
    }

    private void initEventList() {
        mEventAdapter = generateAdapter();
        mEvents.setAdapter(mEventAdapter);
        mEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        // 减少滑动卡顿
        mEvents.setNestedScrollingEnabled(false);
        mEvents.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initCalendarView(){
        Solar today = SolarUtils.today();
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void OnMonthChange(int year, int month) {
                CalendarFragment.this.onMonthChanged(year, month);
            }
        });
        mCalendarView.getToMonth(today.solarYear,today.solarMonth,false);
        mCalendarView.getConfig()
                .setStartOffset(
                        Settings.getInstance()
                                .getWeekdayOffset()
                )
                .config();
        mCalendarView.setOnDateSelectListener(new OnDateSelectedListener() {
            @Override
            public void onSelected(Solar solar) {
                CalendarFragment.this.onDateSelect(solar);
            }
        });
    }

    @OnClick(R.id.fab_add_event)
    protected void addEvent(){
        AddEventActivity.start(getContext(),null,null,null);
    }

    @OnClick(R.id.fab_to_today)
    protected void back2Today() {
        mCalendarView.setSelect(SolarUtils.today());
    }

    @Override
    protected void loadData() {

    }

    private void reloadData() {
        getPresenter().loadEventsOf(mSelected);
    }

    @Override
    public void showEvents(List<BaseEvent> events) {
        if(mEventAdapter == null) {
            initEventList();
        }
        mEventAdapter.setNewData(events);
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
        mToolbar.setTitle(StringUtils.getString(R.string.yyyy_MM, year, month));
        // 判断是否显示fab
        Solar today = SolarUtils.today();
        if (year != today.solarYear || month != today.solarMonth) {
            mFabBackToToday.show();
        }
    }

    private void onDateSelect(Solar solar){
        Logger.i("select date : " +  solar);
        mSelected = solar;
        getPresenter().loadEventsOf(solar);
        // 判断是否显示fab
        if (solar.equals(SolarUtils.today())) {
            mFabBackToToday.hide();
        } else {
            mFabBackToToday.show();
        }
    }

    private EventAdapter generateAdapter() {
        return new EventAdapter(new ArrayList<>());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDrawerStateChanged(OnEventAddedEvent event) {
        // 重新加载数据
        reloadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOptionChanged(OnWeekDayOffsetSelectEvent event) {
        mCalendarView.getConfig().setStartOffset(event.getOffset()).config();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDelete(OnDateEventDeleteAllEvent event) {
        reloadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
