package com.robin.robinutilsmoudle;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.robin.unit.ble.BleAdapter;
import com.robin.unit.ble.BleHelper;
import com.robin.unit.ble.BleListener;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements BleListener {
    BleHelper bleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleHelper = BleAdapter.with(this).init(true,this);
        bleHelper.searchBleList();
        bleHelper.searchNewBle();

    }

    public void click(View view) {
        bleHelper.openBle();

    }

    @Override
    public void onStart(boolean onRun) {
        Toast.makeText(this, "蓝牙开启" + onRun, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearch(Set<BluetoothDevice> devices, BluetoothDevice newDevices) {
        if (newDevices!=null){
            Log.e("BLE_list",newDevices.getName() + "--" + newDevices.getAddress());
        }
        if (devices!=null){
            if (devices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : devices) {
                    // Add the name and address to an array adapter to show in a ListView
                    Log.e("BLE_list",device.getName() + "--" + device.getAddress());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        bleHelper.onSet(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        bleHelper.onDestroy();
        super.onDestroy();
    }
}
