package com.example.nosnooze;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmSetter extends AppCompatActivity {

    private TextView timeText;
    private int tpHour, tpMinute;
    private SimpleDateFormat sdf;
    private String time;
    private Alarm alarm;
    MainActivity ma = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setter);
        timeText = findViewById(R.id.time);
        timeText.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmSetter.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth, (view, hourOfDay, minute) -> {
                tpHour =  hourOfDay;
                tpMinute =  minute;
                sdf = new SimpleDateFormat("HH:mm");
                try {
                    time = sdf.format(sdf.parse(hourOfDay + ":" + minute));
                    timeText.setText(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }, 12, 0, true);
            timePickerDialog.updateTime(tpHour, tpMinute);
            timePickerDialog.show();
        });
        Button buttonCreateAlarm = findViewById(R.id.create_alarm);
        buttonCreateAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                alarm = new Alarm(time, (int) System.currentTimeMillis(), 1);
                ma.addAlarm(alarm);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, tpHour);
                calendar.set(Calendar.MINUTE, tpMinute);
                calendar.set(Calendar.SECOND, 0);
                onTimeSet(calendar);
            }
        });
    }

    public void onTimeSet(Calendar calendar) {
        startAlarm(calendar);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("extra", "alarm on");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getId(), intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Intent gotoIntent = new Intent(this, MainActivity.class);
        startActivity(gotoIntent);
    }

    public void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
        timeText.setText("Alarm canceled");
    }
}