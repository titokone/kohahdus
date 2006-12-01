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
						<td><span style="font-family:impact; font-size:30px">TitoTrainer</span></a></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/studentTaskList.jsp"><%=menu.getString("taskList")%></a></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/modify_user.jsp"><%=menu.getString("modify")%></a></td>
					</tr>
				</table>
			</td>
			<td class="userCell">
				<table>
					<tr>
						<td>Kurssi: <c:out value="${courseName}"/></td>
						<td><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<c:out value="${user.status}"/>)</td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/login.jsp?userAction=logout"><%=menu.getString("logout")%></a></td>
					</tr>
				</table>
			</td>
		</c:when>
		<c:when test="${user.teacher || user.admin}">
			<td class="linksCell">
				<table>
					<tr>
						<td><span style="font-family:impact; font-size:30px">TitoTrainer</span></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/composer.jsp?task_id=EN_TEMPLATE&save_type=new">Create new task (English)</a></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/composer.jsp?task_id=FI_TEMPLATE&save_type=new">Create new task (Finnish)</a></td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/teacherTaskList.jsp">Task list</a></td>
						<!--<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/statistics.jsp">Search statistics</a></td>-->
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/searchUsers.jsp">Search users</a></td>
					</tr>
				</table>
			</td>
			<td class="userCell">
				<table>
					<tr>
						<td><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<c:out value="${user.status}"/>)</td>
						<td><a href="<c:out value="${pageContext.request.contextPath}"/>/www/login.jsp?userAction=logout">Logout</a></td>
					</tr>
				</table>
			</td>
		</c:when>
		<c:when test="${empty user}">
			<td class="linksCell">
				<table>
					<tr>
						<td><span style="font-family:impact; font-size:30px">TitoTrainer</span></td>
					</tr>
				</table>
			</td>
			<td class="userCell">
				<table>
					<tr>
						<td></td>
					</tr>
				</table>
			</td>
		</c:when>
	</c:choose>	
</tr>
</table>
