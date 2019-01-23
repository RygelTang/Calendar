package cn.rygel.gd.widget.timeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.rygel.gd.widget.timeline.bean.TimeLineItem;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.timeline.adapter.TimeLineAdapter;
import rygel.cn.uilibrary.widget.SmoothRecyclerScroller;

public class TimeLineView extends RecyclerView {

    private static final LunarUtils.Solar EVENT_START = new LunarUtils.Solar(1901,1,1);
    private static final LunarUtils.Solar EVENT_END = new LunarUtils.Solar(2099,12,31);

    private LinearLayoutManager mLayoutManager = null;
    private SmoothRecyclerScroller mScroller = null;
    private TimeLineAdapter mTimeLineAdapter = new TimeLineAdapter(new ArrayList<>());

    private ILoadMoreListener mLoadMoreListener = null;
    private IDateSelectListener mDateSelectListener = null;

    private boolean mShouldTaskRunDelay = false;
    private Set<Runnable> mTaskAfterScroll = new HashSet<>();

    private LunarUtils.Solar mSelectDate = CalendarUtils.today();
    private boolean mScrolling = false;

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            mScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
            if(!mScrolling){
                if(mTimeLineAdapter.getData().size() > 0){
                    if(mLayoutManager != null){
                        final TimeLineItem firstItem = getFirstVisibleItem();
                        if(firstItem != null){
                            dispatchOnDateSelect(firstItem.getDate());
                        }
                    }
                }
                for (Runnable r : mTaskAfterScroll){
                    r.run();
                }
                mTaskAfterScroll.clear();
                mShouldTaskRunDelay = false;
            }
        }
    };

    public TimeLineView(@NonNull Context context) {
        super(context);
        init();
    }

    public TimeLineView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLineView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        mLayoutManager = new LinearLayoutManager(getContext());
        mScroller = new SmoothRecyclerScroller(getContext()).bind(this);
        initAdapter();
        addOnScrollListener(mOnScrollListener);
        setLayoutManager(mLayoutManager);
        setAdapter(mTimeLineAdapter);
    }

    private void initAdapter(){
        mTimeLineAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(mShouldTaskRunDelay) {
                    mTaskAfterScroll.add(new Runnable() {
                        @Override
                        public void run() {
                            onLoadMore(false);
                        }
                    });
                } else {
                    onLoadMore(false);
                }
            }
        },this);
        mTimeLineAdapter.setUpFetchEnable(true);
        mTimeLineAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {
                if(mShouldTaskRunDelay) {
                    mTaskAfterScroll.add(new Runnable() {
                        @Override
                        public void run() {
                            onLoadMore(true);
                        }
                    });
                } else {
                    onLoadMore(true);
                }
            }
        });
        mTimeLineAdapter.setStartUpFetchPosition(15);
        mTimeLineAdapter.setPreLoadNumber(15);
        mTimeLineAdapter.openLoadAnimation();
    }

    public void addEvents(List<TimeLineItem> items){
        if(mTimeLineAdapter != null){
            if(mTimeLineAdapter.isLoading()){
                if(items.size() > 0) {
                    if(!CalendarUtils.compare(EVENT_END,items.get(items.size() - 1).getDate())){
                        mTimeLineAdapter.loadMoreEnd();
                        Logger.i("load end");
                        return;
                    }
                    mTimeLineAdapter.addData(items);
                    mTimeLineAdapter.loadMoreComplete();
                    Logger.i("load complete");
                }

            }
        }
    }

    public void addEvents(int position, List<TimeLineItem> items){
        if(mTimeLineAdapter != null){
            if(mTimeLineAdapter.isLoading()){
                mTimeLineAdapter.loadMoreComplete();
            }
            if(items.size() > 0){
                if(CalendarUtils.compare(items.get(0).getDate(),EVENT_START)){
                    mTimeLineAdapter.addData(position,items);
                }
            }
            TimeLineItem firstVisible = getFirstVisibleItem();
            if(!mScrolling && firstVisible != null && !mSelectDate.equals(firstVisible.getDate())){
                smoothScrollToPosition(getItemLocation(mSelectDate));
            }
        }
    }

    private void dispatchOnDateSelect(LunarUtils.Solar solar){
        if(mDateSelectListener != null){
            mDateSelectListener.onDateSelect(solar);
        }
    }

    private void dispatchOnLoadMore(LunarUtils.Solar start,int interval,boolean isStart){
        if(mLoadMoreListener != null){
            mLoadMoreListener.onLoadMore(start,interval - 1,isStart);
        }
    }

    private void onLoadMore(boolean isStart){
        LunarUtils.Solar start = null;
        if(mTimeLineAdapter.getData().size() > 0) {
            LunarUtils.Solar solar = null;
            if(isStart){
                solar = CalendarUtils.yesterday(CalendarUtils.clone(mTimeLineAdapter.getData().get(0).getDate()));
            } else {
                solar = CalendarUtils.tomorrow(CalendarUtils.clone(mTimeLineAdapter.getData().get(mTimeLineAdapter.getData().size() - 1).getDate()));
            }
            start = new LunarUtils.Solar(solar.solarYear,solar.solarMonth,1);
        } else {
            start = new LunarUtils.Solar(mSelectDate.solarYear,mSelectDate.solarMonth,1);
        }
        int interval = CalendarUtils.getMonthDay(start.solarYear,start.solarMonth);
        dispatchOnLoadMore(start,interval ,isStart);
    }

    public void setDate(LunarUtils.Solar solar){
        mSelectDate = CalendarUtils.clone(solar);
        if(!checkOutOfRange(solar)){
            mTimeLineAdapter.getData().clear();
            mTimeLineAdapter.notifyDataSetChanged();
            onLoadMore(true);
        } else {
            final TimeLineItem firstItem = getFirstVisibleItem();
            if(firstItem != null && !firstItem.getDate().equals(solar)) {
                final int index = getItemLocation(solar);
                mShouldTaskRunDelay = index <= 15                                                                                                                                                                           ;
                smoothScrollToPosition(index);
            }
        }
    }

    public void setLoadMoreListener(ILoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void setDateSelectListener(IDateSelectListener dateSelectListener) {
        mDateSelectListener = dateSelectListener;
    }

    public void clear() {
        mTimeLineAdapter.setNewData(new ArrayList<>());
    }

    @Override
    public void smoothScrollToPosition(int position) {
        Logger.i("smooth move to position : " + position);
        mScroller.smoothMoveToPosition(position);
    }

    public TimeLineItem getFirstVisibleItem(){
        if(mTimeLineAdapter != null && mLayoutManager != null){
            final List<TimeLineItem> items = mTimeLineAdapter.getData();
            int index = mLayoutManager.findFirstCompletelyVisibleItemPosition();
            if(index < 0){
                index = mLayoutManager.findFirstVisibleItemPosition();
            }
            if(index >= 0 && items.size() > index){
                return items.get(index);
            }
        }
        return null;
    }

    private int getItemLocation(LunarUtils.Solar solar){
        return CalendarUtils.getIntervalDays(mTimeLineAdapter.getData().get(0).getDate(),solar);
    }

    public TimeLineAdapter getTimeLineAdapter(){
        return mTimeLineAdapter;
    }

    public List<TimeLineItem> getData(){
        if(mTimeLineAdapter == null){
            return null;
        }
        return mTimeLineAdapter.getData();
    }

    private boolean checkOutOfRange(LunarUtils.Solar solar){
        if(solar == null || mTimeLineAdapter == null || mTimeLineAdapter.getData().size() <= 0){
            return false;
        }
        return CalendarUtils.compare(solar,mTimeLineAdapter.getData().get(0).getDate()) &&
                CalendarUtils.compare(mTimeLineAdapter.getData().get(mTimeLineAdapter.getData().size() - 1).getDate(),solar);
    }

    public interface IDateSelectListener {
        void onDateSelect(LunarUtils.Solar date);
    }

    public interface ILoadMoreListener {
        void onLoadMore(LunarUtils.Solar start, int interval, boolean isStart);
    }
}
