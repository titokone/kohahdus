<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>



<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="../login.jsp?role=teacher"/>	
</c:if>

<table border="0" style="width:100%">
<tr>
	<td align="left">
		<c:choose>
			<c:when test="${user.student}">
				<a href="studentTaskList.jsp">Task list</a>
				<a href="modify_user.jsp">Personal information</a>
			</c:when>
			<c:otherwise>
				<a href="composer.jsp">Create new task</a>
				<a href="teacherTaskList.jsp">Task list</a>
				<a href="statistics.jsp">Statistics</a>
			</c:otherwise>
		<c:choose>
	</td>
	<td align="right">
		<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<c:out value="${user.status}"/>)
		<a href="login.jsp?action=logout">Logout</a>
	</td>
</tr>
</table>
