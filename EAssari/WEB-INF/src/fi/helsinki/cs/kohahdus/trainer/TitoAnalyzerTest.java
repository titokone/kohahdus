package fi.helsinki.cs.kohahdus.trainer;

import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_FORBIDDEN_INSTRUCTIONS;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_PUBLIC_OUTPUT;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_PUBLIC_REGISTER_PREFIX;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_PUBLIC_SYMBOL_PREFIX;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_REQUIRED_INSTRUCTIONS;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_SECRET_OUTPUT;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_SECRET_SYMBOL_PREFIX;
import junit.framework.TestCase;
import java.util.*;

import fi.helsinki.cs.kohahdus.criteria.*;


/**Test class for TitoAnalyzer */
//Tuloksia:
//- ForbiddenInstructions kriteerin tarkistus ei toimi oikein.
//- ScreenOutputCriterion ei toimi oikein, titostaten getScreenOutput()
//- palauttaa 2:lla tulostuksella muotoa
//4
//, -8
//olevan Stringin, oikein?
//
public class TitoAnalyzerTest extends TestCase {
	Task task1, task2;
	LinkedList<Criterion> criteria1, criteria2;
	String programcode1, programcode2;
	String input1, input2;
	
	protected void setUp() throws Exception {
		super.setUp();
			
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
/*
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.TitoAnalyzer.TitoAnalyzer()'
	 */
	public void testTitoAnalyzer() {
	}
	

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.TitoAnalyzer.Analyze(Task, List<Criterion>, String, String)'
	 */
	//Tests programming task with only criteria
	public void testAnalyze() {
		//TASK1, answer checked by only criteria, programming task
		task1=new Task();
		task1.setName("Simple Math");
		task1.setCategory("Easy tasks");
		task1.setLanguage("EN");
		task1.setFillInTask(false);
		task1.setFailFeedBack("Miss.");
		task1.setPassFeedBack("Good game.");
		task1.setMaximumNumberOfInstructions(1000);
		
		//Create criteria
		criteria1=new LinkedList<Criterion>();
		//registers
		RegisterCriterion r1=new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 12, false, 1);
		r1.setAcceptanceTestValue("4");
		r1.setAcceptanceFeedback("Rekisteri 1 on oikein.");
		r1.setFailureFeedback("R1 vituiks.");
		criteria1.add(r1);
		RegisterCriterion r0=new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 123, false, 0);
		r0.setAcceptanceTestValue("3");
		r0.setAcceptanceFeedback("Rekisteri 0 on oikein.");
		r0.setFailureFeedback("R0 vituiks.");
		criteria1.add(r0);
		//symbols
		SymbolCriterion s1=new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 36, false);
		s1.setAcceptanceFeedback("Symboli X on oikein.");
		s1.setFailureFeedback("Symboli X vituillaan.");
		s1.setSymbolName("x");
		s1.setAcceptanceTestValue("3");
		SymbolCriterion s2=new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 78, false);
		s2.setSymbolName("y");
		s2.setAcceptanceTestValue("2");
		s2.setAcceptanceFeedback("Symboli Y on oikein.");
		s2.setFailureFeedback("Symboli Y vituillaan.");
		criteria1.add(s1);
		criteria1.add(s2);
		//instructions
		InstructionCriterion i1=new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false);
		InstructionCriterion i2=new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		i1.setAcceptanceTestValue("STORE, ADD, SUB, DIV");
		i1.setAcceptanceFeedback("Vaaditut käskyt täytetty.");
		i1.setFailureFeedback("Vaaditut käskyt vituillaan.");
		i2.setAcceptanceTestValue("");
		i2.setAcceptanceFeedback("Kielletyt käskyt täytetty.");
		i2.setFailureFeedback("Kielletyt käskyt vituillaan.");
		criteria1.add(i1);
		criteria1.add(i2);
		
		//no input
		input1="";
		//students anwer
		programcode1="X DC 0\n" +
			"Y DC 0\n" +
			"LOAD R1, =2\n" +
			"ADD R1, =2\n" +
			"LOAD R0, =8\n" +
			"SUB R0, =5\n" +
			"STORE R0, X\n" +
			"LOAD R0, =10\n" +
			"DIV R0, =5\n" +
			"STORE R0, Y\n" +
			"LOAD R0, =3\n" +
			"SVC SP, =HALT";
		
		
		//Solve
		TitoAnalyzer analys1=new TitoAnalyzer();
		TitoFeedback feed=analys1.Analyze(task1, criteria1, programcode1, input1);
		System.out.println(feed.isSuccessful());
		System.out.println(feed.getOverallFeedback());
		System.out.println(feed.getCompileError());
		System.out.println(feed.getRunError());
		
		List<TitoCriterionFeedback>  criteriafeed=feed.getCriteriaFeedback();
		for (TitoCriterionFeedback c : criteriafeed) {
			System.out.print(c.getName());
			System.out.print("    "+c.isPassedAcceptanceTest());
			System.out.println("    "+c.getFeedback());
		}
		System.out.println("\n\n\n----------------------------------");
		assertEquals(true, feed.isSuccessful());
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.TitoAnalyzer.Analyze(Task, List<Criterion>, String, String)'
	 */
	//Tests programming task with model answer
	public void testAnalyzeModel() {

		//TASK2, answer checked by comparing to model answer, programming task
		task2=new Task();
		task2.setName("Simple Math");
		task2.setCategory("Easy tasks");
		task2.setLanguage("FI");
		task2.setFillInTask(false);
		task2.setFailFeedBack("Tehtäväsi oli väärin.");
		task2.setPassFeedBack("Tehtävä on täydellisesti oikein");
		task2.setMaximumNumberOfInstructions(1000);
		task2.setValidateByModel(true);
		task2.setPublicInput("");
		String teachercode="X DC 0\n" +
		"Y DC 0\n" +
		"LOAD R1, =4\n" +
		"LOAD R0, =3\n" +
		"STORE R0, X\n" +
		"LOAD R0, =2\n" +
		"STORE R0, Y\n" +
		"LOAD R0, =3\n" +
		"SVC SP, =HALT";
		task2.setModelAnswer(teachercode);
		
		//Create criteria
		criteria2=new LinkedList<Criterion>();
		//registers
		RegisterCriterion r2=new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 12, false, 1);
		r2.setAcceptanceFeedback("Rekisteri 1 on oikein.");
		r2.setFailureFeedback("R1 vituiks.");
		r2.setCompareToModel("true");
		criteria2.add(r2);
		RegisterCriterion r3=new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 123, false, 0);
		r3.setAcceptanceFeedback("Rekisteri 0 on oikein.");
		r3.setFailureFeedback("R0 vituiks.");
		r3.setCompareToModel("true");
		criteria2.add(r3);
		//symbols
		SymbolCriterion s3=new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 36, false);
		s3.setAcceptanceFeedback("Symboli X on oikein.");
		s3.setFailureFeedback("Symboli X vituillaan.");
		s3.setSymbolName("x");
		s3.setCompareToModel("true");
		SymbolCriterion s4=new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 78, false);
		s4.setSymbolName("y");
		s4.setAcceptanceFeedback("Symboli Y on oikein.");
		s4.setFailureFeedback("Symboli Y vituillaan.");
		s4.setCompareToModel("true");
		criteria2.add(s3);
		criteria2.add(s4);
		//instructions
		InstructionCriterion i3=new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false);
		InstructionCriterion i4=new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		i3.setAcceptanceTestValue("STORE, ADD, SUB, DIV");
		i3.setAcceptanceFeedback("Vaaditut käskyt täytetty.");
		i3.setFailureFeedback("Vaaditut käskyt vituillaan.");
		i4.setAcceptanceTestValue("");
		i4.setAcceptanceFeedback("Kielletyt käskyt täytetty.");
		i4.setFailureFeedback("Kielletyt käskyt vituillaan.");
		criteria2.add(i3);
		criteria2.add(i4);
		
		//no input
		input2="";
		//students anwer
		programcode2="X DC 0\n" +
			"Y DC 0\n" +
			"LOAD R1, =2\n" +
			"ADD R1, =2\n" +
			"LOAD R0, =8\n" +
			"SUB R0, =5\n" +
			"STORE R0, X\n" +
			"LOAD R0, =10\n" +
			"DIV R0, =5\n" +
			"STORE R0, Y\n" +
			"LOAD R0, =3\n" +
			"SVC SP, =HALT";

				
		
		TitoAnalyzer analys2=new TitoAnalyzer();
		TitoFeedback feed=analys2.Analyze(task2, criteria2, programcode2, input2);
		System.out.println(feed.isSuccessful());
		System.out.println(feed.getOverallFeedback());
		System.out.println(feed.getCompileError());
		System.out.println(feed.getRunError());
		
		List<TitoCriterionFeedback>  criteriafeed=feed.getCriteriaFeedback();
		for (TitoCriterionFeedback c : criteriafeed) {
			System.out.print(c.getName());
			System.out.print("    "+c.isPassedAcceptanceTest());
			System.out.println("    "+c.getFeedback());
		}
		System.out.println("\n\n\n----------------------------------");
		assertEquals(true, feed.isSuccessful());
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.TitoAnalyzer.Analyze(Task, List<Criterion>, String, String)'
	 */
	//Test inputs and outputs
	public void testAnalyzeInputsOutputs() {
		//TASK1, answer checked by only criteria, programming task
		task1=new Task();
		task1.setName("Inputs");
		task1.setCategory("Easy tasks");
		task1.setLanguage("EN");
		task1.setFillInTask(false);
		task1.setFailFeedBack("Miss.");
		task1.setPassFeedBack("Good game.");
		task1.setMaximumNumberOfInstructions(1000);
		//inputs, both public and sssecret
		task1.setPublicInput("1,-4");
		task1.setSecretInput("3,2");
		
		/* 2 inputtia, r1=4, r2=2, input1*r1, input2*r2.
		 * r1=input1*r1, r2=input2*r2
		 * tavan syötteellä halutaan, että r1=4, salainen r1=12
		 *                                 r2=-8,         r2=4
		 * kriteereinä siis että tulostuu tavan syötteellä 4,-8 ja sal 12,4
		*/
		//Create criteria
		criteria1=new LinkedList<Criterion>();
		//outputs
		ScreenOutputCriterion out1=new ScreenOutputCriterion(ID_PUBLIC_OUTPUT, false);
		ScreenOutputCriterion out2=new ScreenOutputCriterion(ID_SECRET_OUTPUT, true);
		out1.setAcceptanceTestValue("4,-8");
		out2.setAcceptanceTestValue("12,4");
		out1.setAcceptanceFeedback("julkisella syötteellä läpi");
		out2.setAcceptanceFeedback("salaisella syötteellä läpi");
		out1.setFailureFeedback("julkisella syötteellä vituiks");
		out2.setFailureFeedback("salaisella syötteellä vituiks");
		criteria1.add(out1);
		criteria1.add(out2);
		input1="1,-4"; //no sense changing
		programcode1="" +
		"IN R1,=KBD\n" +
		"MUL R1, =4\n" +
		"IN R2, =KBD\n" +
		"MUL R2, =2\n" +
		"OUT R1, =CRT\n" +
		"OUT R2, =CRT\n" +
		"SVC SP, =HALT";
		
		
		//Solve
		TitoAnalyzer analys1=new TitoAnalyzer();
		TitoFeedback feed=analys1.Analyze(task1, criteria1, programcode1, input1);
		System.out.println(feed.isSuccessful());
		System.out.println(feed.getOverallFeedback());
		System.out.println(feed.getCompileError());
		System.out.println(feed.getRunError());
		
		List<TitoCriterionFeedback>  criteriafeed=feed.getCriteriaFeedback();
		for (TitoCriterionFeedback c : criteriafeed) {
			System.out.print(c.getName());
			System.out.print("    "+c.isPassedAcceptanceTest());
			System.out.println("    "+c.getFeedback());
		}
		System.out.println("\n\n\n----------------------------------");
		assertEquals(true, feed.isSuccessful());
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.TitoAnalyzer.Analyze(Task, List<Criterion>, String, String)'
	 */
	//Test fill-in task in comparison to model answer
	public void testAnalyzeFillIn() {
//		TASK2, answer checked by comparing to model answer, fillin task
		task2=new Task();
		task2.setName("Some fillin task");
		task2.setCategory("Easy tasks");
		task2.setLanguage("EN");
		task2.setFillInTask(true);
		task2.setFailFeedBack("Task was wrong.");
		task2.setPassFeedBack("Task was correct.");
		task2.setMaximumNumberOfInstructions(1000);
		task2.setValidateByModel(true);
		task2.setPublicInput("");
		String fillinpre="" +
		"X DC 5\n" +
		"Y DC 2\n";
		String teachercode="" +
		"LOAD R1, X\n" +
		"ADD R1, Y\n";
		String fillinpost="OUT R1, =CRT\n" +
		"SVC SP, =HALT";
		
		task2.setFillInPreCode(fillinpre);
		task2.setModelAnswer(teachercode);
		task2.setFillInPostCode(fillinpost);
		
		//Create criteria
		criteria2=new LinkedList<Criterion>();
		//registers
		RegisterCriterion r1=new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 12, false, 1);
		r1.setAcceptanceFeedback("Register 1 correct.");
		r1.setFailureFeedback("Register 1 wrong.");
		r1.setCompareToModel("true");
		criteria2.add(r1);
		
		//instructions
		InstructionCriterion i1=new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		i1.setAcceptanceTestValue("MUL"); //ei käytössä, väittää väärin
		i1.setAcceptanceFeedback("No forbidden instructions. Good.");
		i1.setFailureFeedback("U used forbidden instructions.");
		criteria2.add(i1);
		
		//no input
		input2="";
		//students anwer
		programcode2="" +
		"LOAD R1, =7\n";
				
		
		TitoAnalyzer analys2=new TitoAnalyzer();
		TitoFeedback feed=analys2.Analyze(task2, criteria2, programcode2, input2);
		System.out.println(feed.isSuccessful());
		System.out.println(feed.getOverallFeedback());
		System.out.println(feed.getCompileError());
		System.out.println(feed.getRunError());
		
		List<TitoCriterionFeedback>  criteriafeed=feed.getCriteriaFeedback();
		for (TitoCriterionFeedback c : criteriafeed) {
			System.out.print(c.getName());
			System.out.print("    "+c.isPassedAcceptanceTest());
			System.out.println("    "+c.getFeedback());
		}
		System.out.println("\n\n\n----------------------------------");
		assertEquals(true, feed.isSuccessful());
	}
	

}
