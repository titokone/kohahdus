package fi.helsinki.cs.kohahdus;

/** Code size criterion class */  
public class CodeSizeCriterion extends MeasuredCriterion {
	
	/** Empty constructor for deserialization */
	protected CodeSizeCriterion() {
		super();		
	}

	/**  */
	public CodeSizeCriterion(String id, boolean usesSecretInput, boolean mustPass, String positiveFeedback,
							 String negativeFeedback, String limit) {
		super(id, usesSecretInput, mustPass, positiveFeedback, negativeFeedback, limit);
	}

	protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getCodeSize();
	}
}
