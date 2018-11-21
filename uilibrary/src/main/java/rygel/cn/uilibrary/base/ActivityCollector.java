package rygel.cn.uilibrary.base;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityCollector {

    public static ArrayList<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

}
