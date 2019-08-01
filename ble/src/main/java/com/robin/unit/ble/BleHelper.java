package com.robin.unit.ble;

import android.app.Activity;
import android.content.Intent;

/**
 * @author lubin
 * @version 1.0
 */
public interface BleHelper {
    /**
     * 初始化
     *
     * @return 初始化结果
     */
    boolean init(Activity activity);

    /**
     * 启动蓝牙
     */
    void openBle(BleListener listener);

    /**
     * 关闭蓝牙
     */
    void closeBle();

    /**
     * 启动回调处理
     *
     * @param requestCode 请求code {BleConfig.REQUEST_ENABLE_BT (0x01)}
     * @param resultCode  返回结果状态
     * @param data        可携带的数据
     */
    void onSet(int requestCode, int resultCode, Intent data);
}
