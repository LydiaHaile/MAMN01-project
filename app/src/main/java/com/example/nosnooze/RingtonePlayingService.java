package com.example.nosnooze;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RingtonePlayingService extends Service {

    private static final String CHANNEL_ID = "personal_notification";
    private static final int NOTIFICATION_ID = 001;
    MediaPlayer mediaSong;
    int startId;
    boolean isRunning;
    Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};

        String state = intent.getExtras().getString("extra");
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1) {
            mediaSong = MediaPlayer.create(this, R.raw.alarm);
            mediaSong.start();
            this.isRunning= true;
            this.startId = 0;

        } else if (this.isRunning && startId == 0){
            mediaSong.stop();
            mediaSong.reset();
            this.isRunning = false;
            this.startId = 0;
        } else if (!this.isRunning && startId == 0){
            this.isRunning = false;
            this.startId = 0;
        } else if(this.isRunning && startId == 1){
            this.isRunning = true;
            this.startId = 1;
        }
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "on destroy called", Toast.LENGTH_SHORT).show();
    }
}