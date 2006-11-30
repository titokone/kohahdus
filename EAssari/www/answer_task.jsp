<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>

<%-- check that user is logged in --%>
<c:if test="${empty user}">
	<%--Not logged in - redirecting to login --%>
	<c:redirect url="login.jsp"/>	
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
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="javascript" type="text/javascript" src="js/visibilityFunctions.js"></script>
<script language="javascript" type="text/javascript" src="js/inputValidityFunctions.js"></script>
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
	document.getElementById("inputFeedback").style.display = "none";

	if(document.answerform.keyboardInput.value == "") {
		return true;
	}

	if(!isValidTitokoneInputOutput(document.answerform.keyboardInput.value)) {
		document.getElementById("inputFeedback").style.display = "block";
		document.answerform.keyboardInput.focus();
		return false;
	}
	
	return true;
}

function showhideTitokoneReport() {
	if(titokoneVisible == false) {
		showElementById('titokone_report');
		document.answerform.titokone_report_button.value = "<%=rb.getString("hideTitokoneReportButtonValue")%>";
		titokoneVisible = true;
		location.href = "#output";
	} else {
		hideElementById('titokone_report');
		document.answerform.titokone_report_button.value = "<%=rb.getString("showTitokoneReportButtonValue")%>";
		titokoneVisible = false;
	}
}

function setDefaultInput(){
	var keyb = document.getElementById("keyboardInput");
	keyb.value = '<c:out value="${task.publicInput}"/>';
}
</script>
</head>

<body>

<jsp:include page="menu.jsp"/>

<h2><c:out value="${task.name}"/></h2>


<form name="answerform" action="../Answer" method="POST" onSubmit="return checkInput()">
<table border="0">
<tr>
	<td colspan="2">
	<table class="presentationTable">
		<tr>
			<td class="titleBar"><%=rb.getString("instructions")%></td>
		</tr>
		<tr>
			<%--<td><pre><c:out value="${task.description}"/></pre></td>--%>
			<td><textarea readonly style="width:100%" rows="20"><c:out value="${task.description}"/></textarea></td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td colspan="2">
		<table class="presentationTable">
			<tr>
				<td colspan="3" class="titleBar">
					<%=rb.getString("input")%>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:helpWindow('input_keyboard')">?</a>
				</td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${not empty keyboardInput}">
						<c:set var="input" value="${keyboardInput}"/>
					</c:when>
					<c:otherwise>
						<c:set var="input" value="${task.publicInput}"/>
					</c:otherwise>
				</c:choose>

				<td><b><%=rb.getString("keyboardInput")%>&nbsp;</b></td>
				<td style="width:100%">
					<input id="keyboardInput" name="keyboardInput" type="text" style="width:100%" value="<c:out value="${input}"/>">
				</td>
				<td>
					<input type="button" value="<%=rb.getString("keyboardInputRestore")%>" onClick="javascript:setDefaultInput();">
				</td>
			</tr>
		</table>
	</td>
</tr>
<tr id="inputFeedback" style="display:none;">
	<td class="errorMsg"><!--// LANGPROB: <% //=rb.getString("keyboardInputErrorMsg")%> --></td>
</tr>
<tr>
	<td valign="top">
	
		<table class="presentationTable" style="width:500px">
			<tr>
				<td class="titleBar">
					<a name="code"></a>
					<%=rb.getString("programCode")%>
				</td>
			</tr>
			<tr>
				<td>
					<c:if test="${task.fillInTask}">
						<div><b><pre><c:out value="${task.fillInPreCode}"/></pre></b><br><br></div>
					</c:if>	
					<textarea name="programCode" style="width:100%" rows="40"><c:out value="${programCode}"/></textarea>
					<c:if test="${task.fillInTask}">
						<div><br><b><pre><c:out value="${task.fillInPostCode}"/></pre></b></div>
					</c:if>
				</td>
			</tr>
		</table>
	
		<br>
	
		<input type="submit" value="<%=rb.getString("executeButtonValue")%>"> 
		<c:if test="${param.analyzed == 'true'}">
			<input type="button" name="titokone_report_button" value="<%=rb.getString("showTitokoneReportButtonValue")%>" onClick="showhideTitokoneReport()">
		</c:if>
	</td>
	<td valign="top" width="500px">
		<c:if test="${param.analyzed == 'true'}">
			<table class="presentationTable">
				<tr>
					<td colspan="2" class="titleBar"><b><%=rb.getString("gradingTitle")%></b></td>
				</tr>
				<tr>
					<td><b><%=rb.getString("gradeText")%></b></td>
					<td>
						<c:choose>
							<c:when test="${feedback.successful == 'true'}">
								<img src="images/positive.gif" border="0"><%=rb.getString("taskSuccessfulText")%>
							</c:when>
							<c:otherwise>
								<img src="images/negative.gif" border="0"><font color="#cc0000"><b><%=rb.getString("taskFailedText")%></b></font>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td><b><%=rb.getString("commentsText")%>&nbsp;</b></td>
					<td><c:out value="${feedback.overallFeedback}"/></td>
				</tr>
			</table>
			<br>
			<c:if test="${not empty feedback.compileError}">
				<table class="presentationTable">
					<tr>
						<td class="titleBar"><b>Compiler error</b></td>
					</tr>
					<tr>
						<td>
							<c:out value="${feedback.compileError}"/>				
						</td>
					</tr>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty feedback.runError}">
				<table class="presentationTable">
					<tr>
						<td colspan="2" class="titleBar"><b>Run error</b></td>
					</tr>
					<tr>
						<td>
							<c:out value="${feedback.runError}"/>				
						</td>
					</tr>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty feedback.criteriaFeedback}">
				<table class="presentationTable">
					<tr>
						<td colspan="3" class="titleBar"><%=rb.getString("criteriaText")%></td>
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
		</c:if>
	</td>
</tr>
</table>

</form>

<c:if test="${param.analyzed == 'true'}">
	<div id="titokone_report" style="display: none">
		<hr>
		<a name="output"></a>
		<b><%=rb.getString("titokoneReportTitle")%></b>
		
		<table class="presentationTable" style="float: left; margin: 10px; width: auto;">
		<tr><td colspan=3 class="titleBar"><%=rb.getString("titostateMemory")%></td></tr>
		<tr><td><%=rb.getString("titostateAddress")%></td><td><%=rb.getString("titostateValue")%></td><td><%=rb.getString("titostateCode")%></td></tr>
		<tr><td>0</td><td>16846845</td><td>LOAD R1, =4</td></tr>
		<tr><td>1</td><td>15465467</td><td>LOAD R1, =4</td></tr>
		<tr><td>2</td><td>17684684</td><td>LOAD R1, =4</td></tr>
		<tr><td>3</td><td>16846845</td><td>LOAD R1, =4</td></tr>
		<tr><td>4</td><td>15465467</td><td>LOAD R1, =4</td></tr>
		<tr><td>5</td><td>17684684</td><td>LOAD R1, =4</td></tr>
		<tr><td>6</td><td>16846845</td><td>LOAD R1, =4</td></tr>
		<tr><td>7</td><td>15465467</td><td>LOAD R1, =4</td></tr>
		<tr><td>8</td><td>17684684</td><td>LOAD R1, =4</td></tr>
		<tr><td>9</td><td>16846845</td><td>LOAD R1, =4</td></tr>
		<tr><td>10</td><td>15465467</td><td>LOAD R1, =4</td></tr>
		<tr><td>11</td><td>17684684</td><td>LOAD R1, =4</td></tr>
		<tr><td>12</td><td>16846845</td><td>LOAD R1, =4</td></tr>
		<tr><td>13</td><td>15465467</td><td>LOAD R1, =4</td></tr>
		<tr><td>14</td><td>17684684</td><td>LOAD R1, =4</td></tr>
		<tr><td>15</td><td>20</td><td></td></tr>
		<tr><td>16</td><td>40</td><td></td></tr>
		<tr><td>17</td><td>60</td><td></td></tr>
		</table>

		<table class="presentationTable" style="margin: 10px; width: auto;">
		<tr><td colspan=2 class="titleBar"><%=rb.getString("titostateExecuteInfo")%></td></tr>
		<tr><td><%=rb.getString("titostateOutput")%></td>				<td>2, 5, 6, 7, 8325, 214</td></tr>
		<tr><td><%=rb.getString("titostateExecutedInstructions")%></td>		<td>314</td></tr>
		<tr><td><%=rb.getString("titostateMemoryReferences")%></td>	<td>389</td></tr>
		<tr><td><%=rb.getString("titostateDataReferences")%></td>		<td>75</td></tr>
		<tr><td><%=rb.getString("titostateCodeSize")%></td>			<td>15</td></tr>
		<tr><td><%=rb.getString("titostateDataSize")%></td>			<td>3</td></tr>
		<tr><td><%=rb.getString("titostateStackHeight")%></td><td>12</td></tr>
		</table>

		<table class="presentationTable" style="float: left; margin: 10px; width: auto;">
		<tr><td colspan=2  class="titleBar"><%=rb.getString("titostateRegisters")%></td></tr>
		<tr><td>R0</td><td>20</td></tr>
		<tr><td>R1</td><td>20</td></tr>
		<tr><td>R2</td><td>20</td></tr>
		<tr><td>R3</td><td>20</td></tr>
		<tr><td>R4</td><td>20</td></tr>
		<tr><td>R5</td><td>20</td></tr>
		<tr><td>SP</td><td>20</td></tr>
		<tr><td>FP</td><td>20</td></tr>
		<tr><td>PC</td><td>20</td></tr>
		</table>

		<table class="presentationTable" style="margin: 10px; width: auto;">
		<tr><td colspan=3 class="titleBar"><%=rb.getString("titostateSymbols")%></td></tr>
		<tr><td><%=rb.getString("titostateSymbol")%></td><td><%=rb.getString("titostateAddress")%></td><td><%=rb.getString("titostateValue")%></td></tr>
		<tr><td>X</td><td>15</td><td>20</td></tr>
		<tr><td>Y</td><td>16</td><td>40</td></tr>
		<tr><td>ZDF</td><td>17</td><td>60</td></tr>
		</table>
	</div>
</c:if>



<%-- If user has answered a task all task specific info should be purged from session --%>
<c:remove var="programCode" scope="session"/>
<c:remove var="keyboardInput" scope="session"/>

</body>
</html>

