package com.anand.clock;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Alarms.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "alarms";
    private static final String ALARM_ID = "id";
    private static final String ALARM_STATUS = "status";
    private static final String ALARM_TIME = "time";
    public static final String ALARM_MEDIA = "media";

    public AlarmHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ALARMS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ALARM_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + ALARM_TIME + " TEXT, " + ALARM_STATUS + " TEXT, " + ALARM_MEDIA + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE_ALARMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addAlarm(String time, String status, String mediaCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALARM_TIME, time);
        values.put(ALARM_STATUS, status);
        values.put(ALARM_MEDIA, mediaCode);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor viewAlarm() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void deleteAlarm(AlarmEntryClass alarmEntry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ALARM_ID + " =?", new String[]{String.valueOf(alarmEntry.getId())});
        db.close();
    }

    public String getAlarmStatus(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ALARM_ID + " = " + id + "", null);
        cursor.moveToFirst();
        return cursor.getString(2);
    }

    public void updateAlarmStatus(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALARM_STATUS, status);
        db.update(TABLE_NAME, values, ALARM_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    /*public void updateAlarmTime(String id, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALARM_TIME, time);
        db.update(TABLE_NAME, values, ALARM_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }*/
}
