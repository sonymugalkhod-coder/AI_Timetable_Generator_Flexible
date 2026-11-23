package com.aitimetable.model;

public class Subject {
    public String section, code, name, faculty, room;
    public int weeklyPeriods;
    public Subject(String section, String code, String name, String faculty, String room, int weeklyPeriods) {
        this.section=section; this.code=code; this.name=name; this.faculty=faculty; this.room=room; this.weeklyPeriods=weeklyPeriods;
    }
}
