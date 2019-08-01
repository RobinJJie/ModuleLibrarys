package com.robin.unit.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;

import com.robin.unit.ble.broadcast.BleSysBroadcast;

import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.bluetooth.BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
import static com.robin.unit.ble.BleConfig.REQUEST_ENABLE_BT;

/**
 * @author lubin
 * @version 1.0
 */
public class BleAdapter implements BleHelper {
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static BleAdapter newInstance;
    private BluetoothAdapter mBluetoothAdapter;
    private BleListener listener;

    private BleSysBroadcast bleSysBroadcast;

    static {
        newInstance = new BleAdapter();
    }

    public static BleHelper with(Activity activity) {
        BleAdapter.activity = activity;
        return newInstance;
    }

    @Override
    public BleHelper init(boolean sysBleBroadcast, BleListener listener) {
        this.listener = listener;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (sysBleBroadcast) {
            registerBro();
        }
        if (listener != null) {
            listener.onStart(mBluetoothAdapter.isEnabled());
        }
        return newInstance;
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
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(bleSysBroadcast, filter);
        if (mBluetoothAdapter!=null&&mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void searchBleList() {
        if (mBluetoothAdapter!=null){
            Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
            if (listener!=null){
                listener.onSearch(bondedDevices,null);
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
    private void registerBro() {
        IntentFilter intentFilter = new IntentFilter(ACTION_SCAN_MODE_CHANGED);
        activity.registerReceiver(bleSysBroadcast, intentFilter);
        if (bleSysBroadcast == null) {
            bleSysBroadcast = new BleSysBroadcast(new BleSysBroadcast.BroadcastCall() {
                @Override
                public void onBLEState() {

                }

                @Override
                public void onSearchDevice(BluetoothDevice device) {
                    if (listener!=null){
                        listener.onSearch(null,device);
                    }
                }
            });

        }
    }


    @Override
    public void onDestroy() {
        if (bleSysBroadcast!=null){
            activity.unregisterReceiver(bleSysBroadcast);
        }
        activity = null;
        newInstance = null;
    }
}
