package com.example.nosnooze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String get_your_string = intent.getExtras().getString("extra");
        int get_your_interaction = intent.getExtras().getInt("interaction");
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", get_your_string);
        context.startService(serviceIntent);
        Intent ringtoneIntent = new Intent(context, RingtonePlayingService.class);
        ringtoneIntent.putExtra("extra", get_your_string);
        context.startService(ringtoneIntent);

        Intent accStart = new Intent(context, BackgroundService.class);
        accStart.putExtra("interaction", get_your_interaction);
        context.startService(accStart);
    }
}
