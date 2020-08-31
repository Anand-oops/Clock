package com.anand.clock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class TimerFragment extends Fragment {

    private long timerTime;
    private LinearLayout buttonsLayout, inputLayout;
    private Button setButton, startButton, pauseButton, stopButton;
    private EditText hourEditText, minuteEditText, secondEditText;
    private TextView timerTextView;
    private boolean isRunning;
    private CountDownTimer countDownTimer;

    public TimerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timerTextView = requireView().findViewById(R.id.timer_text);
        hourEditText = requireView().findViewById(R.id.hour_editText);
        minuteEditText = requireView().findViewById(R.id.min_editText);
        secondEditText = requireView().findViewById(R.id.sec_editText);

        setButton = requireView().findViewById(R.id.setTimer_button);
        startButton = requireView().findViewById(R.id.start_button);
        pauseButton = requireView().findViewById(R.id.pause_button);
        stopButton = requireView().findViewById(R.id.stop_button);

        buttonsLayout = requireView().findViewById(R.id.buttons);
        inputLayout = requireView().findViewById(R.id.input_table);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hourEditText.getText().toString().isEmpty() || minuteEditText.getText().toString().isEmpty()
                        || secondEditText.getText().toString().isEmpty()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
                    dialog.setMessage("Invalid Entry.... ");
                    dialog.setTitle("Error !");
                    dialog.setIcon(R.drawable.ic_block);
                    dialog.setPositiveButton("Enter Again",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                }
                            });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    if (hourEditText.getText().toString().equals("0") && minuteEditText.getText().toString().equals("0")
                            && secondEditText.getText().toString().equals("0"))
                        Toast.makeText(requireContext(), "Provide a non zero entry !", Toast.LENGTH_SHORT).show();
                    else {
                        isRunning = true;
                        buttonsLayout.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        int hour = Integer.parseInt(hourEditText.getText().toString().trim());
                        int min = Integer.parseInt(minuteEditText.getText().toString().trim());
                        int sec = Integer.parseInt(secondEditText.getText().toString().trim());
                        timerTime = (hour * 60 * 60 + min * 60 + sec) * 1000;
                        updateCountdown();
                    }
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButton.setVisibility(View.INVISIBLE);
                inputLayout.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                startCountdown();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    isRunning = false;
                    pauseButton.setText("Resume");
                    countDownTimer.cancel();
                } else {
                    isRunning = true;
                    pauseButton.setText("Pause");
                    startCountdown();
                }

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning = false;
                setButton.setVisibility(View.VISIBLE);
                buttonsLayout.setVisibility(View.INVISIBLE);
                inputLayout.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                timerTime = 0;
                countDownTimer.cancel();
                hourEditText.setText("");
                minuteEditText.setText("");
                pauseButton.setText("Pause");
                secondEditText.setText("");
                timerTextView.setText("00:00:00");
            }
        });
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(timerTime, 1000) {
            @Override
            public void onTick(long l) {
                timerTime = l;
                updateCountdown();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
                dialog.setMessage("");
                dialog.setTitle("Timer Finished");
                dialog.setIcon(android.R.drawable.stat_notify_error);
                dialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                isRunning = false;
                                setButton.setVisibility(View.VISIBLE);
                                buttonsLayout.setVisibility(View.INVISIBLE);
                                inputLayout.setVisibility(View.VISIBLE);
                                startButton.setVisibility(View.VISIBLE);
                                pauseButton.setVisibility(View.INVISIBLE);
                                stopButton.setVisibility(View.INVISIBLE);
                                timerTime = 0;
                                pauseButton.setText("Pause");
                                hourEditText.setText("");
                                minuteEditText.setText("");
                                secondEditText.setText("");
                                timerTextView.setText("00:00:00");
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }.start();
        isRunning = true;
    }

    private void updateCountdown() {
        long sec = (timerTime / 1000) % 60;
        long min = ((timerTime / 1000) % 3600) / 60;
        long hour = (timerTime / 1000) / 3600;
        String time = String.format(Locale.getDefault(), "%02d : %02d : %02d", hour, min, sec);
        timerTextView.setText(time);
    }
}
