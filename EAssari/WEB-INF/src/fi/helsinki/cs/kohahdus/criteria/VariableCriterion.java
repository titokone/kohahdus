package fi.helsinki.cs.kohahdus.criteria;

/** Base class for criteria that examine register or symbol variables */
public abstract class VariableCriterion extends Criterion {
	private static final int COMPARE_EQ = 0;
	private static final int COMPARE_NE = 1;
	private static final int COMPARE_LT = 2;
	private static final int COMPARE_GT = 3;
	private static final int COMPARE_LTEQ = 4;
	private static final int COMPARE_GTEQ = 5;
	
	private int comparisonOperator = COMPARE_EQ;
	private long comparisonValue = UNDEFINED;
	private boolean compareToModel = false;

	/** Empty constructor for deserialization */
	protected VariableCriterion() { }
	
	
	public VariableCriterion(String id, boolean usesScretInput) {
		super(id, usesScretInput);
	}

	

	
	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		boolean haveTest = false;
		if ((usingModelAnswer) && (compareToModel)) {
			haveTest = true;
		}
		if ((!usingModelAnswer) && (comparisonValue != UNDEFINED)) {
			haveTest = true;
		}		
		return haveTest;
	}

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		boolean passes = false;
		
		long studentAnswerValue = getCriterionValue(studentAnswer);
		long expectedValue = comparisonValue;
		if ((modelAnswer != null) && (compareToModel)) {
			expectedValue = getCriterionValue(modelAnswer);
		}		
		
		switch (comparisonOperator) {
			case COMPARE_EQ:   passes = (studentAnswerValue == expectedValue); break;
			case COMPARE_NE:   passes = (studentAnswerValue != expectedValue); break;
			case COMPARE_LT:   passes = (studentAnswerValue < expectedValue);  break;
			case COMPARE_GT:   passes = (studentAnswerValue > expectedValue);  break;
			case COMPARE_LTEQ: passes = (studentAnswerValue <= expectedValue); break;
			case COMPARE_GTEQ: passes = (studentAnswerValue >= expectedValue); break;
		}
		
		return passes;
	}	
	
	@Override public String getAcceptanceTest() {
		String value = "";
		if (comparisonValue != UNDEFINED) {
			value = Long.toString(comparisonValue);
		}
		return value;
	}

	/** Set the value the student's answer will be compared to when using
	 * constant test (the value will be ignore when using model answer). */
	@Override public void setAcceptanceTest(String test) {
		try {
			comparisonValue = Integer.parseInt(test);
		} catch (Exception e) {} { // NumberFormatException, NullPointerException
			comparisonValue = UNDEFINED;
		}		
	}

	
	
	public String getComparisonOperator() {
		String operator = "==";		
		switch (comparisonOperator) {
			case COMPARE_EQ:   operator = "=="; break;
			case COMPARE_NE:   operator = "!="; break;
			case COMPARE_LT:   operator = "<";  break;
			case COMPARE_GT:   operator = ">";  break;
			case COMPARE_LTEQ: operator = "<="; break;
			case COMPARE_GTEQ: operator = ">="; break;
		}
		return operator;
	}

		
	
	/** Set the comparison operator for this VariableCriterion. If an invalid
	 * operator string is given, the default operator "==" is used.  
	 * @param operator any of <code>"==", "!=", "&lt;=", "&gt;=", "&gt;", "&lt;"</code> */
	public void setComparisonOperator(String operator) {
		if (operator == null) {
			this.comparisonOperator = COMPARE_EQ;
		} else if (operator.equals("=")) {
			this.comparisonOperator = COMPARE_EQ;
		} else if (operator.equals("==")) {
			this.comparisonOperator = COMPARE_EQ;
		} else if (operator.equals("!=")) {
			this.comparisonOperator = COMPARE_NE;
		} else if (operator.equals("<")) {
			this.comparisonOperator = COMPARE_LT;
		} else if (operator.equals(">")) {
			this.comparisonOperator = COMPARE_GT;
		} else if (operator.equals("<=")) {
			this.comparisonOperator = COMPARE_LTEQ;
		} else if (operator.equals(">=")) {
			this.comparisonOperator = COMPARE_GTEQ;
		} else {
			this.comparisonOperator = COMPARE_EQ;
		}	
	}
	

	
	@Override protected String serializeSubClass() {
		return toXML("cmpval", comparisonValue) +
			   toXML("cmpop", comparisonOperator) +
			   toXML("cmpmodel", compareToModel);
	}
	

	@Override protected void initSubClass(String serializedXML) {
		comparisonValue = parseXMLLong(serializedXML, "cmpval");
		comparisonOperator = (int) parseXMLLong(serializedXML, "cmpop");
		compareToModel = parseXMLBoolean(serializedXML, "cmpmodel");
	}
	

	/** Return the value of the variable this VariableCriterion subclass is intrested in.
	 * @return int value of the variable, UNDEFINED if variable does not exist */
	protected abstract long getCriterionValue(TitoState answer);
}
