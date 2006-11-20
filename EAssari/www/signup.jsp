<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="java.util.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Sign up</title>
<script language="javascript" type="text/javascript" src="js/textValidityFunctions.js"></script>
<script language="Javascript">

	/* Function to check the validity of form inputs that can be checked client-side - called on submit event. */
	function checkForm() {
		var form = document.sign_up_form;
		var returnvalue = true;
		var emailExp = /([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})$/;	// name followed by @ followed by domain
		var userNameExp = /([a-zA-Z0-9])+/;

		// remove old error messages
		document.getElementById("first_name_error_msg_space").innerHTML = '';
		document.getElementById("last_name_error_msg_space").innerHTML = '';
		document.getElementById("student_number_error_msg_space").innerHTML = '';
		document.getElementById("ssn_error_msg_space").innerHTML = '';
		document.getElementById("email_error_msg_space").innerHTML = '';
		document.getElementById("user_name_error_msg_space").innerHTML = '';
		document.getElementById("password_error_msg_space").innerHTML = '';
		document.getElementById("repeat_password_error_msg_space").innerHTML = '';

		// missing inputs
		if(form.first_name.value == '') {
			var elem = document.getElementById("first_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please fill in your first name.</b></font>';
			returnvalue = false;
		}

		if(form.last_name.value == '') {
			var elem = document.getElementById("last_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please fill in your last name.</b></font>';
			returnvalue = false;
		}

		if(form.student_number.value == '' && form.social_security_number.value == '') {
			var elem = document.getElementById("student_number_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please fill in either your student number or social security number.</b></font>';
			returnvalue = false;
		}

		if(form.email.value == '') {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please fill in your e-mail address.</b></font>';
			returnvalue = false;
		}

		if(form.user_name.value == '') {
			var elem = document.getElementById("user_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please choose a user name.</b></font>';
			returnvalue = false;
		}

		if(form.password.value == '') {
			var elem = document.getElementById("password_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please choose a password.</b></font>';
			returnvalue = false;
		}

		if(form.repeat_password.value == '') {
			var elem = document.getElementById("repeat_password_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please repeat your chosen password.</b></font>';
			returnvalue = false;
		}
		
		// first name contains illegal html-characters
		if(containsHtmlCharacters(form.first_name.value)) {
			var elem = document.getElementById("first_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Field may not contain characters ", <, >, &.</b></font>';
			returnvalue = false;
		}
		
		// first name not of valid length
		if(form.first_name.value.length > 40) {
			var elem = document.getElementById("first_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>First name must be 1-40 characters long.</b></font>';
			returnvalue = false;
		}
		
		// last name contains illegal html-characters
		if(containsHtmlCharacters(form.last_name.value)) {
			var elem = document.getElementById("last_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Field may not contain characters ", <, >, &.</b></font>';
			returnvalue = false;
		}
		
		// last name not of valid length
		if(form.last_name.value.length > 40) {
			var elem = document.getElementById("last_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Last name must be 1-40 characters long.</b></font>';
			returnvalue = false;
		}

		// student number of wrong format
		if((form.student_number.value != '') && (!studentNumberValid(form.student_number.value))) {
			var elem = document.getElementById("student_number_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Your student number is of wrong format.</b></font>';
			returnvalue = false;
		}

		// social security number of wrong format
		if((form.social_security_number.value != '') && (!socialSecurityNumberValid(form.social_security_number.value))) {
			var elem = document.getElementById("ssn_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Social security number must be of Finnish format.</b></font>';
			returnvalue = false;
		}

		// e-mail address of wrong format
		if((form.email.value != '') && (!emailExp.test(form.email.value))) {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Your e-mail address isn\'t of a valid format. A valid format would be e.g. user@cs.helsinki.fi</b></font>';
			returnvalue = false;
		}

		// e-mail address contains illegal html-characters
		if(containsHtmlCharacters(form.email.value)) {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Field may not contain characters ", <, >, &.</b></font>';
			returnvalue = false;
		}

		// e-mail address not of valid length
		if(form.email.value.length > 80) {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>E-mail address may contain only up to 80 characters.</b></font>';
			returnvalue = false;
		}

		// user name contains illegal characters
		if((form.user_name.value != '') && (!userNameExp.test(form.user_name.value))) {
			var elem = document.getElementById("user_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>User name may only contain characters a-z, A-Z and 0-9.</b></font>';
			returnvalue = false;
		}
		
		// user name not of valid length
		if((form.user_name.value != '') && ((form.user_name.value.length < 3) || (form.user_name.value.length > 20))) {
			var elem = document.getElementById("user_name_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>User name must be 3-20 characters long.</b></font>';
			returnvalue = false;
		}

		// "password" and "repeat password" don't match and neither is empty
		if((form.password.value != '') && (form.repeat_password.value != '') && (form.password.value != form.repeat_password.value)) {
			var elem = document.getElementById("password_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Different values in password fields. Please check your typing.</b></font>';			
			returnvalue = false;
		}
		
		// password not of valid length
		if((form.password.value != '') && ((form.password.value.length < 6) || (form.password.value.length > 12))) {
			var elem = document.getElementById("password_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Password must be 6-12 characters long.</b></font>';
			returnvalue = false;
		}

		return returnvalue;
	}

	/* Function to check if student number is of valid format. */
	function studentNumberValid(numberString) {
		var weights = new Array(7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1);
		var numberLength = numberString.length - 1;
		var sum = 0;
		var checkSymbol = numberString.charAt(numberLength);

		if((checkSymbol <'0') || (checkSymbol > '9')) {
			return false;
		}

		var checkNumber = Number(checkSymbol);

		numberLength--;

		// check that student number contains only digits and sum for calculating correct checkNumber
		for (var counter = 0; counter <= numberLength; counter++) {
			var singleDigit = numberString.charAt(numberLength - counter);
			if((singleDigit <'0') || (singleDigit > '9')) {
				return false;
			}
			sum += weights[counter] * Number(singleDigit);
		}

		var last = sum % 10;

		var correctCheck = 0;

		if(last == 0) {
			correctCheck = 0;
		} else {
			correctCheck = 10 - last;
		}

		if(correctCheck != checkNumber) {
			return false;
		} else {
			return true;
		}
	}

	/* Function to check if social security number is of valid format. */
	function socialSecurityNumberValid(ssn) {
		ssn = ssn.toUpperCase();
		var checkSymbolArray = "0123456789ABCDEFHJKLMNPRSTUVWXY";

		// wrong length
		if(ssn.length != 11) {
			return false;
		}

		// Finnish social security number is of format ddmmyyNxxxS, in which ddmmyy is birthday, N separator character, 
		// xxx individual number and S checking symbol.
		var separator = ssn.charAt(6);

		// - for those born in 20th century and A for those born in 21st
		if((separator == '-') || (separator == 'A')) {
       			ssnWithoutSeparatorAndCheck = ssn.substring(0, 6) + ssn.substring(7, ssn.length-1);
		} else {
			return false;
		}

		// Must contain only numbers
		for (var counter = 0; counter < ssnWithoutSeparatorAndCheck.length; counter++) { 
			if ((ssnWithoutSeparatorAndCheck.charAt(counter) < '0') || (ssnWithoutSeparatorAndCheck.charAt(counter)>'9')) {
				return false;
			}
		}

		// check symbol is calculated by treating everything else as a 9-digit number and taking a modulo 31
		var numberToDivide = Number(ssnWithoutSeparatorAndCheck);
		var checkSymbol = ssn.charAt(ssn.length-1);
		var mod31= numberToDivide % 31;

		if(checkSymbol != checkSymbolArray.charAt(mod31)){
			return false;
		} else {
			return true;
		}
	}
</script>
</head>

<body>

<h2>TitoTrainer - Sign up</h2>


<c:if test="${param.action=='signup'}">
	
	<% //Create a new user and store it in pagecontext as a temp object
	   	User newUser = new User(request.getParameter("user_name"));	   	
	   	newUser.setStatus(User.STATUS_STUDENT);
	   	newUser.setLanguage(request.getParameter("language").equals("english") ? "EN" : "FI");
	   	pageContext.setAttribute("newUser", newUser);
		Log.write("Signup: creating a new user");
	%>
	
		<%-- set new users properties as requested --%>		
		<c:set target="${newUser}" property="firstName" value="${param.first_name}"/>
		<c:set target="${newUser}" property="lastName" value="${param.last_name}"/>
		<c:set target="${newUser}" property="socialSecurityNumber" value="${param.social_security_number}"/>
		<c:set target="${newUser}" property="studentNumber" value="${param.student_number}"/>
		<c:set target="${newUser}" property="email" value="${param.email}"/>
		<c:set target="${newUser}" property="password" value="${param.password}"/>
						
	<% 	//Check DB for existing users with same id
		//FIXME: is there a possibility that username would be null
	   	User oldUser = DBHandler.getInstance().getUser(request.getParameter("user_name"));
	   	if (oldUser != null) pageContext.setAttribute("oldUser", oldUser); 		   	   
	%>
	
	<c:choose>
		<c:when test="${not empty oldUser}">
			<%-- TODO: display a proper error msg --%>
			<p>User name invalid or already in use. Please pick another.
		</c:when>
		<c:otherwise>			
			<%
				if (!newUser.isValid()) {
					//TODO: ohjaus erroriin
					out.print("ERROR -- user data corrupt or incomplete");
					Log.write("Signup: error in new users data");
				} else {
					//Add new user to db and set up session
					DBHandler.getInstance().createUser(newUser);
					pageContext.setAttribute("user", newUser, PageContext.SESSION_SCOPE);
					pageContext.setAttribute("course", request.getParameter("course"), PageContext.SESSION_SCOPE);
					pageContext.setAttribute("language", request.getParameter("language"), PageContext.SESSION_SCOPE);
					Log.write("Signup: new user created");
			%>		
					<c:redirect url="student/studentTaskList.jsp"/>
			<%		
				}
			%>	
		</c:otherwise>
	</c:choose>			   
</c:if>

<%-- signup form - already inserted values are preserved in newUser variable --%>
<form name="sign_up_form" action="signup.jsp" onsubmit="return checkForm()" method="POST">
<input type="hidden" name="action" value="signup">

<p>Please fill in all the fields.</p>

<div>
	<table border="0" cellpadding="5">
		<tr>
			<td><b>First name </b></td>
			<td><input type="text" name="first_name" value="<c:out value="${newUser.firstName}"/>" onChange="trimWhitespace(this)"></td>
			<td id="first_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Last name </b></td>
			<td><input type="text" name="last_name" value="<c:out value="${newUser.lastName}"/>" onChange="trimWhitespace(this)"></td>
			<td id="last_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>E-mail: </b></td>
			<td><input type="text" name="email" value="<c:out value="${newUser.email}"/>" onChange="trimWhitespace(this)"></td>
			<td id="email_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Student number<sup>1</sup> </b></td>
			<td><input type="text" name="student_number" value="<c:out value="${newUser.studentNumber}"/>" onChange="trimWhitespace(this)"></td>
			<td id="student_number_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Social security number<sup>1</sup> </b></td>
			<td><input type="text" name="social_security_number" value="<c:out value="${newUser.socialSecurityNumber}"/>" onChange="trimWhitespace(this)"></td>
			<td id="ssn_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2"><small><sup>1</sup> either student number or social security number is required</small></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2"><hr></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b>Course</b></td>
				
			<%	
				List<Course> courses = DBHandler.getInstance().getCourses();
				pageContext.setAttribute("courses", courses);				
			%>
				
			<td>
				<select name="course">
					<c:if test="${not empty courses}">
						<c:forEach var="course" items="${pageScope.courses}">
							<option value="<c:out value="${course.ID}"/>"><c:out value="${course.name}"/></option>							
						</c:forEach>
					</c:if>	
				</select>
			</td>
		</tr>			
		<tr>
			<td><b>Language</b></td>
			<td>
				<select name="language">
					<option value="EN">English</option>
					<option value="FI">Finnish</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><b>User name </b></td>
			<td><input type="text" name="user_name" value="<c:out value="${newUser.userID}"/>" onChange="trimWhitespace(this)"></td>
			<td id="user_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Password </b></td>
			<td><input type="password" name="password" onChange="trimWhitespace(this)"></td>
			<td id="password_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Password again </b></td>
			<td><input type="password" name="repeat_password" onChange="trimWhitespace(this)"></td>
			<td id="repeat_password_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" name="sign_up_button" value="Sign up"></td>
			<td>&nbsp;</td>
		</tr>
	</table>
</div>

</form>

<%-- Just to make sure that old data doesn't interfere with next request --%>
<c:remove var="newUser" scope="page"/>
<c:remove var="oldUser" scope="page"/>

</body>
</html>