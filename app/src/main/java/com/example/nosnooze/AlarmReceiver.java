package com.example.nosnooze;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String get_your_string = intent.getExtras().getString("extra");
        int get_your_interaction = intent.getExtras().getInt("interaction");

        //Start RingTonePlayingService for sound
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", get_your_string);
        context.startService(serviceIntent);

        //Start Service based off alarm type
        startAlarmActivity(context, get_your_interaction);
    }


    private void startAlarmActivity(Context context, int interaction) {
        Intent alarmActivity = new Intent(context, PhoneTilt.class);
        if (interaction == 1) {
            // TODO implement PhoneSkake class
            alarmActivity = new Intent(context, BackgroundService.class);
        } else if (interaction == 2) {
            // TODO implement MatchNote class
            alarmActivity = new Intent(context, BackgroundService.class);
        } else if (interaction == 3) {
            // TODO implement TakeSteps class
            alarmActivity = new Intent(context, BackgroundService.class);
        } else {
            alarmActivity = new Intent(context, PhoneTilt.class);
        }
        alarmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmActivity);
    }
}
