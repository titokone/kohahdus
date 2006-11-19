<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>

<%-- check that user is logged in --%>
<c:if test="${empty user}">
	<%--Not logged in - redirecting to login --%>
	<c:redirect url="../login.jsp"/>	
</c:if>
<c:if test="${empty course}">
	<%--Course not selected - redirecting to login/error? --%>
	<c:redirect url="../login.jsp"/>
</c:if>	

<%
	String lang = (String)session.getAttribute("language");
	ResourceBundle rb = LanguageManager.getTextResource(lang , "studentTaskList");
%>



<html>
<head>
<title>TitoTrainer - <%=rb.getString("tasksTitle")%></title>
</head>

<body>



<jsp:include page="../menu.jsp"/>


<%-- DEBUG --%>
<p>Logged in as <c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/>
<p>Kurssi: <c:out value="${course}"/>
<p>Kieli: <c:out value="${language}"/>

<%-- DEBUG --%>
<h1 align="center"><%=rb.getString("testi")%></h1>

<p>
<tr>
<td>
<table border="0">
	<tr>
		<td style="padding: 6px; border: solid 2px #000000"><img src="positive.gif" style="vertical-align:middle"> <%=rb.getString("acceptedText")%><br>
			<img src="negative.gif" style="vertical-align:middle"> <%=rb.getString("unfinishedText")%></td>
		</td>
	</tr>
</table>
</p>

<p>
<table border="1" cellpadding="4">
	<tr>
		<td>&nbsp;<a href="studentTaskList.jsp?sortTasks=5"><%=rb.getString("statusText")%></a></td>
		<td><a href="studentTaskList.jsp?sortTasks=0"><b><%=rb.getString("nameText")%></b></a></td>
		<td><a href="studentTaskList.jsp?sortTasks=1"><b><%=rb.getString("typeText")%></b></a></td>
		<td><a href="studentTaskList.jsp?sortTasks=2"><b><%=rb.getString("categoryText")%></b></a></td>
		<td><a href="studentTaskList.jsp?sortTasks=6"><b><%=rb.getString("triesText")%></b></a></td>
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
			<td bgcolor="#FFFFFF" colspan="4"><%=rb.getString("noTasksText")%></td>
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
							<img src="positive.gif">
							<c:set var="accepted" value="${accepted + 1}"/>
						</c:when>
						<c:when test="${task.noOfTries == 0}">
							<img src="blank.gif">
						</c:when>
						<c:otherwise>
							<img src="negative.gif">
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

<p><b><%=rb.getString("totalTasksTitle")%>:</b><br>

<%-- TODO: get status info --%>
<c:out value="${accepted}"/> <%=rb.getString("acceptedText")%><br>
<c:out value="${unfinished}"/> <%=rb.getString("unfinishedText")%></p>

</body>
</html>