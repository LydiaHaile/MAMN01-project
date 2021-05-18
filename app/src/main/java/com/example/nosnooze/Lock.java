package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.Random;


public class Lock extends AppCompatActivity implements SensorEventListener {

    private ImageView image;
    private TextView header;
    private TextView combinationView;

    private TextView test1;
    private TextView test2;
    private TextView test3;

    private int counter = 0;
    private int[] combination = new int[3];
    private boolean[] foundCombinations = new boolean[3];
    int X = 0;


    private float currentDegree = 0f;

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        image = (ImageView) findViewById(R.id.image);
        header = (TextView) findViewById(R.id.header);
        combinationView = (TextView) findViewById(R.id.combinationView);

        test1 = (TextView) findViewById(R.id.aCombination1);
        test2 = (TextView) findViewById(R.id.aCombination2);
        test3 = (TextView) findViewById(R.id.aCombination3);

        header.setText("To turn off the alarm, unlock the lock by " +
                "turning your phone and find the combination");

        //RANDOMIZE COMBINATION AND SAVE IN THE ARRAY COMBINATION
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            combination[i] = rand.nextInt(100);
        }

        for (int a = 0; a < 3; a++) {
            foundCombinations[a] = false;
        }

        //DISPLAY COMBINATIONS IN VIEW
        combinationView.setText("Number " + X+1 + "in combination: " + combination[0]);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //mediaPlayer = MediaPlayer.create(this, R.raw.sound); //FIND UNLOCKING SOUND
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

        header.setText("" + Math.round(degree/3.6));

        if(Math.round(degree/3.6) == combination[X]) {
            foundCombinations[X] = true;
            feedback();
            X++;

            if(X < 3) {
                combinationView.setText("Number " + (X+1) + "in combination: " + combination[X]);
            } else {
                combinationView.setText("");
            }
        }

        if(X == 3) {
            combinationView.setTextColor(Color.GREEN);
            Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
            serviceIntent.putExtra("extra", "alarm_off");
            this.startService(serviceIntent);
            Intent returnIntent = new Intent(this, MainActivity.class);
            this.startActivity(returnIntent);
        }

        //CHECK IF ANY COMBINATION IS FOUND. IF FOUND: PHONE VIBRATES AND FOUND COMBINATION IS ADDED IN ARRAY FOUND_COMBINATIONS;
        /*if (Math.round(degree/3.6) == combination[0]) {
            if(foundCombinations[0] == -1) {
                test1.setText("Hold phone still 3 sec.");


                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                if (Math.round(degree/3.6) == combination[0]) {
                                    test1.setText("" + Math.round(degree / 3.6));
                                    foundCombinations[0] = (int) Math.round(degree / 3.6);
                                    counter++;
                                    vibrate();
                                }
                            }
                        },
                        3000
                );
            }
        } else if (Math.round(degree/3.6) == combination[1]) {
            if(foundCombinations[1] == -1) {
                test2.setText("Hold phone still 3 sec.");

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                if (Math.round(degree/3.6) == combination[1]) {
                                    test2.setText("" + Math.round(degree / 3.6));
                                    foundCombinations[1] = (int)Math.round(degree/3.6);
                                    counter++;
                                    vibrate();
                                }
                            }
                        },
                        3000
                );
            }
        } else if (Math.round(degree/3.6) == combination[2]) {
            if(foundCombinations[2] == -1) {
                test3.setText("Hold phone still 3 sec.");

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                if (Math.round(degree/3.6) == combination[1]) {
                                    test3.setText("" + Math.round(degree/3.6) );
                                    foundCombinations[2] = (int) Math.round(degree / 3.6);
                                    counter++;
                                    vibrate();
                                }
                            }
                        },
                        3000
                );
            }
        }*/
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
    }
}
        /*
        private void changeColorBackground(String color) {
            if(color == "GREEN") {
                getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            } else if(color == "WHITE"){
                getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            }
        }
}

       try {
        int chosenHeading = Integer.parseInt(input.getText().toString());
        directionTitle.setText("Your direction: " + chosenHeading + "°");

        if(chosenHeading >= 0 && chosenHeading <= 360) {

            int lowerBound = (chosenHeading - 15);
            int higherBound = (chosenHeading + 15);

            if(degree > lowerBound && degree < higherBound){
                changeColorBackground("GREEN");
                if(degree == chosenHeading) {
                    launchVibrationAndSound();
                }
            } else {
                changeColorBackground("WHITE");
            }
        } else {
            heading.setText("You can only choose 0-365°");
            changeColorBackground("WHITE");
        }

    } catch (NumberFormatException e) {
        directionTitle.setText("Your direction: 360°");

        if(degree > 345 || degree < 15){
            changeColorBackground("GREEN");
            if(degree == 360) {
                launchVibrationAndSound();
            }
        } else {
            changeColorBackground("WHITE");
        }
    }*/