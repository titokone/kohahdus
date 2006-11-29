<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.trainer.*" %>
<%@ page import="fi.helsinki.cs.kohahdus.languages.*" %>

<%-- check that user is logged in --%>
<c:if test="${empty user}">
	Not logged in - redirecting to login
	<c:redirect url="login.jsp"/>	
</c:if>

<%
	String lang = (String)session.getAttribute("language");
	ResourceBundle rb = LanguageManager.getTextResource(lang , "modify_user");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<title>TitoTrainer - <%=rb.getString("editTitle")%></title>
<link rel="stylesheet" type="text/css" title="TitoTrainer stylesheet" href="styles/titotrainer.css">
<script language="javascript" type="text/javascript" src="js/inputValidityFunctions.js"></script>
<script language="javascript" type="text/javascript">

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
			elem.innerHTML = 'Please fill in your first name.';
			returnvalue = false;
		}

		if(form.last_name.value == '') {
			var elem = document.getElementById("last_name_error_msg_space");
			elem.innerHTML = 'Please fill in your last name.';
			returnvalue = false;
		}

		if(form.student_number.value == '' && form.social_security_number.value == '') {
			var elem = document.getElementById("student_number_error_msg_space");
			elem.innerHTML = 'Please fill in either your student number or social security number.';
			returnvalue = false;
		}

		if(form.email.value == '') {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = 'Please fill in your e-mail address.';
			returnvalue = false;
		}
			
		// first name contains illegal html-characters
		if(containsHtmlCharacters(form.first_name.value)) {
			var elem = document.getElementById("first_name_error_msg_space");
			elem.innerHTML = 'Field may not contain characters ", <, >, &.';
			returnvalue = false;
		}
		
		// first name not of valid length
		if(form.first_name.value.length > 40) {
			var elem = document.getElementById("first_name_error_msg_space");
			elem.innerHTML = 'First name must be 1-40 characters long.';
			returnvalue = false;
		}
		
		// last name contains illegal html-characters
		if(containsHtmlCharacters(form.last_name.value)) {
			var elem = document.getElementById("last_name_error_msg_space");
			elem.innerHTML = 'Field may not contain characters ", <, >, &.';
			returnvalue = false;
		}
		
		// last name not of valid length
		if(form.last_name.value.length > 40) {
			var elem = document.getElementById("last_name_error_msg_space");
			elem.innerHTML = 'Last name must be 1-40 characters long.';
			returnvalue = false;
		}

		// student number of wrong format
		if((form.student_number.value != '') && (!studentNumberValid(form.student_number.value))) {
			var elem = document.getElementById("student_number_error_msg_space");
			elem.innerHTML = 'Your student number is of wrong format.';
			returnvalue = false;
		}

		// social security number of wrong format
		if((form.social_security_number.value != '') && (!socialSecurityNumberValid(form.social_security_number.value))) {
			var elem = document.getElementById("ssn_error_msg_space");
			elem.innerHTML = 'Social security number must be of Finnish format.';
			returnvalue = false;
		}
		
		// e-mail address of wrong format
		if((form.email.value != '') && (!emailExp.test(form.email.value))) {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = 'Your e-mail address isn\'t of a valid format. A valid format would be e.g. user@cs.helsinki.fi';
			returnvalue = false;
		}
		
		// e-mail address contains illegal html-characters
		if(containsHtmlCharacters(form.email.value)) {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = 'Field may not contain characters ", <, >, &.';
			returnvalue = false;
		}

		// e-mail address not of valid length
		if(form.email.value.length > 80) {
			var elem = document.getElementById("email_error_msg_space");
			elem.innerHTML = 'E-mail address may contain only up to 80 characters.';
			returnvalue = false;
		}
		
		// new password chosen, but not repeated
		if((form.new_password.value != '') && (form.repeat_new_password.value == '')) {
			var elem = document.getElementById("repeat_new_password_error_msg_space");
			elem.innerHTML = 'Please repeat your new password.';
			returnvalue = false;
		}

		// new password repeated, but not chosen
		if((form.repeat_new_password.value != '') && (form.new_password.value == '')) {
			var elem = document.getElementById("new_password_error_msg_space");
			elem.innerHTML = 'Please type your new password.';
			returnvalue = false;
		}

		// Only if "password" and "repeat password" don't match and neither is empty
		if ((form.new_password.value != '') && (form.repeat_new_password.value != '') && (form.new_password.value != form.repeat_new_password.value)) {
				var elem = document.getElementById("new_password_error_msg_space");
				elem.innerHTML = 'Different values in password fields. Please check your typing.';			
				returnvalue = false;
		}
		
		// new password not of valid length
		if((form.new_password.value != '') && ((form.new_password.value.length < 6) || (form.new_password.value.length > 12))) {
			var elem = document.getElementById("new_password_error_msg_space");
			elem.innerHTML = 'Password must be 6-12 characters long.';
			returnvalue = false;
		}
		
		return returnvalue;
	}	// end function checkForm()

</script>
</head>



<body>

<jsp:include page="menu.jsp"/>

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


<h2><%=rb.getString("editTitle")%></h2>



<br>

<form name="modify_user_form" action="modify_user.jsp" onsubmit="return checkForm()" method="POST">
<input type="hidden" name="action" value="modify">




<div>
        <table class="formTable">
                <tr>
                        <td><%=rb.getString("userName")%></td>
                        <td><c:out value="${user.userID}"/></td>
                </tr>
                <tr>
                        <td><%=rb.getString("firstName")%></td>
                        <td><input type="text" name="first_name" value="<c:out value="${user.firstName}"/>" onChange="trimWhitespace(this)">    </td>
                        <td id="first_name_error_msg_space" class="errorMsg">&nbsp;</td>                        
                </tr>
                <tr>
                        <td><%=rb.getString("lastName")%></td>
                        <td><input type="text" name="last_name" value="<c:out value="${user.lastName}"/>" onChange="trimWhitespace(this)"></td>
                        <td id="last_name_error_msg_space" class="errorMsg">&nbsp;</td>
                </tr>
                <tr>
                        <td><%=rb.getString("email")%></td>
                        <td><input type="text" name="email" value="<c:out value="${user.email}"/>" onChange="trimWhitespace(this)"></td>
                        <td id="email_error_msg_space" class="errorMsg">&nbsp;</td>
                </tr>
    
                <tr>
                        <td><%=rb.getString("studentNumber")%>*</td>
                        <td><input type="text" name="student_number" value="<c:out value="${user.studentNumber}"/>" onChange="trimWhitespace(this)"> </td>
                        <td id="student_number_error_msg_space" class="errorMsg">&nbsp;</td>
                </tr>
                <tr>
                        <td><%=rb.getString("ssn")%>*</td>
                        <td><input type="text" name="social_security_number" value="<c:out value="${user.socialSecurityNumber}"/>" onChange="trimWhitespace(this)"> </td>
                        <td id="ssn_error_msg_space" class="errorMsg">&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2" class="footNote">* <%=rb.getString("studentNumberOrSsnRequired")%></td>
                        <td>&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2"><hr></td>
                        <td>&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2" class="subTitle"><%=rb.getString("changePasswordTitle")%></td>
                </tr>
                <tr>
                        <td><%=rb.getString("newPassword")%></td>
                        <td><input type="password" name="new_password"></td>
                         <td id="new_password_error_msg_space" class="errorMsg">&nbsp;</td>
                </tr>
                <tr>
                        <td><%=rb.getString("newPasswordAgain")%></td>
                        <td><input type="password" name="repeat_new_password"></td>
                        <td id="repeat_new_password_error_msg_space" class="errorMsg">&nbsp;</td>
                </tr>
                <tr>
                        <td colspan="2" class="formButtonCell"><input type="submit" name="save_button" value="<%=rb.getString("saveButtonValue")%>"></td>
                </tr>
        </table>
</div>

</form>
</body>
</html>