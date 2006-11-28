<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<jsp:include page="../menu.jsp"/>


<html>
<head>
	<title>Course Autumn 2006 tasks done</title>
	<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="../../styles/titotrainer.css">
</head>

<body id="showStatistics">

<% 	
	String courseID = request.getParameter("courseID");
	String courseName = DBHandler.getInstance().getCourseName(courseID);		
	LinkedList<StudentAnswers> students = DBHandler.getInstance().getAllStudentAnswers(courseID);		
	if (students != null) pageContext.setAttribute("students", students);
	LinkedList<String> taskNames = DBHandler.getInstance().getAnsweredTaskNames(courseID);		
	if (taskNames != null) pageContext.setAttribute("taskNames", taskNames);	
%>

	<c:set var="total" value="0"/>

<h1>Course <%=courseName%> tasks done</h1>

<table class="listTable">
	<tr>
		<td>Student</td>
		<c:forEach var="taskName" items="${taskNames}">
			<td>
				<div style="direction:ltr; writing-mode:tb-rl">
					<c:out value="${taskName}"/>
				</div>
			</td>
		</c:forEach>
	</tr>
	<c:forEach var="student" items="${students}">
		<tr>
			<td>
				<a href="showUser.jsp?userID=<c:out value="${student.userID}"/>">
					
					<c:out value="${student.lastname}"/>, <c:out value="${student.firstname}"/>
				</a>
			</td>
			<c:forEach var="taskName" items="${taskNames}">
				<td>
					<c:if test="${not empty student.answers[taskName]}">
					
					<c:choose>
						<c:when test="${student.answers[taskName].hasSucceeded}">
							<img src="positive.gif">
						</c:when>
						<c:otherwise>
							<img src="negative.gif">
						</c:otherwise>
					</c:choose>			
					
					</c:if>
				</td>
			</c:forEach>
		</tr>
	</c:forEach>
</table>

<p><br>
<img src="positive.gif"> accepted<br>
<img src="negative.gif"> unfinished
</p>


</body>
</html>