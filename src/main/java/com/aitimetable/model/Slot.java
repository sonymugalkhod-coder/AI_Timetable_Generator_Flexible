package com.aitimetable.model;

public class Slot {
    public final String day, time;
    public Slot(String day, String time) { this.day=day; this.time=time; }
    public String key() { return day + "|" + time; }
}
