// NOTE: Works ONLY with composer.jsp

// initialize TTK-91 instruction arrays and switch to initial view
function initTaskCreation()
{
	// Javascript version of a hash table - location of a instruction name in the array "instructionNames" is the same as the location of the
	// instruction status in array "instructionStatus".
	instructionNames = new Array("load", "store", "push", "pop", "pushr", "popr", "in", "out", "add", "sub", "mul", "div", "mod", "not", "and", "or",
	"xor", "shl", "shr", "comp", "jump", "jneg", "jzer", "jpos", "jnneg", "jnzer", "jnpos", "jles", "jequ", "jgre", "jnle", "jnequ", "jngre", "call",
	"exit", "svc", "nop", "equ", "dc", "ds");

	instructionStatus = new Array(instructionNames.length);

	for (var counter = 0; counter < instructionStatus.length; counter++) {
		instructionStatus[counter] = NEUTRAL_STATUS;
	}

	// parse values already in required and forbidden instructions fields and set symbols in GUI
	if(requiredInstructionsField.value != "") {
		var requiredInstructions = requiredInstructionsField.value.split(", ")
		var img;

		for (var requiredCounter = 0; requiredCounter < requiredInstructions.length; requiredCounter++) {
			for (var nameCounter = 0; nameCounter < instructionNames.length; nameCounter++) {
				if(instructionNames[nameCounter].toUpperCase() == requiredInstructions[requiredCounter].toUpperCase()) {
					instructionStatus[nameCounter] = REQUIRED_STATUS;
					img = document.getElementById(instructionNames[nameCounter] + "_img");
					img.src = positive.src;
					break;
				}
			}
		}
	}

	if(forbiddenInstructionsField.value != "") {
		var forbiddenInstructions = forbiddenInstructionsField.value.split(", ")
		var img;

		for (var forbiddenCounter = 0; forbiddenCounter < forbiddenInstructions.length; forbiddenCounter++) {
			for (var nameCounter = 0; nameCounter < instructionNames.length; nameCounter++) {
				if(instructionNames[nameCounter].toUpperCase() == forbiddenInstructions[forbiddenCounter].toUpperCase()) {
					instructionStatus[nameCounter] = FORBIDDEN_STATUS;
					img = document.getElementById(instructionNames[nameCounter] + "_img");
					img.src = negative.src;
					break;
				}
			}
		}
	}

	if(document.task_creation_form.correctness_by[1].checked == true) {
		switchToExampleView();	
	} else {
		switchToCriteriaView(); // default to predefined values if somehow selection has not been made
	}

	if(document.task_creation_form.task_type[1].checked == true) {
		switchToPartOfProgramView();	
	} else {
		switchToWholeProgramView();	// default to whole program view if somehow selection has not been made
	}
}

function onFormSubmit() {
	instructionRequirementsIntoText();
	document.task_creation_form.symbol_criterion_count.value = variableCounter;
	
	// trim white space
	var textareas = document.getElementsByTagName('textarea');
	for (var i = 0; i < textareas.length; i++) {
		trimWhitespace(textareas[i]);
	}
	
	var inputs = document.getElementsByTagName('input');
	for (var i = 0; i < inputs.length; i++){
		if (inputs[i].type == 'text'){
			trimWhitespace(inputs[i]);
		}
	}
	
	// find and report errors
	var alertText = 'Your form contains the following errors. Please fix them before resubmitting.\n';
	var alertCounter = 0;

	if(document.task_creation_form.task_name.value.length == 0) {
		alertCounter++;
		alertText += '\n' + alertCounter + '. Please specify a task name.';
	}

	if(document.task_creation_form.task_name.value.length > 40) {
		alertCounter++;
		alertText += '\n' + alertCounter + '. Task name may only be up to 40 characters long.';
	}
	
	if(containsHtmlCharacters(document.task_creation_form.task_name.value)) {
		alertCounter++;
		alertText += '\n' + alertCounter + '. Task name may not contain characters ", <, > and &.';
	}
	
	// inputs
	if(document.task_creation_form.public_input.value != '') {
		if(!isValidTitokoneInputOutput(document.task_creation_form.public_input.value)) {
			alertCounter++;
			alertText += '\n' + alertCounter + '. Public input must be integers separated by commas with optional spaces.';
		}
	}	

	if(document.task_creation_form.secret_input.value != "") {
		if(!isValidTitokoneInputOutput(document.task_creation_form.secret_input.value)) {
			alertCounter++;
			alertText += '\n' + alertCounter + '. Secret input must be integers separated by commas with optional spaces.';
		}
	}	

	// registers and variables (only if correctness is determined by predefined values)
	if(document.task_creation_form.correctness_by[0].checked == true) {
		for (i=0; i<inputs.length; i++) {
			if (((inputs[i].name.indexOf(registerPrefix) != -1) && (inputs[i].name.indexOf("_value") != -1)) || ((inputs[i].name.indexOf(variablePrefix) != -1) && (inputs[i].name.indexOf("_value") != -1))) {
				if((inputs[i].value != "") && (!isInteger(inputs[i].value))) {
					alertCounter++;
					alertText += '\n' + alertCounter + '. Register and variable values must be integers.';
					break;
				}
			}
		}
	}
	

	// outputs
	if(publicOutputField.value != "") {
		if(!isValidTitokoneInputOutput(publicOutputField.value)) {
			alertCounter++;
			alertText += '\n' + alertCounter + '. Simulator output with public input must be integers separated by commas with optional spaces.';
		}
	}	
	
	if(secretOutputField.value != '') {
		if(!isValidTitokoneInputOutput(secretOutputField.value)) {
			alertCounter++;
			alertText += '\n' + alertCounter + '. Simulator output with secret input must be integers separated by commas with optional spaces.';
		}
	}	
	
	
	// quality criteria
	for (i=0; i<inputs.length; i++) {
		if ((inputs[i].name.indexOf("_acceptance_limit") != -1) || (inputs[i].name.indexOf("_quality_limit") != -1)) {
			if((inputs[i].value != "") && ((!isInteger(inputs[i].value)) || (Number(inputs[i].value) < 0))) {
				alertCounter++;
				alertText += '\n' + alertCounter + '. Acceptance and quality limits of quality criteria may only be non-negative integers.';
				break;
			}
		}
	}
	
	// endless loop prevention
	if((document.task_creation_form.maximum_number_of_executed_instructions.value == '') ||
	(!isInteger(document.task_creation_form.maximum_number_of_executed_instructions.value)) ||
	(Number(document.task_creation_form.maximum_number_of_executed_instructions.value) <= 0)) {
		alertCounter++;
		alertText += '\n' + alertCounter + '. Maximum number of executed instructions must be a number greater than 0.';
	}
	
	if(alertCounter == 0) {
		if(Number(document.task_creation_form.maximum_number_of_executed_instructions.value) > 100000) {
			return confirm('Are you sure you want the maximum number of executed instructions to be ' + document.task_creation_form.maximum_number_of_executed_instructions.value + '?');
		} else {
			return true;
		}
	}
	else {
		alert(alertText);
	
		return false;
	}
}