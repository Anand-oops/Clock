package com.anand.clock;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    public static boolean isCharging(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert intent != null;
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_tap);

        disableAlarmButton = findViewById(R.id.disableAlarm);


        disableAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCharging(NotificationTap.this)) {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    assert manager != null;
                    manager.cancel(1);
                    AlarmReceiver.player.release();
                    Toast.makeText(NotificationTap.this, "Alarm Disabled...", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Snackbar.make(view, "Plug in charger...", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
