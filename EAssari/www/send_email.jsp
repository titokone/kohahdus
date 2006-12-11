<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<html>
<head>
	<title>TitoTrainer</title>
	<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<head>


<body>

<jsp:include page="menu.jsp"/>


<h2>Email forgotten password</h2>
<c:if test="${empty param.action}">
	To get your new password, write your username and click the "Send email" button. <br>
	<br>
	<form name="form" action="send_email.jsp" method="POST">
		<input type="hidden" name="action" value="send">
		Username:
		<input type="text" name="userID" value=""> <input type="submit" value="Send email">
	</form>
</c:if>
<c:if test="${param.action=='send'}">
	<p>
	<%
		User user = DBHandler.getInstance().getUser(request.getParameter("userID"));
		if (user == null){
			out.println("Username doesn't exist.");
		} else if (user.getEmail().indexOf("cs.helsinki.fi") > -1){
			user.setPassword(Emailer.sendNewPasswordEmail(user.getEmail()));
			DBHandler.getInstance().updateUser(user);
			out.println("Your new password has been sent to your email.");
		} else {
			out.println("Email can only be sent to cs.helsinki.fi addresses. Please contact your teacher.");
		}
	%>
	</p>
</c:if>

<br>
Back to <a href="login.jsp">login</a> page

</body>