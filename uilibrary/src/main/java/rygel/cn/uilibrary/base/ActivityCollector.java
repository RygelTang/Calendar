package rygel.cn.uilibrary.base;

import android.app.Activity;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class ActivityCollector {

    private static final String TAG = "ActivityCollector";

    public static ArrayList<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        Logger.i(TAG,"add activity : " + activity.getClass().getName());
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        Logger.i(TAG,"remove activity : " + activity.getClass().getName());
        activities.remove(activity);
    }

}
