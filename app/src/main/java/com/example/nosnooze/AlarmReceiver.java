package com.example.nosnooze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Come on, come on\n" +
                "I see no changes, wake up in the morning, and I ask myself\n" +
                "Is life worth living, should I blast myself?\n" +
                "I'm tired of bein' poor, and even worse I'm black\n" +
                "My stomach hurts, so I'm lookin' for a purse to snatch", Toast.LENGTH_SHORT).show();
        String get_your_string = intent.getExtras().getString("extra");
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", get_your_string);
        context.startService(serviceIntent);

        Intent accStart = new Intent(context, BackgroundService.class);
        context.startService(accStart);
    }
}
