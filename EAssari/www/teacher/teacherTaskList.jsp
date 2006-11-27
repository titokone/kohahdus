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

<html>
<head>
<title>Task listing</title>
<script language="javascript" type="text/javascript" src="../js/inputValidityFunctions.js"></script>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="../../styles/titotrainer.css">
<script language="javascript" type="text/javascript">

var courses;
var tasks;
var coursesAvailable;
var tasksAvailable;

/* Object constructor for Course objects */
function Course(id, name) {
	this.name = name;
	this.id = id;
}

/* Object constructor for Task objects */
function Task(id, name, type, category, language, author, modificationDate) {
	this.id = id;
	this.name = name;
	this.type = type;
	this.category = category;
	this.language = language
	this.author = author;
	this.modificationDate = modificationDate;
}

function initPage() {
	initCourses();
	initTasks();
	
	writeCourseList();
	writeTaskList();
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

/* input courses into course list */
function writeCourseList() {

	var headerHtml = '<table class="listTable"><tr><td class="titleBar">ID</td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortCoursesByName()">Name</a></td>';
	headerHtml += '<td class="titleBar" colspan="2">&nbsp;</td></tr>';
	
	var listHtml = '';

	if(coursesAvailable == true) {
		for(var i = 0; i < courses.length; i++) {
			listHtml += '<tr><td>' + courses[i].id + '</td>';
			listHtml += '<td>' + courses[i].name + '</td>';
			listHtml += '<td><input type="button" value="Statistics" onClick="Javascript:location.href=\'showStatistics.jsp?courseID=' + courses[i].id + '\';"></td>';
			listHtml += '<td><input type="button" value="Delete" onclick="Javascript:deleteCourse(\'' + courses[i].name + '\', \'' + courses[i].id + '\');"></td></tr>';
		}
	} else {	
		listHtml = '<tr><td colspan="4">No available courses.</td></tr>';	
	}
	var newCourseHtml = '<tr><td>Course name</td><td><input type="text" name="new_course"></td>';
	newCourseHtml += '<td colspan="2"><input type="submit" name="create_course_button" value="Create new course"></td>';
	newCourseHtml += '</tr></table>';
	
	var courseListElem = document.getElementById("courseList");
	courseListElem.innerHTML = headerHtml + listHtml + newCourseHtml; 
}

/* input tasks into task list */
function writeTaskList() {
	var headerHtml = '<table class="listTable"><tr><td class="titleBar">ID</td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByName()">Name</a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByType()">Type</a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByCategory()">Category</a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByLanguage()">Language</a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript: sortTasksByAuthor()">Author</a></td>';
	headerHtml += '<td class="titleBar">Modification date</a></td>';
	headerHtml += '<td class="titleBar" colspan="3"></td></tr>';
	
	var listHtml = '';

	if(tasksAvailable == true) {
		for(var i = 0; i < tasks.length; i++) {
			listHtml += '<tr><td>' + tasks[i].id + '</td>';
			listHtml += '<td>' + tasks[i].name + '</td>';
			listHtml += '<td>' + tasks[i].type + '</td>';
			listHtml += '<td>' + tasks[i].category + '</td>';
			listHtml += '<td>' + tasks[i].language + '</td>';
			listHtml += '<td>' + tasks[i].author + '</td>';
			listHtml += '<td>' + tasks[i].modificationDate + '</td>';
			listHtml += '<td><input type="button" value="Modify" onclick="location.href = \'composer.jsp?task_id=' + tasks[i].id + '&save_type=update\'"></td>';
			listHtml += '<td><input type="button" value="Modify as new" onclick="location.href = \'composer.jsp?task_id=' + tasks[i].id + '&save_type=new\'"></td>';
			listHtml += '<td><input type="button" value="Delete" onclick="Javascript:deleteTask(\'' + tasks[i].name + '\', \'' + tasks[i].id + '\');"></td></tr>';
		}
	} else {	
		listHtml = '<tr><td colspan="9">No tasks.</td></tr>';	
	}
	
	listHtml += '</table>';
	
	var taskListElem = document.getElementById("taskList");
	taskListElem.innerHTML = headerHtml + listHtml;
}

/* use insertion sort to sort courses into alphabetical order */
function sortCoursesByName() {
	for(var i = 1; i < courses.length; i++) {
		var temp = courses[i];
		var j = i;
		
		while (j > 0 && courses[j-1].name.toLowerCase() > temp.name.toLowerCase()) {
			courses[j] = courses[j-1]; 
			--j;
		}
		courses[j] = temp;
	}

	writeCourseList();
}

/* use insertion sort to sort tasks into alphabetical order by task name */
function sortTasksByName() {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		while (j > 0 && tasks[j-1].name.toLowerCase() > temp.name.toLowerCase()) {
			tasks[j] = tasks[j-1]; 
			--j;
		}
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by task type */
function sortTasksByType() {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		while (j > 0 && tasks[j-1].type.toLowerCase() > temp.type.toLowerCase()) {
			tasks[j] = tasks[j-1]; 
			--j;
		}
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by task category */
function sortTasksByCategory() {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		while (j > 0 && tasks[j-1].category.toLowerCase() > temp.category.toLowerCase()) {
			tasks[j] = tasks[j-1]; 
			--j;
		}
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by task language */
function sortTasksByLanguage() {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		while (j > 0 && tasks[j-1].language.toLowerCase() > temp.language.toLowerCase()) {
			tasks[j] = tasks[j-1]; 
			--j;
		}
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by author */
function sortTasksByAuthor() {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		while (j > 0 && tasks[j-1].author.toLowerCase() > temp.author.toLowerCase()) {
			tasks[j] = tasks[j-1]; 
			--j;
		}
		tasks[j] = temp;
	}

	writeTaskList();
}

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


<body onLoad="initPage()">

<br>

<form name="create_course_form" action="teacherTaskList.jsp" method="POST" onSubmit="return checkNewCourseInputValidity()">
<input type="hidden" name="action" value="create_course">
<h2 class="headerAboveListTable">Courses</h2>
<div id="courseList">
<!-- Course list will be written in Javascript. -->
</div>
</form>
<div id="new_course_creation_feedback" class="errorMsg"></div>		

<br>

<h2 class="headerAboveListTable">Tasks</h2>
<div id="taskList">
<!-- Task list will be written in Javascript. -->
</div>
</p>

</body>
</html>
