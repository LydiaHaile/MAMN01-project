package com.example.nosnooze;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmSetter extends AppCompatActivity {

    TextView timePicker, timeShow;
    int tpHour, tpMinute;

    TextView updateText;
    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    Context context;
    PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setter);
        this.context = this;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmTimePicker.setIs24HourView(true);
        updateText = (TextView) findViewById(R.id.updateText);
        final Calendar calendar = Calendar.getInstance();

        Intent myIntent = new Intent(this.context, AlarmReceiver.class);

        Button startAlarm = (Button) findViewById(R.id.start_alarm);

        startAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                setAlarmText("Alarm on!");
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
                int hour = alarmTimePicker.getHour();
                int minute = alarmTimePicker.getMinute();
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);
                if (minute <10 ){
                    minute_string = "0" + String.valueOf(minute);
                }
                setAlarmText("Alarm set to: " +hour_string + ":"+ minute_string);

                myIntent.putExtra("extra", "alarm on");

                pendingIntent = PendingIntent.getBroadcast(AlarmSetter.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        Button stopButton = (Button) findViewById(R.id.end_alarm);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmText("Alarm off!");
                alarmManager.cancel(pendingIntent);
                myIntent.putExtra("extra", "alarm off");
                sendBroadcast(myIntent);
            }
        });



    }

    private void setAlarmText(String output) {
        updateText.setText(output);
    }
}