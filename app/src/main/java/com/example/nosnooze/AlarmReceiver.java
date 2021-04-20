package com.example.nosnooze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver", "Yay!");

        String get_your_string = intent.getExtras().getString("extra");
        Log.e("key", get_your_string);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        serviceIntent.putExtra("extra", get_your_string);

        context.startService(serviceIntent);
    }
}
