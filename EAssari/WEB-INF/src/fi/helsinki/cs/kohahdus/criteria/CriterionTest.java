package fi.helsinki.cs.kohahdus.criteria;

import static fi.helsinki.cs.kohahdus.criteria.Criterion.*; 
import junit.framework.TestCase;
import java.util.*;

/** JUnit test class that tests all criterion types */
public class CriterionTest extends TestCase {
	private Set<Criterion> allCriteria;
	private Set<RegisterCriterion> registerCriteria;
	private Set<SymbolCriterion> symbolCriteria;
	private Set<MeasuredCriterion> measuredCriteria;
	private Set<InstructionCriterion> instructionCriteria;
	private Set<ScreenOutputCriterion> outputCriteria;
	private Set<VariableCriterion> variableCriteria;

	
	public CriterionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		registerCriteria = new HashSet<RegisterCriterion>();
		registerCriteria.add(new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + 0, false, 0));
		registerCriteria.add(new RegisterCriterion(ID_SECRET_REGISTER_PREFIX + 0, true, 0));

		symbolCriteria = new HashSet<SymbolCriterion>();
		symbolCriteria.add(new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 0, false));
		symbolCriteria.add(new SymbolCriterion(ID_SECRET_SYMBOL_PREFIX + 0, true));

		outputCriteria = new HashSet<ScreenOutputCriterion>();
		outputCriteria.add(new ScreenOutputCriterion(ID_PUBLIC_OUTPUT, false));
		outputCriteria.add(new ScreenOutputCriterion(ID_SECRET_OUTPUT, true));

		instructionCriteria = new HashSet<InstructionCriterion>();
		instructionCriteria.add(new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false));
		instructionCriteria.add(new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false));

		measuredCriteria = new HashSet<MeasuredCriterion>();
		measuredCriteria.add(new CodeSizeCriterion(ID_CODE_SIZE, false));
		measuredCriteria.add(new DataAreaSizeCriterion(ID_DATA_AREA_SIZE, false));
		measuredCriteria.add(new StackSizeCriterion(ID_STACK_SIZE, false));
		measuredCriteria.add(new ExecutetionStepsCriterion(ID_EXECUTION_STEPS, false));
		measuredCriteria.add(new MemReferencesCriterion(ID_MEMORY_REFERENCES, false));
		measuredCriteria.add(new DataReferencesCriterion(ID_DATA_REFERENCES, false));
		
		variableCriteria = new HashSet<VariableCriterion>();
		variableCriteria.addAll(registerCriteria);
		variableCriteria.addAll(symbolCriteria);
		
		allCriteria = new HashSet<Criterion>();
		allCriteria.addAll(registerCriteria);
		allCriteria.addAll(symbolCriteria);
		allCriteria.addAll(measuredCriteria);
		allCriteria.addAll(instructionCriteria);
		allCriteria.addAll(outputCriteria);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	
	
// <Feedback Tests>	
	/* Test setHighQualityFeedback(String), getHighQualityFeedback() of all Criterion types */
	public void testSetHighQualityFeedback() {
		String fb = "Korkean laadun palaute";
		for (Criterion c : allCriteria) {
			c.setHighQualityFeedback(fb);
			assertEquals(c.getHighQualityFeedback(), fb);			
			c.setHighQualityFeedback(null);
			assertEquals(c.getHighQualityFeedback(), "");			
		}
	}

	
	/* Test method for 'setAcceptanceFeedback(String)' */
	public void testSetAcceptanceFeedback() {
		String fb = "Onnistumisen palaute";
		for (Criterion c : allCriteria) {
			c.setAcceptanceFeedback(fb);
			assertEquals(c.getAcceptanceFeedback(), fb);
			c.setAcceptanceFeedback(null);
			assertEquals(c.getAcceptanceFeedback(), "");
		}
	}

	/* Test method for 'setFailureFeedback(String)' */
	public void testSetFailureFeedback() {
		String fb = "Epäonnistumisen palaute";
		for (Criterion c : allCriteria) {
			c.setFailureFeedback(fb);
			assertEquals(c.getFailureFeedback(), fb);			
			c.setFailureFeedback(null);
			assertEquals(c.getFailureFeedback(), "");			
		}
	}
// </Feedback Tests>	

	
	
	
	
	/* Test method for 'serializeToXML()' */
	public void testSerializeToXML() {
		for (SymbolCriterion c : symbolCriteria) {
			c.setSymbolName("X");
		}
		for (VariableCriterion c :variableCriteria) {
			c.setComparisonOperator("<");
		}		
		for (Criterion c1 : allCriteria) {
			c1.setHighQualityFeedback("1");
			c1.setAcceptanceFeedback("2");
			c1.setFailureFeedback("3");
			c1.setAcceptanceTestValue("30");			

			
			String xml = c1.serializeToXML();
			Criterion c2 = Criterion.deserializeFromXML(xml);
			
			assertEquals(c1.getClass(), c2.getClass());
			assertEquals(c1.getId(), c2.getId());
			assertEquals(c1.getHighQualityFeedback(), c2.getHighQualityFeedback());
			assertEquals(c1.getAcceptanceFeedback(), c2.getAcceptanceFeedback());
			assertEquals(c1.getFailureFeedback(), c2.getFailureFeedback());
			assertEquals(c1.isSecretInputCriterion(), c2.isSecretInputCriterion());
			assertEquals(c1.getAcceptanceTestValue(), c2.getAcceptanceTestValue());
			assertEquals(c1.getQualityTestValue(), c2.getQualityTestValue());
			assertEquals(c1.hasAcceptanceTest(true), c2.hasAcceptanceTest(true));
			assertEquals(c1.hasAcceptanceTest(false), c2.hasAcceptanceTest(false));
			assertEquals(c1.hasQualityTest(true), c2.hasQualityTest(true));
			assertEquals(c1.hasQualityTest(false), c2.hasQualityTest(false));
			if (c1 instanceof VariableCriterion) {
				VariableCriterion v1 = (VariableCriterion)c1;
				VariableCriterion v2 = (VariableCriterion)c2;
				assertEquals(v1.getComparisonOperator(), v2.getComparisonOperator());
			}
			if (c1 instanceof SymbolCriterion) {
				SymbolCriterion s1 = (SymbolCriterion)c1;
				SymbolCriterion s2 = (SymbolCriterion)c2;
				assertEquals(s1.getSymbolName(), s2.getSymbolName());				
			}
			assertEquals(xml, c2.serializeToXML());
		}
	}

	
// <Acceptance Value Tests>
// Test that:
// - initial state for hasAcceptanceTest(..) is always false
// - initial state for getAcceptanceTestValue() is always ""
// - when set to valid value, hasAcceptanceTest(..) gives true
// - when set to valid value, getAcceptanceTestValue() returns that value
// - when set to invalid value, hasAcceptanceTest(..) gives false
// - when set to invalid value, getAcceptanceTestValue() returns ""	
// - when set to null value, hasAcceptanceTest(..) gives false
// - when set to null value, getAcceptanceTestValue() returns ""	
	public void testMeasuredCriterionAcceptanceTestValue() {
		for (MeasuredCriterion c : measuredCriteria) {
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());			

			c.setAcceptanceTestValue("500");
			assertTrue(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals("500", c.getAcceptanceTestValue());
			
			c.setAcceptanceTestValue("Invalid input");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());
			
			c.setAcceptanceTestValue(null);
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());			
		}
	}		
	
	public void testScreenOutputCriterionAcceptanceTestValue() {
		for (ScreenOutputCriterion c : outputCriteria) {
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());			

			c.setAcceptanceTestValue("1, 2,  3,garbage,4;5\t6,");
			assertTrue(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals("1, 2, 3, 4, 5, 6", c.getAcceptanceTestValue());		

			c.setAcceptanceTestValue("Invalid input,");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());

			c.setAcceptanceTestValue(null);
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());			
		}		
	}
	
	public void testInstructionCriterionAcceptanceTestValue() {
		for (InstructionCriterion c : instructionCriteria) {
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());			

			c.setAcceptanceTestValue("NOP, MUL,  ADD,,XOR;DIV ; SUB,");
			assertTrue(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals(c.getAcceptanceTestValue(), "NOP, MUL, ADD, XOR, DIV, SUB");		

			c.setAcceptanceTestValue("");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());

			c.setAcceptanceTestValue(null);
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());			
		}		
	}
	
	public void testRegisterCriterionAcceptanceTestValue() {
		for (RegisterCriterion c : registerCriteria) {
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());			

			// Valid value, Model = true
			c.setAcceptanceTestValue("500");
			c.setCompareToModel("yes");
			assertTrue(c.hasAcceptanceTest(false));
			assertTrue(c.hasAcceptanceTest(true));
			assertEquals("500", c.getAcceptanceTestValue());

			// Valid value, Model = false
			c.setAcceptanceTestValue("500");
			c.setCompareToModel("");
			assertTrue(c.hasAcceptanceTest(false));
			assertFalse(c.hasAcceptanceTest(true));
			assertEquals("500", c.getAcceptanceTestValue());

			// Invalid value, Model = true
			c.setAcceptanceTestValue("Invalid input");
			c.setCompareToModel("yes");
			assertFalse(c.hasAcceptanceTest(false));
			assertTrue(c.hasAcceptanceTest(true));
			assertEquals("", c.getAcceptanceTestValue());
			
			// Invalid value, Model = false		
			c.setAcceptanceTestValue(null);
			c.setCompareToModel(null);
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());
		}
	}

	public void testSymbolCriterionAcceptanceTestValue() {
		for (SymbolCriterion c : symbolCriteria) {
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());
			assertEquals("", c.getSymbolName());			

			// Invalid value, Invalid symbol, Model = false
			c.setAcceptanceTestValue("Invalid Input");
			c.setSymbolName("");
			c.setCompareToModel("");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());
			assertEquals("", c.getSymbolName());
			
			// Valid value, Invalid symbol,  Model = false
			c.setAcceptanceTestValue("500");
			c.setSymbolName("");
			c.setCompareToModel("");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("500", c.getAcceptanceTestValue());
			assertEquals("", c.getSymbolName());

			// Invalid value, Valid symbol,  Model = false 
			c.setAcceptanceTestValue("Invalid Input");
			c.setSymbolName("X");
			c.setCompareToModel("");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());
			assertEquals("X", c.getSymbolName());
			
			// Valid value, Valid symbol,  Model = false
			c.setAcceptanceTestValue("500");
			c.setSymbolName("X");
			c.setCompareToModel("");
			assertFalse(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals("500", c.getAcceptanceTestValue());
			assertEquals("X", c.getSymbolName());
			
			// Invalid value, Invalid symbol, Model = true
			c.setAcceptanceTestValue(null);
			c.setSymbolName(null);
			c.setCompareToModel("yes");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());
			assertEquals("", c.getSymbolName());
			
			// Valid value, Invalid symbol,  Model = true
			c.setAcceptanceTestValue("500");
			c.setSymbolName(null);
			c.setCompareToModel("yes");
			assertFalse(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("500", c.getAcceptanceTestValue());
			assertEquals("", c.getSymbolName());

			// Invalid value, Valid symbol,  Model = true 
			c.setAcceptanceTestValue(null);
			c.setSymbolName("X");
			c.setCompareToModel("yes");
			assertTrue(c.hasAcceptanceTest(true));
			assertFalse(c.hasAcceptanceTest(false));
			assertEquals("", c.getAcceptanceTestValue());
			assertEquals("X", c.getSymbolName());
			
			// Valid value, Valid symbol,  Model = true
			c.setAcceptanceTestValue("500");
			c.setSymbolName("X");
			c.setCompareToModel("yes");
			assertTrue(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals("500", c.getAcceptanceTestValue());
			assertEquals("X", c.getSymbolName());
		}
	}
// </Acceptance Value Tests>	

	
// <Quality Value Tests>
	/** Test Quality test-methods of all MeasuredCriterion. */  
	public void testMeasuredCriterionQualityTestValue() {
		for (MeasuredCriterion c : measuredCriteria) {
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getQualityTestValue());			

			c.setQualityTestValue("500");
			assertTrue(c.hasQualityTest(true));
			assertTrue(c.hasQualityTest(false));
			assertEquals("500", c.getQualityTestValue());
			
			c.setQualityTestValue("Invalid input");
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getQualityTestValue());
			
			c.setQualityTestValue(null);
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getAcceptanceTestValue());			
		}
	}		

	
	/** Test Quality test-methods of all other criteria than MeasuredCriterion.
	 * Since MeasuredCriterion are the only ones to support quality test,
	 * hasQualityTest(..) should always return false and getQualityTestValue()  
	 * should always return "".
	 */  
	public void testCriterionQualityTestValue() {
		allCriteria.removeAll(measuredCriteria);
		
		for (Criterion c : allCriteria) {
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getQualityTestValue());			

			c.setQualityTestValue("NOP");
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getQualityTestValue());
			
			c.setQualityTestValue("500");
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getQualityTestValue());
			
			c.setQualityTestValue("Invalid input");
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getQualityTestValue());
			
			c.setQualityTestValue(null);
			assertFalse(c.hasQualityTest(true));
			assertFalse(c.hasQualityTest(false));
			assertEquals("", c.getAcceptanceTestValue());			
		}
	}	
// </Quality Value Tests>
	
	
		

}
