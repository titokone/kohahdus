<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<html>
<head>
	<title>Search Users</title>
<head>


<body>
<c:if test="${empty user}"><script language="Javascript">
	Not logged in - redirecting to login
	<c:redirect url="../login.jsp"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="../student/studentTaskList.jsp"/>
</c:if>

<jsp:include page="../menu.jsp"/>


<b>Search users</b><br>

<form name="searchUser_form" action="searchUsers.jsp" method="POST">
	<input type="hidden" name="action" value="search">
	<input type="text" name="query" value="<c:out value="${param.query}"/>"> <input type="submit" value="Search" onclick="">
</form>

<c:if test="${param.action=='search'}">

	<%
		List<User> users = DBHandler.getInstance().getUsers(request.getParameter("query"));
		if (users != null) pageContext.setAttribute("users", users);		
	%>
	
	<p>Search results</p>
	<p>
		<c:forEach var="user" items="${users}">	
			<a href="showUser.jsp"><c:out value="${user.lastName}"/>, <c:out value="${user.firstName}"/> (<c:out value="${user.userID}"/>)</a><br>
		</c:forEach>
	</p>
</c:if>

</body>