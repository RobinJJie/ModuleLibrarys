package com.robin.robinutilsmoudle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.robin.unit.ble.BleAdapter;
import com.robin.unit.ble.BleHelper;
import com.robin.unit.ble.BleListener;

public class MainActivity extends AppCompatActivity implements BleListener {
    BleHelper bleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleHelper = new BleAdapter();
        boolean isRun  = bleHelper.init(this);
        Toast.makeText(this, "蓝牙开启" + isRun, Toast.LENGTH_SHORT).show();
    }

    public void click(View view) {
        bleHelper.startBle(this);
    }

    @Override
    public void onStart(boolean onRun) {
        Toast.makeText(this, "蓝牙开启" + onRun, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        bleHelper.onSet(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
