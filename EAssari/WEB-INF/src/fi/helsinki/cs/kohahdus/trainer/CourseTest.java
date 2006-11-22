package fi.helsinki.cs.kohahdus.trainer;

import junit.framework.TestCase;

//3 runs, 0 errors, 0 failures
public class CourseTest extends TestCase {
	private String expected;
	private String result;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Course.Course'
	 */
	public void testCourse() {
		Course kurs=new Course("crap", "6");
		assertEquals("crap", kurs.getName());
		assertEquals("6", kurs.getID());
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Course.setName(String)'
	 */
	public void testSetName() {
		Course kurs=new Course();
		expected="Taidou";
		kurs.setName("Taidou");
		result=kurs.getName();
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Course.toString()'
	 */
	public void testToString() {
		Course kurs=new Course("Titoh", "0001");
		expected="Titoh(0001)";
		result=kurs.toString();
		assertEquals(expected, result);
	}

}
