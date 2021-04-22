package com.example.nosnooze;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Accelerometer extends AppCompatActivity implements SensorEventListener{

    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager sensorM;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        sensorM = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Assign TextVIew
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        double x1 = Math.round(event.values[0]*100.0)/100.0;
        double y1 = Math.round(event.values[1]*100.0)/100.0;
        double z1 = Math.round(event.values[2]*100.0)/100.0;
        xText.setText("X: " +  x1);
        yText.setText("Y: " + y1);
        zText.setText("Z: " + z1);

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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}