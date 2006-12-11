<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Sign in</title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="Javascript" type="text/javascript" src="js/common.js"></script>
<script language="Javascript">

	// Test for javascript support
	setCookie("testi", "true");
	if (getCookie("testi") != "true"){
		window.location = "error.jsp?errorMsg=Cookie support is needed to access this site.";
	}
	
function doOnLoad(){
	<c:if test="${empty param.role}">
		var courseID = getCookie("courseID");
		if (courseID != "") {
			var option = document.getElementById(courseID);
			option.selected = true;
		}
		var language = getCookie("language");
		if (language != "") {
			var option = document.getElementById(language);
			option.selected = true;
		}
	</c:if>
}


</script>
<noscript>
	<h2>Your browser does not support JavaScript or you have switched off the JavaScript support.</h2>
	<p><b>Switch on JavaScript support or upgrade to a browser that supports JavaScript.<b></p>
</noscript>
</head>

<body onLoad="javascript:doOnLoad()">

<c:if test="${param.userAction=='logout'}">
	<c:remove var="user" scope="session"/>
</c:if>

<c:if test="${param.userAction=='login' && not empty user}">
	<c:remove var="user" scope="session"/>
</c:if>

<c:if test="${param.userAction=='login'}">
	<%
	User user = DBHandler.getInstance().getUser(request.getParameter("username"), request.getParameter("password"));
	if (user != null) pageContext.setAttribute("user", user, PageContext.SESSION_SCOPE);
	%>
	<c:if test="${empty user}">
		<p class="errorMsg">Username or password not correct.</p>
	</c:if>
	<c:if test="${not empty user}">		
		Login ok and then redirect here....
		<c:if test="${user.admin}">		
			<c:set var="language" value="EN" scope="session"/>
			<c:redirect url="teacherTaskList.jsp"/>
			You are an admin so not redirecting to anywhere...
		</c:if>
		<c:if test="${user.teacher}">		
			<c:set var="language" value="EN" scope="session"/>
			<c:redirect url="teacherTaskList.jsp"/>
		</c:if>
		<c:if test="${user.student}">
			<%-- Student logs in from teachers page -> forwarded to normal login page --%> 
			<c:if test="${not empty param.role}">
				<c:remove var="user"/>
				<c:redirect url="login.jsp"/>
			</c:if>	
			<%
				Cookie cookie = new Cookie("courseID", request.getParameter("course"));
				cookie.setMaxAge(60*60*24*365);
				response.addCookie(cookie);
				cookie = new Cookie("language", request.getParameter("language"));
				cookie.setMaxAge(60*60*24*365);
				response.addCookie(cookie);
				String courseName = DBHandler.getInstance().getCourseName(request.getParameter("course"));
				session.setAttribute("courseName", courseName);
			%>			
			<c:set var="course" value="${param.course}" scope="session"/>
			<c:set var="language" value="${param.language}" scope="session"/>
			<c:redirect url="studentTaskList.jsp"/>
		</c:if>
	</c:if>
</c:if>

<jsp:include page="menu.jsp"/>

<h2>Sign in</h2>


<form name="f" action="error.jsp?errorMsg=Javascript+is+turned+off.+Please+enable+javascript" method="POST"
      onclick="document.f.action='login.jsp<c:if test="${not empty param.role}">?role=<c:out value="${param.role}"/></c:if>'; return true;">
<input type="hidden" name="userAction" value="login">

<div>
	<table id="loginTable">
		<tr>
			<td><b>Username</b></td>
			<td><input type="text" name="username"></td>
		</tr>
		<tr>
			<td><b>Password</b></td>
			<td><input type="password" name="password"></td>
		</tr>
		<c:if test="${empty param.role}">
			<tr>
				<td><b>Course</b></td>
				
				<%	
					List<Course> courses = DBHandler.getInstance().getCourses();
					pageContext.setAttribute("courses", courses);				
				%>
				
				<%-- TODO: haluttiinko näihin default arvot cookien kautta --%>
				
				<td>
					<select name="course">
						<c:if test="${not empty courses}">
							<c:forEach var="course" items="${pageScope.courses}">
								<option id="<c:out value="${course.ID}"/>" value="<c:out value="${course.ID}"/>"><c:out value="${course.name}"/></option>							
							</c:forEach>
						</c:if>	
					</select>
				</td>
			</tr>		
			<tr>
				<td><b>Language</b></td>
				<td>
					<select name="language">
						<option id="EN" value="EN">English</option>
						<option id="FI" value="FI">Finnish</option>
					</select>
				</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="3" class="formButtonCell">
				<br>
				<!--<input type="button" value="Sign in" onClick="javascript:document.f.submit();">-->
				<input type="submit" value="Sign in">
			</td>
		</tr>
	</table>
	
	<c:if test="${empty param.role}">
		<div class="footNote">New user? <a href="signup.jsp">Sign up</a></div>
	</c:if>
	<br>
	<div class="footNote"><a href="send_email.jsp">Forgot your password?</a></div>
</div>

</form>

</body>
</html>