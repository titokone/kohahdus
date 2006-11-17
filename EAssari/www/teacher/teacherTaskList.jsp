<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<%--
	TODO: poista ID kentät listauksista
--%>



<html>
<head>
<title>Task listing</title>
<script language="javascript" type="text/javascript" src="../js/textValidityFunctions.js"></script>
<script language="Javascript">

function deleteTask(taskName, taskID) {
	if (window.confirm('Do you really want to delete task '+taskName+'?')) {
		location.href="teacherTaskList.jsp?action=deleteTask&taskID="+taskID;
	}
}

function deleteCourse(courseName, courseID) {
	if (window.confirm('Do you really want to delete course '+courseName+'?')) {
		location.href="teacherTaskList.jsp?action=deleteCourse&courseID="+courseID;
	}
}

function checkNewCourseInputValidity() {
	trimWhitespace(document.create_course_form.new_course);

	var courseName = document.create_course_form.new_course.value;
	var feedbackElem = document.getElementById('new_course_creation_feedback');
	
	if(containsHtmlCharacters(courseName)) {
		feedbackElem.innertText = 'Field may not contain characters ", <, >, &.';
		return false;
	}
	
	if((courseName.length < 1) || (courseName.length > 40)) {
		feedbackElem.innertText = 'Course name must be 1-40 characters long.';
		return false;
	}
	
	return true;
}
</script>

</head>


<body>

<c:if test="${empty user}"><script language="Javascript">
	Not logged in - redirecting to login
	<c:redirect url="../login.jsp"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="../student/studentTaskList.jsp"/>
</c:if>

<jsp:include page="../menu.jsp"/>

<c:if test="${param.action=='deleteTask'}">
	<%-- FIXME: Remove this when not needed --%>
	Deleting task <c:out value="${param.taskID}"/>
	<%
		Task task = DBHandler.getInstance().getTask(request.getParameter("taskID"));
		if (task != null) DBHandler.getInstance().removeTask(task);
	%>	
</c:if>

<c:if test="${param.action=='deleteCourse'}">
	<%-- FIXME: Remove this when not needed --%>
	Deleting course <c:out value="${param.courseID}"/>
	<%
		try {
			DBHandler.getInstance().removeCourse(request.getParameter("courseID"));
		} catch(SQLException e) {
			//DBHandler has already logged this event
			
			//TODO: should we redirect to error page or notify about it here
		}
	%>
</c:if>

<c:if test="${param.action=='create_course'}">
	<%-- FIXME: Remove this when not needed --%>
	<p>CREATING COURSE <c:out value="${param.new_course}"/>
	
	<% 	
		String s = request.getParameter("new_course");
						
		//Check whether the given course already exists
		List<Course> courses = DBHandler.getInstance().getCourses();
		
		boolean ok = true;
		for (Course c : courses) {
			if (c.getName().equals(s)) {
				ok = false;
				break;
			}
		}
		
		if (ok) {		
			Course newCourse = new Course();
			newCourse.setName(s);
			DBHandler.getInstance().createCourse(newCourse);
			Log.write("New course "+newCourse.getName()+" inserted in DB.");		
		} else {
			//FIXME: Handle error condition more gracefully
			out.print("ERROR: Duplicate course");
		}		
	%>
</c:if>



<p>
<p>
<table border="0">
	<tr>
		<td><font size="+2"><b>Courses</b></font></td>
	</tr>
</table>	

<table border="0" bgcolor="#000000" cellpadding="4">
	<tr>
		<td bgcolor="#CECECE"><b>ID</b></td>
		<td bgcolor="#CECECE"><a href="teacherTaskList.jsp?sortCourses=true"><b>Name</b></a></td>
		<td bgcolor="#CECECE" colspan="2"><b></b></td>
	</tr>
	<%  
		List<Course> courses = DBHandler.getInstance().getCourses();	
		if (courses != null) pageContext.setAttribute("courses", courses);		
	%>
	<c:if test="${empty courses}">
		<tr>
			<td bgcolor="#FFFFFF" colspan="4">No available courses.</td>
		</tr>
	</c:if>
	<c:if test="${not empty courses}">
		<c:if test="${not empty param.sortCourses}">
			<%-- sort courses with comparator object. Comparison type is stored in request --%>
			<%
				CourseComparator cc = new CourseComparator();
				Collections.sort(courses, cc);
			%>
		</c:if>	
		<c:forEach var="course" items="${pageScope.courses}">	
			<tr>
				<%-- TODO: add implementation for statistics and delete buttons --%>
				<td bgcolor="#FFFFFF"><c:out value="${course.courseID}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${course.name}"/></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Statistics" onClick="Javascript:location.href='showStatistics.jsp?courseID=<c:out value="${course.courseID}"/>';"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Delete" onclick="Javascript:deleteCourse('<c:out value="${course.name}"/>', '<c:out value="${course.courseID}"/>');"></td>
			</tr>
		</c:forEach>	
	</c:if>
	<form name="create_course_form" action="teacherTaskList.jsp" method="POST" onSubmit="return checkNewCourseInputValidity()">
	<input type="hidden" name="action" value="create_course">
		<tr>
			<td bgcolor="#FFFFFF" align="right">Course name</td>
			<td bgcolor="#FFFFFF"><input type="text" name="new_course"></td>
			<td bgcolor="#FFFFFF" colspan=2><input type="submit" name="create_course_button" value="Create new course"></td>
		</tr>
	</form>
</table>
<p id="new_course_creation_feedback"></p>		



<% //FIXME: allaolevan taulukon nappuloiden paikkaa ja tarpeellisuutta voi miettiä
   //ehkä pelkkä tasks otsikko riittää. Toisaalta, jos kursseja on paljon, joudutaan create_task
   //nappulaa haettaessa scrollaamaan ihan sivun alkuun
%>    

<p>
<table border="0">
	<tr>
		<td><font size="+2"><b>Tasks</b></font></td>
		<!--
		<td width="100">&nbsp;</td>
		<td><input type="button" value="Create task" onclick="location.href = 'composer.jsp?task_id=EN_TEMPLATE&save_type=new'">
		<td><input type="button" value="Create task in finnish" onclick="location.href = 'composer.jsp?task_id=FI_TEMPLATE&save_type=new'">
		-->
	</tr>
</table>
</p>

<%-- FIXME: kun halutaan sortata listausta, niin sivu ladataan uudestaan. Voisi ohjata selaimen suoraan task listauksen alkuun eikä sivun alkuun --%>

<table border="0" bgcolor="#000000" cellpadding="4">
	<tr>
		<td bgcolor="#CECECE"><b>ID</b></td>
		<td bgcolor="#CECECE"><a href="teacherTaskList.jsp?sortTasks=0"><b>Name</b></a></td>
		<td bgcolor="#CECECE"><a href="teacherTaskList.jsp?sortTasks=1"><b>Type</b></a></td>
		<td bgcolor="#CECECE"><a href="teacherTaskList.jsp?sortTasks=2"><b>Category</b></a></td>
		<td bgcolor="#CECECE"><a href="teacherTaskList.jsp?sortTasks=3"><b>Language</b></a></td>
		<td bgcolor="#CECECE"><a href="teacherTaskList.jsp?sortTasks=4"><b>Author</b></a></td>
		<td bgcolor="#CECECE" colspan="3"><b></b></td>
	</tr>
	
	<%	//Get tasks from DB
		LinkedList<Task> tasks = DBHandler.getInstance().getTasks();	
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
	%>
	<c:if test="${empty tasks}">
		<tr>
			<td bgcolor="#FFFFFF" colspan="9">Ei tehtäviä.</td>
		</tr>
	</c:if>
	<c:if test="${not empty tasks}">
	
		<c:if test="${not empty param.sortTasks}">
			<%
				TaskComparator tc = null;
				String sortRequest = request.getParameter("sortTasks");
				try {
					int sort = Integer.parseInt(sortRequest);
					tc = new TaskComparator(sort);
				} catch (NumberFormatException e) {
					Log.write("TeacherTaskList: Invalid sort parameter");
				}
				if (tc != null) {
					Collections.sort(tasks, tc);
				}
			%>				
		</c:if>
	
		<c:forEach var="task" items="${pageScope.tasks}"> 
			<tr>
			
				<%-- TODO: add implementation for modify, modify as new and delete --%>
			
				<td bgcolor="#FFFFFF"><c:out value="${task.taskID}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.name}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.titoTaskType}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.category}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.language}"/></td>
				<td bgcolor="#FFFFFF"><c:out value="${task.author}"/></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Modify" onclick="location.href = 'composer.jsp?task_id=<c:out value="${task.taskID}"/>&save_type=update'"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Modify as new" onclick="location.href = 'composer.jsp?task_id=<c:out value="${task.taskID}"/>&save_type=new'"></td>
				<td bgcolor="#FFFFFF"><input type="button" value="Delete" onclick="Javascript:deleteTask('<c:out value="${task.name}"/>', '<c:out value="${task.taskID}"/>');"></td>
			</tr>
		</c:forEach>
	</c:if>		
</table>
</p>

</body>
</html>
