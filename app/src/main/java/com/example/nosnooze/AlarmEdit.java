package com.example.nosnooze;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class AlarmEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int tpHour, tpMinute, selectedInteraction;
    private SimpleDateFormat sdf;
    private TextView timeText;
    private String time = "00:00";
    private Alarm alarm;
    private final MainActivity ma = new MainActivity();
    private Random random;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Button cancelButton = findViewById(R.id.abort_alarm);
        timeText = findViewById(R.id.time);
        random = new Random();

        cancelButton.setOnClickListener(v -> {
            Intent goBackIntent = new Intent(this, MainActivity.class);
            startActivity(goBackIntent);
        });

        int alarmPos = getIntent().getIntExtra("position",0);
        int alarmId = getIntent().getIntExtra("alarmId",0);
        String alarmTime = getIntent().getStringExtra("time");
        String hourAndMinute[] = alarmTime.split(":");
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(Integer.parseInt(hourAndMinute[0]));
        timePicker.setMinute(Integer.parseInt(hourAndMinute[1]));
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            tpHour = hourOfDay;
            tpMinute = minute;
            sdf = new SimpleDateFormat("HH:mm");
            try {
                time = sdf.format(sdf.parse(timePicker.getHour() + ":" + timePicker.getMinute()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        sdf = new SimpleDateFormat("HH:mm");
        try {
            time = sdf.format(sdf.parse(timePicker.getHour() + ":" + timePicker.getMinute()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Button buttonCreateAlarm = findViewById(R.id.create_alarm);
        buttonCreateAlarm.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, tpHour);
            calendar.set(Calendar.MINUTE, tpMinute);
            calendar.set(Calendar.SECOND, 0);
            alarm = new Alarm(time, (int) System.currentTimeMillis(),  selectedInteraction, calendar);


            //ta bort det gamla larmet
            ma.removeAlarm(alarmPos);
            ma.getAlarmAdapter().disableAlarm(alarmId, alarmTime);

            ma.addAlarm(alarm);
            startAlarm(alarm);
        });

        Spinner interactionSpinner = (Spinner) findViewById(R.id.interaction_spinner);
        String[] interactions = getResources().getStringArray(R.array.interactions);
        ArrayAdapter myAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, interactions);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interactionSpinner.setAdapter(myAdapter);
        interactionSpinner.setOnItemSelectedListener(this);
    }

    private void startAlarm(Alarm alarm) {
        Calendar c = alarm.getCalendar();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("extra", "alarm on");
        intent.putExtra("interaction", selectedInteraction);
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
            selectedInteraction = position;
        }

        /*if(selectedInteraction == 0){
            int numberOfInteractions = parent.getAdapter().getCount()-1;
            selectedInteraction = random.nextInt(numberOfInteractions)+1;
        }
        */
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}