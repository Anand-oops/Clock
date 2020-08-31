package com.anand.clock;

import android.app.NotificationManager;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class NotificationTap extends AppCompatActivity {

    Button disableAlarmButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_tap);

        disableAlarmButton = findViewById(R.id.disableAlarm);

        disableAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BatteryManager batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
                if (batteryManager.isCharging()) {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    assert manager != null;
                    manager.cancel(1);
                    AlarmReceiver.player.release();
                    Toast.makeText(NotificationTap.this, "Alarm Disabled...", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Snackbar.make(view, "Plug in the charger first...", Snackbar.LENGTH_SHORT).setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark)).show();
                }
            }
        });
    }
}
