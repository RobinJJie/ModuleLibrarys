package com.robin.badgenumber;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author lubin
 * @version 1.0
 * 角标工具类
 */
public class BadgeHelper implements BadgeContract {
    private static BadgeHelper instance;

    private int badgeNum = 0;
    private Application application;
    private String launcherPackageName = "ee";
    private String hostActivity;

    public static BadgeHelper with() {
        if (instance == null) {
            instance = new BadgeHelper();
        }
        return instance;
    }

    @Override
    public BadgeContract init(Application application, String hostActivity) {
        this.application = application;
        this.hostActivity = hostActivity;
        launcherPackageName = getLauncherPackageName(application);
        Log.e("android_name", launcherPackageName);
        Log.e("android_name", application.getOpPackageName());
        Log.e("android_name", application.getPackageName());
        return instance;
    }

    @Override
    public Notification setBadgeNumber(int number, Notification notification) throws Exception {
        this.badgeNum = number;
        if (number > 0) {
            badgeNum = badgeNum + number;
        } else {
            badgeNum = 0;
        }
        if (LauncherConfig.LAUNCHER_GOOGLE.equals(launcherPackageName)) {
            googleBadgeNum(badgeNum);
        } else if (LauncherConfig.LAUNCHER_HUAWEI.equals(launcherPackageName)) {
            huaWeiBadge(badgeNum, hostActivity);
        } else if (LauncherConfig.LAUNCHER_MIUI.equals(launcherPackageName)) {
            xiaomiBadgeNum(badgeNum, notification);
        } else if (LauncherConfig.LAUNCHER_SAMSUNG.equals(launcherPackageName)) {
            samsungBadgeNum(number);
        } else {

        }
        return notification;
    }


    @Override
    public String getLauncherPackageName(Application application) {
        //获取ApplicationContext
        final Context context = application.getBaseContext();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        assert res != null;
        if (res.activityInfo == null) {
            // should not happen. A home is always installed.
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            return null;
        } else {
            return res.activityInfo.packageName;
        }

    }

    @Override
    public void destroy() {
        instance = null;
    }

    private void huaWeiBadge(int badgeNum, String host) {
        if (application == null || instance == null) return;
        Bundle extra = new Bundle();
        extra.putString("package", application.getOpPackageName());
        extra.putString("class", host);
        extra.putInt("badgenumber", badgeNum);
        application.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, extra);
    }

    private Notification xiaomiBadgeNum(int count, Notification notification) throws Exception {
        Field field = notification.getClass().getDeclaredField("extraNotification");
        Object extraNotification = field.get(notification);
        assert extraNotification != null;
        Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
        method.invoke(extraNotification, count);
        return notification;
    }

    private void samsungBadgeNum(int count) throws Exception {
        final Context context = application.getBaseContext();
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", application.getPackageName());
        context.sendBroadcast(intent);
    }

    private void googleBadgeNum(int count) throws Exception {
        final Context context = application.getBaseContext();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            throw new Exception("error " + "Google");
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", application.getPackageName()); // com.test. badge.MainActivity is your apk main activity
        context.sendBroadcast(intent);
    }

}
