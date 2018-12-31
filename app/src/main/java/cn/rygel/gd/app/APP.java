package cn.rygel.gd.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.ftinc.scoop.Scoop;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;

import cn.rygel.gd.BuildConfig;
import cn.rygel.gd.R;
import cn.rygel.gd.db.boxstore.BoxStoreHolder;
import cn.rygel.gd.db.entity.MyObjectBox;
import cn.rygel.gd.utils.sp.SPUtils;
import io.objectbox.android.AndroidObjectBrowser;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

public class APP extends Application {

    private static APP sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initLogger();
        initBoxStore();
        initLeakCanary();
        initScoop();
        initAutoSize();
        initSharedPreferences();
    }

    /**
     * 初始化Logger
     */
    private void initLogger(){
        FormatStrategy strategy = PrettyFormatStrategy
                .newBuilder()
                .tag("Rygel")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy));
    }

    /**
     * 初始化BoxStore
     */
    private void initBoxStore(){
        BoxStoreHolder.getInstance()
                .init(MyObjectBox.builder()
                        .androidContext(this)
                        .build()
                );
        if(BuildConfig.DEBUG){
            new AndroidObjectBrowser(BoxStoreHolder.getInstance()
                    .getBoxStore())
                    .start(this);
        }
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
    private void initScoop(){
        Scoop.waffleCone()
                .addFlavor("Default", R.style.Theme_Scoop,true)
                .addFlavor("Light",R.style.Theme_Scoop_Light)
                .addDayNightFlavor("DayNight",R.style.Theme_Scoop_DayNight)
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize();
    }

    /**
     * 初始化SP
     */
    private void initSharedPreferences(){
        SPUtils.getInstance().init(this);
    }

    /**
     * 初始化AutoSize
     */
    private void initAutoSize(){
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
    }

    /**
     * 获取APP的实例
     * @return
     */
    public static APP getInstance(){
        return sInstance;
    }

}
