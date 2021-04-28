package com.example.nosnooze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView alarmList;
    private static final ArrayList<Alarm> alarms = new ArrayList<>();
    private static AlarmAdapter alarmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmList = findViewById(R.id.alarm_list);
        alarmAdapter = new AlarmAdapter(this, alarms);
        alarmList.setAdapter(alarmAdapter);


        for (Alarm alarm : alarms) {
            Log.d("debugging", "alarm " + alarm.getTime() + " is " + alarm.getActive());
        }

        //ADDING STANDARD ALARMS FOR DEBUGGING (note that these are being created every time app enters  MainActivity)
        addAlarm(new Alarm("08:00", 1, 1));
        addAlarm(new Alarm("08:15", 2, 2));
        addAlarm(new Alarm("08:30", 3, 3));
    }

    public void addAlarm(Alarm alarm) {
        boolean timeOccupied = false;
        for (Alarm compAlarm : alarms) {
            if (alarm.getTime().equals(compAlarm.getTime())) {
                timeOccupied = true;
                Log.d("alarms", "tried to create alarm but failed: " + alarm.getTime());
                break;
            }
        }
        if (!timeOccupied) {
            alarms.add(alarm);
            alarmAdapter.notifyDataSetChanged();
        }
    }

    public void shutOffAlarm(View view) {
        Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", "alarm_off");
        this.startService(serviceIntent);
    }

    public void removeAlarm(int index) {
        alarms.remove(index);
        alarmAdapter.notifyDataSetChanged();
        Log.d("alarms", "ArrayList size: " + alarms.size());
        Log.d("alarms", "Adapter size: " + alarmAdapter.getCount());
    }

    public void stepCounter(View view) {
        Intent intent = new Intent(this, StepCounter.class);
        startActivity(intent);
    }


    public void gotoAlarm(View view) {
        Intent intent = new Intent(this, AlarmSetter.class);
        startActivity(intent);
    }
}
