package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Arrays;

public class SortNumbers extends AppCompatActivity {

    private int[] numbers = new int[5];
    private int[] clickedNumbers = new int[5];
    private int counter = 0;

    //TEXT-VIEWS
    private TextView title;
    private Button first;
    private Button second;
    private Button third;
    private Button fourth;
    private Button fifth;

    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_numbers);

        //FIND TEXT-VIEW BY ID
        title  =(TextView)findViewById(R.id.title);
        first = (Button)findViewById(R.id.firstNumber);
        second = (Button)findViewById(R.id.secondNumber);
        third = (Button)findViewById(R.id.thirdNumber);
        fourth = (Button)findViewById(R.id.fourthNumber);
        fifth = (Button)findViewById(R.id.fifthNumber);

        test  = (TextView)findViewById(R.id.test);

        Random rand = new Random();

        //RANDOMIZE FIVE NUMBERS - PLACE IN ARRAY NUMBERS
        for(int i = 0; i < 5; i++) {
            numbers[i] = rand.nextInt(100);
        }

        //SET TEXT-VIEW TO THE RANDOMIZED NUMBERS IN THE ARRAY NUMBERS
        first.setText("" + numbers[0]);
        second.setText("" + numbers[1]);
        third.setText("" + numbers[2]);
        fourth.setText("" + numbers[3]);
        fifth.setText("" + numbers[4]);

        //SORT THE ARRAY NUMBERS
        Arrays.sort(numbers);
        test.setText("In order : " + numbers[0] + " " +numbers[1] + " " +numbers[2] + " " + numbers[3] + " " + numbers[4]);
    }

    public void onClick(View view) {
        Button button;

        //FIND BUTTON THAT USER CLICKS ON
        if(counter <= 4) {
            if(view.getId() == R.id.firstNumber){
                 button = first;
            } else if (view.getId() == R.id.secondNumber) {
                 button = second;
            } else if (view.getId() == R.id.thirdNumber) {
                 button = third;
            } else if (view.getId() == R.id.fourthNumber) {
                 button = fourth;
            } else {
                 button = fifth;
            }

            //FOR EACH CLICK: TEXT-COLOR BUTTON TURNS GREEN, COUNTER GOES UP, TEXT ON BUTTON IS ADDED TO THE ARRAY CLICKED_NUMBERS
            //AFTER 4 CLICKS, ONE FOR EACH BUTTON, HELP-METHOD IS CALLED (CASE 4)
            switch (counter) {
                case 0:
                    button.setTextColor(Color.GREEN);
                    clickedNumbers[0] = Integer.parseInt(button.getText().toString());
                    counter++;
                    break;
                case 1:
                    button.setTextColor(Color.GREEN);
                    clickedNumbers[1] = Integer.parseInt(button.getText().toString());
                    counter++;
                    break;
                case 2:
                    button.setTextColor(Color.GREEN);
                    clickedNumbers[2] = Integer.parseInt(button.getText().toString());
                    counter++;
                    break;
                case 3:
                    button.setTextColor(Color.GREEN);
                    clickedNumbers[3] = Integer.parseInt(button.getText().toString());
                    counter++;
                    break;
                case 4:
                    button.setTextColor(Color.GREEN);
                    clickedNumbers[4] = Integer.parseInt(button.getText().toString());
                    shutOffOrReset();
                    break;
            }
         }
    }

    private void shutOffOrReset() {
        int comparison = 0;

        //ARRAYS CLICKED_NUMBERS AND NUMBERS ARE COMPARED, IF SAME INT COMPARISON == 5
        for(int k = 0; k < 5; k++) {
            if(numbers[k] == clickedNumbers[k]) {
                comparison++;
            }
        }

        //IF TRUE: ALARM IS SHUTOFF
        //IF FALSE: EVERYTHING IS RESET, PHONE VIBRATES.
        if(comparison == 5) {
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

            clickedNumbers = new int[clickedNumbers.length];
            counter = 0;
            vibrate();
            Toast.makeText(this, "Not the correct order. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(500);
        }

    }

}