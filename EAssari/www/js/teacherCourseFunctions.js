// NOTE: Works only with teacherTaskList.jsp

/* Object constructor for Course objects */
function Course(id, name) {
	this.name = name;
	this.id = id;
}

/* input courses into course list */
function writeCourseList() {

	var headerHtml = '<table class="listTable" border="1px"><tr><td class="titleBar">ID</td>';
	headerHtml += '<td class="titleBar">Name <a href="javascript: sortCoursesByName(courses, true)"><img src="images/arrow-down.gif" border="0"></a><a href="javascript: sortCoursesByName(courses, false)"><img src="images/arrow-up.gif" border="0"></a></td>';
	headerHtml += '<td class="titleBar">&nbsp;</td></tr>';
	
	var listHtml = '';

	if(coursesAvailable == true) {
		for(var i = 0; i < courses.length; i++) {
			listHtml += '<tr><td>' + courses[i].id + '</td>';
			listHtml += '<td>' + courses[i].name + '</td>';
			listHtml += '<td><input type="button" value="Statistics" onClick="Javascript:location.href=\'showStatistics.jsp?courseID=' + courses[i].id + '\';">&nbsp;';
			listHtml += '<input type="button" value="Delete" onclick="Javascript:deleteCourse(\'' + courses[i].name + '\', \'' + courses[i].id + '\');"></td></tr>';
		}
	} else {	
		listHtml = '<tr><td colspan="4">No available courses.</td></tr>';	
	}
	var newCourseHtml = '<tr><td>Course name</td><td><input type="text" name="new_course"></td>';
	newCourseHtml += '<td colspan="2"><input type="submit" name="create_course_button" value="Create new course"></td>';
	newCourseHtml += '</tr></table>';
	
	var courseListElem = document.getElementById("courseList");
	courseListElem.innerHTML = headerHtml + listHtml + newCourseHtml; 
}

/* use insertion sort to sort courses into alphabetical order */
function sortCoursesByName(courses, asc) {
	for(var i = 1; i < courses.length; i++) {
		var temp = courses[i];
		var j = i;
		
		if(asc) {	// ascending order
			while (j > 0 && courses[j-1].name.toLowerCase() > temp.name.toLowerCase()) {
				courses[j] = courses[j-1]; 
				--j;
			}
		} else {	// descending order
			while (j > 0 && courses[j-1].name.toLowerCase() < temp.name.toLowerCase()) {
				courses[j] = courses[j-1]; 
				--j;
			}
		}
		
		courses[j] = temp;
	}

	writeCourseList();
}

function deleteCourse(courseName, courseID) {
	if (window.confirm('Do you really want to delete course '+courseName+'?')) {
		location.href="teacherTaskList.jsp?action=deleteCourse&courseID="+courseID;
	}
}

function checkNewCourseInputValidity() {
	trimWhitespace(document.create_course_form.new_course);

	var courseName = document.create_course_form.new_course.value;
	var feedbackElem = document.getElementById('new_course_creation_feedback');
	
	feedbackElem.innerHTML = '';
	
	if(containsHtmlCharacters(courseName)) {
		feedbackElem.innerHTML = 'Course name may not contain characters ", <, >, &.';
		return false;
	}
	
	if((courseName.length < 1) || (courseName.length > 40)) {
		feedbackElem.innerHTML = 'Course name must be 1-40 characters long.';
		return false;
	}
	
	return true;
}