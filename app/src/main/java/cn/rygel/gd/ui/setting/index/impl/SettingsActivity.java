package cn.rygel.gd.ui.setting.index.impl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;
import com.xdandroid.hellodaemon.IntentWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnWeekDayOffsetSelectEvent;
import cn.rygel.gd.ui.about.AboutActivity;
import cn.rygel.gd.ui.setting.index.ISettingView;
import cn.rygel.gd.ui.setting.theme.ThemeActivity;
import pub.devrel.easypermissions.EasyPermissions;
import rygel.cn.uilibrary.mvp.BaseActivity;
import rygel.cn.uilibrary.utils.UIUtils;
import skin.support.content.res.SkinCompatUserThemeManager;

public class SettingsActivity extends BaseActivity<SettingPresenter> implements ISettingView {

    MaterialDialog mWeekDaySelector = null;

    List<String> mWeekDays = null;

    @BindView(R.id.tb_setting)
    Toolbar mTbSetting;

    @BindView(R.id.switch_keep_alive)
    SwitchButton mSwitchKeepAlive;

    @BindView(R.id.tv_summary_weekday)
    TextView mTvWeekDay;

    @BindView(R.id.switch_hide_status)
    SwitchButton mSwitchHideStatus;

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        initWeekDaySelector();
        mTbSetting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        mSwitchHideStatus.setTintColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));
        mSwitchKeepAlive.setTintColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));
        setStatusBarColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimaryDark).getColorDefault()));
        super.onResume();
    }

    private void initWeekDaySelector() {
        mWeekDays =  Arrays.asList(UIUtils.getStringArray(this,R.array.weekdays));
        BaseQuickAdapter<String,BaseViewHolder> adapter = new BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_weekday, mWeekDays) {
            @Override
            protected void convert(final BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_weekday,item);
                helper.getView(R.id.tv_weekday).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onWeekItemSelect(helper.getAdapterPosition());
                    }
                });
            }

        };
        mWeekDaySelector = new MaterialDialog.Builder(this)
                .adapter(adapter,new LinearLayoutManager(this))
                .build();
    }

    @Override
    protected void loadData() {
        mSwitchKeepAlive.setChecked(getPresenter().isKeepAlive());
        mTvWeekDay.setText(mWeekDays.get(getPresenter().getWeekdayOffset()));
    }

    @OnClick(R.id.btn_theme)
    protected void onSelectTheme() {
        startActivity(new Intent(this, ThemeActivity.class));
    }

    @OnClick(R.id.btn_backup)
    protected void onBackup() {
        if (!checkAndGetPermission()) {
            return;
        }
        onLoading();
        getPresenter().backup();
    }

    @OnCheckedChanged(R.id.switch_keep_alive)
    protected void onKeepAliveChanged(boolean state){
        if (!getPresenter().isKeepAlive()){
            IntentWrapper.whiteListMatters(this, StringUtils.getString(R.string.add_to_white_list));
        }
        if(!getPresenter().putKeepAlive(state)) {
            showToast(R.string.save_fail);
        }
    }

    @OnCheckedChanged(R.id.switch_hide_status)
    protected void onHideStatusChanged(boolean state) {
        if(!getPresenter().putHideStatus(state)) {
            showToast(R.string.save_fail);
        }
    }

    @OnClick(R.id.btn_keep_alive)
    protected void onClickKeepAlive() {
        mSwitchKeepAlive.setChecked(!mSwitchKeepAlive.isChecked());
    }

    @OnClick(R.id.btn_restore)
    protected void onRestore() {
        if (!checkAndGetPermission()) {
            return;
        }
        onLoading();
        getPresenter().restore();
    }

    @OnClick(R.id.btn_about)
    protected void onClickAbout() {
        AboutActivity.start(this);
    }

    @OnClick(R.id.btn_select_first_weekday)
    protected void onClickSelectFirstWeekday() {
        mWeekDaySelector.show();
    }

    @OnClick(R.id.btn_hide_status)
    protected void onClickHideStatus() {
        mSwitchHideStatus.setChecked(!mSwitchHideStatus.isChecked());
    }

    protected void onWeekItemSelect(int index) {
        if(mWeekDaySelector != null && mWeekDaySelector.isShowing()) {
            mWeekDaySelector.dismiss();
        }
        if(!getPresenter().putWeekDayOffset(index)) {
            showToast(R.string.save_fail);
            return;
        }
        EventBus.getDefault().post(new OnWeekDayOffsetSelectEvent(index));
        mTvWeekDay.setText(mWeekDays.get(index));
    }

    @Override
    public void onBackupSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoadFinish();
                showToast(R.string.save_success);
            }
        });
    }

    @Override
    public void onBackupFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoadFinish();
                showToast(R.string.save_fail);
            }
        });
    }

    @Override
    public void onRestoreSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoadFinish();
                showToast(R.string.restore_success);
            }
        });
    }

    @Override
    public void onRestoreFail(String err) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoadFinish();
                showToast(err);
            }
        });
    }

    private boolean checkAndGetPermission() {
        String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, StringUtils.getString(R.string.permission_to_backup_and_restore), 8, perms);
        }
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    public void refresh() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeekDaySelector = null;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context,SettingsActivity.class);
        context.startActivity(intent);
    }

}
