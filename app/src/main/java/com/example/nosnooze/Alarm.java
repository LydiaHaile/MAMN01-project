package com.example.nosnooze;

import android.widget.Switch;

public class Alarm {

    private final String SHAKE_PHONE = " Disable alarm by shaking your phone";
    private final String MATCH_NOTE = " Disable alarm by matching the note";
    private final String TAKE_STEPS = " Disable alarm by taking 10 steps";

    private boolean active = true;
    private String time;
    private int id, method;

    public Alarm(String time, int id, int method) {
        this.time = time;
        this.id = id;
        this.method = method;
    }

    public String getTime() {
        return this.time;
    }

    public int getId() {
        return this.id;
    }

    public String getMethod() {
        if (this.method == 1) {
            return SHAKE_PHONE;
        } else if (this.method == 2) {
            return MATCH_NOTE;
        } else {
            return TAKE_STEPS;
        }
    }

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
}
