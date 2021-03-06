package com.example.nosnooze;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.Calendar;

class AlarmAdapter extends ArrayAdapter<Alarm> {

    Context context;
    ArrayList<Alarm> alarms;
    MainActivity mainActivity = new MainActivity();

    AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, R.layout.row, R.id.alarm_time, alarms);
        this.context = context;
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Alarm alarm = alarms.get(position);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView alarmTime = row.findViewById(R.id.alarm_time);
        alarmTime.setText(alarms.get(position).getTime());
        TextView disableMethod = row.findViewById(R.id.disable_method);
        disableMethod.setText(alarms.get(position).getMethod());
        SwitchCompat toggler = row.findViewById(R.id.toggle);
        ImageButton imageButton = row.findViewById(R.id.imageButton);
        toggler.setChecked(alarms.get(position).getActive());
        if (toggler.isChecked()) {
            alarmTime.setTextColor(context.getResources().getColor(R.color.himmelsklila));
            disableMethod.setTextColor(context.getResources().getColor(R.color.himmelsklila));
        } else {
            alarmTime.setTextColor(Color.GRAY);
            disableMethod.setTextColor(Color.GRAY);
        }
        toggler.setOnClickListener(v -> {
            if (toggler.isChecked()) {
                alarmTime.setTextColor(context.getResources().getColor(R.color.himmelsklila));
                disableMethod.setTextColor(context.getResources().getColor(R.color.himmelsklila));
                alarm.setActive(true);
                enableAlarm(alarm);
            } else {
                alarmTime.setTextColor(Color.GRAY);
                disableMethod.setTextColor(Color.GRAY);
                alarm.setActive(false);
                disableAlarm(alarm.getId(), alarm.getTime());
            }
        });
        row.setOnClickListener(v -> {
            Intent edit = new Intent(context, AlarmPopup.class);
            edit.putExtra("alarmId", alarm.getId());
            edit.putExtra("position", position);
            edit.putExtra("time", alarm.getTime());
            context.startActivity(edit);

        });
        imageButton.setOnClickListener(v -> {
            removeAlarmPopup(alarm, position);
        });
        return row;
    }

    public void disableAlarm(int alarmId, String alarmTime) { //gjorde om som man inte skickar ett alarm som parameter. enklare f??r att edita alarm. lite fult kanske.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Disabled alarm " + alarmTime, Toast.LENGTH_SHORT).show();
    }

    public void enableAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("extra", "alarm on");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        if (alarm.getCalendar().before(Calendar.getInstance())) {
            alarm.getCalendar().add(Calendar.DATE, 1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), pendingIntent);
        Intent gotoIntent = new Intent(context, MainActivity.class);
        gotoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(gotoIntent);
        Toast.makeText(context, "Enabled alarm " + alarm.getTime(), Toast.LENGTH_SHORT).show();
    }

    private void removeAlarmPopup(Alarm alarm, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Alarm")
                .setMessage("Do you really want to delete alarm for " + alarm.getTime())
                .setPositiveButton((Html.fromHtml("<font color='#333f4f'>yes</font>")), (dialog, whichButton) -> {
                    mainActivity.removeAlarm(position);
                    disableAlarm(alarm.getId(), alarm.getTime());
                } )
                .setNegativeButton((Html.fromHtml("<font color='#333f4f'>no</font>")), null).show();
    }

}