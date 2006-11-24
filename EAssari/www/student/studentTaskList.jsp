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
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="../../styles/titotrainer.css">
</head>

<body>



<jsp:include page="../menu.jsp"/>

<p>
<tr>
<td>
<table border="0">
	<tr>
		<td class="infoBox"><img src="positive.gif" style="vertical-align:middle"> <%=rb.getString("acceptedText")%><br>
			<img src="negative.gif" style="vertical-align:middle"> <%=rb.getString("unfinishedText")%></td>
		</td>
	</tr>
</table>
</p>

<p>
<table class="listTable">
	<tr>
		<td class="titleBar"><a href="studentTaskList.jsp?sortTasks=5"><%=rb.getString("statusText")%></a></td>
		<td class="titleBar"><a href="studentTaskList.jsp?sortTasks=0"><%=rb.getString("nameText")%></a></td>
		<td class="titleBar"><a href="studentTaskList.jsp?sortTasks=1"><%=rb.getString("typeText")%></a></td>
		<td class="titleBar"><a href="studentTaskList.jsp?sortTasks=2"><%=rb.getString("categoryText")%></a></td>
		<td class="titleBar"><a href="studentTaskList.jsp?sortTasks=6"><%=rb.getString("triesText")%></a></td>
		<td class="titleBar"><%=rb.getString("languageText")%></b></td>
	</tr>
	
	<%-- get all tasks from db and store them in page context --%>
	<%
		
		
		String courseID = (String) session.getAttribute("course");
		User u = (User) session.getAttribute("user");
		
		List<Task> tasks = DBHandler.getInstance().getTasks(courseID, u.getUserID());
			
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
	%>
	
	<%-- TODO: get student status information from somewhere --%>
	
	<c:set var="accepted" value="0"/>
	<c:set var="unfinished" value="0"/>	

	<c:if test="${empty tasks}">
		<tr>
			<td colspan="4"><%=rb.getString("noTasksText")%></td>
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
			%>		
					<c:redirect url="../error.jsp">
						<c:param name="errorMsg" value="Invalid sort criteria!"/>
					</c:redirect>
			<%		
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
				<td><a href="answer_task.jsp?task_id=<c:out value="${task.taskID}"/>"><c:out value="${task.name}"/></a></td>
				<td><c:out value="${task.titoTaskType}"/></td>
				<td><c:out value="${task.category}"/></td>
				<td align="center"><c:out value="${task.noOfTries}"/></td>
				<td><c:out value="${task.language}"/></td>
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