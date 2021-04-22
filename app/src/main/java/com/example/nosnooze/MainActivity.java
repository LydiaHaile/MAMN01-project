package com.example.nosnooze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView alarmList;
    private final String[] alarms = {"08:40", "08:50", "09:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmList = findViewById(R.id.alarm_list);
        AlarmAdapter alarmAdapter = new AlarmAdapter(this, alarms);
        alarmList.setAdapter(alarmAdapter);
    }

    public void addAlarm(View view) {
        Intent intent = new Intent(this, AlarmSetter.class);
        startActivity(intent);
    }

    public void stepCounter(View view) {
        Intent intent= new Intent(this, StepCounter.class);
        startActivity(intent);
    }

    public void accelerometer(View view){
        Intent intent = new Intent(this, Accelerometer.class);
        startActivity(intent);
    }

}
