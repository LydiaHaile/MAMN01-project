package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneShake extends AppCompatActivity implements SensorEventListener{
    private Sensor mySensor;
    private SensorManager sensorM;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 5000;
    private ProgressBar progressBar;
    int progress;
    private TextView progressText;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_shake);
        sensorM = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        TextView currentTime = findViewById(R.id.current_time);
        currentTime.setText(getIntent().getStringExtra("time"));
        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
            if (speed > SHAKE_THRESHOLD) {
                vibrator.vibrate(300);
                if(progress<100){ //kan annars gå över 100 konstigt nog
                    progress +=10;
                }

                progressBar.setProgress(progress);
                progressText.setText(progress + "%");
            }
            if(progress>90){
                Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
                serviceIntent.putExtra("extra", "alarm_off");
                this.startService(serviceIntent);
                Intent returnIntent = new Intent(this, MainActivity.class);
                this.startActivity(returnIntent);

            }

            last_x = x;
            last_y = y;
            last_z = z;
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorM.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }
}


