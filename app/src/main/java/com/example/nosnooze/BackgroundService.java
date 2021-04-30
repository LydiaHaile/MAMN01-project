package com.example.nosnooze;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BackgroundService extends Service implements SensorEventListener {
    private Sensor mySensor;
    private SensorManager sensorM;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 100;
    private float steps = 0;
    private int typeOfInteraction;
    private Sensor currentSensor;
    private TextView BackgroundServiceText;

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        typeOfInteraction = intent.getExtras().getInt("interaction");
        Toast.makeText(this, "" + typeOfInteraction, Toast.LENGTH_SHORT).show();
        sensorM = (SensorManager) getSystemService(SENSOR_SERVICE);
        //setContentView(R.layout.activity_accelerometer);
        if(typeOfInteraction == 1){
            mySensor = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else if(typeOfInteraction == 2){
            //För annan sensor exempelvis pitch
        }
        else{
            mySensor = sensorM.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }

        sensorM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        //stepCounter.setText("Steps : " + steps);
        return START_STICKY;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
        double x1 = Math.round(event.values[0]*100.0)/100.0;
        double y1 = Math.round(event.values[1]*100.0)/100.0;
        double z1 = Math.round(event.values[2]*100.0)/100.0;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
                serviceIntent.putExtra("extra", "alarm_off");
                this.startService(serviceIntent);

                Toast.makeText(getApplicationContext(), "Your phone just shook", Toast.LENGTH_LONG).show();
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }}
        else if(event.sensor.getType()== Sensor.TYPE_GRAVITY){
            // För pitch sensor senare.
        }
        else {
            steps++;
            String temp = Float.toString(steps);
            Toast.makeText(getApplicationContext(), "Steps: " + temp, Toast.LENGTH_LONG).show();

           // stepCounter.setText("Steps : " + steps);

            if (steps > 15) {
                Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
                serviceIntent.putExtra("extra", "alarm_off");
                this.startService(serviceIntent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}