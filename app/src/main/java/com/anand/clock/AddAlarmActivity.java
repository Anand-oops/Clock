package com.anand.clock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddAlarmActivity";
    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    int hour, min, reqCode;
    AlarmHelper alarmHelper;
    Calendar setCalendar = Calendar.getInstance();
    private TextView timeText;
    private MediaPlayer mediaPlayer;
    private int mediaCode = 0;
    private String time;

    @Override
    protected void onResume() {
        reqCode = sharedPreferences.getInt("reqCode", 0);
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        alarmHelper = new AlarmHelper(this);
        Button setAlarm = findViewById(R.id.setAlarmButton);
        timeText = findViewById(R.id.alarmTimeTV);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePicker = new TimePickerDialog(AddAlarmActivity.this, AddAlarmActivity.this, hour, min,
                        DateFormat.is24HourFormat(AddAlarmActivity.this));
                timePicker.show();
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time == null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddAlarmActivity.this);
                    dialog.setMessage("");
                    dialog.setTitle("Select Time...");
                    dialog.setIcon(R.drawable.ic_block);
                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                }
                            });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else if (mediaCode == 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddAlarmActivity.this);
                    dialog.setMessage("");
                    dialog.setTitle("Select Media...");
                    dialog.setIcon(R.drawable.ic_block);
                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                }
                            });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    alarmHelper.addAlarm(time, "true", String.valueOf(mediaCode));
                    startAlarm();
                    finish();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.findViewById(i).getId()) {
                    case R.id.radio1:
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        mediaPlayer = MediaPlayer.create(AddAlarmActivity.this, R.raw.cocka_doodle);
                        mediaPlayer.start();
                        mediaCode = 1;
                        break;
                    case R.id.radio2:
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        mediaPlayer = MediaPlayer.create(AddAlarmActivity.this, R.raw.normal_alarm);
                        mediaPlayer.start();
                        mediaCode = 2;
                        break;
                    case R.id.radio3:
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        mediaPlayer = MediaPlayer.create(AddAlarmActivity.this, R.raw.carol_alarm);
                        mediaPlayer.start();
                        mediaCode = 3;
                        break;
                    case R.id.radio4:
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        mediaPlayer = MediaPlayer.create(AddAlarmActivity.this, R.raw.siren_alarm);
                        mediaPlayer.start();
                        mediaCode = 4;
                        break;
                    case R.id.radio5:
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        mediaPlayer = MediaPlayer.create(AddAlarmActivity.this, R.raw.funny_alarm);
                        mediaPlayer.start();
                        mediaCode = 5;
                        break;
                }
            }
        });
    }

    private void startAlarm() {
        reqCode++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("reqCode", reqCode).apply();
        Log.d(TAG, "startAlarm: reqCode" + reqCode);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("mediaCode", mediaCode);
        intent.putExtra("reqCode", reqCode);
        if (setCalendar.before(Calendar.getInstance())) {
            setCalendar.add(Calendar.DATE, 1);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reqCode, intent, 0);
        manager.setExact(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(AddAlarmActivity.this, "Alarm Scheduled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        setCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        setCalendar.set(Calendar.MINUTE, minute);
        setCalendar.set(Calendar.SECOND, 0);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatConvert = new SimpleDateFormat("hh:mm a");
        Date dateConvert = setCalendar.getTime();
        time = formatConvert.format(dateConvert);
        timeText.setText(time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
