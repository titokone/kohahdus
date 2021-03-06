<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>

<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="login.jsp?role=teacher"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="studentTaskList.jsp"/>
</c:if>

<%	
	//Get task from DB
	Task task = DBHandler.getInstance().getTask(request.getParameter("task_id"));	
	CriterionMap criteriaStatus;
	if (task != null) {
		pageContext.setAttribute("task", task);
	    // Get all criteria from the database
		criteriaStatus = DBHandler.getInstance().getCriteriaMap(task);
		if (!criteriaStatus.isEmpty()) {
			// Criterion map is done in two parts because if you overload a class, 
			// then in JSTL all the normal getters are disabled and the hashmap is used instead.
			pageContext.setAttribute("criteria", criteriaStatus.getMap());
			pageContext.setAttribute("criteriaStatus", criteriaStatus);
		}
	} 
	List categories = DBHandler.getInstance().getCategories();
	if (categories != null) pageContext.setAttribute("categories", categories);
	
	String templateName = task.getLanguage() + "_TEMPLATE";
	// Get template criteria from the database
	CriterionMap templateCrit = DBHandler.getInstance().getCriteriaMap(DBHandler.getInstance().getTask(templateName));
	if (!templateCrit.isEmpty()) {
		pageContext.setAttribute("templateCrit", templateCrit.getMap());
	}
%>

<c:if test="${empty task}">
	<c:redirect url="error.jsp">
		<c:param name="errorMsg" value="Task not found!"/>
	</c:redirect>
</c:if>
<c:if test="${empty criteria}">
	<c:redirect url="error.jsp">
		<c:param name="errorMsg" value="Criteria not found!"/>
	</c:redirect>
</c:if>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Create Task</title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="javascript" type="text/javascript" src="js/visibilityFunctions.js"></script>
<script language="javascript" type="text/javascript" src="js/inputValidityFunctions.js"></script>
<script language="javascript" type="text/javascript" src="js/composerViews.js"></script>
<script language="javascript" type="text/javascript" src="js/composerTTK91.js"></script>
<script language="javascript" type="text/javascript" src="js/composerInitSubmit.js"></script>
<script language="javascript">

var variableCounter = <c:out value="${criteriaStatus.symbolCriterionCount}"/>;

<c:set var="pi" value='PUBSYM${0}'/>
<c:set var="si" value='SECSYM${0}'/>
<c:set var="tpub" value="${templateCrit[pi]}"/>
<c:set var="tsec" value="${templateCrit[si]}"/>
var varAcceptanceFeedbackPublic = '<c:out value="${tpub.acceptanceFeedback}"/>';
var varFailureFeedbackPublic = '<c:out value="${tpub.failureFeedback}"/>';
var varAcceptanceFeedbackSecret = '<c:out value="${tsec.acceptanceFeedback}"/>';
var varFailureFeedbackSecret = '<c:out value="${tsec.failureFeedback}"/>';

var positive = new Image();
positive.src = "images/positive.gif";
var negative = new Image();
negative.src = "images/negative.gif";
var neutral = new Image();
neutral.src = "images/neutral.gif";

var instructionNames;
var instructionStatus;
var REQUIRED_STATUS = 1;
var FORBIDDEN_STATUS = -1;
var NEUTRAL_STATUS = 0;

var registerPrefix = "REG";
var variablePrefix = "SYM";

var requiredInstructionsField;
var forbiddenInstructionsField;

var publicOutputField;
var secretOutputField;

function composerOnLoad() {
	requiredInstructionsField = document.task_creation_form.REQOPCODES_instructions;
	forbiddenInstructionsField = document.task_creation_form.BANOPCODES_instructions;
	
	publicOutputField = document.task_creation_form.PUBOUToutput_value;
	secretOutputField = document.task_creation_form.SECOUToutput_value;

	initTaskCreation();
}

// "object" for storing variable values while rewriting the variable cell
function ttk91variable(name, publicOp, publicValue, publicChecked, cfp, wfp, secretOp, secretValue, secretChecked, cfs, wfs) {
	this.publicChecked = publicChecked;
	this.secretChecked = secretChecked;
	this.name = name;
	this.publicOp = publicOp;
	this.publicValue = publicValue;
	this.correctFeedbackPublic = cfp;
	this.wrongFeedbackPublic = wfp;
	this.secretOp = secretOp;
	this.secretValue = secretValue;
	this.correctFeedbackSecret = cfs;
	this.wrongFeedbackSecret = wfs;
}

// add a new variable field into HTML
function addVariable() {
	var cell = document.getElementById("variables_cell");
	var varArray = new Array();

	var html = cell.innerHTML;
	
	// Mozilla/Firefox browsers forget the contents of the fields, so save them...
	for(var i = 0; i < variableCounter; i++) {
		varArray[i] = new ttk91variable(
				document.getElementsByName('SYM' + i + '_name').item(0).value,
				document.getElementsByName('PUBSYM' + i + '_comparison_op').item(0).selectedIndex,
				document.getElementsByName('PUBSYM' + i + '_value').item(0).value,
				document.getElementsByName('PUBSYM' + i + '_checked').item(0).checked,
				document.getElementsByName('PUBSYM' + i + '_acceptance_feedback').item(0).value,
				document.getElementsByName('PUBSYM' + i + '_failure_feedback').item(0).value,
				document.getElementsByName('SECSYM' + i + '_comparison_op').item(0).selectedIndex,
				document.getElementsByName('SECSYM' + i + '_value').item(0).value,
				document.getElementsByName('SECSYM' + i + '_checked').item(0).checked,
				document.getElementsByName('SECSYM' + i + '_acceptance_feedback').item(0).value,
				document.getElementsByName('SECSYM' + i + '_failure_feedback').item(0).value);
	}

	var beginhtml = html.substring(0, (html.length - 16));
	var endhtml = html.substring(html.length - 16)

	beginhtml += '<tr><td><input type="checkbox" name="PUBSYM' + variableCounter + '_checked"></td>';
	beginhtml += '<td><input name="SYM' + variableCounter + '_name" type="text" size="4"></td>';
	beginhtml += '<td><select name="PUBSYM' + variableCounter + '_comparison_op"><option>=</option><option>!=</option><option><</option><option>></option><option><=</option><option>>=</option></select></td>';
	beginhtml += '<td><input name="PUBSYM' + variableCounter + '_value" type="text" size="4"></td>';
	beginhtml += '<td><textarea name="PUBSYM' + variableCounter + '_acceptance_feedback" cols="20" rows="4">' + varAcceptanceFeedbackPublic + '</textarea></td>';
	beginhtml += '<td><textarea name="PUBSYM' + variableCounter + '_failure_feedback" cols="20" rows="4">' + varFailureFeedbackPublic + '</textarea></td>';
	beginhtml += '<td><select name="SECSYM' + variableCounter + '_comparison_op"><option>=</option><option>!=</option><option><</option><option>></option><option><=</option><option>>=</option></select></td>';
	beginhtml += '<td><input name="SECSYM' + variableCounter + '_value" type="text" size="4">';
	beginhtml += '<input type="checkbox" name="SECSYM' + variableCounter + '_checked"></td>';
	beginhtml += '<td><textarea name="SECSYM' + variableCounter + '_acceptance_feedback" cols="20" rows="4">' + varAcceptanceFeedbackSecret + '</textarea></td>';
	beginhtml += '<td><textarea name="SECSYM' + variableCounter + '_failure_feedback" cols="20" rows="4">' + varFailureFeedbackSecret + '</textarea></td></tr>';

	cell.innerHTML = beginhtml + endhtml;
	
	// make sure that the new row conforms to the current view
	if(document.task_creation_form.correctness_by[0].checked == true) {
		switchToCriteriaView();
	} else {
		switchToExampleView();
	}
	
	// ...and put them back.
	for(var i = 0; i < varArray.length; i++) {
		document.getElementsByName('SYM' + i + '_name').item(0).value = varArray[i].name;
		document.getElementsByName('PUBSYM' + i + '_comparison_op').item(0).selectedIndex = varArray[i].publicOp;
		document.getElementsByName('PUBSYM' + i + '_value').item(0).value =  varArray[i].publicValue;
		document.getElementsByName('PUBSYM' + i + '_checked').item(0).checked = varArray[i].publicChecked;
		document.getElementsByName('PUBSYM' + i + '_acceptance_feedback').item(0).value = varArray[i].correctFeedbackPublic;
		document.getElementsByName('PUBSYM' + i + '_failure_feedback').item(0).value = varArray[i].wrongFeedbackPublic;
		document.getElementsByName('SECSYM' + i + '_comparison_op').item(0).selectedIndex = varArray[i].secretOp;
		document.getElementsByName('SECSYM' + i + '_value').item(0).value = varArray[i].secretValue;
		document.getElementsByName('SECSYM' + i + '_checked').item(0).checked = varArray[i].secretChecked;
		document.getElementsByName('SECSYM' + i + '_acceptance_feedback').item(0).value = varArray[i].correctFeedbackSecret;
		document.getElementsByName('SECSYM' + i + '_failure_feedback').item(0).value = varArray[i].wrongFeedbackSecret;
	}

	variableCounter++;
}

</script>
</head>

<body id="composer" onLoad="composerOnLoad();">

<jsp:include page="menu.jsp"/>

<h1 align="center">Create Task</h1>

<table border="0px">
<tr>
<td>
<form name="task_creation_form" method="POST" action="save_task.jsp" onSubmit="return onFormSubmit()">

<c:set var="reqopcodes" value="${criteria['REQOPCODES']}"/>
<c:set var="banopcodes" value="${criteria['BANOPCODES']}"/>

<input type="hidden" name="REQOPCODES_instructions" value="<c:out value="${reqopcodes.acceptanceTestValue}"/>">
<input type="hidden" name="BANOPCODES_instructions" value="<c:out value="${banopcodes.acceptanceTestValue}"/>">
<input type="hidden" name="save_type" value="<c:out value="${param.save_type}"/>">
<input type="hidden" name="task_id" value="<c:out value="${param.task_id}"/>">
<input type="hidden" name="symbol_criterion_count" value="">

<div align="center">

	<table class="presentationTable">
		<tr>
			<td class="presentationTitle" colspan="2"><h2>Properties</h2></td>
		</tr>
		<tr>
			<td>Task language </td>
			<td>
				<input name="language" type="radio" value="FI" <c:if test="${task.language=='FI'}">checked</c:if>> Finnish
				<input name="language" type="radio" value="EN" <c:if test="${task.language=='EN'}">checked</c:if>> English
			</td>
		</tr>
		<tr>
			<td>Task name </td>
			<td><input name="task_name" type="text" size="66" value="<c:out value="${task.name}"/>"></td>
		</tr>
		<tr>
			<td>Category </td>
			<td>
				<select name="category">
					<c:forEach var="category" items="${categories}">
						<option <c:if test="${task.category==category}">selected</c:if>>
							<c:out value="${category}"/>
						</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>

	<br><br><br>

	<table class="presentationTable">
		<tr>
			<td class="presentationTitle" colspan="2"><h2>Definition</h2></td>
		</tr>
		<tr>
			<td>Instructions </td>
			<td><textarea name="instructions" cols="90" rows="20"><c:out value="${task.description}"/></textarea></td>
		</tr>
		<tr>
			<td>Public input </td>
			<td><input name="public_input" type="text" size="60" value="<c:out value="${task.publicInput}"/>"></td>
		</tr>
		<tr>
			<td>Secret input </td>
			<td><input name="secret_input" type="text" size="60" value="<c:out value="${task.secretInput}"/>"></td>
		</tr>
		<tr>
			<td>Feedback on success </td>
			<td><textarea name="feedback_pass" cols="90" rows="5"><c:out value="${task.passFeedBack}"/></textarea></td>
		</tr>
		<tr>
			<td>Feedback on failure </td>
			<td><textarea name="feedback_fail" cols="90" rows="5"><c:out value="${task.failFeedBack}"/></textarea></td>
		</tr>
	</table>

	<br><br><br>

	<table class="presentationTable">
		<tr>
			<td class="presentationTitle" colspan="2"><h2>Type</h2></td>
		</tr>
		<tr>
			<td>Task type </td>
			<td>
				<input name="task_type" type="radio" value="whole_program" onClick="switchToWholeProgramView();" <c:if test="${task.programmingTask}">checked</c:if>> Programming
				<input name="task_type" type="radio" value="partial" onClick="switchToPartOfProgramView();" <c:if test="${task.fillInTask}">checked</c:if>> Fill-in
			</td>
		</tr>
		<tr>
			<td>Correctness </td>
			<td>
				<input name="correctness_by"  type="radio" value="predefined_values" onClick="switchToCriteriaView();" <c:if test="${not task.validateByModel}">checked</c:if>> Predefined values
				<input name="correctness_by"  type="radio" value="example_program" onClick="switchToExampleView();" <c:if test="${task.validateByModel}">checked</c:if>> Values given by example program
			</td>
		</tr>
	</table>

	<br>

	<div id="exampleTable">
		<table class="presentationTable">
			<tr>
				<td class="presentationTitle"><h2>Program code</h2></td>
			</tr>
			<tr>
				<td>
					<div id="partialProgramDiv1"><b>Visible code, part 1:</b><br><textarea name="partial_code1" cols="100" rows="30"><c:out value="${task.fillInPreCode}"/></textarea></div>

					<div id="exampleCodeDiv"><b>Example code:</b><br><textarea name="example_code" cols="100" rows="40"><c:out value="${task.modelAnswer}"/></textarea></div>

					<div id="partialProgramDiv2"><b>Visible code, part 2:</b><br><textarea name="partial_code2" cols="100" rows="30"><c:out value="${task.fillInPostCode}"/></textarea></div>
				</td>
			</tr>
		</table>
	
		<br><br>
	</div>

	<table class="presentationTable">
		<tr>
			<td class="presentationTitle"><h2>Criteria</h2></td>
		</tr>
		<tr>
			<td class="titleBar">Registers</td>
		</tr>
		<tr>
			<td>
				<table class="innerComposerTable">
					<tr>
						<td colspan="3"></td>
						<td align="center" colspan="3"><b>With public input</b></td>
						<td></td>
						<td align="center" colspan="3"><b>With secret input</b></td>
					</tr>
					<tr>
						<td colspan="3"></td>
						<td>Value</td>
						<td>Feedback if correct</td>
						<td>Feedback if wrong</td>
						<td></td>
						<td>Value</td>
						<td>Feedback if correct</td>
						<td>Feedback if wrong</td>
					</tr>				
					<c:forEach begin="0" end="7" step="1" var="i">
						
						<c:set var="pubIndex" value='PUBREG${i}'/>
						<c:set var="secIndex" value='SECREG${i}'/>
						<c:set var="pub" value='${criteria[pubIndex]}'/>
						<c:set var="sec" value="${criteria[secIndex]}"/>
						<tr>
						<td><input type="checkbox" name="<c:out value="${pub.id}"/>_checked" <c:if test="${pub.modelAcceptanceTest}">checked</c:if>></td>
							<td>R<c:out value="${i}"/></td>
							<td>
								<select name="<c:out value="${pub.id}"/>_comparison_op">
									<option <c:if test="${pub.comparisonOperator=='=='}">selected</c:if>>=</option>
									<option <c:if test="${pub.comparisonOperator=='!='}">selected</c:if>>!=</option>
									<option <c:if test="${pub.comparisonOperator=='<'}">selected</c:if>><</option>
									<option <c:if test="${pub.comparisonOperator=='>'}">selected</c:if>>></option>
									<option <c:if test="${pub.comparisonOperator=='<='}">selected</c:if>><=</option>
									<option <c:if test="${pub.comparisonOperator=='>='}">selected</c:if>>>=</option>
								</select>
							</td>
							<td><input name="<c:out value="${pub.id}"/>_value" type="text" size="4" value="<c:out value="${pub.acceptanceTestValue}"/>"></td>
							<td><textarea name="<c:out value="${pub.id}"/>_acceptance_feedback" cols="21" rows="4"><c:out value="${pub.acceptanceFeedback}"/></textarea></td>
							<td><textarea name="<c:out value="${pub.id}"/>_failure_feedback" cols="21" rows="4"><c:out value="${pub.failureFeedback}"/></textarea></td>
							<td>
								<select name="<c:out value="${sec.id}"/>_comparison_op">
									<option <c:if test="${sec.comparisonOperator=='=='}">selected</c:if>>=</option>
									<option <c:if test="${sec.comparisonOperator=='!='}">selected</c:if>>!=</option>
									<option <c:if test="${sec.comparisonOperator=='<'}">selected</c:if>><</option>
									<option <c:if test="${sec.comparisonOperator=='>'}">selected</c:if>>></option>
									<option <c:if test="${sec.comparisonOperator=='<='}">selected</c:if>><=</option>
									<option <c:if test="${sec.comparisonOperator=='>='}">selected</c:if>>>=</option>
								</select>
							</td>
							<td><input name="<c:out value="${sec.id}"/>_value" type="text" size="4" value="<c:out value="${sec.acceptanceTestValue}"/>">
								<input type="checkbox" name="<c:out value="${sec.id}"/>_checked" <c:if test="${sec.modelAcceptanceTest}">checked</c:if>></td>
							<td><textarea name="<c:out value="${sec.id}"/>_acceptance_feedback" cols="21" rows="4"><c:out value="${sec.acceptanceFeedback}"/></textarea></td>
							<td><textarea name="<c:out value="${sec.id}"/>_failure_feedback" cols="21" rows="4"><c:out value="${sec.failureFeedback}"/></textarea></td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>

		<tr>
			<td class="titleBar">Variables</td>
		</tr>
		<tr>
			<td>
				<table>
					<tr>
						<td id="variables_cell">
							<table class="innerComposerTable">
								<tr>
									<td colspan="3"></td>
									<td align="center" colspan="3"><b>With public input</b></td>
									<td></td>
									<td align="center" colspan="3"><b>With secret input</b></td>
								</tr>
								<tr>
									<td colspan="3"></td>
									<td><p>Value</p></td>
									<td><p>Feedback if correct</p></td>
									<td><p>Feedback if wrong</p></td>
									<td></td>
									<td><p>Value</p></td>
									<td><p>Feedback if correct</p></td>
									<td><p>Feedback if wrong</p></td>
								</tr>
								<c:forEach begin="0" end="${criteriaStatus.symbolCriterionCount-1}" step="1" var="i">
									<c:set var="pubIndex" value='PUBSYM${i}'/>
									<c:set var="secIndex" value='SECSYM${i}'/>
									<c:set var="pub" value="${criteria[pubIndex]}"/>
									<c:set var="sec" value="${criteria[secIndex]}"/>
									<tr>
										<td><input type="checkbox" name="<c:out value="${pub.id}"/>_checked" <c:if test="${pub.modelAcceptanceTest}">checked</c:if>></td>
										<td><input name="SYM<c:out value="${i}"/>_name" type="text" size="4" value="<c:out value="${pub.symbolName}"/>"></td>
										<td>
											<select name="<c:out value="${pub.id}"/>_comparison_op">
												<option <c:if test="${pub.comparisonOperator=='=='}">selected</c:if>>=</option>
												<option <c:if test="${pub.comparisonOperator=='!='}">selected</c:if>>!=</option>
												<option <c:if test="${pub.comparisonOperator=='<'}">selected</c:if>><</option>
												<option <c:if test="${pub.comparisonOperator=='>'}">selected</c:if>>></option>
												<option <c:if test="${pub.comparisonOperator=='<='}">selected</c:if>><=</option>
												<option <c:if test="${pub.comparisonOperator=='>='}">selected</c:if>>>=</option>
											</select>
										</td>
										<td><input name="<c:out value="${pub.id}"/>_value" type="text" size="4" value="<c:out value="${pub.acceptanceTestValue}"/>"></td>
										<td><textarea name="<c:out value="${pub.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${pub.acceptanceFeedback}"/></textarea></td>
										<td><textarea name="<c:out value="${pub.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${pub.failureFeedback}"/></textarea></td>
										<td>
											<select name="<c:out value="${sec.id}"/>_comparison_op">
												<option <c:if test="${sec.comparisonOperator=='=='}">selected</c:if>>=</option>
												<option <c:if test="${sec.comparisonOperator=='!='}">selected</c:if>>!=</option>
												<option <c:if test="${sec.comparisonOperator=='<'}">selected</c:if>><</option>
												<option <c:if test="${sec.comparisonOperator=='>'}">selected</c:if>>></option>
												<option <c:if test="${sec.comparisonOperator=='<='}">selected</c:if>><=</option>
												<option <c:if test="${sec.comparisonOperator=='>='}">selected</c:if>>>=</option>
											</select>
										</td>
										<td><input name="<c:out value="${sec.id}"/>_value" type="text" size="4" value="<c:out value="${sec.acceptanceTestValue}"/>">
											<input type="checkbox" name="<c:out value="${sec.id}"/>_checked" <c:if test="${sec.modelAcceptanceTest}">checked</c:if>></td>
										<td><textarea name="<c:out value="${sec.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${sec.acceptanceFeedback}"/></textarea></td>
										<td><textarea name="<c:out value="${sec.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${sec.failureFeedback}"/></textarea></td>
									</tr></c:forEach></table></td>
						<!-- DO NOT separate </tr></table></td> above. Required for Javascript function addVariable() to function correctly. -->
					</tr>
					<tr>
						<td colspan="3"><input type="button" name="add_variable" value="Add variable" onclick="addVariable()"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="titleBar">TTK-91 Instructions</td>
		</tr>
		<tr>
			<td>
				<table id="ttk91Table">
					<tr>
						<td>
							<table class="innerComposerTable">
								<tr>
									<td>
										<a href="#" onClick="return ttkInstructionOnClick('load')"><img id="load_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> LOAD<br>
										<a href="#" onClick="return ttkInstructionOnClick('store')"><img id="store_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> STORE<br>
										<a href="#" onClick="return ttkInstructionOnClick('push')"><img id="push_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> PUSH<br>
										<a href="#" onClick="return ttkInstructionOnClick('pop')"><img id="pop_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> POP<br>
										<a href="#" onClick="return ttkInstructionOnClick('pushr')"><img id="pushr_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> PUSHR<br>
										<a href="#" onClick="return ttkInstructionOnClick('popr')"><img id="popr_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> POPR<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('in')"><img id="in_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> IN<br>
										<a href="#" onClick="return ttkInstructionOnClick('out')"><img id="out_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> OUT<br>
									</td>
									<td>
										<a href="#" onClick="return ttkInstructionOnClick('add')"><img id="add_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> ADD<br>
										<a href="#" onClick="return ttkInstructionOnClick('sub')"><img id="sub_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> SUB<br>
										<a href="#" onClick="return ttkInstructionOnClick('mul')"><img id="mul_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> MUL<br>
										<a href="#" onClick="return ttkInstructionOnClick('div')"><img id="div_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> DIV<br>
										<a href="#" onClick="return ttkInstructionOnClick('mod')"><img id="mod_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> MOD<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('not')"><img id="not_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> NOT<br>
										<a href="#" onClick="return ttkInstructionOnClick('and')"><img id="and_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> AND<br>
										<a href="#" onClick="return ttkInstructionOnClick('or')"><img id="or_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> OR<br>
										<a href="#" onClick="return ttkInstructionOnClick('xor')"><img id="xor_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> XOR<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('shl')"><img id="shl_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> SHL<br>
										<a href="#" onClick="return ttkInstructionOnClick('shr')"><img id="shr_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> SHR<br>
										<a href="#" onClick="return ttkInstructionOnClick('comp')"><img id="comp_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> COMP<br>
									</td>
									<td>
										<a href="#" onclick="return ttkInstructionOnClick('jump')"><img id="jump_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JUMP<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('jneg')"><img id="jneg_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JNEG<br>
										<a href="#" onClick="return ttkInstructionOnClick('jzer')"><img id="jzer_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JZER<br>
										<a href="#" onClick="return ttkInstructionOnClick('jpos')"><img id="jpos_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JPOS<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnneg')"><img id="jnneg_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JNNEG<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnzer')"><img id="jnzer_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JNZER<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnpos')"><img id="jnpos_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JNPOS<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('jles')"><img id="jles_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JLES<br>
										<a href="#" onClick="return ttkInstructionOnClick('jequ')"><img id="jequ_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JEQU<br>
										<a href="#" onClick="return ttkInstructionOnClick('jgre')"><img id="jgre_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JGRE<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnle')"><img id="jnle_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JNLE<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnequ')"><img id="jnequ_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JNEQU<br>
										<a href="#" onClick="return ttkInstructionOnClick('jngre')"><img id="jngre_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> JNGRE<br>
									</td>
									<td>
										<a href="#" onClick="return ttkInstructionOnClick('call')"><img id="call_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> CALL<br>
										<a href="#" onClick="return ttkInstructionOnClick('exit')"><img id="exit_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> EXIT<br>
										<a href="#" onClick="return ttkInstructionOnClick('svc')"><img id="svc_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> SVC<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('nop')"><img id="nop_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> NOP<br>
										<br>
										
										<%--
										<a href="#" onClick="return ttkInstructionOnClick('equ')"><img id="equ_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> EQU<br>
										<a href="#" onClick="return ttkInstructionOnClick('dc')"><img id="dc_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> DC<br>
										<a href="#" onClick="return ttkInstructionOnClick('ds')"><img id="ds_img" src="images/neutral.gif" border="0" style="vertical-align:middle"></a> DS<br>
										--%>
										
									</td>
								</tr>
							</table>
						</td>
						<td>
							<table>
								<tr>
									<td class="infoBox">
										<b>Key:</b><br>
										<img src="images/positive.gif"> = required<br>
										<img src="images/negative.gif"> = forbidden<br>
										<img src="images/neutral.gif"> = neither required<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;nor forbidden
									</td>
								</tr>
							</table>
						</td>
						<td>
							Feedback if some forbidden instruction has been used:<br>
							<textarea name="<c:out value="${reqopcodes.id}"/>_feedback" cols="60" rows="6"><c:out value="${reqopcodes.failureFeedback}"/></textarea>
							<br><br>
							Feedback if not all required instructions have been used:<br>
							<textarea name="<c:out value="${banopcodes.id}"/>_feedback" cols="60" rows="6"><c:out value="${banopcodes.failureFeedback}"/></textarea>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="titleBar">Simulator output</td>
		</tr>
		<tr>
			<td>
				<c:set var="pub" value="${criteria['PUBOUT']}"/>
				<b>With public input:</b><br>
				<table class="innerComposerTable">
					<tr>
						<td>Output</td>
						<td>Feedback if correct</td>
						<td>Feedback if wrong</td>
					</tr>
					<tr>
						<td><textarea name="<c:out value="${pub.id}"/>output_value" cols="20" rows="4"><c:out value="${pub.acceptanceTestValue}"/></textarea>
						    <input type="checkbox" name="<c:out value="${pub.id}"/>output_checked" <c:if test="${pub.modelAcceptanceTest}">checked</c:if> /></td>
						<td><textarea name="<c:out value="${pub.id}"/>output_acceptance_feedback" cols="47" rows="4"><c:out value="${pub.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${pub.id}"/>output_failure_feedback" cols="47" rows="4"><c:out value="${pub.failureFeedback}"/></textarea></td>
					</tr>
				</table>
				<c:set var="sec" value="${criteria['SECOUT']}"/>
				<b>With secret input:</b><br>
				<table class="innerComposerTable">
					<tr>
						<td>Output</td>
						<td>Feedback if correct</td>
						<td>Feedback if wrong</td>
					</tr>
					<tr>
						<td><textarea name="<c:out value="${sec.id}"/>output_value" cols="20" rows="4"><c:out value="${sec.acceptanceTestValue}"/></textarea>
						    <input type="checkbox" name="<c:out value="${sec.id}"/>output_checked" <c:if test="${sec.modelAcceptanceTest}">checked</c:if> /></td>
						<td><textarea name="<c:out value="${sec.id}"/>output_acceptance_feedback" cols="47" rows="4"><c:out value="${sec.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${sec.id}"/>output_failure_feedback" cols="47" rows="4"><c:out value="${sec.failureFeedback}"/></textarea></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="titleBar">Quality criteria</td>
		</tr>
		<tr>
			<td>
				<table class="innerComposerTable">
					<tr>
						<td></td>
						<td>Not accepted feedback</td>
						<td>Acceptance<br>limit</td>
						<td>Accepted feedback</td>
						<td>Quality<br>limit</td>
						<td>Accepted with quality limit</td>
					</tr>
					<tr>
						<td>Number of lines<br>(in program)</td>
						<c:set var="codesize" value="${criteria['CODESIZE']}"/>
						<td><textarea name="<c:out value="${codesize.id}"/>_failure_feedback" cols="27" rows="4"><c:out value="${codesize.failureFeedback}"/></textarea></td>
						<td><input name="<c:out value="${codesize.id}"/>_acceptance_limit" value="<c:out value="${codesize.acceptanceTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${codesize.id}"/>_acceptance_feedback" cols="27" rows="4"><c:out value="${codesize.acceptanceFeedback}"/></textarea></td>
						<td><input name="<c:out value="${codesize.id}"/>_quality_limit" value="<c:out value="${codesize.qualityTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${codesize.id}"/>_quality_feedback" cols="27" rows="4"><c:out value="${codesize.highQualityFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Size of data<br>storage</td>
						<c:set var="datasize" value="${criteria['DATASIZE']}"/>
						<td><textarea name="<c:out value="${datasize.id}"/>_failure_feedback" cols="27" rows="4"><c:out value="${datasize.failureFeedback}"/></textarea></td>
						<td><input name="<c:out value="${datasize.id}"/>_acceptance_limit" value="<c:out value="${datasize.acceptanceTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${datasize.id}"/>_acceptance_feedback" cols="27" rows="4"><c:out value="${datasize.acceptanceFeedback}"/></textarea></td>
						<td><input name="<c:out value="${datasize.id}"/>_quality_limit" value="<c:out value="${datasize.qualityTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${datasize.id}"/>_quality_feedback" cols="27" rows="4"><c:out value="${datasize.highQualityFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Maximum size<br>of stack</td>
						<c:set var="stacksize" value="${criteria['STACKSIZE']}"/>
						<td><textarea name="<c:out value="${stacksize.id}"/>_failure_feedback" cols="27" rows="4"><c:out value="${stacksize.failureFeedback}"/></textarea></td>
						<td><input name="<c:out value="${stacksize.id}"/>_acceptance_limit" value="<c:out value="${stacksize.acceptanceTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${stacksize.id}"/>_acceptance_feedback" cols="27" rows="4"><c:out value="${stacksize.acceptanceFeedback}"/></textarea></td>
						<td><input name="<c:out value="${stacksize.id}"/>_quality_limit" value="<c:out value="${stacksize.qualityTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${stacksize.id}"/>_quality_feedback" cols="27" rows="4"><c:out value="${stacksize.highQualityFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Number of executed<br>instructions</td>
						<c:set var="steps" value="${criteria['STEPS']}"/>
						<td><textarea name="<c:out value="${steps.id}"/>_failure_feedback" cols="27" rows="4"><c:out value="${steps.failureFeedback}"/></textarea></td>
						<td><input name="<c:out value="${steps.id}"/>_acceptance_limit" value="<c:out value="${steps.acceptanceTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${steps.id}"/>_acceptance_feedback" cols="27" rows="4"><c:out value="${steps.acceptanceFeedback}"/></textarea></td>
						<td><input name="<c:out value="${steps.id}"/>_quality_limit" value="<c:out value="${steps.qualityTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${steps.id}"/>_quality_feedback" cols="27" rows="4"><c:out value="${steps.highQualityFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Number of executed<br>data references</td>
						<c:set var="dataref" value="${criteria['DATAREF']}"/>
						<td><textarea name="<c:out value="${dataref.id}"/>_failure_feedback" cols="27" rows="4"><c:out value="${dataref.failureFeedback}"/></textarea></td>
						<td><input name="<c:out value="${dataref.id}"/>_acceptance_limit" value="<c:out value="${dataref.acceptanceTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${dataref.id}"/>_acceptance_feedback" cols="27" rows="4"><c:out value="${dataref.acceptanceFeedback}"/></textarea></td>
						<td><input name="<c:out value="${dataref.id}"/>_quality_limit" value="<c:out value="${dataref.qualityTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${dataref.id}"/>_quality_feedback" cols="27" rows="4"><c:out value="${dataref.highQualityFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Number of executed<br>memory references</td>
						<c:set var="memref" value="${criteria['MEMREF']}"/>
						<td><textarea name="<c:out value="${memref.id}"/>_failure_feedback" cols="27" rows="4"><c:out value="${memref.failureFeedback}"/></textarea></td>
						<td><input name="<c:out value="${memref.id}"/>_acceptance_limit" value="<c:out value="${memref.acceptanceTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${memref.id}"/>_acceptance_feedback" cols="27" rows="4"><c:out value="${memref.acceptanceFeedback}"/></textarea></td>
						<td><input name="<c:out value="${memref.id}"/>_quality_limit" value="<c:out value="${memref.qualityTestValue}"/>" type="text" size="4"></td>
						<td><textarea name="<c:out value="${memref.id}"/>_quality_feedback" cols="27" rows="4"><c:out value="${memref.highQualityFeedback}"/></textarea></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="titleBar">Endless loop prevention</td>
		</tr>
		<tr>
			<td>Maximum number of executed instructions <input name="maximum_number_of_executed_instructions" type="text" value="<c:out value="${task.maximumNumberOfInstructions}"/>"></td>
		</tr>
	</table>

	<br><br><br>

	<p>
		<input type="submit" value="Save task">
		<input value="Show printable view" onclick="javascript:switchToPrintableView();" type="button">
		<input value="Hide printable view" onclick="javascript:switchPrintableViewOff();" type="button">
	</p>
</div>
</form>
</td>
</tr>
</table>

</body>


</html>

