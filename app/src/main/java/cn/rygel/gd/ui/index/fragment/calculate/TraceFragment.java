package cn.rygel.gd.ui.index.fragment.calculate;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.rygel.gd.R;
import cn.rygel.gd.utils.CalendarUtils;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.dateselector.DateSelector;
import rygel.cn.uilibrary.base.BaseFragment;
import skin.support.content.res.SkinCompatUserThemeManager;

public class TraceFragment extends BaseFragment {

    @BindView(R.id.btn_start_time)
    Button mBtnStartTime;
    @BindView(R.id.sp_direction)
    MaterialSpinner mSpDirection;
    @BindView(R.id.tv_solar)
    TextView mTvSolar;
    @BindView(R.id.tv_lunar)
    TextView mTvLunar;

    private MaterialDialog mDialog;
    private DateSelector mDateSelector;

    private int mInterval = 0;

    private Solar mStart = SolarUtils.today();
    private boolean mIsStartLunar = false;

    private boolean mDirection = false;

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        initSpinner();
        initDateSelector();
    }

    @OnClick({R.id.btn_start_time})
    protected void showDateSelector() {
        if (mDialog == null || mDateSelector == null) {
            initDateSelector();
            if (mDialog == null || mDateSelector == null) {
                Logger.e("date selector init fail, please check the reason!");
                return;
            }
        }
        mDialog.show();
    }

    @OnTextChanged(R.id.et_interval)
    protected void onIntervalChanged(CharSequence sequence) {
        try {
            mInterval = Integer.getInteger(sequence.toString(), Integer.valueOf(0));
        } catch (NullPointerException e) {
            Logger.e(e.getMessage());
        }
        onResultUpdate();
    }

    private void initSpinner() {
        mSpDirection.setItems(StringUtils.getStringArray(R.array.directions));
        mSpDirection.setSelectedIndex(mDirection?0:1);
        mSpDirection.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mDirection = position == 1;
                onResultUpdate();
            }
        });
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
        mStart = solar;
        mIsStartLunar = isLunarMode;

        final String startStr;
        if (mIsStartLunar) {
            startStr = CalendarUtils.format(mStart);
        } else {
            startStr = CalendarUtils.format(mStart.toLunar());
        }
        mBtnStartTime.setText(startStr);
        onResultUpdate();
    }

    private void onResultUpdate() {
        final Solar end = SolarUtils.getDayByInterval(mStart, mInterval);
        mTvSolar.setText(CalendarUtils.format(end));
        String lunarStr = null;
        try {
            lunarStr = CalendarUtils.format(end.toLunar());
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        if (lunarStr == null) {
            lunarStr = StringUtils.getString(R.string.lunar_out_of_bound);
        }
        mTvLunar.setText(lunarStr);
    }

    @Override
    protected void loadData() {
        onDateSelected(mStart, mIsStartLunar);
        onResultUpdate();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_trace;
    }

}
