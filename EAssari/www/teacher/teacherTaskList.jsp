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
<script language="javascript" type="text/javascript" src="../js/inputValidityFunctions.js"></script>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="../../styles/titotrainer.css">
<script language="javascript" type="text/javascript">

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
	
	feedbackElem.innerHTML = '';
	
	if(containsHtmlCharacters(courseName)) {
		feedbackElem.innerHTML = 'Course name may not contain characters ", <, >, &.';
		return false;
	}
	
	if((courseName.length < 1) || (courseName.length > 40)) {
		feedbackElem.innerHTML = 'Course name must be 1-40 characters long.';
		return false;
	}
	
	return true;
}
</script>

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

<jsp:include page="../menu.jsp"/>

<c:if test="${param.action=='deleteTask'}">
	<%
		Task task = DBHandler.getInstance().getTask(request.getParameter("taskID"));
		if (task != null) DBHandler.getInstance().removeTask(task);
	%>	
</c:if>

<c:if test="${param.action=='deleteCourse'}">
	<%
		DBHandler.getInstance().removeCourse(request.getParameter("courseID"));
	%>
</c:if>

<c:if test="${param.action=='create_course'}">	
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
			//out.print("ERROR: Duplicate course");
	%>		
			<c:redirect url="../error.jsp">
				<c:param name="errorMsg" value="Duplicate course!"/>
			</c:redirect>
	<%		
		}		
	%>
</c:if>

<br><br>

<table>
	<tr>
		<td><h2>Courses</h2></td>
	</tr>
</table>	

<table class="listTable">
	<tr>
		<td class="titleBar">ID</td>
		<td class="titleBar"><a href="teacherTaskList.jsp?sortCourses=true">Name</a></td>
		<td class="titleBar" colspan="2"></td>
	</tr>
	<%  
		List<Course> courses = DBHandler.getInstance().getCourses();	
		if (courses != null) pageContext.setAttribute("courses", courses);		
	%>
	<c:if test="${empty courses}">
		<tr>
			<td colspan="4">No available courses.</td>
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
				<td><c:out value="${course.courseID}"/></td>
				<td><c:out value="${course.name}"/></td>
				<td><input type="button" value="Statistics" onClick="Javascript:location.href='showStatistics.jsp?courseID=<c:out value="${course.courseID}"/>';"></td>
				<td><input type="button" value="Delete" onclick="Javascript:deleteCourse('<c:out value="${course.name}"/>', '<c:out value="${course.courseID}"/>');"></td>
			</tr>
		</c:forEach>	
	</c:if>
	<form name="create_course_form" action="teacherTaskList.jsp" method="POST" onSubmit="return checkNewCourseInputValidity()">
	<input type="hidden" name="action" value="create_course">
		<tr>
			<td>Course name</td>
			<td><input type="text" name="new_course"></td>
			<td colspan=2><input type="submit" name="create_course_button" value="Create new course"></td>
		</tr>
	</form>
</table>
<div id="new_course_creation_feedback" class="errorMsg"></div>		

<br><br>

<table border="0">
	<tr>
		<td><h2>Tasks</h2></td>
		<!--
		<td width="100">&nbsp;</td>
		<td><input type="button" value="Create task" onclick="location.href = 'composer.jsp?task_id=EN_TEMPLATE&save_type=new'">
		<td><input type="button" value="Create task in finnish" onclick="location.href = 'composer.jsp?task_id=FI_TEMPLATE&save_type=new'">
		-->
	</tr>
</table>

<%-- FIXME: kun halutaan sortata listausta, niin sivu ladataan uudestaan. Voisi ohjata selaimen suoraan task listauksen alkuun eikä sivun alkuun --%>

<table class="listTable">
	<tr>
		<td class="titleBar">ID</td>
		<td class="titleBar"><a href="teacherTaskList.jsp?sortTasks=0">Name</a></td>
		<td class="titleBar"><a href="teacherTaskList.jsp?sortTasks=1">Type</a></td>
		<td class="titleBar"><a href="teacherTaskList.jsp?sortTasks=2">Category</a></td>
		<td class="titleBar"><a href="teacherTaskList.jsp?sortTasks=3">Language</a></td>
		<td class="titleBar"><a href="teacherTaskList.jsp?sortTasks=4">Author</a></td>
		<td class="titleBar">Modification date</a></td>
		<td class="titleBar" colspan="3"></td>
	</tr>
	
	<%	//Get tasks from DB
		List<Task> tasks = DBHandler.getInstance().getTasks();	
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
	%>
	<c:if test="${empty tasks}">
		<tr>
			<td colspan="9">Ei tehtäviä.</td>
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
			%>		
					<c:redirect url="../error.jsp">
						<c:param name="errorMsg" value="Invalid sort parameter!"/>
					</c:redirect>
			<%		
				}
				if (tc != null) {
					Collections.sort(tasks, tc);
				}
			%>				
		</c:if>
	
		<c:forEach var="task" items="${pageScope.tasks}"> 
			<tr>					
				<td><c:out value="${task.taskID}"/></td>
				<td><c:out value="${task.name}"/></td>
				<td><c:out value="${task.titoTaskType}"/></td>
				<td><c:out value="${task.category}"/></td>
				<td><c:out value="${task.language}"/></td>
				<td><c:out value="${task.author}"/></td>
				<td><c:out value="${task.modificationDate}"/></td>
				<td><input type="button" value="Modify" onclick="location.href = 'composer.jsp?task_id=<c:out value="${task.taskID}"/>&save_type=update'"></td>
				<td><input type="button" value="Modify as new" onclick="location.href = 'composer.jsp?task_id=<c:out value="${task.taskID}"/>&save_type=new'"></td>
				<td><input type="button" value="Delete" onclick="Javascript:deleteTask('<c:out value="${task.name}"/>', '<c:out value="${task.taskID}"/>');"></td>
			</tr>
		</c:forEach>
	</c:if>		
</table>
</p>

</body>
</html>
