package com.robin.badgenumber;

import android.app.Application;
import android.app.Notification;

/**
 * @author lubin
 * @version 1.0 wang_junjie007@163.com
 * 角标契约类
 */
public interface BadgeContract {
    BadgeContract init(Application application, String hostActivity);

    Notification setBadgeNumber(int number, Notification notification) throws Exception;

    String getLauncherPackageName(Application application);

    void destroy();
}
