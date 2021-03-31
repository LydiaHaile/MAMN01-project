package com.example.nosnooze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class AlarmAdapter extends ArrayAdapter<String> {

    Context context;
    String[] alarms;

    AlarmAdapter(Context context, String[] alarms) {
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
        alarmTime.setText(alarms[position]);
        return row;
    }
}