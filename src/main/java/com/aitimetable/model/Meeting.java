package com.aitimetable.model;

public class Meeting {
    public final Subject subject;
    public Slot slot;
    public Meeting(Subject subject) { this.subject=subject; }
}
