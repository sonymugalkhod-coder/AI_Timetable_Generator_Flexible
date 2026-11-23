<%@ page import="java.util.*,com.aitimetable.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
List<Meeting> meetings=(List<Meeting>)request.getAttribute("meetings");
List<String> days=(List<String>)request.getAttribute("days");
List<Slot> periods=(List<Slot>)request.getAttribute("periods");
String title=(String)request.getAttribute("title");
%>
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Generated Timetable</title><link rel="stylesheet" href="style.css"></head><body><main class="wrap wide">
<header><div class="eyebrow">AI GENERATED RESULT</div><h1><%=title%></h1><p>Review your generated timetable. Use Print to save it as PDF.</p></header>
<div class="actions"><a class="secondary" href="index.jsp">← Edit inputs</a><button onclick="window.print()">Print / Save PDF</button></div>
<%
Set<String> sections=new TreeSet<>(); for(Meeting m:meetings) sections.add(m.subject.section);
for(String section:sections){
%><section class="panel"><h2>Section <%=section%></h2><div class="table-scroll"><table><thead><tr><th>Day</th><% for(Slot p:periods){%><th><%=p.time%></th><%}%></tr></thead><tbody>
<% for(String day:days){%><tr><th><%=day%></th><% for(Slot p:periods){Meeting found=null; for(Meeting m:meetings) if(m.subject.section.equals(section)&&m.slot.day.equals(day)&&m.slot.time.equals(p.time)){found=m;break;} %>
<td><% if(found!=null){%><div class="subject"><b><%=found.subject.code%></b><span><%=found.subject.name%></span><small><%=found.subject.faculty%></small><small><%=found.subject.room%></small></div><%}else{%><span class="free">—</span><%}%></td><%}%></tr><%}%>
</tbody></table></div></section><%}%>
<footer>Generated using Constraint Satisfaction Problem (CSP), Backtracking and Local Search.</footer></main></body></html>
