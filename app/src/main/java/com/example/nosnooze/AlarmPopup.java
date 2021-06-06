package com.example.nosnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlarmPopup extends AppCompatActivity {
    private int alarmId, alarmPos;
    private String alarmTime;
    private Button deleteButton;

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

        getWindow().setLayout((int)(width*0.8), (int)(height*0.3));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y =  -20;
        getWindow().setAttributes(params);


        alarmId = getIntent().getIntExtra("alarmId",0);
        alarmPos = getIntent().getIntExtra("position",0);
        alarmTime = getIntent().getStringExtra("time");
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm(v);
                finish();
            }
        });
    }

    public void editAlarm(View view) {
        Intent intent = new Intent(this, AlarmEdit.class);
        intent.putExtra("alarmId", alarmId);
        intent.putExtra("position", alarmPos);
        intent.putExtra("time", alarmTime);
        startActivity(intent);
    }

    public void deleteAlarm(View view){
        Log.d("values ", alarmId + " " + alarmPos + " " + alarmTime);
        MainActivity.removeAlarm(alarmPos);
        MainActivity.getAlarmAdapter().disableAlarm(alarmId, alarmTime);
    }
}