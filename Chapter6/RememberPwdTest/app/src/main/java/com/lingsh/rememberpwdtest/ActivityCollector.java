package com.lingsh.rememberpwdtest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lingsh
 * @date: 2019/12/17 14:19
 * @description:
 * @version: 1.0
 **/
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}
