<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<jsp:include page="../menu.jsp"/>

<%
	User user = DBHandler.getInstance().getUser(request.getParameter("userID"));
	if (user != null) pageContext.setAttribute("user", user);		
%>


<html>
<head>
<title><c:out value="${user.lastName}"/>, <c:out value="${user.firstName}"/></title>
</head>

<body>

<h2></h2>


<a href="">Remove user</a><br>

<h3>User information</h3>
<ul>
	<li>First name: <c:out value="${user.firstName}"/></li>
	<li>Last name: <c:out value="${user.lastName}"/></li>
	<li>Email: <c:out value="${user.email}"/></li>
	<li>Social security number: <c:out value="${user.socialSecurityNumber}"/></li>
	<li>Student number: <c:out value="${user.studentNumber}"/></li>
	<li>Student number: <c:out value="${user.studentNumber}"/></li>
</ul>

<h3>Tasks</h3>
<p>
<table border="1" cellpadding="4">
<tr>
<td>&nbsp;</td>
<td><b>Task</b></td>
<td><b>Type</b></td>
<td><b>Number of tries</b></td>
<td><b>Last date</b></td>
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
