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

	
	/* Test method for setHighQualityFeedback(String), getHighQualityFeedback() */
	public void testSetHighQualityFeedback() {
		String fb = "Korkean laadun palaute";
		for (Criterion c : allCriteria) {
			c.setHighQualityFeedback(fb);
			assertEquals(c.getHighQualityFeedback(), fb);			
		}
	}

	
	/* Test method for 'setAcceptanceFeedback(String)' */
	public void testSetAcceptanceFeedback() {
		String fb = "Onnistumisen palaute";
		for (Criterion c : allCriteria) {
			c.setAcceptanceFeedback(fb);
			assertEquals(c.getAcceptanceFeedback(), fb);			
		}
	}

	/* Test method for 'setFailureFeedback(String)' */
	public void testSetFailureFeedback() {
		String fb = "Ep�onnistumisen palaute";
		for (Criterion c : allCriteria) {
			c.setFailureFeedback(fb);
			assertEquals(c.getFailureFeedback(), fb);			
		}
	}

	/* Test method for 'serializeToXML()' */
	public void testSerializeToXML() {
		for (Criterion c1 : allCriteria) {
			c1.setHighQualityFeedback("1");
			c1.setAcceptanceFeedback("2");
			c1.setFailureFeedback("3");
			
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

	
	public void testMeasuredCriterionAcceptanceTestValue() {
		for (MeasuredCriterion c : measuredCriteria) {
			c.setAcceptanceTestValue("500");
			assertTrue(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals("500", c.getAcceptanceTestValue());
		}
	}		
	public void testRegisterCriterionAcceptanceTestValue() {
		for (VariableCriterion c : registerCriteria) {
			c.setAcceptanceTestValue("500");
			assertTrue(c.hasAcceptanceTest(false));
			assertFalse(c.hasAcceptanceTest(true));
			assertEquals("500", c.getAcceptanceTestValue());
		}
	}
	public void testScreenOutputCriterionAcceptanceTestValue() {
		for (ScreenOutputCriterion c : outputCriteria) {
			c.setAcceptanceTestValue("1, 2,  3,,4;5 ; 6,");
			assertTrue(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals(c.getAcceptanceTestValue(), "1, 2, 3, 4, 5, 6");		
		}		
	}
	public void testInstructionCriterionAcceptanceTestValue() {
		for (InstructionCriterion c : instructionCriteria) {
			c.setAcceptanceTestValue("NOP, MUL,  ADD,,XOR;DIV ; SUB,");
			assertTrue(c.hasAcceptanceTest(true));
			assertTrue(c.hasAcceptanceTest(false));
			assertEquals(c.getAcceptanceTestValue(), "NOP, MUL, ADD, XOR, DIV, SUB");		
		}		
	}

	
	/* Test method for 'setQualityTestValue(String)' */
	public void testSetQualityTestValue() {
		for (MeasuredCriterion c : measuredCriteria) {
			c.setAcceptanceTestValue("");
		}
	}

}