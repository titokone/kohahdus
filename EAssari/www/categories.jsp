<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>


<c:if test="${param.action=='create_category'}">
	<%
		DBHandler.getInstance().addCategory(request.getParameter("new_category"));
	%>
</c:if>
<%
	List<String> categories = DBHandler.getInstance().getCategories();
	if (categories != null) pageContext.setAttribute("categories", categories);
%>

<form name="categories_form" action="teacherTaskList.jsp" method="post">
<input name="action" value="create_category" type="hidden">
<h2 class="headerAboveListTable">Categories</h2>
<table class="listTable" border="1">
<tbody>
	<tr>
		<td colspan="2" class="titleBar">Category</td>
		<td class="titleBar">&nbsp;</td>
	</tr>
	<c:forEach var="category" items="${categories}">
		<tr>
			<td colspan="2"><c:out value="${category}"/></td>
			<td>
				<input value="Delete" onclick="Javascript:deleteCategory('<c:out value="${category}"/>');" type="button">
			</td>
		</tr>
	</c:forEach>	
	<tr>
		<td>New category</td>
		<td><input name="new_category" type="text"></td>
		<td colspan="2"><input name="create_category_button" value="Create new category" type="submit"></td>
	</tr>
</tbody>
</table>
</form>

