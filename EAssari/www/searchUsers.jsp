<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<html>
<head>
	<title>Search Users</title>
	<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<head>


<body>
<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="login.jsp"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="studentTaskList.jsp"/>
</c:if>

<jsp:include page="menu.jsp"/>


<h2>Search users</h2>

<form name="searchUser_form" action="searchUsers.jsp" method="POST">
	<input type="hidden" name="action" value="search">
	<input type="text" name="query" value="<c:out value="${param.query}"/>"> <input type="submit" value="Search">
</form>

<c:if test="${param.action=='search'}">

	<%
		List<User> users = DBHandler.getInstance().getUsers(request.getParameter("query"));
		if (users != null) pageContext.setAttribute("users", users);		
	%>
	
	<p>Search results</p>
	<p>
	<table class="listTable" border="1">
		<tr>
		<td class="titlebar">Lastname, Firstname</td><td class="titlebar">Username</td><td class="titlebar">Studentnumber</td><td class="titlebar">Social security number</td><td class="titlebar">Status</td>
		</tr>
		<c:forEach var="user" items="${users}">	
			<tr>
			<td><a href="showUser.jsp?userID=<c:out value="${user.userID}"/>"><c:out value="${user.lastName}"/>, <c:out value="${user.firstName}"/></td><td><c:out value="${user.userID}"/></td><td> <c:if test="${not empty user.studentNumber}"><c:out value="${user.studentNumber}"/></c:if></td><td><c:if test="${not empty user.socialSecurityNumber}"><c:out value="${user.socialSecurityNumber}"/></c:if></td><td> <c:if test="${user.status!='student'}"><c:out value="${user.status}"/></c:if></td></a>
			</tr>
		</c:forEach>
	</table>
	</p>
</c:if>

</body>