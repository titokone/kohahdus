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
	
	<% //Create a new user and store it in pagecontext
	   	User newUser = new User(request.getParameter("user_name"));
	   	newUser.setStatus(User.STATUS_STUDENT);
	   	newUser.setLanguage("EN");
	   	pageContext.setAttribute("newUser", newUser);
		Log.write("Signup: creating a new user");
	%>
				
		<c:set target="${newUser}" property="firstName" value="${param.first_name}"/>
		<c:set target="${newUser}" property="lastName" value="${param.last_name}"/>
		<c:set target="${newUser}" property="socialSecurityNumber" value="${param.social_security_number}"/>
		<c:set target="${newUser}" property="studentNumber" value="${param.student_number}"/>
		<c:set target="${newUser}" property="email" value="${param.email}"/>
		<c:set target="${newUser}" property="password" value="${param.password}"/>
				
		<p>ID <c:out value="${newUser.userID}"/>
		<p>FN <c:out value="${newUser.firstName}"/>
		<p>LN <c:out value="${newUser.lastName}"/>
		<p>SSN <c:out value="${newUser.socialSecurityNumber}"/>
		<p>STN <c:out value="${newUser.studentNumber}"/>
		<p>EM <c:out value="${newUser.email}"/>
		<p>PW <c:out value="${newUser.password}"/>   
				
	<% 	//Check DB for existing users with same id
		//FIXME: is there a possibility that username would be null
	   	User oldUser = DBHandler.getInstance().getUser(request.getParameter("user_name"));
	   	if (oldUser != null) pageContext.setAttribute("oldUser", oldUser); 		   	   
	%>
	
	<c:choose>
		<c:when test="${not empty oldUser}">
			<p>User name invalid or already in use. Please pick another.
		</c:when>
		<c:otherwise>		
			<p>ADDING TO DATABASE...
					
			<%
				if (!newUser.isValid()) {
					//TODO: ohjaus erroriin
					out.print("Grave ERROR");
					Log.write("Signup: error in new users data");
				} else {
					//Add new user to db and set up session
					DBHandler.getInstance().createUser(newUser);
					out.print("USER CREATED -- FORWARDING TO LOGIN");
					pageContext.setAttribute("user", newUser, PageContext.SESSION_SCOPE);
					Log.write("Signup: new user created");
					//TODO: forwardointi
			%>
					<c:redirect url="student/studentTaskList.jsp"/>
			<%		
				}
			%>	
		</c:otherwise>
	</c:choose>			   
</c:if>

<form name="sign_up_form" action="signup.jsp" onsubmit="return checkForm()" method="POST">
<input type="hidden" name="action" value="signup">

<p>Please fill in all the fields.</p>

<p>DEBUG HUOM: kantaan tallennetaan vain jos sotu ja opnum ova täytetty

<div>
	<table border="0" cellpadding="5">
		<tr>
			<td><b>First name </b></td>
			<td><input type="text" name="first_name" value="<c:out value="${newUser.firstName}"/>"></td>
			<td id="first_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Last name </b></td>
			<td><input type="text" name="last_name" value="<c:out value="${newUser.lastName}"/>"></td>
			<td id="last_name_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Student number<sup>1</sup> </b></td>
			<td><input type="text" name="student_number" value="<c:out value="${newUser.studentNumber}"/>"></td>
			<td id="student_number_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td><b>Social security number<sup>1</sup> </b></td>
			<td><input type="text" name="social_security_number" value="<c:out value="${newUser.socialSecurityNumber}"/>"></td>
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
			<td><input type="text" name="email" value="<c:out value="${newUser.email}"/>"></td>
			<td id="email_error_msg_space">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2"><hr></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b>User name </b></td>
			<td><input type="text" name="user_name" value="<c:out value="${newUser.userID}"/>"></td>
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

<c:remove var="newUser" scope="page"/>
<c:remove var="oldUser" scope="page"/>

</body>
</html>