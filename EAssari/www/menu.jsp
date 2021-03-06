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
						<td><a href="studentTaskList.jsp"><%=menu.getString("taskList")%></a></td>
						<td><a href="modify_user.jsp"><%=menu.getString("modify")%></a></td>
					</tr>
				</table>
			</td>
			<td class="userCell">
				<table>
					<tr>
						<td><%=menu.getString("courseText")%>: <c:out value="${courseName}"/></td>
						<td><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<%=menu.getString("studentText")%>)</td>
						<td><a href="login.jsp?userAction=logout"><%=menu.getString("logout")%></a></td>
					</tr>
				</table>
			</td>
		</c:when>
		<c:when test="${user.teacher || user.admin}">
			<td class="linksCell">
				<table>
					<tr>
						<td><span style="font-family:impact; font-size:30px">TitoTrainer</span></td>
						<td><a href="composer.jsp?task_id=EN_TEMPLATE&save_type=new">New task (English)</a></td>
						<td><a href="composer.jsp?task_id=FI_TEMPLATE&save_type=new">New task (Finnish)</a></td>
						<td><a href="teacherTaskList.jsp">Task list</a></td>
						<td><a href="searchUsers.jsp">Search users</a></td>
						<td><a href="modify_user.jsp">Modify own account</a></td>
					</tr>
				</table>
			</td>
			<td class="userCell">
				<table>
					<tr>
						<td><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/> (<c:out value="${user.status}"/>)</td>
						<td><a href="login.jsp?role=teacher&userAction=logout">Logout</a></td>
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
