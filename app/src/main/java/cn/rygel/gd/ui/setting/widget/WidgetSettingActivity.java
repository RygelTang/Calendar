package cn.rygel.gd.ui.setting.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.WidgetType;
import rygel.cn.uilibrary.base.BaseActivity;

public class WidgetSettingActivity extends BaseActivity {

    private static final String WIDGET_TYPE = "WIDGET_TYPE";

    @BindView(R.id.sb_corner_radius)
    SeekBar mSbCornerRadius;

    @BindView(R.id.sb_alpha)
    SeekBar mSbBgAlpha;

    @BindView(R.id.layout_option_blur)
    View mLayoutOptionBlur;

    @OnCheckedChanged(R.id.switch_color_background)
    protected void onOptionColorChanged(boolean colorOnly) {
        if (colorOnly) {
            mLayoutOptionBlur.setVisibility(View.GONE);
        } else {
            mLayoutOptionBlur.setVisibility(View.VISIBLE);
            selectPicture();
        }
    }

    @OnCheckedChanged(R.id.switch_blur)
    protected void onOptionBlurChanged(boolean shouldBlur) {
        
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void loadData() {

    }

    /**
     * 选择背景图片
     */
    protected void selectPicture() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_widget_setting;
    }

    public static void start(Context context, WidgetType type) {
        Intent intent = new Intent(context, WidgetSettingActivity.class);
        intent.putExtra(WIDGET_TYPE, type);
        context.startActivity(intent);
    }
}
