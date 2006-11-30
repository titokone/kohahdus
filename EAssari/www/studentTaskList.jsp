<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>

<%-- check that user is logged in --%>
<c:if test="${empty user}">
	<%--Not logged in - redirecting to login --%>
	<c:redirect url="login.jsp"/>	
</c:if>
<c:if test="${empty course}">
	<%--Course not selected - redirecting to login/error? --%>
	<c:redirect url="login.jsp"/>
</c:if>	

<%
	String lang = (String)session.getAttribute("language");
	ResourceBundle rb = LanguageManager.getTextResource(lang , "studentTaskList");
%>



<html>
<head>
<title>TitoTrainer - <%=rb.getString("tasksTitle")%></title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="Javascript" type="text/javascript" src="js/studentTaskFunctions.js"></script>
<script language="Javascript" type="text/javascript" src="js/common.js"></script>
<script language="javascript">

var tasks;
var tasksAvailable;

function initPage() {
	initTaskList();
	writeTaskList();
}

function initTaskList() {
	tasks = new Array();
	var taskCounter = 0;
	var taskStatus;
	<%-- get all tasks from db and store them in page context --%>
	<%	
		String courseID = (String) session.getAttribute("course");
		User u = (User) session.getAttribute("user");
		
		List<Task> tasks = DBHandler.getInstance().getTasks(courseID, u.getUserID());
		if (tasks != null) pageContext.setAttribute("tasks", tasks);
		
		StudentAnswers answers = DBHandler.getInstance().getStudentAnswers(u.getUserID());
		if (answers != null) pageContext.setAttribute("answers", answers);
	%>
	
	<%-- TODO: get student status information from somewhere --%>
	
	<c:set var="accepted" value="0"/>
	<c:set var="unfinished" value="0"/>	

	<c:if test="${empty tasks}">
		tasksAvailable = false;
	</c:if>
	<c:if test="${not empty tasks}">
		tasksAvailable = true;	
		<c:forEach var="task" items="${pageScope.tasks}">
			<c:set var="answer" value="${answers.answerMap[task.taskID]}"/>	
			<c:choose>
				<c:when test="${answer.hasSucceeded}">
					taskStatus = 'success';
					<c:set var="accepted" value="${accepted + 1}"/>
				</c:when>
				<%-- FIXME: Pitäisikö tämä laittaa näin, vai estää null:ien pääseminen tietokantaan? --%>
				<c:when test="${answer.lastTryNumber == 0  || answer.lastTryNumber == null}">
					taskStatus = '-';
				</c:when>
				<c:otherwise>
					taskStatus = 'failure';
					<c:set var="unfinished" value="${unfinished + 1}"/>
				</c:otherwise>
			</c:choose>				

			tasks[taskCounter++] = new Task('<c:out value="${task.taskID}"/>', taskStatus, 
			'<c:out value="${task.name}"/>', '<c:out value="${task.titoTaskType}"/>', 
			'<c:out value="${task.category}"/>', '<c:out value="${task.language}"/>', 
			'<c:out value="${answer.lastTryNumber}"/>');
		</c:forEach>
	</c:if>		
}

/* input tasks into task list */
function writeTaskList() {
	var headerHtml = '<table class="listTable" border="1px" cellspacing="0"><tr>';
	headerHtml += '<td class="titleBar"><%=rb.getString("statusText")%> <a href="javascript: sortTasksByStatus(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByStatus(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar"><%=rb.getString("nameText")%> <a href="javascript: sortTasksByName(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByName(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar"><%=rb.getString("typeText")%> <a href="javascript: sortTasksByType(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByType(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar"><%=rb.getString("categoryText")%> <a href="javascript: sortTasksByCategory(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByCategory(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar"><%=rb.getString("triesText")%> <a href="javascript: sortTasksByTries(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByTries(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar"><%=rb.getString("languageText")%> <a href="javascript: sortTasksByLanguage(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByLanguage(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td></tr>';
	
	var listHtml = '';
	var statusImg;

	if(tasksAvailable == true) {
		for(var i = 0; i < tasks.length; i++) {
			if(tasks[i].status == 'success') {
				statusImg = 'positive.gif'
			} else if(tasks[i].status == 'failure') {
				statusImg = 'negative.gif'			
			} else {
				statusImg = 'blank.gif'
			}
		
			listHtml += '<tr><td align="center"><img src="images/' + statusImg + '"></td>';
			listHtml += '<td><a href="answer_task.jsp?task_id=' + tasks[i].id + '">' + tasks[i].name + '</a></td>';
			listHtml += '<td>' + tasks[i].type + '</td>';
			listHtml += '<td>' + tasks[i].category + '</td>';
			listHtml += '<td style="text-align: center">' + tasks[i].tries + '</td>';
			listHtml += '<td>' + tasks[i].language + '</td></tr>';
		}
	} else {	
		listHtml = '<tr><td colspan="6"><%=rb.getString("noTasksText")%></td></tr>';	
	}
	
	listHtml += '</table>';
	
	var taskListElem = document.getElementById("taskList");
	taskListElem.innerHTML = headerHtml + listHtml;
}
</script>
</head>

<body onLoad="initPage()">

<jsp:include page="menu.jsp"/>

<p>
<tr>
<td>
<table border="0">
	<tr>
		<td class="infoBox"><img src="images/positive.gif" style="vertical-align:middle"> <%=rb.getString("acceptedText")%><br>
			<img src="images/negative.gif" style="vertical-align:middle"> <%=rb.getString("unfinishedText")%></td>
		</td>
	</tr>
</table>
</p>

<div id="taskList">
<!-- Task list will be written in Javascript. -->
</div>

<p><b><%=rb.getString("totalTasksTitle")%>:</b><br>

<%-- TODO: get status info --%>
<c:out value="${accepted}"/> <%=rb.getString("acceptedText")%><br>
<c:out value="${unfinished}"/> <%=rb.getString("unfinishedText")%></p>

</body>
</html>