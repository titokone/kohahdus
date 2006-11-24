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
<p>
<table class="listTable">
<tr>
<td class="titleBar">&nbsp;</td>
<td class="titleBar">Task</td>
<td class="titleBar">Type</td>
<td class="titleBar">Number of tries</td>
<td class="titleBar">Last date</td>
</tr>
<tr>
<td><img src="positive.gif"></td>
<td>Tehtävä 1</td>
<td>Täydennystehtävä</td>
<td align="center">5</td>
<td>5.5.2006</td>
</tr>
<tr>
<td>&nbsp;</td>
<td>Tehtävä 2</td>
<td>Staattinen tehtävä</td>
<td align="center">0</td>
<td>-</td>
</tr>
<tr>
<td><img src="positive.gif"></td>
<td>Tehtävä 3</td>
<td>Staattinen tehtävä</td>
<td align="center">1</td>
<td>27.4.2006</td>
</tr>
<tr>
<td><img src="negative.gif"></td>
<td>Tehtävä 4</td>
<td>Täydennystehtävä</td>
<td align="center">2</td>
<td>2.5.2006</td>
</tr>
</table>
</p>

<p><b>Tehtyjä tehtäviä yhteensä:</b><br>
2 hyväksyttyä<br>
1 keskeneräistä</p>

</body>
</html>
