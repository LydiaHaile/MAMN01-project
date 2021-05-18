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

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private TextView progressText;
    private ProgressBar progressBar;
    private Sensor stepCounterSensor;
    private SensorManager sensorManager;
    private int steps = 0;
    private int progress = 0;
    private double MagnitudePrevious = 0;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        progressText = findViewById(R.id.progress_text);
        progressBar = findViewById(R.id.progress_bar);
        TextView currentTime = findViewById(R.id.current_time);
        currentTime.setText(getIntent().getStringExtra("time"));
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x_acceleration = event.values[0];
        float y_acceleration = event.values[1];
        float z_acceleration = event.values[2];
        double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
        double MagnitudeDelta = Magnitude - MagnitudePrevious;
        MagnitudePrevious = Magnitude;

        if (MagnitudeDelta > 5) {
            steps++;
            progress += 10;
            progressText.setText(steps + "/10");
            progressBar.setProgress(progress);
            vibrator.vibrate(300);
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