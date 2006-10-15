<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<%-- 
	TeacherTaskList is able to list all courses and tasks that reside in DB.
	Inserting a course in DB should work also.
	Current implementation lacks delete functionality and links to task creation and
	statistics. We are also missing the possibility of arranging listing by name, category, etc.
--%>	



<html>
<head>
<title>Task listing</title>
</head>

<body>

<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="../login.jsp"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="../student/studentTaskList.jsp"/>
</c:if>
		
<c:if test="${param.action=='create_course'}">
	<p>CREATING COURSE <c:out value="${param.new_course}"/>
	
	<% 	//TODO: luodaan kurssiolio ja annetaan dbHandlerille
		//Course newCourse = new Course();
		//FIXME: ID == name ??
		String s = request.getParameter("new_course");
						
		//Check whether the given course already exists
		List<MockCourse> courses = MockCourse.getCourses();
		//List<Course> courses = DBHandler.getInstance().getCourses();
		
		boolean ok = true;
		for (Course c : courses) {
			if (c.getName().equals(s) || c.getCourseID().equals(s)) {
				ok = false;
				break;
			}
		}
		
		
		//FIXME: vaihdettava DBHandler töihin
		if (ok) {		
			MockCourse newCourse = new MockCourse(s,s);
			MockCourse.createCourse(newCourse);
			//Log.write("Kurssi luotu");
			
			//Course() params: int modules, String courseID, String  coursetype, String courseStyle
			//Course newCourse = new Course(0, s, "titotrainer", "");
			//DBHandler.getInstance().createCourse(newCourse);
					
		} else {
			out.print("Duplicate course");
		}		
	%>
</c:if>

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
	<%  //FIXME: uncomment DBHandler to integrate with system (also from tasks)
		List<MockCourse> courses = MockCourse.getCourses(); 
		//LinkedList<Course> courses = DBHandler.getInstance().getCourses();	
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
				<%-- TODO: add implementation for statisics and delete buttons --%>
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
		//FIXME: uncomment DBHandler to integrate with other system (also from courses)
		List<MockTask> tasks = MockTask.getTasks(); 
		//LinkedList<Task> tasks = DBHandler.getInstance().getTasks();	
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
			
				<%-- TODO: add implementation for modify, modify as new and delete --%>
			
				<td bgcolor="#FFFFFF"><c:out value="${task.taskID}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.name}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.titoTaskType}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.category}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.language}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.author}"/></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Modify"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Modify as new"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Delete" onclick="window.confirm('Do you really want to delete task?');"></td>
			</tr>
		</c:forEach>
	</c:if>		
</table>
</p>

</body>
</html>
