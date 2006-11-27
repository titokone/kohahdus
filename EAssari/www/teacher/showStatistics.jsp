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
		<td>Opiskelija</td>
		<td>
		<c:forEach var="taskName" items="${taskNames}">
			<c:out value="${taskName}"/>
		</c:forEach>
		</td>
		<td><c:out value="${total}"/></td>
	</tr>
	<c:forEach var="studentAnswer" items="${students}">
		<tr>
			<td>
				<a href="showUser.jsp?userID=<c:out value="${studentAnswer.userID}"/>">
					<c:out value="${studentAnswer.lastname}"/>, <c:out value="${studentAnswer.firstname}"/>
				</a>
			</td>
			<td>
			<c:forEach var="taskName" items="${taskNames}">
				<c:if test="${not empty studentAnswer[taskName]}">
					<c:out value="${studentAnswer[taskName].hasSucceeded}"/>
				</c:if>
			</c:forEach>
			</td>
			<td><c:out value="${total}"/></td>
		</tr>
	</c:forEach>
</table>


</body>
</html>