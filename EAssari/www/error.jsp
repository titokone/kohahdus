<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Sign in</title>
</head>

<body>

<h2>TitoTrainer - Error</h2>

<jsp:include page="${pageContext.request.contextPath + '/menu.jsp'}"/>

Error occurred:<br>
<br>
<c:out value="${param.errorMgs}"/>

</body>
</html>