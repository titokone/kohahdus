<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>


<table style="width:100%;background:#eeeeff;border-style:solid;border-width:2">
<tr>
	<td align="left">
		<c:choose>
			<c:when test="${user.student}">
				&nbsp;&nbsp;&nbsp;<a href="<c:out value="${pageContext.request.contextPath}"/>/www/student/studentTaskList.jsp">Task list</a>
				&nbsp;&nbsp;&nbsp;<a href="<c:out value="${pageContext.request.contextPath}"/>/www/student/modify_user.jsp">Personal information</a>
			</c:when>
			<c:otherwise>
				&nbsp;&nbsp;&nbsp;<a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/composer.jsp?task_id=EN_TEMPLATE&save_type=new">Create new task (English)</a>
				&nbsp;&nbsp;&nbsp;<a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/composer.jsp?task_id=FI_TEMPLATE&save_type=new">Create new task (Finnish)</a>
				&nbsp;&nbsp;&nbsp;<a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/teacherTaskList.jsp">Task list</a>
				<!--&nbsp;&nbsp;&nbsp;<a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/statistics.jsp">Search statistics</a>-->
				&nbsp;&nbsp;&nbsp;<a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/searchUsers.jsp">Search users</a>
			</c:otherwise>
		</c:choose>
	</td>
	<td align="right">
		<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<c:out value="${user.status}"/>)&nbsp;&nbsp;&nbsp;
		<a href="<c:out value="${pageContext.request.contextPath}"/>/www/login.jsp?action=logout">Logout</a>&nbsp;&nbsp;&nbsp;
	</td>
</tr>
</table>
