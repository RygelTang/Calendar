package cn.rygel.gd.ui.event.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.DefaultEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.dialog.DatePicker;
import cn.rygel.gd.dialog.TimePicker;
import cn.rygel.gd.ui.event.IAddEventView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import rygel.cn.uilibrary.mvp.BaseActivity;
import rygel.cn.uilibrary.utils.UIUtils;

public class AddEventActivity extends BaseActivity<AddEventPresenter> implements IAddEventView {

    private static final String KEY_EVENT_DATE = "KEY_EVENT_DATE";
    private static final String KEY_EVENT_TYPE = "KEY_EVENT_TYPE";
    private static final String KEY_EVENT_USER = "KEY_EVENT_USER";

    private DefaultEvent mEvent = new DefaultEvent();

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
    SwitchButton mSwitchAllDay;

    @BindView(R.id.btn_user)
    Button mBtnUser;

    private DatePicker mDatePicker = null;
    private TimePicker mStartTimePicker = null;
    private TimePicker mEndTimePicker = null;

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
        mToolbar.inflateMenu(R.menu.add_event_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getPresenter().saveEvent(mEvent);
                return true;
            }
        });
        initPickers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_event_menu,menu);
        return true;
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
        mBtnUser.setText(mEvent.getUser());
        onStartTimeSelect(0,0);
        onStartTimeSelect(0,0);
    }

    private void loadIntentData(){
        Intent intent = getIntent();
        if(intent != null){
            String userName = intent.getStringExtra(KEY_EVENT_USER);
            onUserSelect(userName != null ? userName : UIUtils.getString(this,R.string.default_user));
            EventType eventType = intent.getParcelableExtra(KEY_EVENT_TYPE);
            if(eventType != null){
                selectEventType(eventType);
            } else {
                selectEventType(EventType.EVENT_TYPE_SUPPORT.get(EventType.TYPE_DEFAULT));
            }
            LunarUtils.Solar solar = intent.getParcelableExtra(KEY_EVENT_DATE);
            if(solar == null){
                solar = CalendarUtils.today();
            }
            onDateSelect(solar,LunarUtils.solarToLunar(solar),false);
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

    private void onDateSelect(LunarUtils.Solar solar, LunarUtils.Lunar lunar,boolean isLunar){
        mEvent.setEventSolarDate(CalendarUtils.clone(solar));
        mEvent.setEventLunarDate(CalendarUtils.clone(lunar));
        mEvent.setLunarEvent(isLunar);
        mBtnEventDate.setText(isLunar ? LunarUtils.getLunarString(lunar) : LunarUtils.getSolarString(solar));
    }

    private void onStartTimeSelect(int hour,int minute){
        mBtnStartTime.setText(formatTime(hour, minute));
        mEvent.setStart(hour * 60 + minute);
        mEndTimePicker.setSelectTime(hour, minute);
        onEndTimeSelect(hour, minute);
    }

    private void onEndTimeSelect(int hour,int minute){
        long duration = hour * 60 + minute - mEvent.getStart();
        if(duration < 0){
            showSnackBar(mBtnEndTime,R.string.do_not_select_time_before_start);
            return;
        }
        mEvent.setDuration(duration);
        mBtnEndTime.setText(formatTime(hour, minute));
    }

    private void onUserSelect(String user){
        mEvent.setUser(user);
        mBtnUser.setText(user);
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

    public void showSnackBar(View view,String str){
        Snackbar.make(view, str, Snackbar.LENGTH_LONG).show();
    }

    public void showSnackBar(View view,@StringRes int strRes){
        Snackbar.make(view, strRes, Snackbar.LENGTH_LONG).show();
    }

    private static String formatTime(int hour, int minute){
        return (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : String.valueOf(minute));
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
