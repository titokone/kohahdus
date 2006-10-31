<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer</title>
</head>

<body>

<jsp:include page="menu.jsp"/>

<h2>Task saved</h2>




Params from TaskMaker:

<%
	TaskMaker tm = new TaskMaker(request);
	
	/* DEBUG
	Map<String, String> map = tm.getParams();
	Set<String> keys = map.keySet();
	
	for (String s : keys) {
		out.print(s + " : " + map.get(s));
	}	 
	*/
	
	List<Criterion> crits = tm.getCriteria();
	
	for (Criterion c : crits) {
		out.print(c.serializeToXML());
		out.print("<br>");
	}	
	
	Task t = tm.getTask();
	out.print("<p><pre>Task name: "+t.getName()+"</pre>");
	out.print("<p><pre>Author: "+t.getAuthor()+"</pre>");
	out.print("<p><pre>Category: "+t.getCategory()+"</pre>");
	out.print("<p><pre>Instructions: "+t.getDescription()+"</pre>");
	out.print("<p><pre>Pub input: "+t.getPublicInput()+"</pre>");
	out.print("<p><pre>Sec input: "+t.getSecretInput()+"</pre>");
	
	//DEBUG: korvataan aina vanha..
	//t.setTaskID("TESTING");
	t.setAuthor("TEST");
	t.setLanguage("EN");
	//DBHandler.getInstance().updateTask(t, tm.getCriteria());		
%>
<c:if test="${param.save_type=='new'}">	
	Task saved.... yeah right
<%	
	
	DBHandler.getInstance().createTask(tm.getTask(), tm.getCriteria());
%>
</c:if>
<c:if test="${param.save_type=='update'}">
	Task updated...
<%
	
	Task task = tm.getTask();
	task.setTaskID(request.getParameter("task_id"));
	DBHandler.getInstance().updateTask(task, tm.getCriteria());		
%>
</c:if>

Task xxx saved... not yet! ;D<br>
<br>

Request parameters:<br>
<c:forEach var="pMap" items="${paramValues}">
	<c:out value="${pMap.key}: "/>
	<c:forEach var="value" items="${pMap.value}">
		<c:out value="${value}"/>,
	</c:forEach>
	<br>
</c:forEach>



</body>
</html>