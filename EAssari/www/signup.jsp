<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - Sign up</title>
<script language="Javascript">

	/* Function to check the validity of form inputs that can be checked client-side - called on submit event. */
	function checkForm() {
		var form = document.sign_up_form;
		var returnvalue = true;

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

		// student number of wrong format
		if((form.student_number.value != '') && (form.student_number.value.length != 9 || !stringContainsOnlyNumbers(form.student_number.value))) {
			var elem = document.getElementById("student_number_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Your student number is either of wrong length or contains non-numeric characters.</b></font>';
			returnvalue = false;
		}


		// "password" and "repeat password" don't match and neither is empty
		if((form.password.value != '') && (form.repeat_password.value != '') && (form.password.value != form.repeat_password.value)) {
			var elem = document.getElementById("password_error_msg_space");
			elem.innerHTML = '<font color="#FF0000"><b>Different values in password fields. Please check your typing.</b></font>';			
			returnvalue = false;
		}

		return returnvalue;
	}

	/* Function to check if the string given as a parameter contains only numeric characters. */
	function stringContainsOnlyNumbers(aString) {

		for (var charCounter = 0; charCounter < aString.length; charCounter++) {
			if(isNaN(aString.charAt(charCounter)) || aString.charAt(charCounter) == ' '.charAt(0)) {	// JS interprets white space as numeric
				return false;
			}
		}

		return true;
	}

</script>
</head>

<body>

<h2>TitoTrainer - Sign up</h2>


<c:if test="${param.action=='signup'}">
	
	SIGNING UP WITH FOLLOWING INFO

	<p>User name: <c:out value="${param.user_name}"/>
	<p>First name: <c:out value="${param.firstName}"/>
	<p>Last name: <c:out value="${param.last_name}"/>
	<p>Student num: <c:out value="${param.student_number}"/>
	<p>Soc Sec num: <c:out value="${param.social_security_number}"/>
	<p>Email: <c:out value="${param.email}"/>
	<p>Password: <c:out value="${param.password}"/>
	
	<% User user = DBHandler.getInstance().getUser(request.getParameter("user_name"));
	   if (user != null) pageContext.setAttribute("user", user, PageContext.SESSION_SCOPE);
	   
	   //For debugging purposes only
	   if (user != null) {
	   		out.print("OLD USER HAS A SAME ID");
	   }		 		   	   
	%>
	
	<c:choose>
		<c:when test="${not empty user}">
			<p>Current user name already in use. Please pick another.
		</c:when>
		<c:otherwise>		
			<p>ADDING TO DATABASE
			<% //Create a new user
			   	user = new User(request.getParameter("user_name"));
			   	user.setStatus(User.STATUS_STUDENT);
			   	user.setLanguage("EN");
			   	pageContext.setAttribute("user", user, PageContext.SESSION_SCOPE);
			   	
			   	  	
			   	//TODO: sama JSTL:llä
			   	user.setFirstName(request.getParameter("firstName"));
			   	user.setLastName(request.getParameter("last_name"));
			   	user.setSocialSecurityNumber(request.getParameter("social_security_number"));
			   	user.setStudentNumber(request.getParameter("student_number"));
			   	user.setEmail(request.getParameter("email"));
			   	user.setPassword(request.getParameter("password"));
			%>
			<p>ID <c:out value="${user.userID}"/>
			<p>FN <c:out value="${user.firstName}"/>
			<p>LN <c:out value="${user.lastName}"/>
			<p>SSN <c:out value="${user.socialSecurityNumber}"/>
			<p>STN <c:out value="${user.studentNumber}"/>
			<p>EM <c:out value="${user.email}"/>
			<p>PW <c:out value="${user.password}"/>   	
			
			<%
				if (!user.isValid()) {
					//TODO: ohjaus erroriin
					out.print("Grave ERROR");
				} else {
					DBHandler.getInstance().createUser(user);
					out.print("USER CREATED -- FORWARDING TO LOGIN");
					//TODO: forwardointi
				}
			%>	
			
		</c:otherwise>
	</c:choose>			   
</c:if>








<form name="sign_up_form" action="signup.jsp" onsubmit="return checkForm()" method="POST">
<input type="hidden" name="action" value="signup">

<p>Please fill in all the fields.</p>

<div>
	<table border="0" cellpadding="5">
		<tr>
			<td><b>First name </b></td>
			<td><input type="text" name="firstName"></td>
			<td id="first_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Last name </b></td>
			<td><input type="text" name="last_name"></td>
			<td id="last_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Student number<sup>1</sup> </b></td>
			<td><input type="text" name="student_number"></td>
			<td id="student_number_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Social security number<sup>1</sup> </b></td>
			<td><input type="text" name="social_security_number"></td>
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
			<td><b>E-mail: </b></td>
			<td><input type="text" name="email"></td>
			<td id="email_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2"><hr></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b>User name </b></td>
			<td><input type="text" name="user_name"></td>
			<td id="user_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Password </b></td>
			<td><input type="password" name="password"></td>
			<td id="password_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Password again </b></td>
			<td><input type="password" name="repeat_password"></td>
			<td id="repeat_password_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" name="sign_up_button" value="Sign up"></td>
			<td>&nbsp;</td>
		</tr>
	</table>
</div>

</form>

</body>
</html>