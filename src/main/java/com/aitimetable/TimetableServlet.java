package com.aitimetable;

import com.aitimetable.ai.TimetableScheduler;
import com.aitimetable.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/generate")
public class TimetableServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String daysText=req.getParameter("days");
            String periodsText=req.getParameter("periods");
            String subjectText=req.getParameter("subjects");
            List<String> days=Arrays.stream(daysText.split(",")).map(String::trim).filter(s->!s.isEmpty()).toList();
            List<Slot> slots=new ArrayList<>();
            for (String line: periodsText.split("\\R")) {
                String t=line.trim();
                if (t.isEmpty()) continue;
                if (t.toUpperCase().startsWith("BREAK:")) continue;
                slots.add(new Slot("",t));
            }
            List<Slot> daySlots=new ArrayList<>();
            for (String day:days) for (Slot p:slots) daySlots.add(new Slot(day,p.time));
            List<Subject> subjects=new ArrayList<>();
            for (String line:subjectText.split("\\R")) {
                if (line.trim().isEmpty()) continue;
                String[] p=line.split(",",-1);
                if (p.length<6) throw new IllegalArgumentException("Each subject row must have 6 comma-separated values: Section, Code, Name, Faculty, Room, WeeklyPeriods.");
                subjects.add(new Subject(p[0].trim(),p[1].trim(),p[2].trim(),p[3].trim(),p[4].trim(),Integer.parseInt(p[5].trim())));
            }
            if (days.isEmpty() || daySlots.isEmpty() || subjects.isEmpty()) throw new IllegalArgumentException("Enter working days, teaching periods and at least one subject.");
            List<Meeting> result=new TimetableScheduler(subjects,daySlots).generate();
            req.setAttribute("meetings",result);
            req.setAttribute("days",days);
            req.setAttribute("periods",slots);
            req.setAttribute("title", Optional.ofNullable(req.getParameter("title")).orElse("My Timetable"));
            req.getRequestDispatcher("/result.jsp").forward(req,resp);
        } catch (Exception e) {
            req.setAttribute("error",e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req,resp);
        }
    }
}
