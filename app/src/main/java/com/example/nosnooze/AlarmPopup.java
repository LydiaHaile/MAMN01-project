package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmPopup extends AppCompatActivity {
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_popup);
        TextView popupTime = findViewById(R.id.popupTime);
        popupTime.setText("Alarm: " +getIntent().getStringExtra("time"));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y =  -20;

        getWindow().setAttributes(params);
    }

    public void editAlarm(View view) {
        Intent intent = new Intent(this, AlarmEdit.class);
        intent.putExtra("alarmId", getIntent().getIntExtra("alarmId",0));
        intent.putExtra("position",  getIntent().getIntExtra("position",0));
        intent.putExtra("time", getIntent().getStringExtra("time"));
        startActivity(intent);
    }
}