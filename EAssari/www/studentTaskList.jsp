<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>

<%-- check that user is logged in --%>
<c:if test="${empty user}">
	<%--Not logged in - redirecting to login --%>
	<c:redirect url="../login.jsp"/>	
</c:if>
<c:if test="${empty course}">
	<%--Course not selected - redirecting to login/error? --%>
	<c:redirect url="../login.jsp"/>
</c:if>	

<%
	String lang = (String)session.getAttribute("language");
	ResourceBundle rb = LanguageManager.getTextResource(lang , "studentTaskList");
%>



<html>
<head>
<title>TitoTrainer - <%=rb.getString("tasksTitle")%></title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="../../styles/titotrainer.css">
<script language="Javascript" type="text/javascript" src="../js/common.js"></script>
<script language="javascript">



var tasks;
var tasksAvailable;

/* Object constructor for Task objects */
function Task(id, status, name, type, category, language, tries) {
	this.id = id;
	this.status = status;
	this.name = name;
	this.type = type;
	this.category = category;
	this.language = language
	this.tries = tries;
}

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
	var headerHtml = '<table class="listTable"><tr>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByStatus()"><%=rb.getString("statusText")%></a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByName()"><%=rb.getString("nameText")%></a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByType()"><%=rb.getString("typeText")%></a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByCategory()"><%=rb.getString("categoryText")%></a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByTries()"><%=rb.getString("triesText")%></a></td>';
	headerHtml += '<td class="titleBar"><a href="javascript:sortTasksByLanguage()"><%=rb.getString("languageText")%></a></td></tr>';
	
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
		
			listHtml += '<tr><td align="center"><img src="' + statusImg + '"></td>';
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

/* use insertion sort to sort tasks into order by task status - order: success, failure, - (not tried)*/
function sortTasksByStatus() {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		while (j > 0 && tasks[j-1].status.toLowerCase() < temp.status.toLowerCase()) {
			tasks[j] = tasks[j-1]; 
			--j;
		}
		tasks[j] = temp;
	}

	writeTaskList();
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

/* use insertion sort to sort tasks into reverse order by number of tries */
function sortTasksByTries() {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		while (j > 0 && tasks[j-1].tries < temp.tries) {
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
</script>
</head>

<body onLoad="initPage()">

<jsp:include page="../menu.jsp"/>

<p>
<tr>
<td>
<table border="0">
	<tr>
		<td class="infoBox"><img src="positive.gif" style="vertical-align:middle"> <%=rb.getString("acceptedText")%><br>
			<img src="negative.gif" style="vertical-align:middle"> <%=rb.getString("unfinishedText")%></td>
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