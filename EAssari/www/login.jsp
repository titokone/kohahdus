<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Sign in</title>
</head>

<body>

<h2>TitoTrainer - Sign in</h2>

<form action="">

<div>
	<table border="0">
		<tr>
			<td><b>User name</b></td>
			<td>&nbsp;</td>
			<td><input type="text" name="user_name"></td>
		</tr>
		<tr>
			<td><b>Password</b></td>
			<td>&nbsp;</td>
			<td><input type="password" name="password"></td>
		</tr>
		<c:if test="${empty param.role}">
			<tr>
				<td><b>Course</b></td>
				<td>&nbsp;</td>
				<td>
					<select name="course">
						<option value="course 1">Course 1</option>
						<option value="course 2">Course 2</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><b>Language</b></td>
				<td>&nbsp;</td>
				<td>
					<select name="language">
						<option value="english">English</option>
						<option value="finnish">Finnish</option>
					</select>
				</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="3" align="right"><br><input type="submit" value="Sign in"></td>
		</tr>
	</table>
</div>

<c:if test="${empty param.role}">
	<p><small>New user? <a href="signup.html">Sign up</a></small></p>
</c:if>

</form>

</body>
</html>