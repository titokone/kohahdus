<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Error</title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="../styles/titotrainer.css">
</head>

<body>

<jsp:include page="menu.jsp"/>

<h2>TitoTrainer - Error</h2>


Error occurred:<br>
<br>
<c:out value="${param.errorMsg}"/>

</body>
</html>