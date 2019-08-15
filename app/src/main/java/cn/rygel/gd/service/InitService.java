package cn.rygel.gd.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.tencent.bugly.Bugly;

import cn.rygel.gd.BuildConfig;
import cn.rygel.gd.app.APP;
import cn.rygel.gd.constants.Global;

public class InitService extends IntentService {

    private static final String ACTION_INIT = "cn.rygel.gd.service.action.INIT";

    public InitService() {
        super("InitService");
    }

    public static void startInit(Context context) {
        Intent intent = new Intent(context, InitService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                handleActionInit();
            }
        }
    }

    private void handleActionInit() {
        initBugly();
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Bugly.init(APP.getInstance(), Global.BUGLY_ID, BuildConfig.DEBUG);
    }
}
