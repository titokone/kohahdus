<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer</title>
</head>

<body>

<jsp:include page="menu.jsp"/>

<h2>Task saved</h2>


Task xxx saved... not yet! ;D<br>
<br>

Request parameters:<br>
<c:forEach var="pMap" items="${paramValues}">
	<c:out value="${pMap.key}: "/>
	<c:forEach var="value" items="${pMap.value}">
		<c:out value="${value}"/>,
	</c:forEach>
	<br>
</c:forEach>



</body>
</html>