package com.example.nosnooze;

import android.widget.Switch;

import java.io.Serializable;
import java.util.Calendar;

public class Alarm implements Serializable {

    private final String PHOTO_SCAN = " Disable alarm by matching color in photo";
    private final String TAKE_STEPS = " Disable alarm by taking 10 steps";
    private final String SHAKE_PHONE = " Disable alarm by shaking your phone";
    private final String SORT_NUMBERS = " Disable alarm by sorting numbers";
    private final String TILT_PHONE = " Disable alarm by tilting your phone";
    private final String LOCK = " Disable phone by unlocking the lock";

    private Calendar calendar;
    private boolean active = true;
    private String time;
    private int id, method;

    public Alarm(String time, int id, int method, Calendar calendar) {
        this.time = time;
        this.id = id;
        this.method = method;
        this.calendar = calendar;
    }

    public String getTime() {
        return this.time;
    }

    public int getId() {
        return this.id;
    }

    public String getMethod() {
        if (this.method == 1) {
            return PHOTO_SCAN;
        } else if (this.method == 2) {
            return TAKE_STEPS;
        } else if (this.method == 3) {
            return SHAKE_PHONE;
        } else if (this.method == 4){
            return SORT_NUMBERS;
        } else if (this.method == 5) {
            return LOCK;
        } else {
            return "hej";
        }
    }

    public Calendar getCalendar() { return this.calendar; }

    public boolean getActive() { return this.active; }

    public void setId(int id) {
        this.id = id;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setActive(Boolean active) { this.active = active; }

    public void setCalendar(Calendar calendar) { this.calendar = calendar; }
}
