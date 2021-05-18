package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Arrays;

public class SortNumbers extends AppCompatActivity {

    private ArrayList<Integer> numbers = new ArrayList<Integer>();
    private boolean[] isClicked = new boolean[] {false, false, false, false, false};
    private ArrayList<Integer> clickedNumbers = new ArrayList<Integer>();
    private int counter = 0;
    private int index;

    //TEXT-VIEWS
    private Button first;
    private Button second;
    private Button third;
    private Button fourth;
    private Button fifth;

    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_numbers);
        TextView currentTime = findViewById(R.id.current_time);
        currentTime.setText(getIntent().getStringExtra("time"));

        //FIND TEXT-VIEW BY ID
        first = (Button)findViewById(R.id.firstNumber);
        second = (Button)findViewById(R.id.secondNumber);
        third = (Button)findViewById(R.id.thirdNumber);
        fourth = (Button)findViewById(R.id.fourthNumber);
        fifth = (Button)findViewById(R.id.fifthNumber);

        test  = (TextView)findViewById(R.id.test);

        //RANDOMIZE FIVE NUMBERS - PLACE IN ARRAY-LIST NUMBERS (ALSO MAKES SURE THERE ARE NO DUPLICATES)
        Random rand = new Random();
        for(int i = 0; i < 5; i++) {
            numbers.add(rand.nextInt(100 + 100) - 100);
        }

        //SET TEXT-VIEW TO THE RANDOMIZED NUMBERS IN THE ARRAY-LIST NUMBERS
        first.setText("" + numbers.get(0));
        second.setText("" + numbers.get(1));
        third.setText("" + numbers.get(2));
        fourth.setText("" + numbers.get(3));
        fifth.setText("" + numbers.get(4));

        //SORT THE ARRAY-LIST NUMBERS (ONLY FOR TESTING)
        Collections.sort(numbers);
        test.setText("In order : " + numbers.get(0) + " " + numbers.get(1) + " " + numbers.get(2) + " " + numbers.get(3) + " " + numbers.get(4));
    }

    public void onClick(View view) {
        Button button;

        //FIND BUTTON THAT USER CLICKS ON AND SAVES MATCH INDEX FOR IS_CLICKED ARRAY IN CHECK_CLICKS-METHOD
        if(counter <= 4) {
            if(view.getId() == R.id.firstNumber){
                 button = first;
                 index = 0;
            } else if (view.getId() == R.id.secondNumber) {
                 button = second;
                index = 1;
            } else if (view.getId() == R.id.thirdNumber) {
                 button = third;
                index = 2;
            } else if (view.getId() == R.id.fourthNumber) {
                 button = fourth;
                index = 3;
            } else {
                 button = fifth;
                index = 4;
            }

            //FOR EACH CLICK: TEXT-COLOR BUTTON TURNS GREEN, COUNTER GOES UP, TEXT ON BUTTON IS ADDED TO THE ARRAY CLICKED_NUMBERS
            //AFTER 4 CLICKS, ONE FOR EACH BUTTON, HELP-METHOD IS CALLED (CASE 4)
            switch (counter) {
                case 0:
                case 1:
                case 2:
                case 3:
                    checkClicks(button);
                    break;
                case 4:
                    if(checkClicks(button) == 5) {
                        shutOffOrReset();
                    }
                    break;
            }
         }
    }

    //HELP-METHOD: IF BUTTON HAS NOT BEEN CLICKED: REGISTER CLICK + SAVES VALUE OF BUTTON
                // OTHERWISE CLICK IS UNREGISTERED AND VALUES DELETED.
                // ALSO HANDLED COLOR CHANGES
    private int checkClicks(Button button) {
        if(isClicked[index]) {
            isClicked[index] = false;
            button.setTextColor(Color.BLACK);
            clickedNumbers.remove(new Integer(Integer.parseInt(button.getText().toString())));
                if(counter > 0) {
                    counter--;
                }
            return counter;
        } else {
            clickedNumbers.add(Integer.parseInt(button.getText().toString()));
            button.setTextColor(Color.GREEN);
            isClicked[index] = true;
            counter++;
            return counter;
        }
    }

    //HELP-METHOD: EITHER SHUTS OFF ALARM OR RESETS EVERYTHING (MESSAGE TO USER + VIBRATION)
    private void shutOffOrReset() {
        if(numbers.equals(clickedNumbers)) {
            Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
            serviceIntent.putExtra("extra", "alarm_off");
            this.startService(serviceIntent);
            Intent returnIntent = new Intent(this, MainActivity.class);
            this.startActivity(returnIntent);
        } else {
            first.setTextColor(Color.BLACK);
            second.setTextColor(Color.BLACK);
            third.setTextColor(Color.BLACK);
            fourth.setTextColor(Color.BLACK);
            fifth.setTextColor(Color.BLACK);

            isClicked = new boolean[] {false, false, false, false, false};
            clickedNumbers = new ArrayList<Integer>();
            counter = 0;
            vibrate();
            Toast.makeText(this, "Not the correct order. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    //HELP-METHOD: VIBRATES PHONE
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(500);
        }

    }
}