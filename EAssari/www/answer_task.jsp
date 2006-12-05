<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>
<%@ page import="fi.hu.cs.ttk91.TTK91Cpu" %>

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
	<td class="errorMsg"><%=rb.getString("keyboardInputErrorMsg")%></td>
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
					<c:choose>	
						<c:when test="${task.fillInTask}">
							<div><b><pre><c:out value="${task.fillInPreCode}"/></pre></b></div>					
							<textarea name="programCode" style="width:100%" rows="20"><c:out value="${programCode}"/></textarea>					
							<div><b><pre><c:out value="${task.fillInPostCode}"/></pre></b></div>
						</c:when>
						<c:otherwise>					
							<textarea name="programCode" style="width:100%" rows="40"><c:out value="${programCode}"/></textarea>											
						</c:otherwise>
					</c:choose>
					<div style="text-align: right;"><input type="submit" value="<%=rb.getString("executeButtonValue")%>"></div>
				</td>
			</tr>
		</table>
	
		<br>
	
<!--		<input type="submit" value="<%=rb.getString("executeButtonValue")%>"> -->
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
						<td class="titleBar"><b><%=rb.getString("compilerError")%></b></td>
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
						<td colspan="2" class="titleBar"><%=rb.getString("criteriaText")%></td>
					</tr>
					<c:forEach var="criterionFeedback" items="${feedback.criteriaFeedback}">
						<c:choose>
							<c:when test="${criterionFeedback.passedAcceptanceTest}">
								<c:set var="image" value="images/positive.gif"/>
							</c:when>
							<c:otherwise>
								<c:set var="image" value="images/negative.gif"/>
							</c:otherwise>
						</c:choose>	
						<tr>
							<td _width="20%"><b><img src="<c:out value="${image}"/>" border="0"> <c:out value="${criterionFeedback.name}"/></b></td>
							<td _width="80%"><c:out value="${criterionFeedback.feedback}"/></td>
						</tr>
					</c:forEach>
					<c:remove var="image"/>
				</table>
			</c:if>
		</c:if>
	</td>
</tr>
</table>

</form>

<c:if test="${param.analyzed == 'true'}">
	<c:set var="state" value="${feedback.titoState}"/>
	<% 
		TitoFeedback fb = (TitoFeedback)session.getAttribute("feedback");
		TitoState titostate = fb.getTitoState();
		if (titostate != null) {
	%>   	
	<div id="titokone_report">
		<hr>
		<a name="output"></a>
		<b><%=rb.getString("titokoneReportTitle")%></b>
		
		<table class="presentationTable" style="float: left; margin: 10px; width: auto;">
			<tr><td colspan=3 class="titleBar"><%=rb.getString("titostateMemory")%></td></tr>
			<tr>
				<td><%=rb.getString("titostateAddress")%></td>
				<td><%=rb.getString("titostateValue")%></td>
				<td><%=rb.getString("titostateCode")%></td>
			</tr>
		<%
			int memsize = titostate.getDataSize() + titostate.getCodeSize();
			for (int i=0; i<memsize; i++) {
		%>	
				<tr>
					<td><%=i%></td>
					<td><%=titostate.getMemoryLocation(i)%></td>
					<td><%=titostate.getInstruction(i)%></td>
				</tr>
		<%		
			}
		%>			
		</table>

		<table class="presentationTable" style="margin: 10px; width: auto;">
			<tr><td colspan=2 class="titleBar"><%=rb.getString("titostateExecuteInfo")%></td></tr>
			<tr>
				<td><%=rb.getString("titostateOutput")%></td>				
				<td><c:out value="${state.screenOutput}"/></td>
			</tr>
			<tr>
				<td><%=rb.getString("titostateExecutedInstructions")%></td>
				<td><c:out value="${state.executionSteps}"/></td>
			</tr>
			<tr>
				<td><%=rb.getString("titostateMemoryReferences")%></td>		
				<td><c:out value="${state.executionSteps + state.dataReferenceCount}"/></td>
			</tr>
			<tr>
				<td><%=rb.getString("titostateDataReferences")%></td>
				<td><c:out value="${state.dataReferenceCount}"/></td>
			</tr>
			<tr>
				<td><%=rb.getString("titostateCodeSize")%></td>
				<td><c:out value="${state.codeSize}"/></td>
			</tr>
			<tr>
				<td><%=rb.getString("titostateDataSize")%></td>	
				<td><c:out value="${state.dataSize}"/></td>
			</tr>
			<tr>
				<td><%=rb.getString("titostateStackHeight")%></td>
				<td><c:out value="${state.stackMaxSize}"/></td>
			</tr>
		</table>
	
		<table class="presentationTable" style="float: left; margin: 10px; width: auto;">
			<tr><td colspan=2  class="titleBar"><%=rb.getString("titostateRegisters")%></td></tr>
			<tr><td>R0</td><td><%=titostate.getRegister(TTK91Cpu.REG_R0)%></td></tr>
			<tr><td>R1</td><td><%=titostate.getRegister(TTK91Cpu.REG_R1)%></td></tr>
			<tr><td>R2</td><td><%=titostate.getRegister(TTK91Cpu.REG_R2)%></td></tr>
			<tr><td>R3</td><td><%=titostate.getRegister(TTK91Cpu.REG_R3)%></td></tr>
			<tr><td>R4</td><td><%=titostate.getRegister(TTK91Cpu.REG_R4)%></td></tr>
			<tr><td>R5</td><td><%=titostate.getRegister(TTK91Cpu.REG_R5)%></td></tr>
			<tr><td>SP</td><td><%=titostate.getRegister(TTK91Cpu.REG_SP)%></td></tr>
			<tr><td>FP</td><td><%=titostate.getRegister(TTK91Cpu.REG_FP)%></td></tr>
			<tr><td>PC</td><td><%=titostate.getRegister(TTK91Cpu.CU_PC_CURRENT)%></td></tr>
		</table>

		<table class="presentationTable" style="margin: 10px; width: auto;">
			<tr>
				<td colspan=3 class="titleBar"><%=rb.getString("titostateSymbols")%></td>
			</tr>
			<tr>
				<td><%=rb.getString("titostateSymbol")%></td>
				<td><%=rb.getString("titostateAddress")%></td>
				<td><%=rb.getString("titostateValue")%></td>
			</tr>
			
			<%
				//Mapping symbol name strings to integer addresses
				HashMap symbolMap = titostate.getSymbolTable();
				Set keys = symbolMap.keySet();
				
				for (Object key : keys) {
					int mem = (Integer)symbolMap.get(key);
			%>	
					<tr>
						<td><%=(String)key%></td>
						<td><%=mem%></td>
						<td><%=titostate.getMemoryLocation(mem)%></td>
					</tr>
			<%
				}
			}	
			%>			
		</table>
	</div>
</c:if>



<%-- If user has answered a task all task specific info should be purged from session --%>
<c:remove var="programCode" scope="session"/>
<c:remove var="keyboardInput" scope="session"/>
<c:remove var="state" scope="session"/>

</body>
</html>

