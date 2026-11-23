package com.aitimetable.ai;

import com.aitimetable.model.*;
import java.util.*;

public class TimetableScheduler {
    private final List<Subject> subjects;
    private final List<Slot> slots;
    private final List<Meeting> meetings = new ArrayList<>();
    private final Map<String, Meeting> occupied = new HashMap<>();
    private int nodes = 0;

    public TimetableScheduler(List<Subject> subjects, List<Slot> slots) {
        this.subjects=subjects; this.slots=slots;
        for (Subject s: subjects) for (int i=0;i<s.weeklyPeriods;i++) meetings.add(new Meeting(s));
        // Most constrained tasks first: higher weekly load first.
        meetings.sort((a,b)->Integer.compare(b.subject.weeklyPeriods,a.subject.weeklyPeriods));
    }

    public List<Meeting> generate() {
        if (slots.isEmpty()) throw new IllegalArgumentException("Add at least one teaching period.");
        if (meetings.size()>slots.size()*Math.max(1, distinctSections())) {
            throw new IllegalArgumentException("Required weekly periods exceed available section periods.");
        }
        if (!backtrack(0)) throw new IllegalArgumentException("No timetable satisfies all constraints. Add more periods or review faculty/room conflicts.");
        localSearchImprove();
        return meetings;
    }

    private int distinctSections() {
        return (int)subjects.stream().map(s->s.section).distinct().count();
    }

    // CSP + backtracking: each assignment must satisfy section, faculty and room constraints.
    private boolean backtrack(int index) {
        if (index==meetings.size()) return true;
        Meeting m=meetings.get(index);
        List<Slot> candidates=new ArrayList<>(slots);
        // Shuffle equal choices slightly for variety, then prefer slots with fewer clashes.
        Collections.shuffle(candidates, new Random(index+17));
        for (Slot slot:candidates) {
            nodes++;
            if (valid(m,slot)) {
                m.slot=slot; occupied.put(occupancyKey(m.subject,slot),m);
                if (backtrack(index+1)) return true;
                occupied.remove(occupancyKey(m.subject,slot)); m.slot=null;
            }
        }
        return false;
    }

    private String occupancyKey(Subject s, Slot slot) { return s.section+"|"+slot.key(); }

    private boolean valid(Meeting m, Slot slot) {
        Subject s=m.subject;
        for (Meeting other: meetings) {
            if (other.slot==null || other==m || !other.slot.key().equals(slot.key())) continue;
            Subject o=other.subject;
            if (s.section.equalsIgnoreCase(o.section)) return false;
            if (!s.faculty.isBlank() && s.faculty.equalsIgnoreCase(o.faculty)) return false;
            if (!s.room.isBlank() && s.room.equalsIgnoreCase(o.room)) return false;
        }
        // Avoid scheduling the same subject twice on the same day by default.
        for (Meeting other: meetings) {
            if (other.slot!=null && other!=m && other.subject==s && other.slot.day.equals(slot.day)) return false;
        }
        return true;
    }

    // Local search: improve distribution by swapping assignments where constraints remain valid.
    private void localSearchImprove() {
        Random random=new Random(42);
        for (int iteration=0; iteration<Math.min(500, meetings.size()*20); iteration++) {
            if (meetings.size()<2) return;
            Meeting a=meetings.get(random.nextInt(meetings.size()));
            Meeting b=meetings.get(random.nextInt(meetings.size()));
            if (a==b) continue;
            Slot oldA=a.slot, oldB=b.slot;
            a.slot=oldB; b.slot=oldA;
            if (!allValid()) { a.slot=oldA; b.slot=oldB; }
        }
    }

    private boolean allValid() {
        Set<String> section=new HashSet<>(), faculty=new HashSet<>(), room=new HashSet<>(), subjectDay=new HashSet<>();
        for (Meeting m: meetings) {
            Subject s=m.subject; String k=m.slot.key();
            if (!section.add(s.section+"|"+k)) return false;
            if (!s.faculty.isBlank() && !faculty.add(s.faculty+"|"+k)) return false;
            if (!s.room.isBlank() && !room.add(s.room+"|"+k)) return false;
            if (!subjectDay.add(System.identityHashCode(s)+"|"+m.slot.day)) return false;
        }
        return true;
    }
}
