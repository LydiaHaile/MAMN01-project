package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Vibrator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.widget.Toast;


public class Lock extends AppCompatActivity implements SensorEventListener {

    private ImageView image;
    private TextView header;
    private TextView combinationView;

    private TextView test1;
    private TextView test2;
    private TextView test3;

    private int[] combination = new int[3];
    private boolean[] foundCombination = new boolean[3];
    private int X;
    private int counter = 0;
    private long time = 500;
    private TimeUnit time1 = TimeUnit.SECONDS;

    private float currentDegree = 0f;

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        TextView currentTime = findViewById(R.id.current_time);
        currentTime.setText(getIntent().getStringExtra("time"));

        image = (ImageView) findViewById(R.id.image);
        header = (TextView) findViewById(R.id.header);
        combinationView = (TextView) findViewById(R.id.combinationView);

        test1 = (TextView) findViewById(R.id.aCombination1);
        test2 = (TextView) findViewById(R.id.aCombination2);
        test3 = (TextView) findViewById(R.id.aCombination3);

        //header.setText("To turn off the alarm, unlock the lock by " +
                //"turning your phone and find the combination");

        X = 0;

        //RANDOMIZE COMBINATION AND SAVE IN THE ARRAY COMBINATION
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            combination[i] = rand.nextInt(100);
        }

        for (int i = 0; i < 3; i++) {
            foundCombination[i] = false;
        }

        //DISPLAY COMBINATIONS IN VIEW
        combinationView.setText("First number in combination: " + combination[0]);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

        //header.setText("Currently at: " + Math.round(degree/3.6));
        Toast.makeText(this, "" + X, Toast.LENGTH_SHORT).show();

        if (Math.round(degree / 3.6) == combination[X]) {
            feedback();
            foundCombination[X] = true;
            X++;

            if (X < 3) {
                combinationView.setText("Next number in combination: " + combination[X]);
            } else {
                combinationView.setText(" ");
            }
        }

        if (X == 3) {
            Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
            serviceIntent.putExtra("extra", "alarm_off");
            this.startService(serviceIntent);
           /* try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            try {
                // Calling the sleep method on the object of TimeUnit Class
                time1.sleep(time);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception Caught" + e);
            }*/
            Intent returnIntent = new Intent(this, MainActivity.class);
            this.startActivity(returnIntent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used
    }

    private void feedback() {
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }

        if(X == 0) {
            test1.setTextColor(Color.GREEN);
            test1.setText("" + combination[0]);
        } else if (X == 1) {
            test2.setTextColor(Color.GREEN);
            test2.setText("" + combination[1]);
        } else {
            test3.setTextColor(Color.GREEN);
            test3.setText("" + combination[2]);

        }

    }
}
