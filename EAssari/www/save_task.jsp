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
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
</head>

<body>

<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="login.jsp"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="studentTaskList.jsp"/>
</c:if>

<jsp:include page="menu.jsp"/>

<h2>Task saved</h2>

<%
	TaskMaker tm = new TaskMaker(request);
	User user = (User)session.getAttribute("user");
	Task t = tm.getTask();
	t.setAuthor(user.getLastName() + " " + user.getFirstName());	
%>

<c:if test="${param.save_type=='new'}">	
<%	
	DBHandler.getInstance().createTask(tm.getTask(), tm.getCriteria());
%>
</c:if>
<c:if test="${param.save_type=='update'}">
<%	
	Task task = tm.getTask();
	task.setTaskID(request.getParameter("task_id"));
	DBHandler.getInstance().updateTask(task, tm.getCriteria());		
%>
</c:if>

<br>

<script language="javascript">
	setTimeout("location.replace('teacherTaskList.jsp')", 3000);
</script>

<%--
Request parameters:<br>
<c:forEach var="pMap" items="${paramValues}">
	<c:out value="${pMap.key}: "/>
	<c:forEach var="value" items="${pMap.value}">
		<c:out value="${value}"/>,
	</c:forEach>
	<br>
</c:forEach>
--%>

</body>
</html>