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
		
		//criterions for task1
		//register criterions
		Set<RegisterCriterion> registerCriteria1=new HashSet<RegisterCriterion>();
		registerCriteria1.add(new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 1, false, 4));
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
		Set<InstructionCriterion> instructionCriteria;
		instructionCriteria = new HashSet<InstructionCriterion>();
		instructionCriteria.add(new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false));
		instructionCriteria.add(new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false));
		i=1;
		for (InstructionCriterion c : instructionCriteria) {		
			if (i==1)
				c.setAcceptanceTestValue("STORE, ADD, SUB, DIV, DC");
			if (i==2)
				c.setAcceptanceTestValue("");
			i++;
		}
		//no input
		input1="";
		programcode1="DC X\n" +
			"DC Y\n" +
			"LOAD R0, =2\n" +
			"ADD R0, =2\n" +
			"STORE R0, R1\n" +
			"LOAD R0, =8\n" +
			"SUB R0, =5\n" +
			"STORE R0, X\n" +
			"LOAD R0, =10\n" +
			"DIV R0, =5\n" +
			"STORE R0, Y";
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

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
		
		
		
	}

}
