package com.example.swith.Friend;

import android.widget.NumberPicker;

public class SetNumberPicker {
    private NumberPicker picker1, picker2, picker3;
    private int hour, min, sec;

    public SetNumberPicker(NumberPicker picker1, NumberPicker picker2, NumberPicker picker3, int hour, int min, int sec) {
        this.picker1 = picker1;
        this.picker2 = picker2;
        this.picker3 = picker3;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public void setting(int maxHour, int maxMin, int maxSec) {
        picker1.setMaxValue(maxHour);
        picker2.setMaxValue(maxMin);
        picker3.setMaxValue(maxSec);

        picker1.setMinValue(0);
        picker2.setMinValue(0);
        picker3.setMinValue(0);

        picker1.setValue(hour);
        picker2.setValue(min);
        picker3.setValue(sec);

        picker1.setWrapSelectorWheel(true);
        picker2.setWrapSelectorWheel(true);
        picker3.setWrapSelectorWheel(true);
    }

    public long getTotalMillis() {
        return (picker1.getValue() * 3600L + picker2.getValue() * 60L + picker3.getValue()) * 1000L;
    }
}
