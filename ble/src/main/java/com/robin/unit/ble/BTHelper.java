package com.robin.unit.ble;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * @author lubin
 * @version 1.0
 */
public interface BTHelper {
    /**
     * 初始化
     *
     * @return 初始化结果
     */
    BTHelper init(boolean sysBleBroadcast, BTListener listener);

    BluetoothAdapter getBluetoothAdapter();
    /**
     * 设备是否支持蓝牙
     * @return b
     */
    boolean isSupportBlue();

    /**
     * 是否打开
     * @return b
     */
    boolean isBlueEnable();

    /**
     * GPS 是否开启
     * @return b
     */
    boolean checkGPSIsOpen();
    /**
     * 启动蓝牙
     */
    void openBle();

    /**
     * 关闭蓝牙
     */
    void closeBle();

    /**
     * 搜索新设备
     */
    void searchNewBle();

    /**
     * 取消扫描
     * @return b
     */
    boolean cancelScanBule();

    /**
     * 查看链接列表
     */
    void searchBleList();

    /**
     * 启动回调处理
     *
     * @param requestCode 请求code {BleConfig.REQUEST_ENABLE_BT (0x01)}
     * @param resultCode  返回结果状态
     * @param data        可携带的数据
     */
    void onSet(int requestCode, int resultCode, Intent data);

    /**
     * 销毁
     */
    void onDestroy();
}
