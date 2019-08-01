package com.robin.unit.ble;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * @author lubin
 * @version 1.0
 */
public interface BleListener {
    void onStart(boolean onRun);
    void onSearch(Set<BluetoothDevice> devices,BluetoothDevice newDevices);
}
