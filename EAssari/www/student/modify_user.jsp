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

<h2>Edit Your Profile</h2>

<form action="modify_user.jsp" method="POST">
<input type="hidden" name="action" value="modify">

<%  User user = new User("5", "peltonen", "matti", "matti@hot.com", "a", "a", "a", "kissa", "a", null );  %>



<div>
        <table border="0" cellpadding="5">
                <tr>
                        <td><b>User name </b></td>
                        <td>[user name]</td>
                </tr>
                <tr>
                        <td><b>First name </b></td>
                        <td><input type="text" name="first_name" value="<%=user.getFirstName()%>"> </td>
                </tr>
                <tr>
                        <td><b>Last name </b></td>
                        <td><input type="text" name="last_name" value="<%=user.getLastName()%>"> </td>
                </tr>
                <tr>
                        <td><b>Student number* </b></td>
                        <td><input type="text" name="student_number" value="<%=user.getStudentNumber()%>"> </td>
                </tr>
                <tr>
                        <td><b>Social security number* </b></td>
                        <td><input type="text" name="social_security_number" value="<%=user.getSocialSecurityNumber()%>"> </td>
                </tr>
                <tr>
                        <td colspan="2"><small>* either student number or social security number is required</small></td>
                </tr>
                <tr>
                        <td colspan="2"><hr></td>
                </tr>
                <tr>
                        <td><b>E-mail: </b></td>
                        <td><input type="text" name="email" value="<%=user.getEmail()%>"></td>
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
