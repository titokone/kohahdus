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
<c:if test="${empty course}">
	Course not selected - redirecting to login/error?
	<c:redirect url="../login.jsp"/>
</c:if>	

<jsp:include page="../menu.jsp"/>


<%-- DEBUG --%>
<p>Logged in as <c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/>
<p>Kurssi: <c:out value="${course}"/>
<p>Kieli: <c:out value="${language}"/>


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
		<td>&nbsp;<a href="studentTaskList.jsp?sortTasks=5">Status</a></td>
		<td><a href="studentTaskList.jsp?sortTasks=0"><b>Name</b></a></td>
		<td><a href="studentTaskList.jsp?sortTasks=1"><b>Type</b></a></td>
		<td><a href="studentTaskList.jsp?sortTasks=2"><b>Category</b></a></td>
		<td><a href="studentTaskList.jsp?sortTasks=6"><b>Tries</b></a></td>
	</tr>
	
	<%-- get all tasks from db and store them in page context --%>
	<%
		List<MockTask> tasks = MockTask.getTasks();
		/*
		String courseID = (String) session.getAttribute("course");
		User u = (User) session.getAttribute("user");
		
		List<Task> tasks = DBHandler.getInstance().getTasks(courseID, u.getUserID());
		*/
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
	%>
	
	<%-- TODO: get student status information from somewhere --%>
	
	<c:set var="accepted" value="0"/>
	<c:set var="unfinished" value="0"/>	

	<c:if test="${empty tasks}">
		<tr>
			<td bgcolor="#FFFFFF" colspan="4">No available tasks.</td>
		</tr>
	</c:if>
	<c:if test="${not empty tasks}">
	
		<c:if test="${not empty param.sortTasks}">
			<%
				TaskComparator tc = null;
				String sortRequest = request.getParameter("sortTasks");
				try {
					int sort = Integer.parseInt(sortRequest);
					tc = new TaskComparator(sort);
				} catch (NumberFormatException e) {
					Log.write("TeacherTaskList: Invalid sort parameter");
				}
				if (tc != null) {
					Collections.sort(tasks, tc);
				}
			%>	
		</c:if>
		
		<c:forEach var="task" items="${pageScope.tasks}">	
	
			<tr>
				<td>
					<c:choose>
						<c:when test="${task.hasSucceeded}">
							<img src="positiivinen.gif">
							<c:set var="accepted" value="${accepted + 1}"/>
						</c:when>
						<c:when test="${task.noOfTries == 0}">
							<img src="neutraali.gif">
						</c:when>
						<c:otherwise>
							<img src="negatiivinen.gif">
							<c:set var="unfinished" value="${unfinished + 1}"/>
						</c:otherwise>
					</c:choose>				
				</td>
				<td><a href="#"><c:out value="${task.name}"/></a></td>
				<td><c:out value="${task.titoTaskType}"/></td>
				<td><c:out value="${task.category}"/></td>
				<td align="center"><c:out value="${task.noOfTries}"/></td>
			</tr>
		</c:forEach>
	</c:if>		
</table>
</p>

<p><b>Total completed tasks:</b><br>

<%-- TODO: get status info --%>
<c:out value="${accepted}"/> accepted<br>
<c:out value="${unfinished}"/> unfinished</p>

</body>
</html>