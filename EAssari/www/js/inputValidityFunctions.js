function containsHtmlCharacters(aString) {
  if(aString.indexOf('"') != -1) {
    return true;
  }

  if(aString.indexOf('<') != -1) {
    return true;
  }

  if(aString.indexOf('>') != -1) {
    return true;
  }

  if(aString.indexOf('&') != -1) {
    return true;
  }

  return false;
}

function containsSpaces(aString) {
  if(aString.indexOf(' ') != -1) {
    return true;
  }

  return false;	
}

function isInteger(variable)
{
	var integerExp = /^((\+|-)\d)?\d*$/;
	
	return ((variable != '') && (integerExp.test(variable)));
}

/* Check that the parameter string is integers separated by commas. Returns true/false. */
function isValidTitokoneInputOutput(aString)
{
	var splitExp = / *\, */;
		
	var ints = aString.split(splitExp);
		
	for(var i = 0; i < ints.length; i++) {
		if(!isInteger(ints[i])) {
			return false;
		}
	}
	
	return true;
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
	
	// student number is a 9-digit number that always starts with 01
	if((numberString.charAt(0) != '0'.charAt(0)) || (numberString.charAt(1) != '1'.charAt(0)) || (numberString.length != 9)) {
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

/* Remove leading and trailing whitespace from a field. */
function trimWhitespace(inputField)
{
	var aString = inputField.value;

	while (aString.substring(0,1) == ' ')
	{
		aString = aString.substring(1, aString.length);
	}
	
	while (aString.substring(aString.length-1, aString.length) == ' ')
	{
		aString = aString.substring(0, aString.length-1);
	}
	
	inputField.value = aString;
}
