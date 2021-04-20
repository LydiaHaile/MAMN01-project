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

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private int currentSensor;
    private Sensor stepCounterSensor;
    private Sensor accelerometerSensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private TextView textView;
    ListView alarmList;
    Button btnStepCounterOnClick;
    private final String[] alarms = {"08:40", "08:50", "09:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmList = findViewById(R.id.alarm_list);
        AlarmAdapter alarmAdapter = new AlarmAdapter(this, alarms);
        alarmList.setAdapter(alarmAdapter);
        textView = findViewById(R.id.tvResult);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        btnStepCounterOnClick = findViewById(R.id.btnStepCounterOnClick); // behövs den?
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }
    public boolean checkSensorAvailability(int sensorType) {
        boolean isSensor = false;
        if (sensorManager.getDefaultSensor(sensorType) != null) {
            isSensor = true;
        }
        return isSensor;
    }

    public void addAlarm(View view) {
        Intent intent = new Intent(this, AlarmSetter.class);
        startActivity(intent);
    }

    public void stepCounterOnClick(View view) {
        if (checkSensorAvailability(Sensor.TYPE_STEP_DETECTOR)) {
            currentSensor = Sensor.TYPE_STEP_DETECTOR;
        } else {
            textView.setText("Step Counter Sensor not available");
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == currentSensor) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Toast.makeText(getApplicationContext(), "Your phone just shook", Toast.LENGTH_LONG).show();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
      //  float steps = event.values[0];
     //   textView.setText("Steps : " + steps);
    }}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
        public void accelerometerSensorOnClick(View view) {
            if (checkSensorAvailability(Sensor.TYPE_ACCELEROMETER)) {
                currentSensor = Sensor.TYPE_ACCELEROMETER;
            }
            textView.setText("Accelerometer not available");
        }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, stepCounterSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


}
