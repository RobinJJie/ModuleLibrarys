package com.robin.robinutilsmoudle;



import android.app.Notification;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.robin.badgenumber.BadgeContract;
import com.robin.badgenumber.BadgeHelper;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BadgeContract init = BadgeHelper.with().init(getApplication(), AppCompatActivity.class.getName().toString());
        try {
            init.setBadgeNumber(1,new Notification());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
