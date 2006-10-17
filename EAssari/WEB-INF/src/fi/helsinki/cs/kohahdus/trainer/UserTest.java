package fi.helsinki.cs.kohahdus.trainer;

import junit.framework.TestCase;

public class UserTest extends TestCase {
	private User testuser;
	private String expected;
	private String result;

	protected void setUp() throws Exception {
		super.setUp();
		testuser=new User("234","Jackson","Tito","titou@sucks.com","student",
				"120390193", "20121960", "passwordi","EN");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		testuser=null;
	}

	
	
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.User(...)'
	 */
	public void testUser() {
		testuser=new User("234","Jackson","Tito","titou@sucks.com","student",
				"120390193", "20121960", "passwordi","English");
		
		testuser=new User("234","Jackson","Tito","titou@sucks.com","opiskelija",
				"120390193", "20121960", "passwordi","EN");
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setUserID(String)'
	 */
	public void testSetUserID() {
		testuser.setUserID("0901");
		result=testuser.getUserID();
		expected="0901";
		assertEquals(expected, result);
		
		testuser.setUserID("etd");
		result=testuser.getUserID();
		expected="etd";
		assertEquals(expected, result);
		
		testuser.setUserID(null);
		result=testuser.getUserID();
		expected=null;
		assertEquals(expected, result);
	}
	

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setLastName(String)'
	 */
	public void testSetLastName() {
		testuser.setLastName("Jacksoni");
		result=testuser.getLastName();
		expected="Jacksoni";
		assertEquals(expected, result);
		
		testuser.setLastName("");
		result=testuser.getLastName();
		expected="";
		assertEquals(expected, result);
		
		testuser.setLastName(null);
		result=testuser.getLastName();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setFirstName(String)'
	 */
	public void testSetFirstName() {
		testuser.setFirstName("Jack");
		result=testuser.getFirstName();
		expected="Jack";
		assertEquals(expected, result);
		
		testuser.setFirstName("");
		result=testuser.getFirstName();
		expected="";
		assertEquals(expected, result);
		
		testuser.setFirstName(null);
		result=testuser.getFirstName();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setEmail(String)'
	 */
	public void testSetEmail() {
		testuser.setEmail("Stuff@stuffmania.com");
		result=testuser.getEmail();
		expected="Stuff@stuffmania.com";
		assertEquals(expected, result);
		
		testuser.setEmail("");
		result=testuser.getEmail();
		expected="";
		assertEquals(expected, result);
		
		testuser.setEmail(null);
		result=testuser.getEmail();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setStatus(String)'
	 */
	public void testSetStatus() {
		testuser.setStatus("student");
		result=testuser.getStatus();
		expected="student";
		assertEquals(expected, result);
		testuser.setStatus("Student");
		result=testuser.getStatus();
		expected="student";
		assertEquals(expected, result);
		testuser.setStatus("TEACHER");
		result=testuser.getStatus();
		expected="teacher";
		assertEquals(expected, result);
		testuser.setStatus("aDm");
		result=testuser.getStatus();
		expected="adm";
		assertEquals(expected, result);
		
		testuser.setStatus("Opiskelija");
		result=testuser.getStatus();
		expected="adm";
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setStudentNumber(String)'
	 */
	public void testSetStudentNumber() {
		testuser.setStudentNumber("091ewre");
		result=testuser.getStudentNumber();
		expected="091ewre";
		assertEquals(expected, result);
		
		testuser.setStudentNumber("124934689");
		result=testuser.getStudentNumber();
		expected="124934689";
		assertEquals(expected, result);
		
		testuser.setStudentNumber("");
		result=testuser.getStudentNumber();
		expected="";
		assertEquals(expected, result);
		
		testuser.setStudentNumber(null);
		result=testuser.getStudentNumber();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setSocialSecurityNumber(String)'
	 */
	public void testSetSocialSecurityNumber() {
		testuser.setSocialSecurityNumber("091ewre");
		result=testuser.getSocialSecurityNumber();
		expected="091ewre";
		assertEquals(expected, result);
		
		testuser.setSocialSecurityNumber("20121960-123G");
		result=testuser.getSocialSecurityNumber();
		expected="20121960-123G";
		assertEquals(expected, result);
		
		testuser.setSocialSecurityNumber("");
		result=testuser.getSocialSecurityNumber();
		expected="";
		assertEquals(expected, result);
		
		testuser.setSocialSecurityNumber(null);
		result=testuser.getSocialSecurityNumber();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setPassword(String)'
	 */
	public void testSetPassword() {
		testuser.setPassword("crappis");
		result=testuser.getPassword();
		expected="crappis";
		assertEquals(expected, result);
		
		testuser.setPassword("");
		result=testuser.getPassword();
		expected="";
		assertEquals(expected, result);
		
		testuser.setPassword(null);
		result=testuser.getPassword();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.setLanguage(String)'
	 */
	public void testSetLanguage() {
		testuser.setLanguage("FI");
		result=testuser.getLpref();
		expected="FI";
		assertEquals(expected, result);
		
		testuser.setLanguage("En");
		result=testuser.getLpref();
		expected="EN";
		assertEquals(expected, result);
		
		testuser.setLanguage("Enkku");
		result=testuser.getLpref();
		expected="EN";
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.isTeacher()'
	 */
	public void testIsTeacher() {
		testuser.setStatus("teacher");
		assert(testuser.isTeacher());
		testuser.setStatus("student");
		assert(!testuser.isTeacher());
		testuser.setStatus("adm");
		assert(!testuser.isTeacher());
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.isStudent()'
	 */
	public void testIsStudent() {
		testuser.setStatus("student");
		assert(testuser.isStudent());
		testuser.setStatus("adm");
		assert(!testuser.isStudent());
		testuser.setStatus("teacher");
		assert(!testuser.isStudent());
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.isAdmin()'
	 */
	public void testIsAdmin() {
		testuser.setStatus("student");
		assert(!testuser.isAdmin());
		testuser.setStatus("adm");
		assert(testuser.isAdmin());
		testuser.setStatus("teacher");
		assert(!testuser.isAdmin());
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.isEmptyString()'
	 */ //To run this first one must change the method from private to public
	/*private void testIsEmptyString() {
		String test="";
		assert(testuser.isEmptyString(test));
		String test=null;
		assert(testuser.isEmptyString(test));
		String test="stuff";
		assert(!testuser.isEmptyString(test));
	}*/
	

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.User.isValid()'
	 */
	public void testIsValid() {
		assert(testuser.isValid());
		
		testuser.setUserID("");
		assert(!testuser.isValid());
		
		testuser.setUserID(null);
		assert(!testuser.isValid());
	}
	
	
}
