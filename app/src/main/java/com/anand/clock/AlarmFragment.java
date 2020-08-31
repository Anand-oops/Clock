package com.anand.clock;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlarmFragment extends Fragment {

    FloatingActionButton addAlarm;
    RecyclerView recyclerView;
    AlarmHelper alarmHelper;
    AlarmAdapter alarmAdapter;
    ArrayList<AlarmEntryClass> alarmList;

    public AlarmFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alarmHelper = new AlarmHelper(requireContext());
        recyclerView = requireView().findViewById(R.id.alarm_recyclerView);
        addAlarm = requireView().findViewById(R.id.floatingActionButton);
        alarmList = new ArrayList<>();

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AddAlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        viewAlarms();
        super.onResume();
    }

    private void viewAlarms() {
        Cursor cursor = alarmHelper.viewAlarm();
        alarmList.clear();
        TextView textView = requireView().findViewById(R.id.empty_textView);
        if (cursor.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            if (cursor.moveToFirst()) {
                do {
                    AlarmEntryClass alarmEntry = new AlarmEntryClass(cursor.getString(0),
                            cursor.getString(1), cursor.getString(2));
                    alarmList.add(alarmEntry);
                } while (cursor.moveToNext());
            }
            cursor.close();
            alarmHelper.close();
            alarmAdapter = new AlarmAdapter(getContext(), alarmList);
            recyclerView.setAdapter(alarmAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}
