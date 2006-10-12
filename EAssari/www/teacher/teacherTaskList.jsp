<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>


<html>
<head>
<title>Task listing</title>
</head>

<body>

<% 	//TODO: session oikeellisuuden tarkistus - muut kuin opet ohjataan muualle


	//TODO: kurssin luonnin käsittely....


%>



<p>
<table border="0">
	<tr>
		<td><font size="+2"><b>Courses</b></font></td>
		<td width="100">&nbsp;</td>
		<td><input type="button" value="Create task" onclick="location.href = 'create_task.jsp'">
		<input type="button" value="Statistics" onclick="location.href = 'statistics.jsp'"></td>
	</tr>
</table>
</p>

<p>

<table border="0" bgcolor="#000000" cellpadding="4">
	<tr>
		<td bgcolor="#CECECE"><b>ID</b></td>
		<td bgcolor="#CECECE"><b>Name</b></td>
		<td bgcolor="#CECECE" colspan="2"><b></b></td>
	</tr>
	<% 
		//Course[] courses = MockCourse.getCourses(); //DBHandler().getInstance().getCourses();	
		LinkedList<Course> courses = DBHandler.getInstance().getCourses();	
		if (courses != null) pageContext.setAttribute("courses", courses);
	%>
	<c:if test="${empty courses}">
		<tr>
			<td bgcolor="#FFFFFF" colspan="4">Ei kursseja.</td>
		</tr>
	</c:if>
	<c:if test="${not empty courses}">
		<c:forEach var="course" items="${pageScope.courses}">	
			<tr>
				<td bgcolor="#FFFFFF"><c:out value="${course.courseID}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${course.name}"/></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Statistics"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Delete" onclick="window.confirm('Do you really want to delete course?');"></td>
			</tr>
		</c:forEach>	
	</c:if>
</table>

<form name="create_course_form" action="teacherTaskList.jsp" method="POST">
<input type="hidden" name="action" value="create_course">

<table border="0" bgcolor="#000000" cellpadding="4">
	<tr>
		<td bgcolor="#CECECE"><b>Create new course</b></td>
		<td bgcolor="#CECECE"><input type="text" name="new_course"></td>
		<td bgcolor="#CECECE"><input type="submit" name="create_course_button" value="Create"></td>
	</tr>
</table>
</form>	



<% //FIXME: allaolevan taulukon nappuloiden paikkaa ja tarpeellisuutta voi miettiä
   //ehkä pelkkä tasks otsikko riittää. Toisaalta, jos kursseja on paljon, joudutaan create_task
   //nappulaa haettaessa scrollaamaan ihan sivun alkuun
%>    

<p>
<table border="0">
	<tr>
		<td><font size="+2"><b>Tasks</b></font></td>
		<td width="100">&nbsp;</td>
		<td><input type="button" value="Create task" onclick="location.href = 'create_task.jsp'">
		<input type="button" value="Statistics" onclick="location.href = 'statistics.jsp'"></td>
	</tr>
</table>
</p>


<table border="0" bgcolor="#000000" cellpadding="4">
	<tr>
		<td bgcolor="#CECECE"><b>ID</b></td>
		<td bgcolor="#CECECE"><b>Name</b></td>
		<td bgcolor="#CECECE"><b>Type</b></td>
		<td bgcolor="#CECECE"><b>Category</b></td>
		<td bgcolor="#CECECE"><b>Language</b></td>
		<td bgcolor="#CECECE"><b>Author</b></td>
		<td bgcolor="#CECECE" colspan="3"><b></b></td>
	</tr>
	
	<%	//Get tasks from DB
		//FIXME: DBHandler provides getTasks method for a speccific course.
		//In this case one would need a generic version just to get all courses
		//Task[] tasks = MockTask.getTasks(); //DBHandler.getTasks();
		LinkedList<Task> tasks = DBHandler.getInstance().getTasks();	
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
	%>
	<c:if test="${empty tasks}">
		<tr>
			<td bgcolor="#FFFFFF" colspan="9">Ei tehtäviä.</td>
		</tr>
	</c:if>
	<c:if test="${not empty tasks}">
		<c:forEach var="task" items="${pageScope.tasks}"> 
			<tr>
				<td bgcolor="#FFFFFF"><c:out value="${task.taskID}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.name}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.taskTypeString}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.category}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.language}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.author}"/></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Modify"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Modify as new"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Poista" onclick="window.confirm('Do you really want to delete task?');"></td>
			</tr>
		</c:forEach>
	</c:if>		
</table>
</p>

</body>
</html>
