package com.sahil4.quotesapp.models;

import java.util.Calendar;

public class MyPreference {
    final long _id;
    final String title;
    Boolean isChecked;

    public MyPreference(String title, Boolean isChecked) {
        this._id = Calendar.getInstance().getTimeInMillis();
        this.title = title;
        this.isChecked = isChecked;
    }

    public String getTitle() {
        return this.title;
    }

    public Boolean getChecked() {
        return this.isChecked;
    }

    public long get_id() {
        return this._id;
    }

    public void setChecked(Boolean checked) {
        this.isChecked = checked;
    }
}
