<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>AI Timetable Generator</title><link rel="stylesheet" href="style.css"></head>
<body><main class="wrap">
<header><div class="eyebrow">SMART ACADEMIC PLANNER</div><h1>AI Timetable Generator</h1><p>Create a timetable using your own days, timings, subjects and rules.</p></header>
<% if(request.getAttribute("error")!=null){ %><div class="error"><b>Could not generate:</b> <%=request.getAttribute("error")%></div><% } %>
<form action="generate" method="post">
<section class="panel"><h2>01 · Timetable details</h2>
<label>Timetable title<input name="title" placeholder="e.g. ISE 5th Semester Timetable" value="My Timetable"></label>
<label>Working days <small>Separate days with commas. Example: Monday, Tuesday, Wednesday, Thursday, Friday</small><input name="days" required value="Monday, Tuesday, Wednesday, Thursday, Friday"></label>
<label>Period timings <small>Enter one teaching period per line. Use any duration. Breaks can be written as BREAK: 10:00 AM - 10:30 AM and will be excluded.</small>
<textarea name="periods" rows="7" required>8:00 AM - 9:00 AM
9:00 AM - 10:00 AM
BREAK: 10:00 AM - 10:30 AM
10:30 AM - 11:30 AM
11:30 AM - 12:30 PM
BREAK: 12:30 PM - 1:30 PM
1:30 PM - 2:30 PM
2:30 PM - 3:30 PM</textarea></label>
</section>
<section class="panel"><h2>02 · Subjects and sections</h2>
<p class="hint">Enter one subject per line in this exact order: <b>Section, Subject Code, Subject Name, Faculty, Room, Weekly Periods</b></p>
<textarea name="subjects" rows="10" required> A, CS101, Programming, Dr. Meera, Room 101, 3
A, CS102, Mathematics, Prof. Ravi, Room 102, 4
A, CS103, Database Systems, Dr. Anita, Lab 1, 2
B, CS101, Programming, Dr. Meera, Room 201, 3
B, CS102, Mathematics, Prof. Ravi, Room 202, 4</textarea>
<p class="hint">For a single section, use the same section name in every row. For multiple sections, enter different section names. Leave Faculty or Room blank if not applicable, but keep the commas.</p>
</section>
<section class="panel"><h2>03 · Scheduling rules</h2>
<div class="rule"><span>Section clash prevention</span><b>Always ON</b></div>
<div class="rule"><span>Faculty clash prevention</span><b>Always ON</b></div>
<div class="rule"><span>Room clash prevention</span><b>Always ON</b></div>
<div class="rule"><span>Same subject at most once per day</span><b>Default ON</b></div>
<p class="hint">This starter version applies these rules automatically. More advanced controls such as teacher unavailable times, locked periods, preferred slots and double-period labs can be added next.</p>
</section>
<button type="submit">Generate my timetable <span>→</span></button>
</form><footer>Built with Java · JSP · Servlets · CSP Backtracking · Local Search</footer>
</main></body></html>
