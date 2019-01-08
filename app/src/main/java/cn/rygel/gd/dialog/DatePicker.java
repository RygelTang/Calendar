package cn.rygel.gd.dialog;

import android.content.Context;
import android.content.Entity;
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

    private OnWheelChangedListener mYearChangeListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            onYearIndexChange(newIndex);
        }
    };
    private OnWheelChangedListener mMonthChangeListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            onMonthIndexChange(newIndex);
        }
    };
    private OnWheelChangedListener mDayChangeListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView view, int oldIndex, int newIndex) {
            onDayIndexChange(newIndex);
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
        mWheelYear.setOnWheelChangedListener(mYearChangeListener);
        mWheelMonth.setOnWheelChangedListener(mMonthChangeListener);
        mWheelDay.setOnWheelChangedListener(mDayChangeListener);
        mWheelYear.setEntries(YEAR);
        mWheelMonth.setEntries(SOLAR_MONTH);
        mWheelDay.setEntries(getSolarDayEntries(mSelectSolar.solarYear,mSelectSolar.solarMonth));
        switchToSolarMode();
        setSelectSolar(mSelectSolar);
    }

    @OnCheckedChanged(R.id.switch_open_lunar_mode)
    protected void onModeChange(boolean mode){
        mIsLunarMode = mode;
        if(mIsLunarMode) {
            mSelectLunar = LunarUtils.solarToLunar(mSelectSolar);
            switchToLunarMode();
        } else {
            mSelectSolar = LunarUtils.lunarToSolar(mSelectLunar);
            switchToSolarMode();
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

    private void onYearIndexChange(int index) {
        int year = index + BASE_YEAR;
        int monthIndex = mWheelMonth.getCurrentIndex();
        if(mIsLunarMode) {
            mSelectLunar.lunarYear = year;
            mWheelMonth.setEntries(getLunarMonthEntries(year));
            mWheelMonth.setCurrentIndex(monthIndex);
        } else {
            mSelectSolar.solarYear = year;
        }
    }

    private void onMonthIndexChange(int index) {
        int month = index + 1;
        int dayIndex = mWheelDay.getCurrentIndex();
        if(mIsLunarMode) {
            int leap = LunarUtils.leapMonth(mSelectLunar.lunarYear);
            int leapOffset = month > leap ? 1 : 0;
            mSelectLunar.isLeap = leap + 1 == month;
            mSelectLunar.lunarMonth = month - leapOffset;
            mWheelDay.setEntries(getLunarDayEntries(mSelectLunar.lunarYear,mSelectLunar.lunarMonth,leap == month - 1));
            mWheelDay.setCurrentIndex(dayIndex);
        } else {
            mSelectSolar.solarMonth = month;
            mWheelDay.setEntries(getSolarDayEntries(mSelectSolar.solarYear,mSelectSolar.solarMonth));
            mWheelDay.setCurrentIndex(dayIndex);
        }

    }

    private void onDayIndexChange(int index) {
        int day = index + 1;
        if(mIsLunarMode) {
            mSelectLunar.lunarDay = day;
        } else {
            mSelectSolar.solarDay = day;
        }
    }

    private void switchToLunarMode(){
        final LunarUtils.Lunar lunar = CalendarUtils.clone(mSelectLunar);
        final int leap = LunarUtils.leapMonth(lunar.lunarYear);
        final int leapOffset = leap > 0 && (leap > lunar.lunarMonth || lunar.isLeap) ? 1 : 0;
        mWheelYear.setCurrentIndex(lunar.lunarYear - BASE_YEAR);
        mWheelMonth.setEntries(getLunarMonthEntries(lunar.lunarYear));
        mWheelMonth.setCurrentIndex(lunar.lunarMonth - 1 + leapOffset);
        mWheelDay.setEntries(getLunarDayEntries(lunar.lunarYear,lunar.lunarMonth,lunar.isLeap));
        mWheelDay.setCurrentIndex(lunar.lunarDay - 1);
        mSelectLunar = lunar;
    }

    private void switchToSolarMode(){
        LunarUtils.Solar solar = CalendarUtils.clone(mSelectSolar);
        mWheelYear.setCurrentIndex(solar.solarYear - BASE_YEAR);
        mWheelMonth.setEntries(SOLAR_MONTH);
        mWheelMonth.setCurrentIndex(solar.solarMonth - 1);
        mWheelDay.setEntries(getSolarDayEntries(solar.solarYear,solar.solarMonth));
        mWheelDay.setCurrentIndex(solar.solarDay - 1);
        mSelectSolar = solar;
    }

    public void setSelectLunar(LunarUtils.Lunar lunar){
        mSelectLunar = CalendarUtils.clone(lunar);
        mSelectSolar = LunarUtils.lunarToSolar(lunar);
        switchToLunarMode();
    }

    public void setSelectSolar(LunarUtils.Solar solar){
        mSelectSolar = CalendarUtils.clone(solar);
        mSelectLunar = LunarUtils.solarToLunar(solar);
        switchToSolarMode();
    }

    private static List<String> getLunarMonthEntries(int year){
        List<String> months = new ArrayList<>();
        Collections.addAll(months,LUNAR_MONTHS);
        int leapMonth = LunarUtils.leapMonth(year);
        if(leapMonth > 0){
            months.add(leapMonth,"闰" + LUNAR_MONTHS[leapMonth - 1]);
        }
        return months;
    }

    private static List<String> getLunarDayEntries(int year,int month,boolean isLeap) {
        List<String> days = new ArrayList<>();
        int daysInMonth =  LunarUtils.daysInMonth(year,month,isLeap) - 1;
        for(int i = 0;i < daysInMonth;i++){
            days.add(LUNAR_DAYS[i]);
        }
        return days;
    }

    private static List<String> getSolarDayEntries(int year,int month){
        return SOLAR_DAY.subList(0,CalendarUtils.getMonthDay(year, month));
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
