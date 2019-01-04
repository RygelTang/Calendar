package cn.rygel.gd.ui.event.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.ui.event.IAddEventView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import rygel.cn.uilibrary.mvp.BaseActivity;

public class AddEventActivity extends BaseActivity<AddEventPresenter> implements IAddEventView {

    private static final String KEY_EVENT_DATE = "KEY_EVENT_DATE";
    private static final String KEY_EVENT_TYPE = "KEY_EVENT_TYPE";
    private static final String KEY_EVENT_USER = "KEY_EVENT_USER";

    private BaseEvent mEvent = new BaseEvent();

    @BindView(R.id.tb_event)
    Toolbar mToolbar;

    @BindView(R.id.btn_end_time)
    Button mBtnEndTime;

    @BindView(R.id.btn_event_date)
    Button mBtnEventDate;

    @BindView(R.id.btn_start_time)
    Button mBtnStartTime;

    @BindView(R.id.et_event_name)
    EditText mETEventName;

    @BindView(R.id.et_event_description)
    EditText mETEventDescription;

    @BindView(R.id.switch_all_day)
    Switch mSwitchAllDay;

    @BindView(R.id.btn_user)
    Button mBtnUser;

    private LunarUtils.Solar mSolar = null;
    private LunarUtils.Lunar mLunar = null;

    private boolean mIsLeap = false;

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
                selectEventType(eventType);
            } else {
                selectEventType(EventType.EVENT_TYPE_SUPPORT.get(EventType.TYPE_DEFAULT));
            }
            if(userName != null){
                mEvent.setUser(userName);
            }
        }
    }

    @OnEditorAction(R.id.et_event_name)
    protected boolean setEventName(Editable eventName){
        if(eventName == null){
            return false;
        }
        mEvent.setName(eventName.toString());
        return false;
    }

    @OnEditorAction(R.id.et_event_description)
    protected boolean setEventDescription(Editable description){
        if(description == null){
            return false;
        }
        mEvent.setDescription(description.toString());
        return false;
    }

    @OnClick(R.id.btn_user)
    protected void selectUser(){
        // TODO: 2019/1/3 显示选择用户Dialog
    }

    @OnClick(R.id.btn_event_date)
    protected void selectEventDate(){
        // TODO: 2019/1/3 显示选择日期Dialog
    }

    @OnClick(R.id.btn_start_time)
    protected void selectStartTime(){
        // TODO: 2019/1/3 显示选择时间Dialog
    }

    @OnClick(R.id.btn_end_time)
    protected void selectEndTime(){
        // TODO: 2019/1/3 显示选择时间Dialog
    }

    @OnCheckedChanged(R.id.switch_all_day)
    protected void selectAllDay(boolean isChecked){
        if(isChecked){
            // TODO: 2019/1/3 修改事件时间
            hideTimeSelect();
        } else {
            showTimeSelect();
        }
    }

    private void hideTimeSelect(){
        mBtnEndTime.setVisibility(View.GONE);
        mBtnStartTime.setVisibility(View.GONE);
    }

    private void showTimeSelect(){
        mBtnEndTime.setVisibility(View.VISIBLE);
        mBtnStartTime.setVisibility(View.VISIBLE);
    }

    /**
     * 对不同的EventType展示不同的设置项
     * @param eventType
     */
    private void selectEventType(EventType eventType){
        mEvent.setEventType(eventType);
    }

    private void selectDate(LunarUtils.Solar solar, boolean isLeap){
        mSolar = new LunarUtils.Solar(solar.solarYear,solar.solarMonth,solar.solarDay);
        mLunar = LunarUtils.solarToLunar(solar);
        mIsLeap = isLeap;
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
        Logger.d("do not support refresh action!");
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
