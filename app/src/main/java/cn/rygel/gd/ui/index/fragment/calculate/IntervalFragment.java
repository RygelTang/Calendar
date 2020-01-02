package cn.rygel.gd.ui.index.fragment.calculate;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.utils.CalendarUtils;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.dateselector.DateSelector;
import rygel.cn.uilibrary.base.BaseFragment;
import skin.support.content.res.SkinCompatUserThemeManager;

public class IntervalFragment extends BaseFragment {

    @BindView(R.id.btn_start_time)
    Button mBtnStart;
    @BindView(R.id.btn_end_time)
    Button mBtnEnd;
    @BindView(R.id.tv_result_string)
    TextView mTvResult;

    private MaterialDialog mDialog;
    private DateSelector mDateSelector;

    private Solar mStart = SolarUtils.today();
    private boolean mIsStartLunar = false;
    private Solar mEnd = SolarUtils.today();
    private boolean mIsEndLunar = false;

    private boolean mIsSelectingStart = false;

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        initDateSelector();
        onDateUpdated();
    }

    @OnClick({R.id.btn_start_time, R.id.btn_end_time})
    protected void showDateSelector(View view) {
        if (mDialog == null || mDateSelector == null) {
            initDateSelector();
            if (mDialog == null || mDateSelector == null) {
                Logger.e("date selector init fail, please check the reason!");
                return;
            }
        }
        mIsSelectingStart = view.getId() == R.id.btn_start_time;
        mDialog.show();
    }

    private void initDateSelector() {
        if (getContext() == null) {
            Logger.e("context should not be null");
            return;
        }
        mDateSelector = new DateSelector(getContext());
        mDateSelector.setThemeColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));

        mDateSelector.select(mStart, false);
        mDateSelector.setOndateSelectListener(new DateSelector.OnDateSelectListener() {
            @Override
            public void onSelect(Solar solar, boolean isLunarMode) {
                onDateSelected(solar, isLunarMode);
            }
        });

        mDialog = new MaterialDialog.Builder(getContext())
                .customView(mDateSelector, false)
                .build();
    }

    private void onDateSelected(Solar solar, boolean isLunarMode) {
        if (mIsSelectingStart) {
            mStart = new Solar(solar.solarYear, solar.solarMonth, solar.solarDay);
            mIsStartLunar = isLunarMode;
        } else {
            mEnd = new Solar(solar.solarYear, solar.solarMonth, solar.solarDay);
            mIsEndLunar = isLunarMode;
        }

        onDateUpdated();
    }

    private void onDateUpdated() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        final String startStr;
        if (!mIsStartLunar) {
            startStr = CalendarUtils.format(mStart);
        } else {
            startStr = CalendarUtils.format(mStart.toLunar());
        }
        mBtnStart.setText(startStr);

        final String endStr;
        if (!mIsEndLunar) {
            endStr = CalendarUtils.format(mEnd);
        } else {
            endStr = CalendarUtils.format(mEnd.toLunar());
        }
        mBtnEnd.setText(endStr);

        final int interval = SolarUtils.getIntervalDays(mStart, mEnd);
        final String resultStr;
        if (interval == 0) {
            resultStr = startStr + "和" + endStr + "为同一天";
        } else {
            resultStr = startStr + "在" + endStr + (interval > 0 ? "前" : "后") + Math.abs(interval) + "天";
        }
        mTvResult.setText(resultStr);

    }

    @Override
    protected void loadData() {
        onDateSelected(mEnd, false);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_interval;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDialog = null;
    }

}
