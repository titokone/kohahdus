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

/* use insertion sort to sort tasks into order by task status*/
function sortTasksByStatus(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	//  order: success, failure, - (not tried)
			while (j > 0 && tasks[j-1].status.toLowerCase() < temp.status.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// order: not tried, failure, success
			while (j > 0 && tasks[j-1].status.toLowerCase() > temp.status.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by task name */
function sortTasksByName(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && tasks[j-1].name.toLowerCase() > temp.name.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && tasks[j-1].name.toLowerCase() < temp.name.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by task type */
function sortTasksByType(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && tasks[j-1].type.toLowerCase() > temp.type.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && tasks[j-1].type.toLowerCase() < temp.type.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by task category */
function sortTasksByCategory(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && tasks[j-1].category.toLowerCase() > temp.category.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && tasks[j-1].category.toLowerCase() < temp.category.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into order by number of tries */
function sortTasksByTries(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && Number(tasks[j-1].tries) > Number(temp.tries)) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && Number(tasks[j-1].tries) < Number(temp.tries)) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into alphabetical order by task language */
function sortTasksByLanguage(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && tasks[j-1].language.toLowerCase() > temp.language.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && tasks[j-1].language.toLowerCase() < temp.language.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		
		tasks[j] = temp;
	}

	writeTaskList();
}