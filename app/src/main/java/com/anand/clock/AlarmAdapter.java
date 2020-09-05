package com.anand.clock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private static final String TAG = "AlarmAdapter";
    private ArrayList<AlarmEntryClass> alarmArrayList;
    private Context context;

    public AlarmAdapter(Context context, ArrayList<AlarmEntryClass> list) {
        this.context = context;
        alarmArrayList = list;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlarmViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmAdapter.AlarmViewHolder holder, final int position) {
        holder.timeText.setText(alarmArrayList.get(position).getTime());
        if (alarmArrayList.get(position).getStatus().equals("true")) {
            holder.statusSwitch.setChecked(true);
        } else
            holder.statusSwitch.setChecked(false);

        holder.statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.statusSwitch.isChecked()) {
                    AlarmHelper alarmHelper = new AlarmHelper(context);
                    alarmHelper.updateAlarmStatus(alarmArrayList.get(position).getId(), "true");
                    final String time = holder.timeText.getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    int hour, min;

                    if (time.contains("AM")) {
                        hour = Integer.parseInt(time.substring(0, 2));
                    } else
                        hour = Integer.parseInt(time.substring(0, 2)) + 12;

                    min = Integer.parseInt(time.substring(3, 5));
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                    startAlarm(Integer.parseInt(alarmArrayList.get(position).getId()), calendar, Integer.parseInt(alarmArrayList.get(position).getMediaCode()));
                } else {
                    AlarmHelper alarmHelper = new AlarmHelper(context);
                    alarmHelper.updateAlarmStatus(alarmArrayList.get(position).getId(), "false");
                    cancelAlarm(Integer.parseInt(alarmArrayList.get(position).getId()));
                    Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Press and hold to Delete", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete ...");
                dialog.setMessage("Are you sure you want to delete ?");
                dialog.setIcon(android.R.drawable.ic_menu_delete);
                dialog.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                AlarmHelper alarmHelper = new AlarmHelper(context);
                                if (alarmHelper.getAlarmStatus(alarmArrayList.get(position).getId()).equals("true")) {
                                    cancelAlarm(Integer.parseInt(alarmArrayList.get(position).getId()));
                                }
                                alarmHelper.deleteAlarm(alarmArrayList.get(position));
                                alarmArrayList.remove(alarmArrayList.get(position));
                                notifyDataSetChanged();
                            }
                        });
                dialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                return true;
            }
        });
    }

    private void startAlarm(int reqCode, Calendar setCalendar, int mediaCode) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("mediaCode", mediaCode);
        if (setCalendar.before(Calendar.getInstance())) {
            setCalendar.add(Calendar.DATE, 1);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        manager.setExact(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(context, "Alarm Scheduled", Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public int getItemCount() {
        return alarmArrayList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch statusSwitch;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.alarm_time);
            statusSwitch = itemView.findViewById(R.id.alarm_switch);
        }
    }
}
