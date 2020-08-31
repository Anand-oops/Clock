package com.anand.clock;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    static MediaPlayer player;
    int mediaCode;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
        mediaCode = intent.getIntExtra("mediaCode", -1);
        switch (mediaCode) {
            case 1:
                player = MediaPlayer.create(context, R.raw.cocka_doodle);
                break;
            case 2:
                player = MediaPlayer.create(context, R.raw.normal_alarm);
                break;
            case 3:
                player = MediaPlayer.create(context, R.raw.carol_alarm);
                break;
            case 4:
                player = MediaPlayer.create(context, R.raw.siren_alarm);
                break;
            case 5:
                player = MediaPlayer.create(context, R.raw.funny_alarm);
                break;
        }
        player.start();
        Intent intent1 = new Intent(context, NotificationTap.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, "Alarm")
                .setSmallIcon(R.drawable.ic_alarm).setContentTitle("Alarm...")
                .setPriority(NotificationCompat.PRIORITY_HIGH).setContentText("Tap to cancel").
                        setContentIntent(pendingIntent).build();

        notificationManagerCompat.notify(1, notification);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                player.start();
            }
        });

    }
}
