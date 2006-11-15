<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Edit Your Profile</title>
<script language="javascript" type="text/javascript" src="../js/textValidityFunctions.js"></script>
<script language="Javascript">

	/* Function to check the validity of form inputs that can be checked client-side - called on submit event. */
	function checkForm() {
	
		var form = document.modify_user_form;
		var returnvalue = true;
		var emailExp = /([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})$/;	// name followed by @ followed by domain

		// remove old error messages
		document.getElementById("first_name_error_msg_space").innerHTML = '';
		document.getElementById("last_name_error_msg_space").innerHTML = '';
		document.getElementById("student_number_error_msg_space").innerHTML = '';
		document.getElementById("ssn_error_msg_space").innerHTML = '';
		document.getElementById("email_error_msg_space").innerHTML = '';
		document.getElementById("new_password_error_msg_space").innerHTML = '';
		document.getElementById("repeat_new_password_error_msg_space").innerHTML = '';

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
		

		// new password chosen, but not repeated
		if((form.new_password.value != '') && (form.repeat_new_password.value == '')) {
			var elem = document.getElementById("repeat_new_password_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please repeat your new password.</b></font>';
			returnvalue = false;
		}

		// new password repeated, but not chosen
		if((form.repeat_new_password.value != '') && (form.new_password.value == '')) {
			var elem = document.getElementById("new_password_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Please type your new password.</b></font>';
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

		// Only if user is trying to change password and "password" and "repeat password" don't match and neither is empty
		if ((form.new_password.value != '') || (form.repeat_new_password.value != '')) {
			if((form.new_password.value != '') && (form.repeat_new_password.value != '') && (form.new_password.value != form.repeat_new_password.value)) {
				var elem = document.getElementById("new_password_error_msg_space");
				elem.innerHTML = '<font color="#FF0000"><b>Different values in password fields. Please check your typing.</b></font>';			
				returnvalue = false;
			}
		}
		
		// new password not of valid length
		if((form.new_password.value != '') && ((form.new_password.value.length < 6) || (form.new_password.value.length > 12))) {
			var elem = document.getElementById("new_password_error_msg_space");
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

<%-- check that user is logged in --%>
<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="../login.jsp"/>	
</c:if>

<jsp:include page="../menu.jsp"/>

<c:if test="${param.action=='modify'}">


	<c:set target="${user}" property="firstName" value="${param.first_name}"/>
	<c:set target="${user}" property="lastName" value="${param.last_name}"/>
	<c:set target="${user}" property="email" value="${param.email}"/>

	<c:set target="${user}" property="studentNumber" value="${param.student_number}"/>
	<c:set target="${user}" property="socialSecurityNumber" value="${param.social_security_number}"/>


	<c:if test="${not empty param.new_password && not empty param.repeat_new_password && (param.new_password == param.repeat_new_password)}">
		<c:set target="${user}" property="password" value="${param.new_password}"/>
	</c:if>

	
			<%
			User user = (User) session.getAttribute("user");
			if (user.isValid()) {
	 			boolean testi = DBHandler.getInstance().updateUser(user);
	 			out.print("<BR>Updated to database.");
	 			Log.write("Modify_User: Updated user "+user.getFirstName()+" "+user.getLastName());
	 		} else {
	 		out.print("<BR>Error in updating database");
	 		}		
			%>	

	
</c:if>


<h2>Edit Your Profile</h2>



<br>

<form name="modify_user_form" action="modify_user.jsp" onsubmit="return checkForm()" method="POST">
<input type="hidden" name="action" value="modify">




<div>
        <table border="0" cellpadding="5">
                <tr>
                        <td><b>User name </b></td>
                        <td><c:out value="${user.userID}"/></td>
                </tr>
                <tr>
                        <td><b>First name </b></td>
                        <td><input type="text" name="first_name" value="<c:out value="${user.firstName}"/>">    </td>
                        <td id="first_name_error_msg_space">&nbsp;</td>                        
                </tr>
                <tr>
                        <td><b>Last name </b></td>
                        <td><input type="text" name="last_name" value="<c:out value="${user.lastName}"/>"></td>
                        <td id="last_name_error_msg_space">&nbsp;</td>
                </tr>
                <tr>
                        <td><b>E-mail: </b></td>
                        <td><input type="text" name="email" value="<c:out value="${user.email}"/>"></td>
                        <td id="email_error_msg_space">&nbsp;</td>
                </tr>
    
                <tr>
                        <td><b>Student number* </b></td>
                        <td><input type="text" name="student_number" value="<c:out value="${user.studentNumber}"/>"> </td>
                        <td id="student_number_error_msg_space">&nbsp;</td>
                </tr>
                <tr>
                        <td><b>Social security number* </b></td>
                        <td><input type="text" name="social_security_number" value="<c:out value="${user.socialSecurityNumber}"/>"> </td>
                        <td id="ssn_error_msg_space">&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2"><small>* either student number or social security number is required</small></td>
                        <td>&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2"><hr></td>
                        <td>&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2"><b><i>Change password</i></b></td>
                </tr>
                <tr>
                        <td><b>New password </b></td>
                        <td><input type="password" name="new_password"></td>
                         <td id="new_password_error_msg_space">&nbsp;</td>
                </tr>
                <tr>
                        <td><b>New password again </b></td>
                        <td><input type="password" name="repeat_new_password"></td>
                        <td id="repeat_new_password_error_msg_space">&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2" align="right"><input type="submit" name="save_button" value="Save"></td>
                </tr>
        </table>
</div>

</form>
</body>
</html>