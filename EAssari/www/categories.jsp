<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>

<script language="javascript" type="text/javascript">
	function deleteCategory(categoryName) {
		if (window.confirm('Do you really want to delete category '+categoryName+'?')) {
			location.href="teacherTaskList.jsp?action=delete_category&category="+categoryName;
		}
	}
	function updateCategory(fieldName, categoryName) {
		if (document.categories_form[fieldName].value == "") {
			alert('Category cannot be empty');
			return;
		}
		location.href="teacherTaskList.jsp?action=update_category&category="+categoryName+
					  "&modified_category="+document.categories_form[fieldName].value;
	}
	function addCategory() {
		if (document.categories_form.category.value == "") {
			alert('Category cannot be empty');
			return;
		}
		document.categories_form.submit();
	}
</script>

<c:if test="${param.action=='create_category'}">
	<%
		DBHandler.getInstance().addCategory(request.getParameter("category"));
	%>
</c:if>
<c:if test="${param.action=='delete_category'}">
	<% if (!DBHandler.getInstance().checkAndDeleteCategory(request.getParameter("category"))){ %>
		<span class="errorMsg">Failed to delete category. Category already in use in tasks.</span>
	<% } %>
</c:if>
<c:if test="${param.action=='update_category'}">
	<% if (DBHandler.getInstance().updateCategoryAndTasks(request.getParameter("category"), request.getParameter("modified_category"))){ %>
		Category updated.
	<% } else { %>
		<span class="errorMsg">Failed to update category.</span>
	<% } %>
</c:if>
<%
	List categories = DBHandler.getInstance().getCategories();
	if (categories != null) pageContext.setAttribute("categories", categories);
%>

<form name="categories_form" action="teacherTaskList.jsp" method="post">
<input name="action" value="create_category" type="hidden">
<h2 class="headerAboveListTable">Task categories</h2>
<table class="listTable" border="1">
<tbody>
	<tr>
		<td class="titleBar">Category</td>
		<td class="titleBar">&nbsp;</td>
	</tr>
	<c:forEach var="category" items="${categories}" varStatus="status">
		<tr>
			<td>
				<input type="text" name="category_<c:out value="${status.index}"/>" value="<c:out value="${category}"/>">
			</td>
			<td>
				<input value="Update" onclick="Javascript:updateCategory('category_<c:out value="${status.index}"/>', '<c:out value="${category}"/>');" type="button">
				<input value="Delete" onclick="Javascript:deleteCategory('<c:out value="${category}"/>');" type="button">
			</td>
		</tr>
	</c:forEach>	
	<tr>
		<td>New category <input name="category" type="text"></td>
		<td colspan="2">
			<input onclick="Javascript:addCategory();" value="Create new category" type="button">
		</td>
	</tr>
</tbody>
</table>
</form>

