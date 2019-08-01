package com.robin.unit.ble.broadcast;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.bluetooth.BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;

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
            Log.e("broadcast", "**- " + action + " -**");
            assert action != null;
            if (action.equals(ACTION_SCAN_MODE_CHANGED)) {

            } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass deviceCls = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                if (call!=null){
                    call.onSearchDevice(device);
                }
            }
        }
    }

    public interface BroadcastCall {
        void onBLEState();

        void onSearchDevice(BluetoothDevice device);
    }
}
