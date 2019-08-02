package com.robin.unit.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;

import com.robin.unit.ble.broadcast.BleSysBroadcast;

import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.robin.unit.ble.BTConfig.REQUEST_ENABLE_BT;

/**
 * @author lubin
 * @version 1.0
 * “经典”蓝牙
 */
public class BTAdapter implements BTHelper {
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static BTAdapter newInstance;
    private BluetoothAdapter mBluetoothAdapter;
    private BTListener listener;

    private BleSysBroadcast bleSysBroadcast;

    static {
        newInstance = new BTAdapter();
    }

    public static BTHelper with(Activity activity) {
        BTAdapter.activity = activity;
        return newInstance;
    }

    @Override
    public BTHelper init(boolean sysBleBroadcast, BTListener listener) {
        this.listener = listener;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        registerBro(sysBleBroadcast);
        return newInstance;
    }

    @Override
    public boolean isSupportBlue() {
        return mBluetoothAdapter != null;
    }

    @Override
    public boolean isBlueEnable() {
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    @Override
    public boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    @Override
    public void openBle() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void closeBle() {

    }

    @Override
    public void searchNewBle() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public boolean cancelScanBule() {
        if (isSupportBlue()) {
            return mBluetoothAdapter.cancelDiscovery();
        }
        return true;
    }

    @Override
    public void searchBleList() {
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
            if (listener != null) {
                listener.onSearch(bondedDevices, null);
            }
        }
    }

    @Override
    public void onSet(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (listener != null) {
                listener.onStart(resultCode == RESULT_OK);
            }
        }
    }

    /**
     * 监听广播
     */
    private void registerBro(boolean needChange) {
        if (bleSysBroadcast == null) {
            bleSysBroadcast = new BleSysBroadcast(new BleSysBroadcast.BroadcastCall() {
                @Override
                public void onBLEState() {

                }

                @Override
                public void onSearchDevice(BluetoothDevice device) {
                    if (listener != null) {
                        listener.onSearch(null, device);
                    }
                }
            });
        }
        IntentFilter intentFilter = new IntentFilter();
        if (needChange) {
            intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        }
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(bleSysBroadcast, intentFilter);
    }


    @Override
    public void onDestroy() {
        if (bleSysBroadcast != null) {
            activity.unregisterReceiver(bleSysBroadcast);
        }
        activity = null;
        newInstance = null;
    }
}
