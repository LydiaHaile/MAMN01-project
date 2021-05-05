package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private TextView progressText;
    private ProgressBar progressBar;
    private Sensor stepCounterSensor;
    private SensorManager sensorManager;
    private int steps = -1;
    int progress = -10;
    private double MagnitudePrevious = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        progressText = findViewById(R.id.progress_text);
        progressBar = findViewById(R.id.progress_bar);
        String id = getIntent().getStringExtra("id");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x_acceleration = event.values[0];
        float y_acceleration = event.values[1];
        float z_acceleration = event.values[2];
        double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
        double MagnitudeDelta = Magnitude - MagnitudePrevious;
        MagnitudePrevious = Magnitude;

        if (MagnitudeDelta > 1) {
            steps++;
            progress += 10;
            progressText.setText(steps + "/10");
            progressBar.setProgress(progress);
            //MAYBE PLAY A 'DING'
            if(steps >= 10){
                //Stop playing music
                Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
                serviceIntent.putExtra("extra", "alarm_off");
                this.startService(serviceIntent);
                //Return to home page
                Intent returnIntent = new Intent(this, MainActivity.class);
                this.startActivity(returnIntent);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, stepCounterSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

}