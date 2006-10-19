<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.criteria.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>



<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="../login.jsp?role=teacher"/>	
</c:if>
<c:if test="${user.student}">
	Student tried to load a restricted page - redirecting to students tasklisting
	<c:redirect url="../student/studentTaskList.jsp"/>
</c:if>


<%	
	//Get task from DB
	Task task = DBHandler.getInstance().getTask(request.getParameter("task_id"));	
	if (task != null) {
		pageContext.setAttribute("task", task);
		
	    // Get all criteria from the database
		CriterionMap criteria = DBHandler.getInstance().getCriteriaMap(task);
		if (!criteria.isEmpty()) pageContext.setAttribute("criteria", criteria);
	} 
%>

<c:if test="${empty task}">
	<c:redirect url="../error.jsp">
		<c:param name="errorMsg" value="Task not found!"/>
	</c:redirect>
</c:if>
<c:if test="${empty criteria}">
	<c:redirect url="../error.jsp">
		<c:param name="errorMsg" value="Criteria not found!"/>
	</c:redirect>
</c:if>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Create Task</title>
<script language="javascript" type="text/javascript" src="../js/visibilityFunctions.js"></script>
<script language="Javascript">

var variableCounter = <c:out value="${criteria.symbolCriteriaCount}"/>;

var positive = new Image();
positive.src = "positive.gif";
var negative = new Image();
negative.src = "negative.gif";
var neutral = new Image();
neutral.src = "neutral.gif";

var instructionNames;
var instructionStatus;
var REQUIRED_STATUS = 1;
var FORBIDDEN_STATUS = -1;
var NEUTRAL_STATUS = 0;

// TODO
function initTaskCreation()
{
	// Javascript version of a hash table - location of a instruction name in the array "instructionNames" is the same as the location of the
	// instruction status in array "instructionStatus".
	instructionNames = new Array("load", "store", "push", "pop", "pushr", "popr", "in", "out", "add", "sub", "mul", "div", "mod", "not", "and", "or",
	"xor", "shl", "shr", "comp", "jump", );

	instructionStatus = new Array(instructionNames.length);

	for (var counter = 0; counter < instructionStatus.length; counter++) {
		instructionStatus[counter] = NEUTRAL_STATUS;
	}

	switchToCriteriaView();
	switchToWholeProgramView();
}

function switchToExampleView(){
	showElementById('tableExample');
	
	inputs = document.getElementsByTagName('input');
	for (i=0; i<inputs.length; i++){
		if (inputs[i].name.indexOf("check_") != -1){
			inputs[i].style.display = "block";
		} else if (inputs[i].name.indexOf("private_reg_") != -1){
			inputs[i].style.display = "none";
		} else if (inputs[i].name.indexOf("private_var_") != -1){
			inputs[i].style.display = "none";
		} else if (inputs[i].name.indexOf("public_reg_") != -1){
			inputs[i].style.display = "none";
		} else if (inputs[i].name.indexOf("public_var_") != -1){
			inputs[i].style.display = "none";
		}
	}
	
//	selects = document.getElementsByTagName('select');
//	for (i=0; i<selects.length; i++){
//		if (selects[i].name.indexOf("_vertailu") != -1){
//			selects[i].style.display = "none";
//		}
//	}

	textareas = document.getElementsByTagName('textarea');
	for (i=0; i<textareas.length; i++){
		if (textareas[i].name.indexOf("_fileout_") != -1){
			textareas[i].style.display = "none";
		} else if (textareas[i].name.indexOf("_stdout_") != -1){
			textareas[i].style.display = "none";
		}
	}
}

function switchToCriteriaView(){
	hideElementById('tableExample');

	inputs = document.getElementsByTagName('input');
	for (i=0; i<inputs.length; i++){
		if (inputs[i].name.indexOf("check_") != -1){
			inputs[i].style.display = "none";
		} else if (inputs[i].name.indexOf("private_reg_") != -1){
			inputs[i].style.display = "block";
		} else if (inputs[i].name.indexOf("public_reg_") != -1){
			inputs[i].style.display = "block";
		} else if (inputs[i].name.indexOf("private_var_") != -1){
			inputs[i].style.display = "block";
		} else if (inputs[i].name.indexOf("public_var_") != -1){
			inputs[i].style.display = "block";
		}
	}

//	selects = document.getElementsByTagName('select');
//	for (i=0; i<selects.length; i++){
//		if (selects[i].name.indexOf("_vertailu") != -1){
//			selects[i].style.display = "block";
//		}
//	}

	textareas = document.getElementsByTagName('textarea');
	for (i=0; i<textareas.length; i++){
		if (textareas[i].name.indexOf("_fileout_") != -1){
			textareas[i].style.display = "block";
		} else if (textareas[i].name.indexOf("_stdout_") != -1){
			textareas[i].style.display = "block";
		}
	}
}

function switchToWholeProgramView() {
	hideElementById('tablePartialProgram');
}

function switchToPartOfProgramView() {
	showElementById('tablePartialProgram');
}


// add a new variable field into HTML
function addVariable() {
	var cell = document.getElementById("variables_cell");

	var html = cell.innerHTML;

	var beginhtml = html.substring(0, (html.length - 16));
	var endhtml = html.substring(html.length - 16)

	beginhtml += '<tr><td><input name="v' + variableCounter + '_name" type="text" size="2"></td>';
	beginhtml += '<td><select name="PUBSYM' + variableCounter + '_comparison_op"><option><</option><option><=</option><option selected>=</option><option>>=</option><option>></option></select></td>';
	beginhtml += '<td><input name="PUBSYM' + variableCounter + '_value" type="text" size="2"></td>';
	beginhtml += '<td><textarea name="PUBSYM' + variableCounter + '_acceptance_feedback" cols="20" rows="2"> </textarea></td>';
	beginhtml += '<td><textarea name="PUBSYM' + variableCounter + '_failure_feedback" cols="20" rows="2"> </textarea></td>';
	beginhtml += '<td><select name="SECSYM' + variableCounter + '_comparison_op"><option><</option><option><=</option><option selected>=</option><option>>=</option><option>></option></select></td>';
	beginhtml += '<td><input name="SECSYM' + variableCounter + '_value" type="text" size="2"></td>';
	beginhtml += '<td><textarea name="SECSYM' + variableCounter + '_acceptance_feedback" cols="20" rows="2"> </textarea></td>';
	beginhtml += '<td><textarea name="SECSYM' + variableCounter + '_failure_feedback" cols="20" rows="2"> </textarea></td></tr>';

	cell.innerHTML = beginhtml + endhtml;

	variableCounter++;
}


// changes TTK-91 instruction status (required/forbidden/neutral)
function ttkInstructionOnClick(instruction) {
	var img = document.getElementById(instruction + "_img");

	var instructionIndex = -1;

	for (instructionIndex = 0; instructionIndex < instructionNames.length; instructionIndex++) {
		if(instructionNames[instructionIndex] == instruction) {
			break;
		}
	}

	if(instructionIndex >= instructionNames.length) {
		alert("Invalid TTK-91 instruction name.");	// for debugging purposes
		return false;
	}

	if(img.src == neutral.src) {
		img.src = positive.src;
		instructionStatus[instructionIndex] = REQUIRED_STATUS;
	} else if(img.src == positive.src) {
		img.src = negative.src;
		instructionStatus[instructionIndex] = FORBIDDEN_STATUS;
	} else {
		img.src = neutral.src;
		instructionStatus[instructionIndex] = NEUTRAL_STATUS;
	}

  return false;
}

function onFormSubmit() {
	instructionRequirementsIntoText();
}

function showPrintDisplay() {
	instructionRequirementsIntoText();

	// create a new window and display form fields in printable form // TODO
}

// collect TTK-91 instruction requirements into text fields
function instructionRequirementsIntoText() {
	var requiredInstructions = '';
	var forbiddenInstructions = '';
	var firstRequired = true;
	var firstForbidden = true;

	for (var instructionIndex = 0; instructionIndex < instructionStatus.length; instructionIndex++) {
		if(instructionStatus[instructionIndex] == REQUIRED_STATUS) {
			if(firstRequired == true) {
				firstRequired = false;
			}
			else {
				requiredInstructions += ', ';
			}
			requiredInstructions += instructionNames[instructionIndex];
		}
		if(instructionStatus[instructionIndex] == FORBIDDEN_STATUS) {
			if(firstForbidden == true) {
				firstForbidden = false;
			}
			else {
				forbiddenInstructions += ', ';
			}
			forbiddenInstructions += instructionNames[instructionIndex];
		}
	}

	document.task_creation_form.REQOPCODES_instructions.value = requiredInstructions;
	document.task_creation_form.BANOPCODES_instructions.value = forbiddenInstructions;
}

</script>
</head>


<body onLoad="initTaskCreation();">

<jsp:include page="../menu.jsp"/>

<h1 align="center">Create Task</h1>

<form name="task_creation_form" method="POST" action="save_task.jsp" onSubmit="formOnSubmit()">

<c:set var="reqopcodes" value="${criteria['REQOPCODES']}"/>
<c:set var="banopcodes" value="${criteria['BANOPCODES']}"/>

<input type="hidden" name="REQOPCODES_instructions" value="<c:out value="${reqopcodes.acceptanceTestValue}"/>">
<input type="hidden" name="BANOPCODES_instructions" value="<c:out value="${banopcodes.acceptanceTestValue}"/>">

<div align="center">

	<table border="1" cellpadding="3" cellspacing="0">
		<tr>
			<td align="center" colspan="2"><h2>Properties</h2></td>
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
		
		<%--  ??? Mistäs nämä saadaan ja miten lisätään ??? --%>
		
		<tr>
			<td>Category </td>
			<td>
				<select name="category">
					<option></option>
				</select>
			</td>
		</tr>
	</table>

	<br><br><br>

	<table border="1" cellpadding="3" cellspacing="0">
		<tr>
			<td align="center" colspan="2"><h2>Definition</h2></td>
		</tr>
		<tr>
			<td valign="top">Instructions </td>
			<td valign="top"><textarea name="instructions" cols="90" rows="20" value="<c:out value="${task.description}"/>"></textarea></td>
		</tr>
		
		<%-- TODO: inputit taskista --%>
		
		<tr>
			<td>Public input </td>
			<td><input name="public_input" type="text" size="60" value="<c:out value="${task.publicInput}"/>"></td>
		</tr>
		<tr>
			<td>Secret input </td>
			<td><input name="secret_input" type="text" size="60" value="<c:out value="${task.secretInput}"/>"></td>
		</tr>
	</table>

	<br><br><br>

	<table border="1" cellpadding="3" cellspacing="0">
		<tr>
			<td align="center" colspan="2"><h2>???</h2></td>
		</tr>
		<tr>
			<td>Task type </td>
			<td>
				<input name="task_type" type="radio" value="whole_program" onClick="switchToWholeProgramView();" <c:if test="${task.programmingTask}">checked</c:if>> Whole program
				<input name="task_type" type="radio" value="partial" onClick="switchToPartOfProgramView();" <c:if test="${task.fillInTask}">checked</c:if>> Part of a program
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

	<div id="tablePartialProgram">
		<table border="1" cellpadding="3" cellspacing="0" width="300">
			<tr>
				<td align="center"><h2>Partial program code</h2></td>
			</tr>
			<tr>
				<td align="center">
					Write here your partial program code. Write <b>[[fill-in]]</b> where you want the answer to be typed.
					Please pay attention that you should still write the complete code into the example code box if you wish the
					correctness of the answer to be checked by values given by the example program.</td>
			</tr>
			<tr>
				<td valign="top"><textarea name="partial_code" cols="100" rows="30"></textarea></td>
			</tr>
		</table>
	
		<br><br>
	</div>

	<br>

	<div id="tableExample">
		<table border="1" cellpadding="3" cellspacing="0">
			<tr>
				<td align="center"><h2>Example program code</h2></td>
			</tr>
			<tr>
				<td valign="top"><textarea name="example_code" cols="100" rows="40"></textarea></td>
			</tr>
		</table>
	
		<br><br>
	</div>

	<table border="1" cellpadding="3" cellspacing="0">
		<tr>
			<td align="center" colspan="2"><h2>Criteria</h2></td>
		</tr>
		<tr>
			<td valign="top">Registers </td>
			<td>
				<table border="1" cellpadding="3" cellspacing="0">
					<tr>
						<td align="center" colspan="3"></td>
						<td align="center" colspan="3"><b>With public input</b></td>
						<td align="center" colspan="3"><b>With secret input</b></td>
					</tr>
					<tr>
						<td align="center" colspan="3"></td>
						<td><p>Value</p></td>
						<td><p>Feedback if correct</p></td>
						<td><p>Feedback if wrong</p></td>
						<td><p>Value</p></td>
						<td><p>Feedback if correct</p></td>
						<td><p>Feedback if wrong</p></td>
					</tr>				
					<c:forEach begin="0" end="7" step="1" var="i">
						
						<c:set var="pubIndex" value='PUBREG${i}'/>
						<c:set var="secIndex" value='SECREG${i}'/>
						<c:set var="pub" value='${criteria[pubIndex]}'/>
						<c:set var="sec" value="${criteria[secIndex]}"/>
						<tr>
							<td><input type="checkbox" name="r<c:out value="${i}"/>_checked"></td>
							<td>R<c:out value="${i}"/></td>
							<td>
								<select name="<c:out value="${pub.id}"/>_comparison_op">
									<option><</option>
									<option><=</option>
									<option selected>=</option>
									<option>>=</option>
									<option>></option>
								</select>
							</td>
							<td><input name="<c:out value="${pub.id}"/>_value" type="text" size="4" value="<c:out value="${pub.acceptanceTestValue}"/>"></td>
							<td><textarea name="<c:out value="${pub.id}"/>_acceptance_feedback" cols="20" rows="4"><c:out value="${pub.acceptanceFeedback}"/></textarea></td>
							<td><textarea name="<c:out value="${pub.id}"/>_failure_feedback" cols="20" rows="4"><c:out value="${pub.failureFeedback}"/></textarea></td>
							<td>
								<select name="<c:out value="${sec.id}"/>_comparison_op">
									<option><</option>
									<option><=</option>
									<option selected>=</option>
									<option>>=</option>
									<option>></option>
								</select>
							</td>
							<td><input name="<c:out value="${sec.id}"/>_value" type="text" size="4" value="<c:out value="${sec.acceptanceTestValue}"/>"></td>
							<td><textarea name="<c:out value="${sec.id}"/>_acceptance_feedback" cols="20" rows="4"><c:out value="${sec.acceptanceFeedback}"/></textarea></td>
							<td><textarea name="<c:out value="${sec.id}"/>_failure_feedback" cols="20" rows="4"><c:out value="${sec.failureFeedback}"/></textarea></td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>

		<tr>
			<td valign="top">Variables </td>
			<td>
				<table border="0">
					<tr>
						<td id="variables_cell">
							<table border="1" cellpadding="3" cellspacing="0" class="bordered">
								<tr>
									<td align="center" colspan="2"></td>
									<td align="center" colspan="3"><b>With public input</b></td>
									<td align="center" colspan="3"><b>With secret input</b></td>
								</tr>
								<tr>
									<td align="center" colspan="2"></td>
									<td><p>Value</p></td>
									<td><p>Feedback if correct</p></td>
									<td><p>Feedback if wrong</p></td>
									<td><p>Value</p></td>
									<td><p>Feedback if correct</p></td>
									<td><p>Feedback if wrong</p></td>
								</tr>
								<c:forEach begin="0" end="${criteria.symbolCriteriaCount}" step="1" var="i">
									<c:set var="pubIndex" value='PUBSYM${i}'/>
									<c:set var="secIndex" value='SECSYM${i}'/>
									<c:set var="pub" value="${criteria[pubIndex]}"/>
									<c:set var="sec" value="${criteria[secIndex]}"/>
									<tr>
										<td><input name="v<c:out value="${i}"/>_name" type="text" size="2"></td>
										<td>
											<select name="<c:out value="${pub.id}"/>_comparison_op">
												<option><</option>
												<option><=</option>
												<option selected>=</option>
												<option>>=</option>
												<option>></option>
											</select>
										</td>
										<td><input name="<c:out value="${pub.id}"/>_value" type="text" size="2" value="<c:out value="${pub.acceptanceTestValue}"/>"></td>
										<td><textarea name="<c:out value="${pub.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${pub.acceptanceFeedback}"/></textarea></td>
										<td><textarea name="<c:out value="${pub.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${pub.failureFeedback}"/></textarea></td>
										<td>
											<select name="<c:out value="${sec.id}"/>_comparison_op">
												<option><</option>
												<option><=</option>
												<option selected>=</option>
												<option>>=</option>
												<option>></option>
											</select>
										</td>
										<td><input name="<c:out value="${sec.id}"/>_value" type="text" size="2" value="<c:out value="${sec.acceptanceTestValue}"/>"></td>
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
			<td valign="top">
				TTK-91 Instructions <br>
				<img src="positive.gif"> = required<br>
				<img src="negative.gif"> = forbidden<br>
				<img src="neutral.gif"> = neither required nor forbidden
			</td>
			<td>
				<table border="0" width="100%">
					<tr>
						<td>
							<table border="1" cellpadding="3" cellspacing="0">
								<tr>
									<td valign="top">
										<a href="#" onClick="return ttkInstructionOnClick('load')"><img id="load_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> LOAD<br>
										<a href="#" onClick="return ttkInstructionOnClick('store')"><img id="store_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> STORE<br>
										<a href="#" onClick="return ttkInstructionOnClick('push')"><img id="push_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> PUSH<br>
										<a href="#" onClick="return ttkInstructionOnClick('pop')"><img id="pop_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> POP<br>
										<a href="#" onClick="return ttkInstructionOnClick('pushr')"><img id="push_rimg" src="neutral.gif" border="0" style="vertical-align:middle"></a> PUSHR<br>
										<a href="#" onClick="return ttkInstructionOnClick('popr')"><img id="popr_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> POPR<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('in')"><img id="in_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> IN<br>
										<a href="#" onClick="return ttkInstructionOnClick('out')"><img id="out_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> OUT<br>
									</td>
									<td valign="top">
										<a href="#" onClick="return ttkInstructionOnClick('add')"><img id="add_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> ADD<br>
										<a href="#" onClick="return ttkInstructionOnClick('sub')"><img id="sub_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> SUB<br>
										<a href="#" onClick="return ttkInstructionOnClick('mul')"><img id="mul_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> MUL<br>
										<a href="#" onClick="return ttkInstructionOnClick('div')"><img id="div_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> DIV<br>
										<a href="#" onClick="return ttkInstructionOnClick('mod')"><img id="mod_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> MOD<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('not')"><img id="not_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> NOT<br>
										<a href="#" onClick="return ttkInstructionOnClick('and')"><img id="and_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> AND<br>
										<a href="#" onClick="return ttkInstructionOnClick('or')"><img id="or_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> OR<br>
										<a href="#" onClick="return ttkInstructionOnClick('xor')"><img id="xor_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> XOR<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('shl')"><img id="shl_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> SHL<br>
										<a href="#" onClick="return ttkInstructionOnClick('shr')"><img id="shr_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> SHR<br>
										<a href="#" onClick="return ttkInstructionOnClick('comp')"><img id="comp_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> COMP<br>
									</td>
									<td valign="top">
										<a href="#" onclick="return ttkInstructionOnClick('jump')"><img id="jumpimg" src="neutral.gif" border="0" style="vertical-align:middle"></a> JUMP<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('jneg')"><img id="jneg_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JNEG<br>
										<a href="#" onClick="return ttkInstructionOnClick('jzer')"><img id="jzer_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JZER<br>
										<a href="#" onClick="return ttkInstructionOnClick('jpos')"><img id="jpos_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JPOS<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnneg')"><img id="jnneg_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JNNEG<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnzer')"><img id="jnzer_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JNZER<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnpos')"><img id="jnpos_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JNPOS<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('jles')"><img id="jles_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JLES<br>
										<a href="#" onClick="return ttkInstructionOnClick('jequ')"><img id="jequ_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JEQU<br>
										<a href="#" onClick="return ttkInstructionOnClick('jgre')"><img id="jgre_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JGRE<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnle')"><img id="jnle_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JNLE<br>
										<a href="#" onClick="return ttkInstructionOnClick('jnequ')"><img id="jnequ_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JNEQU<br>
										<a href="#" onClick="return ttkInstructionOnClick('jngre')"><img id="jngre_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> JNGRE<br>
									</td>
									<td valign="top">
										<a href="#" onClick="return ttkInstructionOnClick('call')"><img id="call_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> CALL<br>
										<a href="#" onClick="return ttkInstructionOnClick('exit')"><img id="exit_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> EXIT<br>
										<a href="#" onClick="return ttkInstructionOnClick('svc')"><img id="svc_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> SVC<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('nop')"><img id="nop_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> NOP<br>
										<br>
										<a href="#" onClick="return ttkInstructionOnClick('equ')"><img id="equ_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> EQU<br>
										<a href="#" onClick="return ttkInstructionOnClick('dc')"><img id="dc_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> DC<br>
										<a href="#" onClick="return ttkInstructionOnClick('ds')"><img id="ds_img" src="neutral.gif" border="0" style="vertical-align:middle"></a> DS<br>
									</td>
								</tr>
							</table>
						</td>
						<td valign="top">
							Feedback if some forbidden instruction has been used:<br>
							<textarea name="<c:out value="${reqopcodes.id}"/>_feedback" cols="40" rows="6"><c:out value="${reqopcodes.failureFeedback}"/></textarea>
							<br><br>
							Feedback if not all required instructions have been used:<br>
							<textarea name="<c:out value="${banopcodes.id}"/>_feedback" cols="40" rows="6"><c:out value="${banopcodes.failureFeedback}"/></textarea>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td valign="top">Simulator output </td>
			<td>
				<c:set var="pub" value="${criteria['PUBOUT']}"/>
				<b>With public input:</b><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<tr>
						<td>Output</td>
						<td>Feedback if correct</td>
						<td>Feedback if wrong</td>
					</tr>
					<tr>
						<td><textarea name="<c:out value="${pub.id}"/>output_value" cols="20" rows="2"><c:out value="${pub.acceptanceTestValue}"/></textarea></td>
						<td><textarea name="<c:out value="${pub.id}"/>output_acceptance_feedback" cols="20" rows="2"><c:out value="${pub.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${pub.id}"/>output_failure_feedback" cols="20" rows="2"><c:out value="${pub.failureFeedback}"/></textarea></td>
					</tr>
				</table>
				<c:set var="sec" value="${criteria['SECOUT']}"/>
				<b>With secret input:</b><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<tr>
						<td>Output</td>
						<td>Feedback if correct</td>
						<td>Feedback if wrong</td>
					</tr>
					<tr>
						<td><textarea name="<c:out value="${sec.id}"/>output_value" cols="20" rows="2"><c:out value="${sec.acceptanceTestValue}"/></textarea></td>
						<td><textarea name="<c:out value="${sec.id}"/>output_acceptance_feedback" cols="20" rows="2"><c:out value="${sec.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${sec.id}"/>output_failure_feedback" cols="20" rows="2"><c:out value="${sec.failureFeedback}"/></textarea></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td valign="top">Quality criteria </td>
			<td>
				<table border="1" cellpadding="3" cellspacing="0">
					<tr>
						<td><p>Criterium</p></td>
						<td><p>Acceptance limit</p></td>
						<td><p>Quality limit</p></td>
						<td><p>Feedback: Accepted</p></td>
						<td><p>Accepted with high quality</p></td>
						<td><p>Not accepted</p></td>
					</tr>
					<tr>
						<td>Number of lines (in program)</td>
						<c:set var="codesize" value="${criteria['CODESIZE']}"/>
						<td><input name="<c:out value="${codesize.id}"/>_acceptance_limit" value="<c:out value="${codesize.acceptanceTestValue}"/>" type="text" size="2"></td>
						<td><input name="<c:out value="${codesize.id}"/>_quality_limit" value="<c:out value="${codesize.qualityTestValue}"/>" type="text" size="2"></td>
						<td><textarea name="<c:out value="${codesize.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${codesize.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${codesize.id}"/>_quality_feedback" cols="20" rows="2"><c:out value="${codesize.highQualityFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${codesize.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${codesize.failureFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Size of data storage</td>
						<c:set var="datasize" value="${criteria['DATASIZE']}"/>
						<td><input name="<c:out value="${datasize.id}"/>_acceptance_limit" value="<c:out value="${datasize.acceptanceTestValue}"/>" type="text" size="2"></td>
						<td><input name="<c:out value="${datasize.id}"/>_quality_limit" value="<c:out value="${datasize.qualityTestValue}"/>" type="text" size="2"></td>
						<td><textarea name="<c:out value="${datasize.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${datasize.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${datasize.id}"/>_quality_feedback" cols="20" rows="2"><c:out value="${datasize.highQualityFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${datasize.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${datasize.failureFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Maximum size of stack</td>
						<c:set var="stacksize" value="${criteria['STACKSIZE']}"/>
						<td><input name="<c:out value="${stacksize.id}"/>_acceptance_limit" value="<c:out value="${stacksize.acceptanceTestValue}"/>" type="text" size="2"></td>
						<td><input name="<c:out value="${stacksize.id}"/>_quality_limit" value="<c:out value="${stacksize.qualityTestValue}"/>" type="text" size="2"></td>
						<td><textarea name="<c:out value="${stacksize.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${stacksize.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${stacksize.id}"/>_quality_feedback" cols="20" rows="2"><c:out value="${stacksize.highQualityFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${stacksize.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${stacksize.failureFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Number of executed instructions</td>
						<c:set var="steps" value="${criteria['STEPS']}"/>
						<td><input name="<c:out value="${steps.id}"/>_acceptance_limit" value="<c:out value="${steps.acceptanceTestValue}"/>" type="text" size="2"></td>
						<td><input name="<c:out value="${steps.id}"/>_quality_limit" value="<c:out value="${steps.qualityTestValue}"/>" type="text" size="2"></td>
						<td><textarea name="<c:out value="${steps.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${steps.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${steps.id}"/>_quality_feedback" cols="20" rows="2"><c:out value="${steps.highQualityFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${steps.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${steps.failureFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Number of executed data references</td>
						<c:set var="dataref" value="${criteria['DATAREF']}"/>
						<td><input name="<c:out value="${dataref.id}"/>_acceptance_limit" value="<c:out value="${dataref.acceptanceTestValue}"/>" type="text" size="2"></td>
						<td><input name="<c:out value="${dataref.id}"/>_quality_limit" value="<c:out value="${dataref.qualityTestValue}"/>" type="text" size="2"></td>
						<td><textarea name="<c:out value="${dataref.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${dataref.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${dataref.id}"/>_quality_feedback" cols="20" rows="2"><c:out value="${dataref.highQualityFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${dataref.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${dataref.failureFeedback}"/></textarea></td>
					</tr>
					<tr>
						<td>Number of executed memory references</td>
						<c:set var="memref" value="${criteria['MEMREF']}"/>
						<td><input name="<c:out value="${memref.id}"/>_acceptance_limit" value="<c:out value="${memref.acceptanceTestValue}"/>" type="text" size="2"></td>
						<td><input name="<c:out value="${memref.id}"/>_quality_limit" value="<c:out value="${memref.qualityTestValue}"/>" type="text" size="2"></td>
						<td><textarea name="<c:out value="${memref.id}"/>_acceptance_feedback" cols="20" rows="2"><c:out value="${memref.acceptanceFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${memref.id}"/>_quality_feedback" cols="20" rows="2"><c:out value="${memref.highQualityFeedback}"/></textarea></td>
						<td><textarea name="<c:out value="${memref.id}"/>_failure_feedback" cols="20" rows="2"><c:out value="${memref.failureFeedback}"/></textarea></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>Endless loop prevention </td>
			<td>Maximum number of executed instructions <input name="maximum_number_of_executed_instructions" type="text"></td>
		</tr>
	</table>

	<br><br><br>

	<p>
		<input type="submit" value="Save task">
		<input type="button" value="Print task" onClick="showPrintDisplay();">
	</p>
</div>
</form>

</body>
</html>