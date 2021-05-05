package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmHandler extends AppCompatActivity {

    private MainActivity mainActivity;
    private String sdf_time;
    private TextView progress;

    // THIS CLASS IS OUTDATED BUT KEPT FOR REFERENCE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_handler);
        mainActivity = new MainActivity();

        TextView time = findViewById(R.id.alarm_time_handler);
        TextView method = findViewById(R.id.method_handler);
        progress = findViewById(R.id.progress_handler);

        setTime(time);
        setMethod(method);
    }

    private void setTime(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.sdf_time = sdf.format(calendar.getTime());
        String textString = "Alarm for " + this.sdf_time + " is going off!";
        textView.setText(textString);
    }

    private void setMethod(TextView textView) {
        textView.setText(mainActivity.getAlarm(sdf_time).getMethod());
    }

    public void setProgress(String update) {
        if (progress == null) {
            progress = findViewById(R.id.progress_handler);
        }
        Log.d("buggg", "onCreate: " + (progress == null));
    }
}