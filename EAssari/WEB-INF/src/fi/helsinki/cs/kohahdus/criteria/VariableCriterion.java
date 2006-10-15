package fi.helsinki.cs.kohahdus.criteria;

/** Base class for criteria that examine register or symbol variables */
public abstract class VariableCriterion extends Criterion {
	public static final int COMPARE_EQ = 0;
	public static final int COMPARE_NE = 1;
	public static final int COMPARE_LT = 2;
	public static final int COMPARE_GT = 3;
	public static final int COMPARE_LTEQ = 4;
	public static final int COMPARE_GTEQ = 5;
	
	private int comparisonOperator;
	private long comparisonValue = UNDEFINED;
	private boolean compareToModel = false;

	/** Empty constructor for deserialization */
	protected VariableCriterion() { }
	
	
	public VariableCriterion(String id, boolean usesScretInput, String comparisonOperator) {
		super(id, usesScretInput);

		if (comparisonOperator == null) {
			throw new IllegalArgumentException("Comparison operator cannot be null");
		} else if (comparisonOperator.equals("==")) {
			this.comparisonOperator = COMPARE_EQ;
		} else if (comparisonOperator.equals("!=")) {
			this.comparisonOperator = COMPARE_NE;
		} else if (comparisonOperator.equals("<")) {
			this.comparisonOperator = COMPARE_LT;
		} else if (comparisonOperator.equals(">")) {
			this.comparisonOperator = COMPARE_GT;
		} else if (comparisonOperator.equals("<=")) {
			this.comparisonOperator = COMPARE_LTEQ;
		} else if (comparisonOperator.equals(">=")) {
			this.comparisonOperator = COMPARE_GTEQ;
		} else {
			throw new IllegalArgumentException("Invalid comparison operator string: " + comparisonOperator);
		}
		
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
