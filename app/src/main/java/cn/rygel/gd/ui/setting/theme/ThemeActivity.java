package cn.rygel.gd.ui.setting.theme;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.setting.Settings;
import rygel.cn.uilibrary.base.BaseActivity;
import rygel.cn.uilibrary.widget.SmoothCheckBox;
import skin.support.content.res.SkinCompatUserThemeManager;

public class ThemeActivity extends BaseActivity {

    @BindView(R.id.tb_theme)
    Toolbar mTbTheme;

    @BindView(R.id.rv_theme)
    RecyclerView mRvTheme;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;
    private List<String> mTheme = new ArrayList<>();
    private List<String> mThemeColors = new ArrayList<>();

    private int mLastPosition = Settings.getInstance().getThemeIndex();

    private int mInitPosition = Settings.getInstance().getThemeIndex();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setStatusBarColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimaryDark).getColorDefault()));
        setSupportActionBar(mTbTheme);
        mTbTheme.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAndSave(mInitPosition);
                finish();
            }
        });
        mTbTheme.inflateMenu(R.menu.menu_theme);
        mTbTheme.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        applyAndSave(mLastPosition);
                        finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        initTheme();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_theme, menu);
        return true;
    }

    private void applyAndSave(int position) {
        Settings.getInstance().putCustomThemeColor(Color.parseColor(mThemeColors.get(position)));
        Settings.getInstance().putCustomThemeColorDark(Color.parseColor(StringUtils.getStringArray(R.array.theme_dark_colors)[position]));
        Settings.getInstance().putCustomThemeColorLight(Color.parseColor(StringUtils.getStringArray(R.array.theme_light_colors)[position]));
        Settings.getInstance().putCustomAccentColor(Color.parseColor(StringUtils.getStringArray(R.array.accent_colors)[position]));

        Settings.getInstance().putThemeIndex(position);

        SkinCompatUserThemeManager.get().clearColors();
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimary, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColor()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimaryDark, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColorDark()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimaryLight, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColorLight()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorAccent, ColorUtils.int2ArgbString(Settings.getInstance().getCustomAccentColor()));
        SkinCompatUserThemeManager.get().apply();
        setStatusBarColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimaryDark).getColorDefault()));
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
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_theme;
    }

}
