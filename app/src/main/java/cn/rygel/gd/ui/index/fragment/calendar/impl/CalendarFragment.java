package cn.rygel.gd.ui.index.fragment.calendar.impl;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.adapter.TimeLineAdapter;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.impl.CalendarView;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;
import rygel.cn.uilibrary.mvp.BaseFragment;
import rygel.cn.uilibrary.mvp.IPresenter;
import rygel.cn.uilibrary.utils.UIUtils;

public class CalendarFragment extends BaseFragment implements ICalendarView {

    private static final String TAG = "CalendarFragment";

    @BindView(R.id.tb_main)
    Toolbar mToolbar;

    @BindView(R.id.cv_calendar)
    CalendarView mCalendarView;

    @BindView(R.id.rv_time_line)
    RecyclerView mTimeLine;

    TimeLineAdapter mTimeLineAdapter = new TimeLineAdapter(new ArrayList<>());

    @Override
    protected IPresenter createPresenter() {
        return new CalendarPresenter();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this,view);
        mCalendarView.setOnDateSelectListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelect(LunarUtils.Solar date) {
                Toast.makeText(getContext(),new Gson().toJson(date),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateLongClick(LunarUtils.Solar date) {
                Toast.makeText(getContext(),new Gson().toJson(date),Toast.LENGTH_SHORT).show();
            }
        });
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(int year, int month) {
                CalendarFragment.this.onMonthChanged(year, month);
            }
        });
        mCalendarView.setSelect(CalendarUtils.today());
        mTimeLine.setAdapter(mTimeLineAdapter);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_calendar;
    }

    @Override
    public void refresh() {

    }

    private void onMonthChanged(int year,int month){
        mToolbar.setTitle(UIUtils.getString(getContext(),R.string.yyyy_MM,year,month));
    }

    private void onDateSelect(LunarUtils.Solar solar){

    }
}
