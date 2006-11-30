// NOTE: Works only with teacherTaskList.jsp

/* Object constructor for Task objects */
function Task(id, name, type, category, language, author, modificationDate) {
	this.id = id;
	this.name = name;
	this.type = type;
	this.category = category;
	this.language = language
	this.author = author;
	this.modificationDate = modificationDate;
}

/* input tasks into task list */
function writeTaskList() {
	var headerHtml = '<table class="listTable" border="1px"><tr><td class="titleBar">ID</td>';
	headerHtml += '<td class="titleBar">Name <a href="javascript: sortTasksByName(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByName(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar">Type <a href="javascript: sortTasksByType(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByType(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar">Category <a href="javascript: sortTasksByCategory(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByCategory(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar">Language <a href="javascript: sortTasksLanguage(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByLanguage(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar">Author <a href="javascript: sortTasksByAuthor(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByAuthor(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar">Modified <a href="javascript: sortTasksByModificationDate(tasks, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortTasksByModificationDate(tasks, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar" colspan="3"></td></tr>';
	
	var listHtml = '';

	if(tasksAvailable == true) {
		for(var i = 0; i < tasks.length; i++) {
			listHtml += '<tr><td>' + tasks[i].id + '</td>';
			listHtml += '<td><a href="answer_task.jsp?task_id=' + tasks[i].id + '">' + tasks[i].name + '</td>';
			listHtml += '<td>' + tasks[i].type + '</td>';
			listHtml += '<td>' + tasks[i].category + '</td>';
			listHtml += '<td>' + tasks[i].language + '</td>';
			listHtml += '<td>' + tasks[i].author + '</td>';
			listHtml += '<td>' + tasks[i].modificationDate + '</td>';
			listHtml += '<td><input type="button" value="Modify" onclick="location.href = \'composer.jsp?task_id=' + tasks[i].id + '&save_type=update\'">&nbsp;';
			listHtml += '<input type="button" value="Modify as new" onclick="location.href = \'composer.jsp?task_id=' + tasks[i].id + '&save_type=new\'">&nbsp;';
			listHtml += '<input type="button" value="Delete" onclick="Javascript:deleteTask(\'' + tasks[i].name + '\', \'' + tasks[i].id + '\');"></td></tr>';
		}
	} else {	
		listHtml = '<tr><td colspan="9">No tasks.</td></tr>';	
	}
	
	listHtml += '</table>';
	
	var taskListElem = document.getElementById("taskList");
	taskListElem.innerHTML = headerHtml + listHtml;
}

function deleteTask(taskName, taskID) {
	if (window.confirm('Do you really want to delete task '+taskName+'?')) {
		location.href="teacherTaskList.jsp?action=deleteTask&taskID="+taskID;
	}
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

/* use insertion sort to sort tasks into alphabetical order by author */
function sortTasksByAuthor(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && tasks[j-1].author.toLowerCase() > temp.author.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && tasks[j-1].author.toLowerCase() < temp.author.toLowerCase()) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		
		tasks[j] = temp;
	}

	writeTaskList();
}

/* use insertion sort to sort tasks into order by last modification date */
function sortTasksByModificationDate(tasks, asc) {
	for(var i = 1; i < tasks.length; i++) {
		var temp = tasks[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && tasks[j-1].modificationDate > temp.modificationDate) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && tasks[j-1].modificationDate < temp.modificationDate) {
				tasks[j] = tasks[j-1]; 
				--j;
			}
		}
		
		tasks[j] = temp;
	}

	writeTaskList();
}