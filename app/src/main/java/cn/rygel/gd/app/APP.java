package cn.rygel.gd.app;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.mmkv.MMKV;

import cn.rygel.gd.BuildConfig;
import cn.rygel.gd.R;
import cn.rygel.gd.constants.Global;
import cn.rygel.gd.db.boxstore.BoxStoreHolder;
import cn.rygel.gd.db.entity.MyObjectBox;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.db.model.UserModel;
import cn.rygel.gd.deamon.DaemonHolder;
import cn.rygel.gd.service.PushService;
import cn.rygel.gd.setting.Settings;
import io.objectbox.BoxStoreBuilder;
import io.objectbox.android.AndroidObjectBrowser;
import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatUserThemeManager;
import skin.support.design.app.SkinMaterialViewInflater;

public class APP extends Application {

    private static APP sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        // 初始化MultiDex
        initMultiDex();
        initBugly();
        // 初始化工具类
        initUtils();
        // 初始化Logger
        initLogger();
        // 初始化BoxStore
        initBoxStore();
        // 初始化内存泄漏工具
        initLeakCanary();
        // 初始化MMKV
        initMMKV();
        // 初始化换肤框架
        initTheme();
        initPushService();
    }

    /**
     * 初始化推送服务
     */
    private void initPushService() {
        DaemonHolder.init(this, PushService.class);
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Bugly.init(APP.getInstance(), Global.BUGLY_ID, BuildConfig.DEBUG);
    }

    /**
     * 初始化MultiDex
     */
    private void initMultiDex() {
        MultiDex.install(this);
    }

    /**
     * 初始化工具类
     */
    private void initUtils() {
        Utils.init(this);
    }

    /**
     * 初始化Logger
     */
    private void initLogger(){
        Logger.addLogAdapter(
                new AndroidLogAdapter(
                        PrettyFormatStrategy.newBuilder()
                                .methodCount(5)
                                .tag("Rygel")
                                .build()
                )
        );
    }

    /**
     * 初始化BoxStore
     */
    private void initBoxStore(){
        BoxStoreBuilder builder = MyObjectBox.builder()
                .androidContext(this);
        BoxStoreHolder.getInstance()
                .init(builder.build());
        if(BuildConfig.DEBUG){
            Logger.i("object browser started?" + new AndroidObjectBrowser(
                    BoxStoreHolder
                            .getInstance()
                            .getBoxStore()
            ).start(this));
        }
        if (UserModel.getInstance().getUserByName(StringUtils.getString(R.string.default_user)) == null) {
            UserModel.getInstance().putUser(StringUtils.getString(R.string.default_user));
        }
        EventModel.getInstance().init(StringUtils.getString(R.string.default_user));
    }

    /**
     * 初始化内存泄漏检测工具
     */
    private void initLeakCanary(){
        if(BuildConfig.DEBUG){
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }

    /**
     * 初始化换肤工具
     */
    private void initTheme() {
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())
                .loadSkin();
        SkinCompatUserThemeManager.get().clearColors();
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimary, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColor()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimaryDark, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColorDark()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimaryLight, ColorUtils.int2ArgbString(Settings.getInstance().getCustomThemeColorLight()));
        SkinCompatUserThemeManager.get().addColorState(R.color.colorAccent, ColorUtils.int2ArgbString(Settings.getInstance().getCustomAccentColor()));
        SkinCompatUserThemeManager.get().apply();
    }

    /**
     * 初始化MMVKV
     */
    private void initMMKV(){
        String dir = MMKV.initialize(this);
        Logger.i("MMKV root : " + dir);
    }

    /**
     * 获取APP的实例
     * @return 返回Application的实例
     */
    public static APP getInstance(){
        return sInstance;
    }

}
