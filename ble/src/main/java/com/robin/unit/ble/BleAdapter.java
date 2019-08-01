package com.robin.unit.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import static android.app.Activity.RESULT_OK;
import static com.robin.unit.ble.BleConfig.REQUEST_ENABLE_BT;

/**
 * @author lubin
 * @version 1.0
 */
public class BleAdapter implements BleHelper {
    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private BleListener listener;

    @Override
    public boolean init(Activity context) {
        this.activity = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();
    }

    @Override
    public void openBle(BleListener listener) {
        this.listener = listener;
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void closeBle() {

    }

    @Override
    public void onSet(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (listener != null) {
                listener.onStart(resultCode == RESULT_OK);
            }
        }
    }
}
