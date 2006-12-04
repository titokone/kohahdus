<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>

<script language="javascript" type="text/javascript">
	function deleteCategory(categoryName) {
		if (window.confirm('Do you really want to delete category '+categoryName+'?')) {
			location.href="teacherTaskList.jsp?action=delete_category&category="+categoryName;
		}
	}
	function updateCategory(categoryName) {
		if (document.categories_form.modified_category.value == "") {
			alert('Category cannot be empty');
			return;
		}
		location.href="teacherTaskList.jsp?action=update_category&category="+categoryName+
					  "&modified_category="+document.categories_form.modified_category.value;
	}
</script>

<c:if test="${param.action=='create_category'}">
	<%
		DBHandler.getInstance().addCategory(request.getParameter("category"));
	%>
</c:if>
<c:if test="${param.action=='delete_category'}">
	<%
		if (!DBHandler.getInstance().checkAndDeleteCategory(request.getParameter("category"))){
	%>
		<span class="errorMsg">Failed to delete category. Category already in use in tasks.</span>
	<%
		}
	%>
</c:if>
<c:if test="${param.action=='update_category'}">
	<%
		if (!DBHandler.getInstance().updateCategory(request.getParameter("category"), request.getParameter("modified_category"))){
	%>
		<span class="errorMsg">Failed to update category.</span>
	<%
		}
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
			<td colspan="2"><input type="text" name="modified_category" value="<c:out value="${category}"/>"></td>
			<td>
				<input value="Update" onclick="Javascript:updateCategory('<c:out value="${category}"/>');" type="button">
				<input value="Delete" onclick="Javascript:deleteCategory('<c:out value="${category}"/>');" type="button">
			</td>
		</tr>
	</c:forEach>	
	<tr>
		<td>New category</td>
		<td><input name="category" type="text"></td>
		<td colspan="2"><input name="create_category_button" value="Create new category" type="submit"></td>
	</tr>
</tbody>
</table>
</form>

