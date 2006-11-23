<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<jsp:include page="../menu.jsp"/>


<html>
<head>
	<title>Course Autumn 2006 tasks done</title>
</head>

<body>

<% 	List<Task> tasks = DBHandler.getInstance().getTasks();		
	if (tasks != null) pageContext.setAttribute("tasks", tasks);
%>

	<c:set var="total" value="0"/>

<h1 align="center">Kurssin Syksy 2006 tehdyt tehtävät</h1>

<p align="center">
	<table border="1">
		<tr>
		<td>&nbsp;</td>
		
		<c:forEach var="task" items="${pageScope.tasks}">
			<td><c:out value="${task.taskID}"/></td>
		</c:forEach>
		
		<td>Total</td>
		</tr>
		
		<c:forEach var="user" items="${pageScope.users}">
			<c:forEach var="task" items="${pageScope.tasks}">
	
			<tr>
				<td><a href="../teacher/showUser.jsp?userID=<c:out value="${user.userID}"/>"><c:out value="${user.lastName}"</a></td>
				<td>
				<c:choose>
						<c:when test="${task.hasSucceeded}">
							<img src="positive.gif">
							<c:set var="total" value="${total + 1}"/>
						</c:when>
						<c:when test="${task.noOfTries == 0}">
							<img src="blank.gif">
						</c:when>
						<c:otherwise>
							<img src="negative.gif">
						</c:otherwise>
					</c:choose>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				
			</c:forEach>
				<td><c:out value="${total}"/></td>
			</tr>
		</c:forEach>


</table></p>


</body>
</html>