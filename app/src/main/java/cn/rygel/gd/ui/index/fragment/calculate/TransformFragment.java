package cn.rygel.gd.ui.index.fragment.calculate;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

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

public class TransformFragment extends BaseFragment {

    @BindView(R.id.btn_solar)
    Button mBtnSolar;
    @BindView(R.id.btn_lunar)
    Button mBtnLunar;

    private MaterialDialog mDialog;
    private DateSelector mDateSelector;

    private Solar mSelected = SolarUtils.today();

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        initDateSelector();
    }

    @OnClick({R.id.btn_solar, R.id.btn_lunar})
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

    private void initDateSelector() {
        if (getContext() == null) {
            Logger.e("context should not be null");
            return;
        }
        mDateSelector = new DateSelector(getContext());
        mDateSelector.setThemeColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));

        mDateSelector.select(mSelected, false);
        mDateSelector.setOndateSelectListener(new DateSelector.OnDateSelectListener() {
            @Override
            public void onSelect(Solar solar, boolean isLunarMode) {
                onDateSelected(solar);
            }
        });

        mDialog = new MaterialDialog.Builder(getContext())
                .customView(mDateSelector, false)
                .build();
    }

    private void onDateSelected(Solar solar) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mSelected = solar;
        mBtnSolar.setText(CalendarUtils.format(mSelected));
        mBtnLunar.setText(CalendarUtils.format(mSelected.toLunar()));
    }

    @Override
    protected void loadData() {
        onDateSelected(mSelected);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_transform;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDialog = null;
    }
}
