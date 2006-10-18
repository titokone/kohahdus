<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>





<html>
<head>
<title>Tasks</title>
</head>

<body>

<%-- check that user is logged in --%>
<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="../login.jsp"/>	
</c:if>

<jsp:include page="../menu.jsp"/>


<%-- DEBUG --%>
<p>Logged in as <c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/>
<p>Kurssi: <c:out value="${course}"/>
<p>Kurssi: <c:out value="${language}"/>


<h1 align="center">Tasks</h1>

<p>
<tr>
<td>
<table border="0">
	<tr>
		<td style="padding: 6px; border: solid 2px #000000"><img src="positiivinen.gif" style="vertical-align:middle"> Accomplished<br>
			<img src="negatiivinen.gif" style="vertical-align:middle"> Unfinished</td>
		</td>
	</tr>
</table>
</p>

<p>
<table border="1" cellpadding="4">
	<tr>
		<td>&nbsp;</td>
		<td><b>Name</b></td>
		<td><b>Type</b></td>
		<td><b>Category</b></td>
		<td><b>Tries</b></td>
	</tr>
	
	<%-- get all tasks from db and store them in page context --%>
	<%
		List<MockTask> tasks = MockTask.getTasks();
		//List<Task> tasks = DBHandler.getInstance().getTasks();
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
	%>
	
	<%-- TODO: get student status information from somewhere --%>
	
	<c:if test="${empty tasks}">
		<tr>
			<td bgcolor="#FFFFFF" colspan="4">No available tasks.</td>
		</tr>
	</c:if>
	<c:if test="${not empty tasks}">
		<c:forEach var="task" items="${pageScope.tasks}">	
	
			<tr>
				<td><img src="positiivinen.gif"></td>
				<td><a href="#"><c:out value="${task.name}"/></a></td>
				<td><c:out value="${task.titoTaskType}"/></td>
				<td><c:out value="${task.description}"/></td>
				<td align="center">5</td>
			</tr>
		</c:forEach>
	</c:if>		
</table>
</p>

<p><b>Total completed tasks:</b><br>

<%-- TODO: get status info --%>
2 accepted<br>
1 unfinished</p>

</body>
</html>