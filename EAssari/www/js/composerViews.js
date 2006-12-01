// NOTE: Works ONLY with composer.jsp

// show example code and register checkboxes, hide value fields
function switchToExampleView(){
	showElementById('exampleTable');
	showElementById('exampleCodeDiv');
	
	inputs = document.getElementsByTagName('input');
	for (i=0; i<inputs.length; i++){
		if (inputs[i].name.indexOf("_checked") != -1){
			inputs[i].style.display = "block";
		} else if (inputs[i].name.indexOf("_value") != -1){
			inputs[i].style.display = "none";
		}
	}
	
	textareas = document.getElementsByTagName('textarea');
	for (i=0; i<textareas.length; i++){
		if (textareas[i].name.indexOf("output_value") != -1){
			textareas[i].style.display = "none";
		}
	}
}

// show value fields, hide example code and register checkboxes
function switchToCriteriaView(){

	hideElementById('exampleCodeDiv');

	if(document.task_creation_form.task_type[0].checked == true) {
		hideElementById('exampleTable');
	}

	var inputs = document.getElementsByTagName('input');
	for (i=0; i<inputs.length; i++){
		if (inputs[i].name.indexOf("_checked") != -1){
			inputs[i].style.display = "none";
		} else if (inputs[i].name.indexOf("_value") != -1){
			inputs[i].style.display = "block";
		}
	}

	var textareas = document.getElementsByTagName('textarea');
	for (i=0; i<textareas.length; i++){
		if (textareas[i].name.indexOf("output_value") != -1){
			textareas[i].style.display = "block";
		}
	}
}

// hide partial program code snippets, put all code into one box
function switchToWholeProgramView() {
	hideElementById('partialProgramDiv1');
	hideElementById('partialProgramDiv2');
	document.task_creation_form.example_code.value = document.task_creation_form.partial_code1.value +  document.task_creation_form.example_code.value +  document.task_creation_form.partial_code2.value;

	if(document.task_creation_form.correctness_by[0].checked == true) {
		hideElementById('exampleTable');
	}
}

// show boxes for partial code
function switchToPartOfProgramView() {
	showElementById('exampleTable');
	showElementById('partialProgramDiv1');
	showElementById('partialProgramDiv2');
}

// replace textareas with text to have everything in view for easy printing
function switchToPrintableView(){
	elems = document.getElementsByTagName('textarea');
	for (i=0; i<elems.length; i++){
		var area = elems[i];
		var newElement = document.getElementById(area.name + "_printable_element");
		if (newElement == null) {
			newElement = document.createElement('P');
			newElement.innerHTML = area.value;
			newElement.id = area.name + "_printable_element";
			area.parentNode.insertBefore(newElement, area);
		}
		newElement.style.display = "block";
		area.style.display = "none";
	}
}

// return textareas
function switchPrintableViewOff(){
	elems = document.getElementsByTagName('textarea');
	for (i=0; i<elems.length; i++){
		var area = elems[i];
		var pElement = document.getElementById(area.name + "_printable_element");
		pElement.style.display = "none";
		area.style.display = "block";
	}
}
