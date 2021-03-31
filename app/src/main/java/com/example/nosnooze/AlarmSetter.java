package com.example.nosnooze;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmSetter extends AppCompatActivity {

    TextView timePicker, timeShow;
    int tpHour, tpMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setter);
        timePicker = findViewById(R.id.time_picker);
        timeShow = findViewById(R.id.show_time);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmSetter.this, android.R.style.Theme_Material_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        //CREATE ALARM!!

                        tpHour = hourOfDay;
                        tpMinute = minute;
                        String time = hourOfDay + ":" + minute;
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = formatter.parse(time);
                            SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm");
                            timeShow.setText(formatter2.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, 12, 0, false);
                //timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(tpHour, tpMinute);
                timePickerDialog.show();
            }
        });
    }
}