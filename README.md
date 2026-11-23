# Flexible AI Timetable Generator

Java 17 + Maven + Jakarta Servlet 6 + JSP application for Apache Tomcat 10.1.

## Run
1. Extract the ZIP.
2. Open the extracted folder in VS Code.
3. Open Terminal in VS Code and run: `mvn clean package`
4. Copy `target/AI_Timetable_Generator.war` to `C:\apache-tomcat-10.1\webapps\`
5. Start Tomcat and open `http://localhost:8080/AI_Timetable_Generator/`

## Input format
Days: comma-separated, for example `Monday, Tuesday, Wednesday`.
Periods: one teaching period per line. Any text label is accepted. Lines beginning with `BREAK:` are excluded from scheduling.
Subjects: one row per subject, with exactly six comma-separated values:
`Section, SubjectCode, SubjectName, Faculty, Room, WeeklyPeriods`

For one section, use the same section label in all subject rows. For multiple sections, use different section labels. Keep empty fields as empty comma positions, e.g. `A, LAB1, Data Lab, , Lab 2, 2`.

## AI techniques
- CSP: section, faculty and room clashes are prohibited.
- Backtracking: searches for a valid assignment.
- Local Search: performs validity-preserving slot swaps after the CSP solution.

## Current version notes
This is a flexible starter version. It supports custom days, arbitrary period labels/times, breaks, multiple sections, faculty and room clash checks, and weekly subject loads. Advanced optional controls (teacher unavailable periods, fixed/locked slots, preferred slots and double-period labs) are not yet implemented in this version.
