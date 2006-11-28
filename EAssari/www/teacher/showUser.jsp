<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<%
	User user = DBHandler.getInstance().getUser(request.getParameter("userID"));
	if (user != null) pageContext.setAttribute("user", user);		
%>


<html>
<head>
<title>User information</title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="../../styles/titotrainer.css">
<script language="Javascript">

function removeUser(userID) {
	return window.confirm('Do you really want to remove user '+userID+'?');
}
</script>
</head>

<body>

<jsp:include page="../menu.jsp"/>

<h2>User information</h2>

<c:if test="${param.action=='remove'}">
	<% DBHandler.getInstance().removeUser(request.getParameter("userID")); %>
	<c:redirect url="../teacher/removed.jsp"/>
</c:if>

<ul>
	<li>First name: <c:out value="${user.firstName}"/></li>
	<li>Last name: <c:out value="${user.lastName}"/></li>
	<li>Email: <c:out value="${user.email}"/></li>
	<li>Social security number: <c:out value="${user.socialSecurityNumber}"/></li>
	<li>Student number: <c:out value="${user.studentNumber}"/></li>
	<li>Status: <c:out value="${user.status}"/></li>
</ul>

<form name="removeUser_form" onSubmit="return removeUser('<c:out value="${user.userID}"/>')" action="showUser.jsp?action=remove&userID=<c:out value="${user.userID}"/>" method="POST">
	<input type="hidden" name="action" value="remove">
	<input type="submit" value="Remove" onclick="">
	<span class="footNote">(Will remove user account and all user information)</span>
</form>

<h3>Tasks</h3>

<% 	
	StudentAnswers answers = DBHandler.getInstance().getStudentAnswers(request.getParameter("userID"));
	if (answers != null) pageContext.setAttribute("answers", answers);
%>

<p>
<table class="listTable">
<tr>
<td class="titleBar">&nbsp;</td>
<td class="titleBar">Task</td>
<td class="titleBar">Number of tries</td>
<td class="titleBar">Last date</td>
</tr>

<c:set var="accepted" value="0"/>
<c:set var="unfinished" value="0"/>	

<c:forEach var="answers" items="${pageScope.answers.answers}">	

		<tr>
			<td>
			
			
			<c:choose>
				<c:when test="${answers.hasSucceeded}">
					<img src="positive.gif">
					<c:set var="accepted" value="${accepted + 1}"/>
				</c:when>
				<c:when test="${answers.lastTryNumber == 0}">
					<img src="blank.gif">
				</c:when>
				<c:otherwise>
					<img src="negative.gif">
					<c:set var="unfinished" value="${unfinished + 1}"/>
				</c:otherwise>
			</c:choose>			
		
			
			</td>
			<td><c:out value="${answers.taskName}"/></td>
			<td><c:out value="${answers.lastTryNumber}"/></td>
			<td><c:out value="${answers.answerTime}"/></td>
		</tr>
		
</c:forEach>

</table>
</p>

<p><b>Overall:</b><br>
<img src="positive.gif"> <c:out value="${accepted}"/> accepted<br>
<img src="negative.gif"> <c:out value="${unfinished}"/> unfinished</p>

</body>
</html>
