package fi.helsinki.cs.kohahdus.trainer;

import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_FORBIDDEN_INSTRUCTIONS;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_PUBLIC_REGISTER_PREFIX;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_PUBLIC_SYMBOL_PREFIX;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_REQUIRED_INSTRUCTIONS;
import static fi.helsinki.cs.kohahdus.criteria.Criterion.ID_SECRET_SYMBOL_PREFIX;
import junit.framework.TestCase;
import java.util.*;

import fi.helsinki.cs.kohahdus.criteria.*;


/**Test class for TitoAnalyzer */
//VAIHEISSA
public class TitoAnalyzerTest extends TestCase {
	Task task1, task2;
	LinkedList<Criterion> criteria1, criteria2;
	String programcode1, programcode2;
	String input1, input2;
	
	protected void setUp() throws Exception {
		super.setUp();
		
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
		i1.setAcceptanceFeedback("Vaaditut k‰skyt t‰ytetty.");
		i1.setFailureFeedback("Vaaditut k‰skyt vituillaan.");
		i2.setAcceptanceTestValue("");
		i2.setAcceptanceFeedback("Kielletyt k‰skyt t‰ytetty.");
		i2.setFailureFeedback("Kielletyt k‰skyt vituillaan.");
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
		
		
		
		
		//TASK2, answer checked by comparing to model answer, programming task
		task2=new Task();
		task2.setName("Simple Math");
		task2.setCategory("Easy tasks");
		task2.setLanguage("FI");
		task2.setFillInTask(false);
		task2.setFailFeedBack("Teht‰v‰si oli v‰‰rin.");
		task2.setPassFeedBack("Teht‰v‰ on t‰ydellisesti oikein");
		task2.setMaximumNumberOfInstructions(1000);
		task2.setValidateByModel(true);
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
		criteria2.add(r2);
		RegisterCriterion r3=new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 123, false, 0);
		r3.setAcceptanceFeedback("Rekisteri 0 on oikein.");
		r3.setFailureFeedback("R0 vituiks.");
		criteria2.add(r3);
		//symbols
		SymbolCriterion s3=new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 36, false);
		s3.setAcceptanceFeedback("Symboli X on oikein.");
		s3.setFailureFeedback("Symboli X vituillaan.");
		s3.setSymbolName("x");
		SymbolCriterion s4=new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 78, false);
		s4.setSymbolName("y");
		s4.setAcceptanceFeedback("Symboli Y on oikein.");
		s4.setFailureFeedback("Symboli Y vituillaan.");
		criteria2.add(s3);
		criteria2.add(s4);
		//instructions
		InstructionCriterion i3=new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false);
		InstructionCriterion i4=new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		i3.setAcceptanceTestValue("STORE, ADD, SUB, DIV");
		i3.setAcceptanceFeedback("Vaaditut k‰skyt t‰ytetty.");
		i3.setFailureFeedback("Vaaditut k‰skyt vituillaan.");
		i4.setAcceptanceTestValue("");
		i4.setAcceptanceFeedback("Kielletyt k‰skyt t‰ytetty.");
		i4.setFailureFeedback("Kielletyt k‰skyt vituillaan.");
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
	public void testAnalyze1() {
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
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.trainer.TitoAnalyzer.Analyze(Task, List<Criterion>, String, String)'
	 */
	//Tests programming task with model answer
	public void testAnalyze2() {
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
	}
	

}
