package cn.rygel.gd.ui.setting.index.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import cn.rygel.gd.setting.Settings;
import cn.rygel.gd.ui.setting.index.ISettingView;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class SettingPresenter extends BasePresenter<ISettingView> {

    private static final String GROUP_KEY = "JMCMAYQbWXA_cVmnpIHScYKbxhMCm-Yi";

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

    /****************
     *
     * 发起添加群流程。群号：日历用户群(228434421)
     * 调用 joinQQGroup(JMCMAYQbWXA_cVmnpIHScYKbxhMCm-Yi) 即可发起手Q客户端申请加群 日历用户群(228434421)
     *
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + GROUP_KEY));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


}
