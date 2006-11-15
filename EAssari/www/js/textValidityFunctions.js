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
