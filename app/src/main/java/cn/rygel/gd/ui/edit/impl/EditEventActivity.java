package cn.rygel.gd.ui.edit.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnEventAddedEvent;
import cn.rygel.gd.bean.event.AppointmentEvent;
import cn.rygel.gd.bean.event.BirthdayEvent;
import cn.rygel.gd.bean.event.MeetingEvent;
import cn.rygel.gd.bean.event.MemorialEvent;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.DefaultEvent;
import cn.rygel.gd.bean.event.base.LocationEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.constants.Global;
import cn.rygel.gd.ui.edit.IEditEventView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import rygel.cn.uilibrary.mvp.BaseActivity;

public class EditEventActivity extends BaseActivity<EditEventPresenter> implements IEditEventView {

    private static final String KEY_EVENT = "KEY_EVENT";

    @BindView(R.id.btn_time)
    Button mBtnTime;

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

    @BindView(R.id.et_event_location)
    EditText mETLocation;

    @BindView(R.id.et_event_description)
    EditText mETEventDescription;

    @BindView(R.id.switch_all_day)
    SwitchButton mSwitchAllDay;

    @BindView(R.id.switch_alert)
    SwitchButton mSwitchAlert;

    @BindView(R.id.btn_user)
    Button mBtnUser;

    @BindView(R.id.layout_option_duration)
    View mLayoutDuration;

    @BindView(R.id.layout_option_location)
    View mLayoutLocation;

    @BindView(R.id.layout_option_time)
    View mLayoutTime;

    private DatePicker mDatePicker = null;
    private TimePicker mStartTimePicker = null;
    private TimePicker mEndTimePicker = null;

    private LunarUtils.Solar mSolar = null;
    private LunarUtils.Lunar mLunar = null;

    private boolean mIsLunar = false;

    private long mStart = 0L;
    private long mDuration = 0L;

    private String mEventName = null;

    private String mDescription = null;

    private String mLocation = null;

    private boolean mIsShowNotification = false;

    private String mUser = Global.DEFAULT_USER;

    private EventType mEventType = EventType.EVENT_TYPE_SUPPORT.get(EventType.TYPE_DEFAULT);

    private int mEventTypeIndex = EventType.TYPE_DEFAULT;

    private BaseEvent mOldEvent = null;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_add_event);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        onEventSave();
                        break;
                    case R.id.action_appointment_event:
                        onEventTypeSelected(EventType.TYPE_APPOINTMENT);
                        break;
                    case R.id.action_birthday_event:
                        onEventTypeSelected(EventType.TYPE_BIRTHDAY);
                        break;
                    case R.id.action_default_event:
                        onEventTypeSelected(EventType.TYPE_DEFAULT);
                        break;
                    case R.id.action_meeting_event:
                        onEventTypeSelected(EventType.TYPE_MEETING);
                        break;
                    case R.id.action_memorial_event:
                        onEventTypeSelected(EventType.TYPE_MEMORIAL);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        initPickers();
        onDefaultTypeSelect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int typeID = R.id.action_default_event;
        switch (mEventTypeIndex) {
            case EventType.TYPE_APPOINTMENT:
                typeID = R.id.action_appointment_event;
                break;
            case EventType.TYPE_BIRTHDAY:
                typeID = R.id.action_birthday_event;
                break;
            case EventType.TYPE_MEETING:
                typeID = R.id.action_meeting_event;
                break;
            case EventType.TYPE_MEMORIAL:
                typeID = R.id.action_memorial_event;
                break;
            default:
                break;
        }
        menu.findItem(typeID).setChecked(true);
        return super.onPrepareOptionsMenu(menu);
    }

    private void initPickers(){
        mDatePicker = new DatePicker(this);
        mStartTimePicker = new TimePicker(this);
        mEndTimePicker = new TimePicker(this);
        mDatePicker.setOnDateSelectListener(new DatePicker.OnDateSelectListener() {
            @Override
            public void onSelectSolar(LunarUtils.Solar solar) {
                onDateSelect(solar,LunarUtils.solarToLunar(solar),false);
            }

            @Override
            public void onSelectLunar(LunarUtils.Lunar lunar) {
                onDateSelect(LunarUtils.lunarToSolar(lunar),lunar,true);
            }
        });
        mStartTimePicker.setTimeSelectListener(new TimePicker.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(int hour, int minute) {
                onStartTimeSelect(hour, minute);
            }
        });
        mEndTimePicker.setTimeSelectListener(new TimePicker.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(int hour, int minute) {
                onEndTimeSelect(hour, minute);
            }
        });
    }

    @Override
    protected void loadData() {
        loadIntentData();
        mBtnUser.setText(mUser);
        onStartTimeSelect(0,0);
        onStartTimeSelect(0,0);
    }

    private void loadIntentData(){
        Intent intent = getIntent();
        if(intent != null){
            mOldEvent = intent.getParcelableExtra(KEY_EVENT);
            onEventTypeSelected(EventType.EVENT_TYPE_SUPPORT.indexOf(mOldEvent.getEventType()));
            mETEventName.setText(mOldEvent.getName());
            // 设置选中的日期
            onDateSelect(mOldEvent.getEventSolarDate(),mOldEvent.getEventLunarDate(),mOldEvent.isLunarEvent());
            if(mOldEvent.isLunarEvent()) {
                mDatePicker.setSelectLunar(CalendarUtils.clone(mOldEvent.getEventLunarDate()));
            } else {
                mDatePicker.setSelectSolar(CalendarUtils.clone(mOldEvent.getEventSolarDate()));
            }

            onStartTimeSelect((int) mOldEvent.getStart() / 60,(int) mOldEvent.getStart() % 60);
            if(mOldEvent instanceof DefaultEvent) {
                long end = mOldEvent.getStart() + ((DefaultEvent) mOldEvent).getDuration();
                onEndTimeSelect((int) end / 60,(int) end % 60);
            }

            onUserSelect(mOldEvent.getUser());

            if(mOldEvent instanceof LocationEvent) {
                mLocation = ((LocationEvent) mOldEvent).getLocation();
                mETLocation.setText(mLocation);
            }

            mDescription = mOldEvent.getDescription();
            mETEventDescription.setText(mDescription);

            mIsShowNotification = mOldEvent.isShowNotification();
            mSwitchAlert.setChecked(mIsShowNotification);
        }
    }

    @OnTextChanged(value = R.id.et_event_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void setEventName(Editable eventName){
        mEventName = eventName.toString();
    }

    @OnTextChanged(value = R.id.et_event_description, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void setEventDescription(Editable description){
        mDescription = description.toString();
    }

    @OnTextChanged(value = R.id.et_event_location, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void setEventLocation(Editable location) {
        mLocation = location.toString();
    }

    @OnClick(R.id.btn_user)
    protected void selectUser(){
        getPresenter().getUser();
    }

    @OnClick(R.id.btn_event_date)
    protected void selectEventDate(){
        mDatePicker.show();
    }

    @OnClick(R.id.btn_start_time)
    protected void selectStartTime(){
        mStartTimePicker.show();
    }

    @OnClick(R.id.btn_end_time)
    protected void selectEndTime(){
        mEndTimePicker.show();
    }

    @OnClick({R.id.btn_time})
    protected void selectEventTime() {
        mStartTimePicker.show();
    }

    @OnCheckedChanged(R.id.switch_all_day)
    protected void selectAllDay(boolean isChecked){
        if(isChecked){
            onStartTimeSelect(0,0);
            onEndTimeSelect(0,0);
            mStartTimePicker.setSelectTime(0,0);
            mEndTimePicker.setSelectTime(0,0);
            hideTimeSelect();
        } else {
            showTimeSelect();
        }
    }

    @OnCheckedChanged(R.id.switch_alert)
    protected void selectShowNotification(boolean isChecked) {
        mIsShowNotification = isChecked;
    }

    @Override
    public void showUserList(List<String> users) {
        BaseQuickAdapter<String,BaseViewHolder> adapter = new BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_user,users) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_user,item);
            }
        };
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .adapter(adapter,new LinearLayoutManager(this))
                .build();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(adapter.getItem(position) instanceof String){
                    onUserSelect((String) adapter.getItem(position));
                    if(dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }

    /**
     * 当事件类型改变的时候的回调
     * @param index 选中的事件类型的index
     */
    private void onEventTypeSelected(int index) {
        mEventTypeIndex = index;
        mEventType = EventType.EVENT_TYPE_SUPPORT.get(mEventTypeIndex);
        switch (mEventTypeIndex) {
            case EventType.TYPE_DEFAULT:
                onDefaultTypeSelect();
                break;
            case EventType.TYPE_APPOINTMENT:
                onAppointmentTypeSelect();
                break;
            case EventType.TYPE_BIRTHDAY:
                onBirthdayTypeSelect();
                break;
            case EventType.TYPE_MEETING:
                onMeetingTypeSelect();
                break;
            case EventType.TYPE_MEMORIAL:
                onMemorialTypeSelect();
                break;
            default:
                break;
        }
    }

    /**
     * 选择约会类型的回调
     */
    private void onAppointmentTypeSelect() {
        showLocationLayout();
        showDurationLayout();
        hideTimeLayout();
    }

    /**
     * 选择生日类型的回调
     */
    private void onBirthdayTypeSelect() {
        showTimeLayout();
        hideLocationLayout();
        hideDurationLayout();
    }

    /**
     * 选择会议类型的回调
     */
    private void onMeetingTypeSelect() {
        showLocationLayout();
        showDurationLayout();
        hideTimeLayout();
    }

    /**
     * 选择纪念日类型的回调
     */
    private void onMemorialTypeSelect() {
        showTimeLayout();
        hideLocationLayout();
        hideDurationLayout();
    }

    /**
     * 选择默认类型的回调
     */
    private void onDefaultTypeSelect() {
        showDurationLayout();
        hideLocationLayout();
        hideTimeLayout();
    }

    /**
     * 保存事件
     */
    private void onEventSave() {
        BaseEvent event2Save = null;
        if(mEventName == null) {
            showToast(R.string.event_name_can_not_be_null);
            return;
        }
        switch (mEventTypeIndex) {
            case EventType.TYPE_DEFAULT:
                DefaultEvent defaultEvent = new DefaultEvent();
                defaultEvent.setDuration(mDuration);
                defaultEvent.setName(mEventName);
                defaultEvent.setEventSolarDate(mSolar);
                defaultEvent.setLunarEvent(mIsLunar);
                defaultEvent.setEventLunarDate(mLunar);
                defaultEvent.setDescription(mDescription);
                defaultEvent.setShowNotification(mIsShowNotification);
                defaultEvent.setUser(mUser);
                event2Save = defaultEvent;
                break;
            case EventType.TYPE_APPOINTMENT:
                AppointmentEvent appointmentEvent = new AppointmentEvent();
                appointmentEvent.setDuration(mDuration);
                appointmentEvent.setName(mEventName);
                appointmentEvent.setEventSolarDate(mSolar);
                appointmentEvent.setLunarEvent(mIsLunar);
                appointmentEvent.setEventLunarDate(mLunar);
                appointmentEvent.setDescription(mDescription);
                appointmentEvent.setShowNotification(mIsShowNotification);
                appointmentEvent.setLocation(mLocation);
                appointmentEvent.setUser(mUser);
                event2Save = appointmentEvent;
                break;
            case EventType.TYPE_BIRTHDAY:
                BirthdayEvent birthdayEvent = new BirthdayEvent();
                birthdayEvent.setName(mEventName);
                birthdayEvent.setEventSolarDate(mSolar);
                birthdayEvent.setLunarEvent(mIsLunar);
                birthdayEvent.setEventLunarDate(mLunar);
                birthdayEvent.setDescription(mDescription);
                birthdayEvent.setShowNotification(mIsShowNotification);
                birthdayEvent.setUser(mUser);
                event2Save = birthdayEvent;
                break;
            case EventType.TYPE_MEETING:
                MeetingEvent meetingEvent = new MeetingEvent();
                meetingEvent.setDuration(mDuration);
                meetingEvent.setName(mEventName);
                meetingEvent.setEventSolarDate(mSolar);
                meetingEvent.setLunarEvent(mIsLunar);
                meetingEvent.setEventLunarDate(mLunar);
                meetingEvent.setDescription(mDescription);
                meetingEvent.setShowNotification(mIsShowNotification);
                meetingEvent.setLocation(mLocation);
                meetingEvent.setUser(mUser);
                event2Save = meetingEvent;
                break;
            case EventType.TYPE_MEMORIAL:
                MemorialEvent memorialEvent = new MemorialEvent();
                memorialEvent.setName(mEventName);
                memorialEvent.setEventSolarDate(mSolar);
                memorialEvent.setLunarEvent(mIsLunar);
                memorialEvent.setEventLunarDate(mLunar);
                memorialEvent.setDescription(mDescription);
                memorialEvent.setShowNotification(mIsShowNotification);
                memorialEvent.setUser(mUser);
                event2Save = memorialEvent;
                break;
            default:
                break;
        }
        event2Save.setEventType(mEventType);
        event2Save.setStart(mStart);
        event2Save.setId(mOldEvent.getId());
        getPresenter().update(event2Save);
    }

    /**
     * 当日期选中时的回调，solar和lunar都会返回，通过isLunar判断是否是农历日期
     * @param solar 选中的阳历
     * @param lunar 选中的农历
     * @param isLunar 是否选中农历日期
     */
    private void onDateSelect(LunarUtils.Solar solar, LunarUtils.Lunar lunar,boolean isLunar){
        mSolar = solar;
        mLunar = lunar;
        mIsLunar = isLunar;
        mBtnEventDate.setText(isLunar ? LunarUtils.getLunarString(lunar) : LunarUtils.getSolarString(solar));
    }

    /**
     * 选择时间间隔时的开始时间选择的回调
     * @param hour 小时
     * @param minute 分钟
     */
    private void onStartTimeSelect(int hour,int minute){
        mBtnStartTime.setText(formatTime(hour, minute));
        mBtnTime.setText(formatTime(hour, minute));
        mStart = hour * 60 + minute;
        mEndTimePicker.setSelectTime(hour, minute);
        onEndTimeSelect(hour, minute);
    }

    /**
     * 选择时间间隔时的结束时间选择的回调
     * @param hour 小时
     * @param minute 分钟
     */
    private void onEndTimeSelect(int hour,int minute){
        long duration = hour * 60 + minute - mStart;
        if(duration < 0){
            showSnackBar(mBtnEndTime,R.string.do_not_select_time_before_start);
            return;
        }
        mDuration = duration;
        mBtnEndTime.setText(formatTime(hour, minute));
    }

    /**
     * 当成功选择了用户的情况下的回调
     * @param user
     */
    private void onUserSelect(String user){
        mUser = user;
        mBtnUser.setText(user);
    }

    /**
     * 隐藏时间间隔选择的布局
     */
    private void hideTimeSelect(){
        mBtnEndTime.setVisibility(View.GONE);
        mBtnStartTime.setVisibility(View.GONE);
    }

    /**
     * 显示时间间隔选择的布局
     */
    private void showTimeSelect(){
        mBtnEndTime.setVisibility(View.VISIBLE);
        mBtnStartTime.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏选择时间相关的布局
     */
    private void hideTimeLayout() {
        mLayoutTime.setVisibility(View.GONE);
    }

    /**
     * 隐藏所有时间间隔相关的布局
     */
    private void hideDurationLayout() {
        mLayoutDuration.setVisibility(View.GONE);
    }

    /**
     * 隐藏位置设置相关的布局
     */
    private void hideLocationLayout() {
        mLayoutLocation.setVisibility(View.GONE);
    }

    /**
     * 显示选择时间相关的布局
     */
    private void showTimeLayout() {
        mLayoutTime.setVisibility(View.VISIBLE);
    }

    /**
     * 显示所有时间间隔相关的布局
     */
    private void showDurationLayout() {
        mLayoutDuration.setVisibility(View.VISIBLE);
    }

    /**
     * 显示位置设置相关的布局
     */
    private void showLocationLayout() {
        mLayoutLocation.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_event;
    }

    @Override
    public void saveSuccess() {
        showToast(R.string.save_success);
        EventBus.getDefault().post(new OnEventAddedEvent());
        finish();
    }

    @Override
    public void saveFail() {
        showToast(R.string.save_fail);
    }

    @Override
    protected EditEventPresenter createPresenter() {
        return new EditEventPresenter();
    }

    @Override
    public void refresh() {
        Logger.d("do not support refresh action!");
    }

    public void showSnackBar(View view,String str){
        Snackbar.make(view, str, Snackbar.LENGTH_LONG).show();
    }

    public void showSnackBar(View view,@StringRes int strRes){
        Snackbar.make(view, strRes, Snackbar.LENGTH_LONG).show();
    }

    private static String formatTime(int hour, int minute){
        return (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : String.valueOf(minute));
    }

    public static void start(Context context, @NonNull BaseEvent event){
        Intent intent = new Intent(context,EditEventActivity.class);
        intent.putExtra(KEY_EVENT,event);
        context.startActivity(intent);
    }
}
