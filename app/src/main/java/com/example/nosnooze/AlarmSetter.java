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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class AlarmSetter extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView timeText;
    private int tpHour, tpMinute;
    private SimpleDateFormat sdf;
    private String time = "00:00";
    private Alarm alarm;
    MainActivity ma = new MainActivity();

    private int selectedInteraction;

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
        buttonCreateAlarm.setOnClickListener(v -> {
            alarm = new Alarm(time, (int) System.currentTimeMillis(),  selectedInteraction);
            ma.addAlarm(alarm);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, tpHour);
            calendar.set(Calendar.MINUTE, tpMinute);
            calendar.set(Calendar.SECOND, 0);
            startAlarm(calendar);
        });


        //Drop down list på vilken interaktion man vill använda
        Spinner interactionSpinner = (Spinner) findViewById(R.id.interaction_spinner);
        String[] interactions = getResources().getStringArray(R.array.interactions);
        ArrayAdapter myAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, interactions);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interactionSpinner.setAdapter(myAdapter);
        interactionSpinner.setOnItemSelectedListener(this);
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
        gotoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(gotoIntent);
    }

    public void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
        timeText.setText("Alarm canceled");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.interaction_spinner) {
            selectedInteraction = position+1;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}