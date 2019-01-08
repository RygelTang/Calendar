package cn.rygel.gd.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.Wheel3DView;
import com.cncoderx.wheelview.WheelView;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.network.BaseResponse;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import rygel.cn.uilibrary.utils.UIUtils;

public class DatePicker {

    private static final int BASE_YEAR = 1901;

    private static final List<String> YEAR = new ArrayList<>();
    private static final List<String> SOLAR_MONTH = new ArrayList<>();
    private static final List<String> SOLAR_DAY = new ArrayList<>();

    private static final String[] LUNAR_MONTHS = {
            "一月", "二月", "三月", "四月", "五月", "六月",
            "七月", "八月", "九月", "十月", "十一月", "腊月"
    };

    private static final String[] LUNAR_DAYS = {
            "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "廿十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "卅十"
    };

    static {
        initYears();
        initSolarMonths();
        initSolarDays();
    }

    private static void initYears(){
        YEAR.clear();
        for(int i = 1901;i <= 2099;i++) {
            YEAR.add(i + "年");
        }
    }

    private static void initSolarMonths(){
        SOLAR_MONTH.clear();
        for(int i = 1;i <= 12;i++) {
            SOLAR_MONTH.add(i + "月");
        }
    }

    private static void initSolarDays(){
        SOLAR_DAY.clear();
        for(int i = 1;i <= 31;i++) {
            SOLAR_DAY.add(i + "日");
        }
    }

    private View mContent;
    private Context mContext;
    private MaterialDialog mDialog;

    @BindView(R.id.lv_year)
    Wheel3DView mWheelYear;
    @BindView(R.id.lv_month)
    Wheel3DView mWheelMonth;
    @BindView(R.id.lv_day)
    Wheel3DView mWheelDay;

    @BindView(R.id.switch_open_lunar_mode)
    SwitchButton mSwitchLunarMode;

    @BindView(R.id.btn_ok)
    Button mBtnOK;

    boolean mIsLunarMode;

    private LunarUtils.Solar mSelectSolar = CalendarUtils.today();
    private LunarUtils.Lunar mSelectLunar = LunarUtils.solarToLunar(CalendarUtils.today());

    private OnDateSelectListener mOnDateSelectListener = null;

    private OnWheelChangedListener mSolarYearListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            mSelectSolar.solarYear = BASE_YEAR + newIndex;
            onSolarMonthChanged();
        }
    };
    private OnWheelChangedListener mSolarMonthListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            mSelectSolar.solarMonth = newIndex + 1;
            onSolarMonthChanged();
        }
    };
    private OnWheelChangedListener mSolarDayListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            mSelectSolar.solarDay = newIndex + 1;
        }
    };

    private OnWheelChangedListener mLunarYearListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            mSelectLunar.lunarYear = newIndex + BASE_YEAR;
            onLunarYearChanged();
        }
    };
    private OnWheelChangedListener mLunarMonthListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            int leap = LunarUtils.leapMonth(mSelectLunar.lunarYear);
            int leapMonthOffset = leap >= newIndex ? 1 : 0;
            mSelectLunar.isLeap = leap > 0 && leap == newIndex;
            mSelectLunar.lunarMonth = newIndex + 1 - leapMonthOffset;
            onLunarMonthChanged();
        }
    };
    private OnWheelChangedListener mLunarDayListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            mSelectLunar.lunarDay = newIndex + 1;
        }
    };

    public DatePicker(Context context) {
        mContext = context;
        init();
    }

    private void init(){
        initContent();
        initDialog();
    }

    private void initContent(){
        mContent = LayoutInflater.from(mContext).inflate(R.layout.layout_date_picker,null);
        ButterKnife.bind(this,mContent);
        mWheelYear.setEntries(YEAR);
        mWheelMonth.setEntries(SOLAR_MONTH);
        mWheelDay.setEntries(SOLAR_DAY);
        setSelectSolar(mSelectSolar);
    }

    @OnCheckedChanged(R.id.switch_open_lunar_mode)
    protected void onModeChange(boolean mode){
        mIsLunarMode = mode;
        if(mIsLunarMode) {
            switchToLunarMode();
            setSelectLunar(LunarUtils.solarToLunar(mSelectSolar));
        } else {
            switchToSolarMode();
            setSelectSolar(LunarUtils.lunarToSolar(mSelectLunar));
        }
    }

    @OnClick(R.id.btn_ok)
    protected void onSave(){
        if(mDialog != null) {
            mDialog.dismiss();
        }
        if(mOnDateSelectListener != null) {
            if(mIsLunarMode) {
                mOnDateSelectListener.onSelectLunar(mSelectLunar);
            } else {
                mOnDateSelectListener.onSelectSolar(mSelectSolar);
            }
        }
    }

    private void initDialog(){
        mDialog = new MaterialDialog.Builder(mContext)
                .customView(mContent,false)
                .build();
    }

    private void onLunarYearChanged() {
        List<String> month = new ArrayList<>();
        Collections.addAll(month,LUNAR_MONTHS);
        int leapMonth = LunarUtils.leapMonth(mSelectLunar.lunarYear);
        if(leapMonth > 0){
            month.add(leapMonth,"闰" + LUNAR_MONTHS[leapMonth - 1]);
        }
        int currentMonth = mSelectLunar.lunarMonth;
        mWheelMonth.setEntries(month);
        mWheelMonth.setCurrentIndex(currentMonth - 1);
    }

    private void onLunarMonthChanged() {
        List<String> days = new ArrayList<>();
        int daysInMonth =  LunarUtils.daysInMonth(mSelectLunar.lunarYear,mSelectLunar.lunarMonth,mSelectLunar.isLeap) - 1;
        for(int i = 0;i < daysInMonth;i++){
            days.add(LUNAR_DAYS[i]);
        }
        int currentDay = mSelectLunar.lunarDay;
        mWheelDay.setEntries(days);
        mWheelDay.setCurrentIndex(currentDay - 1);
    }

    private void onSolarMonthChanged() {
        List<String> days = SOLAR_DAY.subList(0,CalendarUtils.getMonthDay(mSelectSolar.solarYear,mSelectSolar.solarMonth));
        int currentDay = mSelectSolar.solarDay;
        mWheelDay.setEntries(days);
        mWheelDay.setCurrentIndex(currentDay - 1);
    }

    private void switchToLunarMode(){
        mWheelYear.setOnWheelChangedListener(mLunarYearListener);
        mWheelMonth.setOnWheelChangedListener(mLunarMonthListener);
        mWheelDay.setOnWheelChangedListener(mLunarDayListener);
        int currentMonth = mSelectLunar.lunarMonth;
        int currentDay = mSelectLunar.lunarDay;
        mWheelMonth.setEntries(LUNAR_MONTHS);
        mWheelMonth.setCurrentIndex(currentMonth - 1);
        mWheelDay.setEntries(LUNAR_DAYS);
        mWheelDay.setCurrentIndex(currentDay - 1);
        mSelectLunar = LunarUtils.solarToLunar(mSelectSolar);
    }

    private void switchToSolarMode(){
        mWheelYear.setOnWheelChangedListener(mSolarYearListener);
        mWheelMonth.setOnWheelChangedListener(mSolarMonthListener);
        mWheelDay.setOnWheelChangedListener(mSolarDayListener);
        int currentMonth = mSelectSolar.solarMonth;
        int currentDay = mSelectSolar.solarDay;
        mWheelMonth.setEntries(SOLAR_MONTH);
        mWheelMonth.setCurrentIndex(currentMonth - 1);
        mWheelDay.setEntries(SOLAR_DAY);
        mWheelDay.setCurrentIndex(currentDay - 1);
        mSelectSolar = LunarUtils.lunarToSolar(mSelectLunar);
    }

    public void setSelectLunar(LunarUtils.Lunar lunar){
        mSelectLunar = CalendarUtils.clone(lunar);
        if(mIsLunarMode) {
            mWheelYear.setCurrentIndex(lunar.lunarYear - BASE_YEAR);
            mWheelMonth.setCurrentIndex(lunar.lunarMonth - 1);
            mWheelDay.setCurrentIndex(lunar.lunarDay - 1);
        } else {
            mSwitchLunarMode.setChecked(true);
        }
    }

    public void setSelectSolar(LunarUtils.Solar solar){
        mSelectSolar = CalendarUtils.clone(solar);
        if(mIsLunarMode) {
            mSwitchLunarMode.setChecked(false);
        } else {
            mWheelYear.setCurrentIndex(solar.solarYear - BASE_YEAR);
            mWheelMonth.setCurrentIndex(solar.solarMonth - 1);
            mWheelDay.setCurrentIndex(solar.solarDay - 1);
        }
    }

    public void setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
        mOnDateSelectListener = onDateSelectListener;
    }

    public void show(){
        if(mDialog != null) {
            mDialog.show();
        }
    }

    public void dismiss(){
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }

    public interface OnDateSelectListener{
        void onSelectSolar(LunarUtils.Solar solar);
        void onSelectLunar(LunarUtils.Lunar lunar);
    }

}
