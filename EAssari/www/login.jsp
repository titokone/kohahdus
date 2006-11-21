<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Sign in</title>

<script language="Javascript" type="text/javascript" src="js/common.js"></script>
<script language="Javascript">

	// Test for javascript support
	setCookie("testi", "true");
	if (getCookie("testi") != "true"){
		window.location = "error.jsp?errorMsg=Cookie support is needed to access this site.";
	}

</script>
<noscript>
	<h2>Your browser does not support JavaScript or you have switched off the JavaScript support.</h2>
	<p><b>Switch on JavaScript support or upgrade to a browser that supports JavaScript.<b></p>
</noscript>
</head>

<body>

<h2>TitoTrainer - Sign in</h2>

<c:if test="${param.action=='logout'}">
	<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> logged out.<br>
	<br>
	<c:remove var="user" scope="session"/>
</c:if>

<c:if test="${param.action=='login' && not empty user}">
	<c:remove var="user" scope="session"/>
</c:if>

<c:if test="${param.action=='login'}">
	<%
	User user = DBHandler.getInstance().getUser(request.getParameter("username"), request.getParameter("password"));
	if (user != null) pageContext.setAttribute("user", user, PageContext.SESSION_SCOPE);
	%>
	<c:if test="${empty user}">
		Username or password not correct..
	</c:if>
	<c:if test="${not empty user}">		
		Login ok and then redirect here....
		<c:if test="${user.admin}">		
			<c:redirect url="teacher/teacherTaskList.jsp"/>
			You are an admin so not redirecting to anywhere...
		</c:if>
		<c:if test="${user.teacher}">		
			<c:redirect url="teacher/teacherTaskList.jsp"/>
		</c:if>
		<c:if test="${user.student}">	
			
			<c:set var="course" value="${param.course}" scope="session"/>
			<c:set var="language" value="${param.language}" scope="session"/>
			
			<c:redirect url="student/studentTaskList.jsp"/>
		</c:if>
	</c:if>
</c:if>


<form name="f" action="login.jsp" method="POST">
<input type="hidden" name="action" value="login">

<div>
	<table border="0">
		<tr>
			<td><b>Username</b></td>
			<td>&nbsp;</td>
			<td><input type="text" name="username"></td>
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
				
				<%	//List<MockCourse> courses = MockCourse.getCourses();
					List<Course> courses = DBHandler.getInstance().getCourses();
					pageContext.setAttribute("courses", courses);				
				%>
				
				<%-- TODO: haluttiinko näihin default arvot cookien kautta --%>
				
				<td>
					<select name="course">
						<c:if test="${not empty courses}">
							<c:forEach var="course" items="${pageScope.courses}">
								<option value="<c:out value="${course.ID}"/>"><c:out value="${course.name}"/></option>							
							</c:forEach>
						</c:if>	
					</select>
				</td>
			</tr>		
		</c:if>
	
		<tr>
			<td><b>Language</b></td>
			<td>&nbsp;</td>
			<td>
				<select name="language">
					<option value="EN">English</option>
					<option value="FI">Finnish</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="right">
				<br>
				<!--<input type="button" value="Sign in" onClick="javascript:document.f.submit();">-->
				<input type="submit" value="Sign in">
			</td>
		</tr>
	</table>
</div>

<c:if test="${empty param.role}">
	<p><small>New user? <a href="signup.jsp">Sign up</a></small></p>
</c:if>

</form>

</body>
</html>