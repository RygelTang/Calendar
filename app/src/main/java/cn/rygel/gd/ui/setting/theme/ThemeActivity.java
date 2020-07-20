package cn.rygel.gd.ui.setting.theme;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.setting.Settings;
import cn.rygel.gd.utils.CircleDrawable;
import rygel.cn.uilibrary.base.BaseActivity;
import rygel.cn.uilibrary.utils.LeakClearUtils;
import rygel.cn.uilibrary.widget.SmoothCheckBox;
import skin.support.content.res.SkinCompatUserThemeManager;

public class ThemeActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {

    @BindView(R.id.tb_theme)
    Toolbar mTbTheme;

    @BindView(R.id.rv_theme)
    RecyclerView mRvTheme;

    View mCustomThemeView;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;
    private List<String> mTheme = new ArrayList<>();
    private List<String> mThemeColors = new ArrayList<>();

    private int mLastPosition = Settings.getInstance().getThemeIndex();
    private View mSelected;

    private View.OnClickListener mCustomThemeClickListener = new CustomThemeViewClickListener(this);

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setStatusBarColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimaryDark).getColorDefault()));
        setSupportActionBar(mTbTheme);
        mTbTheme.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initTheme();
    }

    private void applyAndSave(int position) {
        if (position != -1) {
            Settings.getInstance().putCustomThemeColor(Color.parseColor(mThemeColors.get(position)));
            Settings.getInstance().putCustomThemeColorDark(Color.parseColor(StringUtils.getStringArray(R.array.theme_dark_colors)[position]));
            Settings.getInstance().putCustomThemeColorLight(Color.parseColor(StringUtils.getStringArray(R.array.theme_light_colors)[position]));
            Settings.getInstance().putCustomAccentColor(Color.parseColor(StringUtils.getStringArray(R.array.accent_colors)[position]));
        }

        mCustomThemeView.findViewById(R.id.view_color_primary).setBackground(new CircleDrawable(Settings.getInstance().getCustomThemeColor()));
        mCustomThemeView.findViewById(R.id.view_color_primary_dark).setBackground(new CircleDrawable(Settings.getInstance().getCustomThemeColorDark()));
        mCustomThemeView.findViewById(R.id.view_color_primary_light).setBackground(new CircleDrawable(Settings.getInstance().getCustomThemeColorLight()));
        mCustomThemeView.findViewById(R.id.view_color_accent).setBackground(new CircleDrawable(Settings.getInstance().getCustomAccentColor()));

        Settings.getInstance().putThemeIndex(position);

        SkinCompatUserThemeManager.get().clearColors();
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimary, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColor()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimaryDark, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColorDark()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimaryLight, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColorLight()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorAccent, ColorUtils.int2ArgbString(Settings.getInstance().getCustomAccentColor()));
        SkinCompatUserThemeManager.get().apply();

        setStatusBarColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimaryDark).getColorDefault()));
        if(mLastPosition == -1) {
            ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setCheckedColor(Settings.getInstance().getCustomThemeColor());
            ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setChecked(false);
            ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setChecked(true);
        }
    }

    private void initTheme() {
        mTheme.addAll(Arrays.asList(StringUtils.getStringArray(R.array.color_values)));
        mThemeColors.addAll(Arrays.asList(StringUtils.getStringArray(R.array.theme_colors)));
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_theme, mTheme) {
            @Override
            protected void convert(final BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_color, item);
                SmoothCheckBox cb = ((SmoothCheckBox) helper.getView(R.id.cb_selected));
                cb.setOnCheckedChangeListener(null);
                cb.setCheckedColor(Color.parseColor(mThemeColors.get(helper.getAdapterPosition())));
                cb.setChecked(mLastPosition == helper.getAdapterPosition());
                cb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        final int position = helper.getAdapterPosition();
                        final int old = mLastPosition;
                        if (old != position) {
                            mLastPosition = position;
                            applyAndSave(mLastPosition);
                            mRvTheme.post(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(old);
                                }
                            });
                        }
                        if (old == -1) {
                            ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setChecked(false);
                        }
                    }
                });
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SmoothCheckBox cb = ((SmoothCheckBox) view.findViewById(R.id.cb_selected));
                cb.setChecked(true);
            }
        });
        mRvTheme.setAdapter(mAdapter);
        mRvTheme.setLayoutManager(new LinearLayoutManager(this));
        mCustomThemeView = getLayoutInflater().inflate(R.layout.item_theme_foot, null);
        initCustomThemeView();
        mAdapter.setFooterView(mCustomThemeView);
    }

    private void initCustomThemeView() {
        ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mAdapter.notifyItemChanged(mLastPosition);
                    mLastPosition = -1;
                }
            }
        });
        mCustomThemeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setChecked(true);
            }
        });
        ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setCheckedColor(Settings.getInstance().getCustomThemeColor());
        if (mLastPosition == -1) {
            ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setChecked(true);
        }

        mCustomThemeView.findViewById(R.id.view_color_primary).setBackground(new CircleDrawable(Settings.getInstance().getCustomThemeColor()));
        mCustomThemeView.findViewById(R.id.view_color_primary_dark).setBackground(new CircleDrawable(Settings.getInstance().getCustomThemeColorDark()));
        mCustomThemeView.findViewById(R.id.view_color_primary_light).setBackground(new CircleDrawable(Settings.getInstance().getCustomThemeColorLight()));
        mCustomThemeView.findViewById(R.id.view_color_accent).setBackground(new CircleDrawable(Settings.getInstance().getCustomAccentColor()));

        mCustomThemeView.findViewById(R.id.view_color_primary).setOnClickListener(mCustomThemeClickListener);
        mCustomThemeView.findViewById(R.id.view_color_primary_dark).setOnClickListener(mCustomThemeClickListener);
        mCustomThemeView.findViewById(R.id.view_color_primary_light).setOnClickListener(mCustomThemeClickListener);
        mCustomThemeView.findViewById(R.id.view_color_accent).setOnClickListener(mCustomThemeClickListener);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {
        LeakClearUtils.fixColorPickerLeak(dialog);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        if (mLastPosition != -1) {
            mAdapter.notifyItemChanged(mLastPosition);
            mLastPosition = -1;
        }
        ((SmoothCheckBox) mCustomThemeView.findViewById(R.id.cb_selected)).setChecked(true);
        Settings.getInstance().putThemeIndex(-1);
        if (mSelected != null) {
            switch (mSelected.getId()) {
                case R.id.view_color_primary:
                    Settings.getInstance().putCustomThemeColor(selectedColor);
                    break;
                case R.id.view_color_primary_dark:
                    Settings.getInstance().putCustomThemeColorDark(selectedColor);
                    break;
                case R.id.view_color_primary_light:
                    Settings.getInstance().putCustomThemeColorLight(selectedColor);
                    break;
                case R.id.view_color_accent:
                    Settings.getInstance().putCustomAccentColor(selectedColor);
                    break;
                default:
                    break;
            }
            applyAndSave(-1);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_theme;
    }

    private static class CustomThemeViewClickListener implements View.OnClickListener {

        private WeakReference<ThemeActivity> mThemeActivityWeakReference;

        public CustomThemeViewClickListener(ThemeActivity activity) {
            mThemeActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onClick(View v) {
            if (mThemeActivityWeakReference.get() == null) {
                return;
            }
            mThemeActivityWeakReference.get().mSelected = v;
            int color = Color.BLACK;
            switch (v.getId()) {
                case R.id.view_color_primary:
                    color = Settings.getInstance().getCustomThemeColor();
                    break;
                case R.id.view_color_primary_dark:
                    color = Settings.getInstance().getCustomThemeColorDark();
                    break;
                case R.id.view_color_primary_light:
                    color = Settings.getInstance().getCustomThemeColorLight();
                    break;
                case R.id.view_color_accent:
                    color = Settings.getInstance().getCustomAccentColor();
                    break;
                default:
                    break;
            }
            new ColorChooserDialog.Builder(mThemeActivityWeakReference.get(), R.string.choose_color)
                    .titleSub(R.string.choose_color)
                    .doneButton(R.string.select)  // changes label of the done button
                    .cancelButton(R.string.cancel)  // changes label of the cancel button
                    .preselect(color)  // 开始的时候的默认颜色
                    .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                    .build()
                    .show(mThemeActivityWeakReference.get().getSupportFragmentManager());
        }
    }

}
