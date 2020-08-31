package com.anand.clock;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class StopwatchFragment extends Fragment {

    private ImageButton lapButton, playButton, stopButton;
    private boolean isRunning;
    private Chronometer chronometer;
    private ArrayList<String> lapArrayList = new ArrayList<>();
    private ListView lapListView;
    private long pauseMargin;

    public StopwatchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lapButton = requireView().findViewById(R.id.lap_button);
        playButton = requireView().findViewById(R.id.play_button);
        stopButton = requireView().findViewById(R.id.stop_button);
        chronometer = requireView().findViewById(R.id.chronometer);
        lapListView = requireView().findViewById(R.id.lap_list);

        playButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    isRunning = true;
                    playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause, null));
                    lapButton.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.VISIBLE);
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseMargin);
                    chronometer.start();
                } else {
                    isRunning = false;
                    playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play, null));
                    chronometer.stop();
                    lapButton.setVisibility(View.INVISIBLE);
                    pauseMargin = SystemClock.elapsedRealtime() - chronometer.getBase();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                isRunning = false;
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseMargin = 0;
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play, null));
                lapButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                lapArrayList.clear();
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                long hour = (time / 3600000);
                long min = (time - hour * 3600000) / 60000;
                long sec = (time - hour * 3600000 - min * 60000) / 1000;
                String lap;
                if (hour < 10) lap = "0" + hour;
                else lap = String.valueOf(hour);
                lap += ":";
                if (min < 10) lap += "0" + min;
                else lap += String.valueOf(min);
                lap += ":";
                if (sec < 10) lap += "0" + sec;
                else lap += String.valueOf(sec);

                lapArrayList.add(lap);
                lapListView.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.lap_list_item, lapArrayList));
            }
        });
    }
}
