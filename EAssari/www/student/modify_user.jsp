<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Edit Your Profile</title>
</head>

<body>

<c:if test="${param.action=='modify'}">

	<c:set target="${user}" property="firstName" value="${param.first_name}"/>
	<c:set target="${user}" property="lastName" value="${param.last_name}"/>
	<c:set target="${user}" property="email" value="${param.email}"/>

	<c:if test="${not empty param.student_number}">
		<c:set target="${user}" property="studentNumber" value="${param.student_number}"/>
	</c:if>
	
	<c:if test="${not empty param.social_security_number}">
		<c:set target="${user}" property="socialSecurityNumber" value="${param.social_security_number}"/>
	</c:if>



	
	Viedään kantaan:
	<br>
	<c:out value="firstname: ${param.first_name}"/>
	<br>
	<c:out value="lastname: ${param.last_name}"/>
	<br>
	<c:out value="email: ${param.email}"/>
	<br>
	<c:out value="student_number: ${param.student_number}"/>
	<br>
	<c:out value="social_security_number: ${param.social_security_number}"/>

<%
	 //User user = DBHandler.getInstance().getUser(request.getParameter("username"), request.getParameter("password"));
	 User user = (User) session.getAttribute("user");
	 if (user.isValid()) {
	 	boolean testi = DBHandler.getInstance().updateUser(user);
	 	out.print("Nyt yritettiin kirjoittaa kantaa ");
	 	out.print(testi);
	 } else {
	 	out.print("virhe");
	 }		
%>	
	
</c:if>






<h2>Edit Your Profile</h2>

Logged in: <c:out value="${user.firstName} ${user.lastName}"/>



<br>

<form action="modify_user.jsp" method="POST">
<input type="hidden" name="action" value="modify">




<div>
        <table border="0" cellpadding="5">
                <tr>
                        <td><b>User name </b></td>
                        <td>[user name]</td>
                </tr>
                <tr>
                        <td><b>First name </b></td>
                        <td><input type="text" name="first_name" value="<c:out value="${user.firstName}"/>"></td>
                </tr>
                <tr>
                        <td><b>Last name </b></td>
                        <td><input type="text" name="last_name" value="<c:out value="${user.lastName}"/>"></td>
                </tr>
                <tr>
                        <td><b>Student number* </b></td>
                        <td><input type="text" name="student_number" value="<c:out value="${user.studentNumber}"/>"> </td>
                </tr>
                <tr>
                        <td><b>Social security number* </b></td>
                        <td><input type="text" name="social_security_number" value="<c:out value="${user.socialSecurityNumber}"/>"> </td>
                </tr>
                <tr>
                        <td colspan="2"><small>* either student number or social security number is required</small></td>
                </tr>
                <tr>
                        <td colspan="2"><hr></td>
                </tr>
                <tr>
                        <td><b>E-mail: </b></td>
                        <td><input type="text" name="email" value="<c:out value="${user.email}"/>"></td>
                </tr>
                <tr>
                        <td colspan="2"><hr></td>
                </tr>
                <tr>
                        <td colspan="2"><b><i>Change password</i></b></td>
                </tr>
                <tr>
                        <td><b>Old password </b></td>
                        <td><input type="password" name="old_password"></td>
                </tr>
                <tr>
                        <td><b>New password </b></td>
                        <td><input type="password" name="new_password"></td>
                </tr>
                <tr>
                        <td><b>New password again </b></td>
                        <td><input type="password" name="repeat_new_password"></td>
                </tr>
                <tr>
                        <td colspan="2" align="right"><input type="submit" name="save_button" value="Save"></td>
                </tr>
        </table>
</div>

</form>
</body>
</html>