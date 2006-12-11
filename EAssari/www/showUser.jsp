<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>


<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="studentTaskList.jsp"/>
</c:if>

<%
	User user = DBHandler.getInstance().getUser(request.getParameter("userID"));
	if (user != null) pageContext.setAttribute("user", user);		
%>

<html>
<head>
<title>User information</title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="Javascript">

function removeUser(userID) {
	return window.confirm('Do you really want to remove user '+userID+'?');
}
</script>
</head>

<body>

<jsp:include page="menu.jsp"/>

<h2>User information</h2>

<c:if test="${param.action=='remove'}">
	<% DBHandler.getInstance().removeUser(request.getParameter("userID")); %>
	<c:redirect url="removed.jsp"/>
</c:if>
<c:if test="${param.action == 'changeStatus'}">
	<%
		user.setStatus(request.getParameter("status"));
		DBHandler.getInstance().updateUser(user);
	%>
</c:if>
<c:if test="${param.action == 'changePassword'}">
	<%
		user.setPassword(request.getParameter("password"));
		DBHandler.getInstance().updateUser(user);
	%>
	Password updated.
</c:if>


<%-- Different views if the user displayed is student or teacher/admin --%>
<c:choose>
	<c:when test="${user.teacher || user.admin}">
		<ul>
			<li>First name: <c:out value="${user.firstName}"/></li>
			<li>Last name: <c:out value="${user.lastName}"/></li>
			<li>Email: <c:out value="${user.email}"/></li>
			<li>Status: <c:out value="${user.status}"/></li>
		</ul>

		<form name="removeUser_form" onSubmit="return removeUser('<c:out value="${user.userID}"/>')" action="showUser.jsp?action=remove&userID=<c:out value="${user.userID}"/>" method="POST">
			<input type="hidden" name="action" value="remove">
			<input type="submit" value="Remove" onclick="">
			<span class="footNote">(Will remove user account and all user information)</span>
		</form>

		<form name="changeStatus_form" action="showUser.jsp" method="POST">
			<input type="hidden" name="action" value="changeStatus">
			<input type="hidden" name="userID" value="<c:out value="${user.userID}"/>">
			<input type="hidden" name="status" value="student">
			<input type="submit" value="Downgrade status">
			<span class="footNote">(Will downgrade user's status to student)</span>
		</form>
	</c:when>
	<c:when test="${user.student}">
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
		
		<form name="changeStatus_form" action="showUser.jsp" method="POST">
			<input type="hidden" name="action" value="changeStatus">
			<input type="hidden" name="userID" value="<c:out value="${user.userID}"/>">
			<input type="hidden" name="status" value="teacher">
			<input type="submit" value="Upgrade status">
			<span class="footNote">(Will upgrade user's status to teacher)</span>
		</form>

		<form name="changePassword_form" action="showUser.jsp" method="POST">
			<input type="hidden" name="action" value="changePassword">
			<input type="hidden" name="userID" value="<c:out value="${user.userID}"/>">
			New password: <input type="password" name="password">
			<input type="submit" value="Change password">
			<span class="footNote">(Will update user's password)</span>
		</form>

		<h3>Tasks</h3>

		<% 	
			StudentAnswers answers = DBHandler.getInstance().getStudentAnswers(request.getParameter("userID"));
			if (answers != null) pageContext.setAttribute("answers", answers);
		%>

		<p>
		<table class="listTable" border="1px">
			<tr>
				<td class="titleBar">&nbsp;</td>
				<td class="titleBar">Task</td>
				<td class="titleBar">Number of tries</td>
				<td class="titleBar">Last date</td>
				<td class="titleBar">Course name</td>
			</tr>

			<c:set var="accepted" value="0"/>
			<c:set var="unfinished" value="0"/>	

			<c:set var="bgcolor" value="blueRow"/>
			<c:set var="courseName" value=""/>
			<c:forEach var="answers" items="${pageScope.answers.answers}">	
				<c:if test="${courseName!=answers.courseName}">
					<c:set var="courseName" value="${answers.courseName}"/>
					<c:choose>
						<c:when test="${bgcolor=='blueRow'}">
							<c:set var="bgcolor" value="whiteRow"/>
						</c:when>
						<c:when test="${bgcolor=='whiteRow'}">
							<c:set var="bgcolor" value="blueRow"/>
						</c:when>
					</c:choose>
				</c:if>
				<tr class="<c:out value="${bgcolor}"/>">
					<td>
					<c:choose>
						<c:when test="${answers.hasSucceeded}">
							<img src="images/positive.gif">
							<c:set var="accepted" value="${accepted + 1}"/>
						</c:when>
						<%-- // FIXME: Tarvitaanko tätä? Listauksessa ei ainakaan näy tekemättömiä.
						<c:when test="${answers.lastTryNumber == 0}">
							<img src="images/blank.gif">
						</c:when> --%>
						<c:otherwise>
							<img src="images/negative.gif">
							<c:set var="unfinished" value="${unfinished + 1}"/>
						</c:otherwise>
					</c:choose>			
					</td>
					<td><c:out value="${answers.taskName}"/></td>
					<td><c:out value="${answers.lastTryNumber}"/></td>
					<td><c:out value="${answers.answerTime}"/></td>
					<td><c:out value="${answers.courseName}"/></td>
				</tr>
			</c:forEach>
		</table>
		</p>

		<p><b>Overall:</b><br>
		<img src="images/positive.gif"> <c:out value="${accepted}"/> accepted<br>
		<img src="images/negative.gif"> <c:out value="${unfinished}"/> unfinished</p>
	</c:when>
</c:choose>	

</body>
</html>
