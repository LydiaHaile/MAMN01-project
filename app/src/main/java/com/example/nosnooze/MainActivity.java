package com.example.nosnooze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView alarmList;
    private static final ArrayList<Alarm> alarms = new ArrayList<>();
    private static AlarmAdapter alarmAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmList = findViewById(R.id.alarm_list);
        alarmAdapter = new AlarmAdapter(this, alarms);
        alarmList.setAdapter(alarmAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addAlarm(Alarm alarm) {
        boolean timeOccupied = false;
        for (Alarm compAlarm : alarms) {
            if (alarm.getTime().equals(compAlarm.getTime())) {
                timeOccupied = true;
                break;
            }
        }
        if (!timeOccupied) {
            if (alarms.size() != 0) {
                for (int i = 0; i < alarms.size(); i++) {
                    if (LocalTime.parse(alarms.get(i).getTime()).isAfter(LocalTime.parse(alarm.getTime()))) {
                        alarms.add(i, alarm);
                        alarmAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
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
