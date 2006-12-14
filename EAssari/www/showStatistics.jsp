<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<jsp:include page="menu.jsp"/>

<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="login.jsp"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="studentTaskList.jsp"/>
</c:if>

<html>
<head>
<title>Course Autumn 2006 tasks done</title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="javascript" type="text/javascript">
function rotateText(){
	var elems = document.getElementsByTagName('canvas');
	for (var i=0; i<elems.length; i++){
		var canvas = elems[i];
		var ctx = canvas.getContext("2d");
		ctx.rotate(90);	
		ctx.draw();
	}
}	
	
</script>
	
</head>

<body id="showStatistics">

<% 	
	String courseID = request.getParameter("courseID");
	String courseName = DBHandler.getInstance().getCourseName(courseID);		
	LinkedList students = DBHandler.getInstance().getAllStudentAnswers(courseID);		
	if (students != null) pageContext.setAttribute("students", students);
	LinkedList taskNames = DBHandler.getInstance().getAnsweredTaskNames(courseID);		
	if (taskNames != null) pageContext.setAttribute("taskNames", taskNames);	
%>

	<c:set var="total" value="0"/>

<h1>Course <%=courseName%> tasks</h1>

<table class="listTable" border="1px">
	<tr>
		<td>Student</td>
		<td>
			Student or Social<br>
			Security Number
		</td>
		<c:forEach var="taskName" items="${taskNames}" varStatus="status">
			<td align="center">
				<!-- This text rotation works only in IE
				<div style="direction:ltr; writing-mode:tb-rl">
					<c:out value="${taskName}"/>
				</div>
				-->
				<a title="<c:out value="${taskName}"/>">
					<c:out value="${status.count}"/>
				</a>
			</td>
		</c:forEach>
		<td>Total</td>
	</tr>
	<c:forEach var="student" items="${students}">
		<tr>
			<td>
				<a href="showUser.jsp?userID=<c:out value="${student.userID}"/>">
					<c:out value="${student.lastname}"/>, <c:out value="${student.firstname}"/>
				</a>
			</td>
			<td>
				<c:choose>
					<c:when test="${not empty student.studentNumber}">
		 				<c:out value="${student.studentNumber}"/>
		 			</c:when>
		 			<c:when test="${not empty student.socialSecurityNumber}">
		 				<c:out value="${student.socialSecurityNumber}"/>
		 			</c:when>		 			
		 			<c:otherwise>
						illegally missing!
		 			</c:otherwise>
		 		</c:choose>		
			</td>
			<c:forEach var="taskName" items="${taskNames}">
				<td>
					<c:if test="${not empty student.answerMap[taskName]}">
						<c:choose>
							<c:when test="${student.answerMap[taskName].hasSucceeded}">
								<img src="images/positive.gif">
							</c:when>
							<c:otherwise>
								<img src="images/negative.gif">
							</c:otherwise>
						</c:choose>			
					</c:if>
				</td>
			</c:forEach>
			<td align="center">
				<c:out value="${student.correctAnswerCount}"/>
			</td>
		</tr>
	</c:forEach>
</table>
<p>
<img src="images/positive.gif"> accepted<br>
<img src="images/negative.gif"> unfinished
</p>

<br>
<c:forEach var="taskName" items="${taskNames}" varStatus="status">
<table cellpadding="0" cellspacing="0">
<tr>
	<td>
		<c:out value="${status.count}"/>
	</td>
	<td>=</td>
	<td>
		<c:out value="${taskName}"/>
	</td>
</tr>
</table>
</c:forEach>


</body>
</html>