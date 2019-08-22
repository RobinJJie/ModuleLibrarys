package com.robin.robinutilsmoudle;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robin.unit.ble.BTAdapter;
import com.robin.unit.ble.BTHelper;
import com.robin.unit.ble.BTListener;
import com.robin.unit.ble.connect.ConnectThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BTListener {
    BTHelper bleHelper;
    RecyclerView recyclerView;

    List<BluetoothDevice> deviceList = new ArrayList<>();
    private AdapterLs adapterLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.ls);

        adapterLs = new AdapterLs();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterLs);
        bleHelper = BTAdapter.with(this).init(true, this);
        bleHelper.searchBleList();
        bleHelper.searchNewBle();
    }


    @Override
    public void onStart(boolean onRun) {
        Toast.makeText(this, "蓝牙开启" + onRun, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearch(Set<BluetoothDevice> devices, BluetoothDevice newDevices) {
        if (newDevices != null) {
            Log.e("BLE_list", newDevices.getName() + "--" + newDevices.getAddress());
            adapterLs.add(newDevices);
        }
        if (devices != null) {
            if (devices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : devices) {
                    // Add the name and address to an array adapter to show in a ListView
                    Log.e("BLE_list", device.getName() + "--" + device.getAddress());
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

    class AdapterLs extends RecyclerView.Adapter<AdapterLs.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
            return new ViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.txt.setText(String.valueOf(deviceList.get(position).getName() + "\n" + deviceList.get(position).getAddress()));
            holder.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectThread thread = new ConnectThread(deviceList.get(position), bleHelper.getBluetoothAdapter(), UUID.randomUUID());
                    thread.start();
                    Toast.makeText(MainActivity.this, "ddd", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void add(BluetoothDevice device) {
            if (!TextUtils.isEmpty(device.getName())) {
                int size = deviceList.size();
                deviceList.add(device);
                notifyItemRangeInserted(size, 1);
            }

        }

        @Override
        public int getItemCount() {
            return deviceList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.tv);
            }
        }
    }
}
