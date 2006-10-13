package fi.helsinki.cs.kohahdus;

/** Code size criterion class */  
public class CodeSizeCriterion extends MeasuredCriterion {
	
	/** Empty constructor for deserialization */
	protected CodeSizeCriterion() { }

	/**  */
	public CodeSizeCriterion(String id, boolean usesSecretInput, String highQualityFeedback,
			 String passingFeedback, String negativeFeedback, String qualityLimit, String passingLimit) {
		
		super(id, usesSecretInput, highQualityFeedback, passingFeedback, negativeFeedback, qualityLimit, passingLimit);
	}

	protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getCodeSize();
	}
}
