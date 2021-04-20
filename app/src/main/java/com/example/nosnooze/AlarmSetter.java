package com.example.nosnooze;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmSetter extends AppCompatActivity {

    private TextView timeText;
    private int tpHour, tpMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setter);
        timeText = findViewById(R.id.time);
        timeText.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmSetter.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth, (view, hourOfDay, minute) -> {
                tpHour =  hourOfDay;
                tpMinute =  minute;
                String time = hourOfDay + ":" + minute;
                SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm");
                SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
                try {
                    time = date24Format.format(date12Format.parse(time));
                    timeText.setText(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }, 0, 0, true);
            timePickerDialog.updateTime(tpHour, tpMinute);
            timePickerDialog.show();
        });

        Button buttonTimePicker = findViewById(R.id.time_picker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new DialogFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        Button buttonCreateAlarm = findViewById(R.id.create_alarm);
        buttonCreateAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, tpHour);
                calendar.set(Calendar.MINUTE, tpMinute);
                calendar.set(Calendar.SECOND, 0);
                onTimeSet(calendar);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:ss");
                Toast.makeText(AlarmSetter.this, "Set alarm for " + sdf.format(calendar), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onTimeSet(Calendar calendar) {
        updateTimeText(calendar);
        startAlarm(calendar);
    }

    private void updateTimeText(Calendar c) {
        String time = "Alarm set for: ";
        time += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        timeText.setText(time);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        timeText.setText("Alarm canceled");
    }
}