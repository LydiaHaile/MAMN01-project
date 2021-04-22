package com.example.nosnooze;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;

class AlarmAdapter extends ArrayAdapter<Alarm> {

    Context context;
    ArrayList<Alarm> alarms;
    MainActivity ma = new MainActivity();

    AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, R.layout.row, R.id.alarm_time, alarms);
        this.context = context;
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView alarmTime = row.findViewById(R.id.alarm_time);
        alarmTime.setText(alarms.get(position).getTime());
        TextView disableMethod = row.findViewById(R.id.disable_method);
        disableMethod.setText(alarms.get(position).getMethod());
        SwitchCompat toggler = row.findViewById(R.id.toggle);
        toggler.toggle();
        toggler.setOnClickListener(v -> {
            if (toggler.isChecked()) {
                alarmTime.setTextColor(Color.WHITE);
                disableMethod.setTextColor(Color.WHITE);
                alarms.get(position).setActive(true);
            } else {
                alarmTime.setTextColor(Color.GRAY);
                disableMethod.setTextColor(Color.GRAY);
                alarms.get(position).setActive(false);
            }
        });

        row.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Alarm")
                    .setMessage("Do you really want to delete alarm for " + alarms.get(position).getTime())
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ma.removeAlarm(position);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
            return false;
        });
        /*
        Button button = row.findViewById(R.id.remove_alarm);
        button.setOnClickListener(v -> {

        });
        */
        return row;
    }
}