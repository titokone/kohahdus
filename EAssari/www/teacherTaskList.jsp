<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<%--
	TODO: poista ID kentät listauksista
--%>

<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="login.jsp"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="studentTaskList.jsp"/>
</c:if>

<jsp:include page="menu.jsp"/>

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
			<c:redirect url="error.jsp">
				<c:param name="errorMsg" value="Duplicate course!"/>
			</c:redirect>
	<%		
		}		
	%>
</c:if>

<html>
<head>
<title>Task listing</title>
<script language="javascript" type="text/javascript" src="js/inputValidityFunctions.js"></script>
<script language="javascript" type="text/javascript" src="js/teacherCourseFunctions.js"></script>
<script language="javascript" type="text/javascript" src="js/teacherTaskFunctions.js"></script>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="javascript" type="text/javascript">

var courses;
var tasks;
var coursesAvailable;
var tasksAvailable;

function initPage() {
	initCourses();
	initTasks();
	
	writeCourseList(courses);
	//writeTaskList(tasks);
	sortTasksByCategory(tasks, true);
}

/* fill courses array from database */
function initCourses() {
	courses = new Array();
	var courseCounter = 0;
	
	<%  // get courses from DB
		List<Course> DBcourses = DBHandler.getInstance().getCourses();	
		if (DBcourses != null) pageContext.setAttribute("courses", DBcourses);		
	%>
	<c:if test="${empty courses}">
		coursesAvailable = false;
	</c:if>
	<c:if test="${not empty courses}">
		coursesAvailable = true;
		<c:forEach var="course" items="${pageScope.courses}">	
			courses[courseCounter++] = new Course('<c:out value="${course.courseID}"/>', '<c:out value="${course.name}"/>');
		</c:forEach>	
	</c:if>

}
	
</script>

</head>


<body onLoad="initPage()">

<br>

<table>
<tr>
	<td valign="top">
		<form name="create_course_form" action="teacherTaskList.jsp" method="POST" onSubmit="return checkNewCourseInputValidity()">
		<input type="hidden" name="action" value="create_course">
		<h2 class="headerAboveListTable">Courses</h2>
		<div id="courseList">
		<!-- Course list will be written in Javascript. -->
		</div>
		</form>
		<div id="new_course_creation_feedback" class="errorMsg"></div>		
	</td>
	<td valign="top">
		<c:import url="categories.jsp">
			<c:param name="action" value="${param.action}"/>
			<c:param name="category" value="${param.category}"/>
			<c:param name="modified_category" value="${param.modified_category}"/>
		</c:import>
	</td>
</tr>
<tr>
	<td colspan="2" valign="top">
		<h2 class="headerAboveListTable">Tasks</h2>
		<div id="taskList">
		<!-- Task list will be written in Javascript. -->
		</div>
	</td>
</tr>
</table>

</body>

<script language="javascript" type="text/javascript">

/* fill tasks array from database */
function initTasks() {
	tasks = new Array();
	var taskCounter = 0;
	
	<%	//Get tasks from DB
		List<Task> tasks = DBHandler.getInstance().getTasks();	
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
	%>
	<c:if test="${empty tasks}">
		tasksAvailable = false;		
	</c:if>
	<c:if test="${not empty tasks}">
		tasksAvailable = true;
		<c:forEach var="task" items="${pageScope.tasks}"> 
			tasks[taskCounter++] = new Task('<c:out value="${task.taskID}"/>', '<c:out value="${task.name}"/>',
				'<c:out value="${task.titoTaskType}"/>', '<c:out value="${task.category}"/>',
				'<c:out value="${task.language}"/>', '<c:out value="${task.author}"/>',
				'<c:out value="${task.modificationDate}"/>');
		</c:forEach>
	</c:if>		
}
</script>

</html>
