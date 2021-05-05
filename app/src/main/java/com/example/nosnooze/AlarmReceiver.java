package com.example.nosnooze;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Find what alarm is going off
        MainActivity mainActivity = new MainActivity();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(calendar.getTime());
        int alarmId = mainActivity.getAlarm(time).getId();

        String get_your_string = intent.getExtras().getString("extra");
        int get_your_interaction = intent.getExtras().getInt("interaction");

        //Start RingTonePlayingService for sound
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", get_your_string);
        context.startService(serviceIntent);

        if (get_your_interaction == 1) {
            //PHOTO SCAN
            Intent methodIntent = new Intent(context, PhotoScan.class);
            methodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(methodIntent);
        } else if (get_your_interaction == 2) {
            //MATCH NOTE
            Intent methodIntent = new Intent(context, BackgroundService.class);
            methodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(methodIntent);
        } else if (get_your_interaction == 3) {
            //TAKE STEPS
            Intent methodIntent = new Intent(context, StepCounter.class);
            methodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(methodIntent);
        } else {
            //NOTHING HAPPENS
        }
    }
}
