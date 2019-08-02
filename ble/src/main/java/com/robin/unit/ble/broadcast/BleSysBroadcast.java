package com.robin.unit.ble.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * @author lubin
 * @version 1.0
 */
public class BleSysBroadcast extends BroadcastReceiver {
    private BroadcastCall call;

    public BleSysBroadcast(BroadcastCall call) {
        this.call = call;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            assert action != null;
            switch (action){
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Log.d("BLE", "开始扫描...");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d("BLE","结束扫描...");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("BLE,","发现设备...");
                    call.onSearchDevice(device);
                    break;
                case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                    Log.d("BLE,","蓝牙状态改变");
                    break;
            }
        }
    }

    public interface BroadcastCall {
        void onBLEState();

        void onSearchDevice(BluetoothDevice device);
    }
}
