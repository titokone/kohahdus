// NOTE: Works ONLY with composer.jsp

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

	requiredInstructionsField.value = requiredInstructions;
	forbiddenInstructionsField.value = forbiddenInstructions;
}