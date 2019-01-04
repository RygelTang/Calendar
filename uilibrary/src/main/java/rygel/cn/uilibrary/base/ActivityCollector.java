package rygel.cn.uilibrary.base;

import android.app.Activity;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class ActivityCollector {

    public static ArrayList<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        Logger.i("add activity : " + activity.getClass().getName());
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        Logger.i("remove activity : " + activity.getClass().getName());
        activities.remove(activity);
    }

}
