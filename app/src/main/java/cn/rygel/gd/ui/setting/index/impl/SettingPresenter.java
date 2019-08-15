package cn.rygel.gd.ui.setting.index.impl;

import com.blankj.utilcode.util.AppUtils;

import cn.rygel.gd.setting.Settings;
import cn.rygel.gd.ui.setting.index.ISettingView;
import cn.rygel.gd.utils.BackupUtils;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class SettingPresenter extends BasePresenter<ISettingView> {

    public boolean putKeepAlive(boolean state) {
        return Settings.getInstance().putKeepAlive(state);
    }

    public boolean putWeekDayOffset(int offset) {
        return Settings.getInstance().putWeekDayOffset(offset);
    }

    public boolean putHideStatus(boolean isHideStatus) {
        return Settings.getInstance().putHideStatus(isHideStatus);
    }

    public boolean isKeepAlive() {
        return Settings.getInstance().isKeepAlive();
    }

    public int getWeekdayOffset() {
        return Settings.getInstance().getWeekdayOffset();
    }

    public boolean isHideStatus() {
        return Settings.getInstance().isHideStatus();
    }

    public void backup() {
        BackupUtils.backup(new BackupUtils.BackupCallback() {
            @Override
            public void callback(boolean success) {
                if (getView() == null) return;
                if (success) {
                    getView().onBackupSuccess();
                } else {
                    getView().onBackupFail();
                }
            }
        });
    }

    public void restore() {
        BackupUtils.backup(new BackupUtils.BackupCallback() {
            @Override
            public void callback(boolean success) {
                if (success) {
                    BackupUtils.restore(new BackupUtils.RestoreCallback() {
                        @Override
                        public void success() {
                            if (getView() == null) return;
                            getView().onRestoreSuccess();
                            AppUtils.relaunchApp(true);
                        }

                        @Override
                        public void fail(String err) {
                            if (getView() == null) return;
                            getView().onRestoreFail(err);
                        }
                    });
                } else {
                    if (getView() == null) return;
                    getView().onBackupFail();
                }
            }
        });
    }

}
