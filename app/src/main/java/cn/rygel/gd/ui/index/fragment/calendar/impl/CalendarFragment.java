package cn.rygel.gd.ui.index.fragment.calendar.impl;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import cn.rygel.gd.adapter.EventAdapter;
import cn.rygel.gd.ui.event.impl.AddEventActivity;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import cn.rygel.gd.widget.calendar.CustomItemCommon;
import cn.rygel.gd.widget.calendar.CustomItemSelected;
import cn.rygel.gd.widget.calendar.CustomItemToday;
import rygel.cn.calendar.bean.Lunar;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.LunarUtils;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.calendarview.CalendarView;
import rygel.cn.calendarview.listener.OnDateSelectedListener;
import rygel.cn.calendarview.listener.OnMonthChangedListener;
import rygel.cn.dateselector.DateSelector;
import rygel.cn.uilibrary.mvp.BaseFragment;
import skin.support.content.res.SkinCompatUserThemeManager;

public class CalendarFragment extends BaseFragment<CalendarPresenter> implements ICalendarView {

    @BindView(R.id.tb_main)
    Toolbar mToolbar;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.cv_calendar)
    CalendarView mCalendarView;

    @BindView(R.id.rv_events)
    RecyclerView mEvents;

    @BindView(R.id.fab_to_today)
    FloatingActionButton mFabBackToToday;

    EventAdapter mEventAdapter = null;
    Solar mSelected = SolarUtils.today();

    private View mCalendarHeader;

    private TextView mTvDate;
    private TextView mTvLunarInfo;
    private TextView mTvDateInfo;

    private DateSelector mDateSelector;
    private MaterialDialog mDialog = null;

    @Override
    protected CalendarPresenter createPresenter() {
        return new CalendarPresenter();
    }

    @Override
    public void onResume() {
        mCalendarView.getConfig().getOptions().mThemeColor = Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault());
        mCalendarView.getConfig().config();
        mDateSelector.setThemeColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));
        super.onResume();
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this,view);
        mToolbar.setNavigationOnClickListener(l -> {
            EventBus.getDefault().post(new OnDrawerStateChangeEvent(true));
        });
        initPickers();
        mTvTitle.setOnClickListener(l -> {
            mDialog.show();
        });
        initEventList();
        initCalendarView();
    }

    private void initPickers() {
        if (getContext() == null) {
            return;
        }
        mDateSelector = new DateSelector(getContext());
        mDateSelector.setOndateSelectListener(new DateSelector.OnDateSelectListener() {
            @Override
            public void onSelect(Solar solar, boolean isLunarMode) {
                mDialog.dismiss();
                mCalendarView.setSelect(new Solar(solar.solarYear, solar.solarMonth, solar.solarDay));
            }
        });
        mDialog = new MaterialDialog.Builder(getContext())
                .customView(mDateSelector, false)
                .build();
    }

    private void initEventList() {
        mEventAdapter = generateAdapter();
        mEvents.setAdapter(mEventAdapter);
        mEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        mCalendarHeader = LayoutInflater.from(getContext()).inflate(R.layout.item_calendar_events_header, null, false);
        mTvDate = mCalendarHeader.findViewById(R.id.tv_date);
        mTvLunarInfo = mCalendarHeader.findViewById(R.id.tv_date_lunar_info);
        mTvDateInfo = mCalendarHeader.findViewById(R.id.tv_date_info);
        loadDateInfo(mSelected);
        mEventAdapter.setHeaderView(mCalendarHeader);
        // 减少滑动卡顿
        mEvents.setNestedScrollingEnabled(false);
        mEvents.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void loadDateInfo(Solar solar) {
        Solar date = new Solar(solar.solarYear, solar.solarMonth, solar.solarDay);
        Lunar lunar = date.toLunar();
        mTvDate.setText(String.valueOf(date.solarDay));
        String lunarStr = (lunar.isLeap ? "闰" : "") + LunarUtils.LUNAR_MONTHS[lunar.lunarMonth - 1] + "月" + LunarUtils.LUNAR_DAYS[lunar.lunarDay - 1];
        mTvLunarInfo.setText(lunarStr);
        mTvDateInfo.setText(StringUtils.getStringArray(R.array.weekdays)[SolarUtils.getWeekDay(date)]);
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
                        Settings.getInstance().getWeekdayOffset()
                )
                .config();
        mCalendarView.getConfig()
                .setItemCommon(new CustomItemCommon(mCalendarView))
                .setItemSelected(new CustomItemSelected(mCalendarView))
                .setItemToday(new CustomItemToday(mCalendarView))
                .config();
        mCalendarView.setOnDateSelectListener(new OnDateSelectedListener() {
            @Override
            public void onSelected(Solar solar) {
                CalendarFragment.this.onDateSelect(solar);
                loadDateInfo(solar);
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
        mTvTitle.setText(StringUtils.getString(R.string.yyyy_MM, year, month));
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
