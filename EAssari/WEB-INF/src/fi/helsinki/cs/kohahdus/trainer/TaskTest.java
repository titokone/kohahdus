package fi.helsinki.cs.kohahdus.trainer;

import junit.framework.TestCase;

public class TaskTest extends TestCase {
	private Task progtask;
	private Task filltask;
	private String expected;
	private String result;
	
	protected void setUp() throws Exception {
		super.setUp();
		progtask=new Task("eN", "tehtävä1", "Teme", "Create program that sets register R1 to 5",
				"1,4", "2,8",
				"R1=5", "perustehtävät", "programming",
				"", "", true,
				false, 3);
		filltask=new Task("FI", "tehtävä2", "Hartsa", "Luo funktio",
				"4,3,1", "2,7,4",
				"", "täydennystehtävät", "fill-in",
				"R3=R2", "ADD r1, =r2", false,
				true, 4);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		progtask=null;
		filltask=null;
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.Task()'
	 */
	public void testTask() {
		progtask=new Task("enkku", "tehtävä1", "Teme", "Create program that sets register R1 to 5",
				"1,4", "2,8",
				"R1=5", "perustehtävät", "programming",
				"", "", true,
				false, 3);
		filltask=new Task("FI", "tehtävä2", "Hartsa", "Luo funktio",
				"4,3,1", "2,7,4",
				"", "täydennystehtävät", "fillaus",
				"R3=R2", "ADD r1, =r2", false,
				true, 4);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setLanguage(String)'
	 */
	public void testSetLanguage() {
		progtask.setLanguage("FI");
		result=progtask.getLanguage();
		expected="FI";
		assertEquals(expected, result);
		
		progtask.setLanguage("En");
		result=progtask.getLanguage();
		expected="EN";
		assertEquals(expected, result);
		
		progtask.setLanguage("Enkku");
		result=progtask.getLanguage();
		expected="EN";
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setName(String)'
	 */
	public void testSetName() {
		progtask.setName("Tehtävä kutonen");
		result=progtask.getName();
		expected="Tehtävä kutonen";
		assertEquals(expected, result);
		
		progtask.setName("");
		result=progtask.getName();
		expected="";
		assertEquals(expected, result);
		
		progtask.setName(null);
		result=progtask.getName();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setAuthor(String)'
	 */
	public void testSetAuthor() {
		progtask.setAuthor("Hartsa Hartikainen");
		result=progtask.getAuthor();
		expected="Hartsa Hartikainen";
		assertEquals(expected, result);
		
		progtask.setAuthor("");
		result=progtask.getAuthor();
		expected="";
		assertEquals(expected, result);
		
		progtask.setAuthor(null);
		result=progtask.getAuthor();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setDescription(String)'
	 */
	public void testSetDescription() {
		progtask.setDescription("Create another program that does xxx");
		result=progtask.getDescription();
		expected="Create another program that does xxx";
		assertEquals(expected, result);
		
		progtask.setDescription("");
		result=progtask.getDescription();
		expected="";
		assertEquals(expected, result);
		
		progtask.setDescription(null);
		result=progtask.getDescription();
		expected=null;
		assertEquals(expected, result);
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setPublicInput(String)'
	 */
	public void testSetPublicInput() {
		progtask.setPublicInput("0,4,3");
		result=progtask.getPublicInput();
		expected="0,4,3";
		assertEquals(expected, result);
		
		progtask.setPublicInput("dfhfdrt4");
		result=progtask.getPublicInput();
		expected="dfhfdrt4";
		assertEquals(expected, result);
		
		progtask.setPublicInput("");
		result=progtask.getPublicInput();
		expected="";
		assertEquals(expected, result);
		
		progtask.setPublicInput(null);
		result=progtask.getPublicInput();
		expected=null;
		assertEquals(expected, result);
	}
	
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setSecretInput(String)'
	 */
	public void testSetSecretInput() {
		progtask.setSecretInput("0,4,3");
		result=progtask.getSecretInput();
		expected="0,4,3";
		assertEquals(expected, result);
		
		progtask.setSecretInput("dfhfdrt4");
		result=progtask.getSecretInput();
		expected="dfhfdrt4";
		assertEquals(expected, result);
		
		progtask.setSecretInput("");
		result=progtask.getSecretInput();
		expected="";
		assertEquals(expected, result);
		
		progtask.setSecretInput(null);
		result=progtask.getSecretInput();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setModelAnswer(String)'
	 */
	public void testSetModelAnswer() {
		progtask.setModelAnswer("R1=5\nR2=2");
		result=progtask.getModelAnswer();
		expected="R1=5\nR2=2";
		assertEquals(expected, result);
		
		progtask.setModelAnswer("");
		result=progtask.getModelAnswer();
		expected="";
		assertEquals(expected, result);
		
		progtask.setModelAnswer(null);
		result=progtask.getModelAnswer();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setCategory(String)'
	 */
	public void testSetCategory() {
		progtask.setModelAnswer("Helpot tehtävät");
		result=progtask.getModelAnswer();
		expected="Helpot tehtävät";
		assertEquals(expected, result);
		
		progtask.setModelAnswer("");
		result=progtask.getModelAnswer();
		expected="";
		assertEquals(expected, result);
		
		progtask.setModelAnswer(null);
		result=progtask.getModelAnswer();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setTitoTaskType(String)'
	 */
	public void testSetTitoTaskType() {
		progtask.setTitoTaskType("fiLL-in");
		result=progtask.getTitoTaskType();
		expected="fill-in";
		assertEquals(expected, result);
		
		progtask.setTitoTaskType("proGramMing");
		result=progtask.getTitoTaskType();
		expected="programming";
		assertEquals(expected, result);
		
		progtask.setTitoTaskType("Ohjelmointi");
		result=progtask.getTitoTaskType();
		expected="programming";
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.SetFillInPreCode(String)'
	 */
	public void testSetFillInPreCode() {
		filltask.setFillInPreCode("Yläkoodi\nkamaa");
		result=filltask.getFillInPreCode();
		expected="Yläkoodi\nkamaa";
		assertEquals(expected, result);
		
		filltask.setFillInPreCode("");
		result=filltask.getFillInPreCode();
		expected="";
		assertEquals(expected, result);
		
		filltask.setFillInPreCode(null);
		result=filltask.getFillInPreCode();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.SetFillInPostCode(String)'
	 */
	public void testSetFillInPostCode() {
		filltask.setFillInPostCode("Alakoodi\nka maa");
		result=filltask.getFillInPostCode();
		expected="Alakoodi\nka maa";
		assertEquals(expected, result);
		
		filltask.setFillInPostCode("");
		result=filltask.getFillInPostCode();
		expected="";
		assertEquals(expected, result);
		
		filltask.setFillInPostCode(null);
		result=filltask.getFillInPostCode();
		expected=null;
		assertEquals(expected, result);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setFillInTask(boolean)'
	 */
	public void testSetFillInTask() {
		progtask.setFillInTask(true);
		assert(progtask.isFillInTask());
		
		progtask.setFillInTask(false);
		assert(progtask.isProgrammingTask());
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setNoOfTries(int)'
	 */
	public void testSetNoOfTries() {
		progtask.setNoOfTries(7);
		int res=progtask.getNoOfTries();
		int exp=7;
		assertEquals(exp,res);
		
		progtask.setNoOfTries(0);
		res=progtask.getNoOfTries();
		exp=0;
		assertEquals(exp,res);
		
		progtask.setNoOfTries(-5);
		res=progtask.getNoOfTries();
		exp=0;
		assertEquals(exp,res);
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setValidateByModel(boolean)'
	 */
	public void testSetValidateByModel() {
		
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setModificationDate()'
	 */
	public void testSetModificationDate() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.setHasSucceeded(boolean)'
	 */
	public void testSetHasSucceeded() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.Task.getDateAsString()'
	 */
	public void testGetDateAsString() {

	}

}
