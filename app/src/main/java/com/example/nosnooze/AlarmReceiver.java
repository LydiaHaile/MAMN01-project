package com.example.nosnooze;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.nio.channels.Channel;
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
        Alarm alarm = mainActivity.getAlarm(time);

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
            createNotification(methodIntent, context);
        } else if (get_your_interaction == 2) {
            //TAKE STEPS
            Intent methodIntent = new Intent(context, StepCounter.class);
            methodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            methodIntent.putExtra("time", alarm.getTime());
            context.startActivity(methodIntent);
            createNotification(methodIntent, context);
        } else if (get_your_interaction == 3) {
            Intent methodIntent = new Intent(context, PhoneShake.class);
            methodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            methodIntent.putExtra("time", alarm.getTime());
            context.startActivity(methodIntent);
            createNotification(methodIntent, context);
        } else if (get_your_interaction == 4) {
            Intent methodIntent = new Intent(context, SortNumbers.class);
            methodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            methodIntent.putExtra("time", alarm.getTime());
            context.startActivity(methodIntent);
            createNotification(methodIntent, context);
        } else if (get_your_interaction == 5) {
            Intent methodIntent = new Intent(context, Lock.class);
            methodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            methodIntent.putExtra("time", alarm.getTime());
            context.startActivity(methodIntent);
            createNotification(methodIntent, context);
        } else  {
            //NOTHING HAPPENS
        }
    }
/*
    public void sendNotification(Intent intent, Alarm alarm, Context context) { //används ej?
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelID")
                .setSmallIcon(R.drawable.circle)
                .setContentTitle("1")
                .setContentText("2")
                .setPriority(NotificationCompat.PRIORITY_MAX);
        //.setContentIntent(pendingIntent)
        //.setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManagerCompat.from(context).notify(0, notification);
    }
*/
    private void createNotification(Intent intent, Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("alarm", "alarm", NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm");
        //Intent notificationIntent = new Intent(context.getApplicationContext(), AlarmSetter.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,0, intent,0);
        builder.setContentTitle("Wake up!")
                .setContentText("Click here to turn off alarm!")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(notificationPendingIntent)
                .setPriority(Notification.PRIORITY_LOW)
                .setAutoCancel(true);

        NotificationManagerCompat manageCompat = NotificationManagerCompat.from(context);
        manageCompat.notify(1, builder.build());
    }
}