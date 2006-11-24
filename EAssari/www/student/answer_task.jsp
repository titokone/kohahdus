<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>

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
	ResourceBundle rb = LanguageManager.getTextResource(lang , "answer_task");
%>

<c:if test="${empty param.analyzed}">
	<%	
		// Get task from DB if execute button has not been pushed 
		Task task = DBHandler.getInstance().getTask(request.getParameter("task_id"));	
		if (task != null) {
			session.setAttribute("task", task);
		    // Get all criteria from the database
			CriterionMap criteria = DBHandler.getInstance().getCriteriaMap(task);
			if (!criteria.isEmpty()) session.setAttribute("criteria", criteria);
		} 
	%>
</c:if>

<html>
<head>
<title>TitoTrainer - <%=rb.getString("answerTitle")%></title>
<style>
	span.helpButton {padding: 2px 4px; border-style: outset; background-color: #B0C4DE; border-color: #6495ED; color: #000000; text-decoration: none;}
</style>
<script language="javascript" type="text/javascript" src="../js/visibilityFunctions.js"></script>
<script language="javascript" type="text/javascript" src="../js/inputValidityFunctions.js"></script>
<script language="javascript" type="text/javascript">

var titokoneVisible = false;

function helpWindow(topic) {

	var helpWin = window.open('', 'helpWin', 'width=300,height=200,toolbar=0,status=0');

	var helpHtml = '';

	if(topic == 'input_keyboard') {
		helpHtml = '<p><%=rb.getString("keyboardInputHelpMsg")%></p>';
	}

	helpWin.document.write(helpHtml);
	helpWin.document.close();
}

function checkInput() {
	trimWhitespace(document.answerform.keyboardInput);

	if(document.answerform.keyboardInput.value == "") {
		return;
	}

	var numberOfKeyboardInput = amountOfTitokoneInputOutput(document.answerform.keyboardInput.value);
	
	if(numberOfKeyboardInput == -1) {
		alert("Keyboard input must be integers separated by commas.");
		document.answerform.keyboardInput.focus();
	}
}

function showhideTitokoneReport() {
	if(titokoneVisible == false) {
		showElementById('titokone_report');
		document.answerform.titokone_report_button.value = "<%=rb.getString("hideTitokoneReportButtonValue")%>";
		titokoneVisible = true;
	} else {
		hideElementById('titokone_report');
		document.answerform.titokone_report_button.value = "<%=rb.getString("showTitokoneReportButtonValue")%>";
		titokoneVisible = false;
	}
}
</script>
</head>

<body>

<jsp:include page="../menu.jsp"/>

<h2><c:out value="${task.name}"/></h2>

<table border="1" width="750" cellpadding="2">
	<tr>
		<td align="center" bgcolor="#6495ED"><b><%=rb.getString("instructions")%></b></td>
	</tr>
	<tr>
		<%--<td><pre><c:out value="${task.description}"/></pre></td>--%>
		<td><textarea readonly cols="100" rows="20"><c:out value="${task.description}"/></textarea></td>
	</tr>
</table>

<br>

<form name="answerform" action="../../Answer" method="POST">
	<table border="0">
		<tr>
			<td>
				<table border="1">
					<tr>
						<td colspan="2" align="center" bgcolor="#6495ED"><b><%=rb.getString("input")%></b></td>
					</tr>
					<tr>
						<td width="80"><b><%=rb.getString("keyboardInput")%>&nbsp;</b></td>
						<td><input name="keyboardInput" type="text" size="90" value="<c:out value="${keyboardInput}"/>" onBlur="checkInput()"></td>
					</tr>
				</table>
			</td>
			<td>&nbsp;&nbsp;</td>
			<td><a href="javascript:helpWindow('input_keyboard')"><span class="helpButton"><b>?</b></span></a></td>
		</tr>
	</table>

	<br>

	<table border="1" width="750" cellpadding="2">
		<tr>
			<td align="center" bgcolor="#6495ED"><b><%=rb.getString("programCode")%></b></td>
		</tr>
		<tr>
			<td>
				<c:if test="${task.fillInTask}">
					<div><b><pre><c:out value="${task.fillInPreCode}"/></pre></b><br><br></div>
				</c:if>	
				<textarea name="programCode" cols="90" rows="40"><c:out value="${programCode}"/></textarea>
				<c:if test="${task.fillInTask}">
					<div><br><b><pre><c:out value="${task.fillInPostCode}"/></pre></b></div>
				</c:if>
			</td>
		</tr>
	</table>

	<br>

	<input type="submit" value="<%=rb.getString("executeButtonValue")%>"> 
	<input type="button" name="titokone_report_button" value="<%=rb.getString("showTitokoneReportButtonValue")%>" onClick="showhideTitokoneReport()">

</form>

<c:if test="${param.analyzed == 'true'}">
	<div id="titokone_report" style="display: none">
		<hr><b><%=rb.getString("titokoneReportTitle")%></b>
		<c:out value="${feedback.titoState}"/>
	</div>
	<hr>
	<table border="1" width="750" cellpadding="2">
		<tr>
			<td colspan="2" align="center" bgcolor="#6495ED"><b><%=rb.getString("gradingTitle")%></b></td>
		</tr>
		<tr>
			<td width="10%"><b><%=rb.getString("gradeText")%></b></td>
			<td width="90%">
				<c:choose>
					<c:when test="${feedback.successful == 'true'}">
						<%=rb.getString("taskSuccessfulText")%>
					</c:when>
					<c:otherwise>
						<%=rb.getString("taskFailedText")%>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td width="10%"><b><%=rb.getString("commentsText")%>&nbsp;</b></td>
			<td width="90%"><c:out value="${feedback.overallFeedback}"/></td>
		</tr>
	</table>
	<br>
	<table border="1" width="750" cellpadding="2">
		<tr>
			<td colspan="3" align="center" bgcolor="#6495ED"><b><%=rb.getString("criteriaText")%></b></td>
		</tr>
		<c:forEach var="criterionFeedback" items="${feedback.criteriaFeedback}">
			<tr>
				<td width="10%"><b><c:out value="${criterionFeedback.passedAcceptanceTest}"/></b></td>
				<td width="10%"><b><c:out value="${criterionFeedback.name}"/></b></td>
				<td width="80%"><c:out value="${criterionFeedback.feedback}"/></td>
			</tr>
		</c:forEach>
	</table>
</c:if>

<%-- If user has answered a task all task specific info should be purged from session --%>
<c:remove var="programCode" scope="session"/>
<c:remove var="keyboardInput" scope="session"/>

</body>
</html>

