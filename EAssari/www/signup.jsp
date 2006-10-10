<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>

<%/*
	Signup-sivun alku. Vielä pahasti kesken.
	
	Tällä hetkellä osataan kerätä käyttäjän tiedot, jotka näytetään submitin jälkeen.
	
	JSP-jutut voitaisiin toki tehdä kauniimminkin
  */
%>  	

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
	<%
		//TODO: Integrate with DBHandler
		User user = null; //DBHandler.getInstance().getUser(request.getParameter("user_name"));
		
		//For testing purposes inserting "fail" as first name will always fail
		boolean fail = request.getParameter("first_name").equals("fail");
				
		//Username not in use - create new user	
		if (user == null && !fail){
	
			//TODO: this might be a nice spot to use beans
			user = new User(request.getParameter("user_name"));
						
			user.setFirstName(request.getParameter("first_name"));
			/*
			user.setLastName(param.last_name);
			user.setStudentNumber(param.student_number);
			user.setSocialSecurityNumber(param.social_security_number);
			user.setEmail(param.email);			
			user.setPassword(param.password);
			*/
			if (!user.isValid()){
				//forwardointi erroriin
			}			
			
			//TODO: DBHandler might throw exceptions... 
			DBHandler.getInstance().createUser(user);
			
			//TODO: User created - forward to tasklisting
			forwarding
			
			//print user info for debug purposes...
			//HUOM: ei toimi
	%>
			<p>Creating a new user with following info...
	
			<table border="0" cellpadding="5">
				<tr>
					<td>User name</td>
					<td><c:out value="${param.user_name}"></td>
				</tr>
				<tr>
					<td>First name</td>
					<td><c:out value="${param.first_name}"></td>
				</tr>			
				<tr>
					<td>Last name</td>
					<td><c:out value="${param.last_name}"></td>
				</tr>				
				<tr>
					<td>Social security number</td>
					<td><c:out value="${param.social_security_number}"></td>
				</tr>
				<tr>
					<td>Student number</td>
					<td><c:out value="${param.student_number}"></td>
				</tr>
				<tr>
					<td>Email</td>
					<td><c:out value="${param.email}"></td>
				</tr>
				<tr>
					<td>Password</td>
					<td><c:out value="${param.password}"></td>
				</tr>
			</table>	
				
	<%	//Failed because of duplicate username...
	    } else { %>
			<p>Ask the user to insert new username....
	<%  } %>
</c:if>


<form name="sign_up_form" action="signup.jsp" onsubmit="return checkForm()" method="POST">
<input type="hidden" name="action" value="signup">

<p>Please fill in all the fields.</p>

<div>
	<table border="0" cellpadding="5">
		<tr>
			<td><b>First name </b></td>
			<td><input type="text" name="first_name"></td>
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