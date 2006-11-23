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
		//taski1, answer checked by only criteria, programming task
		task1=new Task();
		task1.setName("Simple Math");
		task1.setCategory("Easy tasks");
		task1.setLanguage("EN");
		task1.setFillInTask(false);
		task1.setFailFeedBack("Miss.");
		task1.setPassFeedBack("Good game.");
		task1.setMaximumNumberOfInstructions(1000);
		
		
		criteria1=new LinkedList<Criterion>();
		
		
		//Create criteria
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
		System.out.println(i2.hasAcceptanceTest(false));
		criteria1.add(i1);
		criteria1.add(i2);
		
		/*
		//criterions for task1
		//register criterions
		Set<RegisterCriterion> registerCriteria1=new HashSet<RegisterCriterion>();
		registerCriteria1.add(new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 0, false, 1));
		for (RegisterCriterion c : registerCriteria1) {
			// Valid value, Model = true
			c.setAcceptanceTestValue("4");
		}
		//symbol criterions
		Set<SymbolCriterion> symbolCriteria1;
		symbolCriteria1 = new HashSet<SymbolCriterion>();
		symbolCriteria1.add(new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 0, false));
		symbolCriteria1.add(new SymbolCriterion(ID_SECRET_SYMBOL_PREFIX + 0, false));
		int i=1;
		for (SymbolCriterion c : symbolCriteria1) {
			if (i==1) {
				c.setSymbolName("X");
				c.setAcceptanceTestValue("3");
			}
			if (i==2) {
				c.setSymbolName("Y");
				c.setAcceptanceTestValue("5");
			}
			i++;
		}
		//instruction criterions
		Set<InstructionCriterion> instructionCriteria1;
		instructionCriteria1 = new HashSet<InstructionCriterion>();
		instructionCriteria1.add(new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false));
		instructionCriteria1.add(new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false));
		i=1;
		for (InstructionCriterion c : instructionCriteria1) {		
			if (i==1)
				c.setAcceptanceTestValue("STORE, ADD, SUB, DIV, DC");
			if (i==2)
				c.setAcceptanceTestValue("");
			i++;
		}
		//compile the criteria together to one table
		criteria1.addAll(registerCriteria1);
		criteria1.addAll(symbolCriteria1);
		criteria1.addAll(instructionCriteria1);
		*/
		
		
		//no input
		input1="";
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
			
		/*
		
		RegisterCriterion r1=new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 0, false, 1);
		r1.setAcceptanceTestValue("2");
		
		criteria1.add(r1);
		
		input1="";
		programcode1 = "LOAD R1, =2\n"
                     + "SVC SP, =HALT";
                     */
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
	public void testAnalyze() {
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
	}

}
