package com.sahil4.quotesapp.models;

import java.util.Calendar;

public class NotificationTime {
    private final long _id;
    private final int hour;
    private final int minutes;
    private final boolean isPM;

    public NotificationTime(int hour, int minutes) {
        this._id = Calendar.getInstance().getTimeInMillis();
        this.minutes = minutes;
        if (hour > 12) {
            this.hour = hour - 12;
            this.isPM = true;
        } else {
            this.hour = hour;
            this.isPM = false;
        }
    }

    public long get_id() {
        return this._id;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public boolean getPM() {
        return this.isPM;
    }

    public String getTime() {
        String time;
        if (this.getHour() < 10) {
            time = "0" + this.getHour();
        } else {
            time = String.valueOf(this.getHour());
        }

        if (this.getMinutes() < 10) {
            time += ":0" + this.getMinutes();
        } else {
            time += ":" + this.getMinutes();
        }

        time += this.isPM ? " PM" : " AM";

        return time;
    }
}
