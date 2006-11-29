<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>




<table id="menuTable">
<tr>
	<c:choose>
		<c:when test="${user.student}">
			<%
				String lang = (String)session.getAttribute("language");
				ResourceBundle menu = LanguageManager.getTextResource(lang , "menu");
			%>
			<td class="linksCell">
				<table>
					<tr>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/student/studentTaskList.jsp"><%=menu.getString("taskList")%></a></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/student/modify_user.jsp"><%=menu.getString("modify")%></a></td>
					</tr>
				</table>
			</td>
			<td class="userCell">
				<table>
					<tr>
						<td><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<c:out value="${user.status}"/>)</td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/login.jsp?action=logout"><%=menu.getString("logout")%></a></td>
					</tr>
				</table>
			</td>
		</c:when>
		<c:when test="${user.teacher || user.admin}">
			<td class="linksCell">
				<table>
					<tr>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/composer.jsp?task_id=EN_TEMPLATE&save_type=new">Create new task (English)</a></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/composer.jsp?task_id=FI_TEMPLATE&save_type=new">Create new task (Finnish)</a></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/teacherTaskList.jsp">Task list</a></td>
						<!--<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/statistics.jsp">Search statistics</a></td>-->
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacher/searchUsers.jsp">Search users</a></td>
					</tr>
				</table>
			</td>
			<td class="userCell">
				<table>
					<tr>
						<td><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<c:out value="${user.status}"/>)</td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/login.jsp?action=logout">Logout</a></td>
					</tr>
				</table>
			</td>
		</c:when>
	</c:choose>	
</tr>
</table>
