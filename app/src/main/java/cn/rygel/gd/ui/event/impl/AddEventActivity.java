package cn.rygel.gd.ui.event.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.ui.event.IAddEventView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import rygel.cn.uilibrary.mvp.BaseActivity;

public class AddEventActivity extends BaseActivity<AddEventPresenter> implements IAddEventView {

    private static final String TAG = "AddEventActivity";

    private static final String KEY_EVENT_DATE = "KEY_EVENT_DATE";
    private static final String KEY_EVENT_TYPE = "KEY_EVENT_TYPE";
    private static final String KEY_EVENT_USER = "KEY_EVENT_USER";

    private BaseEvent mEvent = new BaseEvent();

    @BindView(R.id.tb_event)
    Toolbar mToolbar;

    private LunarUtils.Solar mSolar = null;
    private LunarUtils.Lunar mLunar = null;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideActionBar();
    }

    @Override
    protected void loadData() {
        loadIntentData();
    }

    private void loadIntentData(){
        Intent intent = getIntent();
        if(intent != null){
            String userName = intent.getStringExtra(KEY_EVENT_USER);
            EventType eventType = intent.getParcelableExtra(KEY_EVENT_TYPE);
            mSolar = intent.getParcelableExtra(KEY_EVENT_DATE);
            if(mSolar == null){
                mSolar = CalendarUtils.today();
            }
            mLunar = LunarUtils.solarToLunar(mSolar);
            if(eventType != null){
                mEvent.setEventType(eventType);
            }
            if(userName != null){
                mEvent.setUser(userName);
            }
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_event;
    }

    @Override
    public void saveSuccess() {

    }

    @Override
    public void saveFail() {

    }

    @Override
    protected AddEventPresenter createPresenter() {
        return new AddEventPresenter();
    }

    @Override
    public void refresh() {
        Logger.d(TAG,"do not support refresh action!");
    }

    public static void start(Context context, @Nullable LunarUtils.Solar date, @Nullable EventType type, @Nullable String userName){
        Intent intent = new Intent(context,AddEventActivity.class);
        if(date != null){
            intent.putExtra(KEY_EVENT_DATE,date);
        }
        if(type != null){
            intent.putExtra(KEY_EVENT_TYPE,type);
        }
        if(userName != null){
            intent.putExtra(KEY_EVENT_USER,userName);
        }
        context.startActivity(intent);
    }
}
