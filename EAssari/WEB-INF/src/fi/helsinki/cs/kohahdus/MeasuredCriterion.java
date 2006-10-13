package fi.helsinki.cs.kohahdus;

/** Base class for criteria that measure runtime or compile time values of the student's answer.
 * These can be used as pass-fail criteria and/or as quality evaluators.
 * @author mkinnune
 *
 */

public abstract class MeasuredCriterion extends Criterion {
	protected long passingLimit = UNDEFINED;
	protected long qualityLimit = UNDEFINED;
	
	/** Empty constructor for deserialization */
	protected MeasuredCriterion() {	}
	
	
	/** Initialize fields common to all MeasuredCriterion types */
	public MeasuredCriterion(String id, boolean usesSecretInput, String highQualityFeedback,
			 				 String passingFeedback, String negativeFeedback,
			 				 String qualityLimit, String passingLimit) {
		super(id, usesSecretInput, highQualityFeedback, passingFeedback, negativeFeedback);

		// Attempt parsing passing the limit (failure leaves limit UNDEFINED)
		if (passingLimit != null) {
			try {
				this.passingLimit = Integer.parseInt(passingLimit);
			} catch (NumberFormatException e) {}
		}

		// Attempt parsing passing the limit (failure leaves limit UNDEFINED)
		if (qualityLimit != null) {
			try {
				this.qualityLimit = Integer.parseInt(qualityLimit);
			} catch (NumberFormatException e) {}
		}
	}

	
	

	@Override
	public boolean hasAcceptanceTest() {
		return passingLimit != UNDEFINED;
	}


	@Override
	public boolean hasQualityTest() {
		return qualityLimit != UNDEFINED;
	}

	@Override
	public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return getCriterionValue(studentAnswer) <= passingLimit;
	}

	@Override
	public boolean passesQualityTest(TitoState studentAnswer, TitoState modelAnswer) {
		return getCriterionValue(studentAnswer) <= qualityLimit;
	}

	@Override
	protected String serializeSubClass() {
		return toXML("plim", passingLimit) +
			   toXML("qlim", qualityLimit);
	}	

	@Override
	protected void initSubClass(String serializedXML) {
		passingLimit = parseXMLLong(serializedXML, "plim");		
		qualityLimit = parseXMLLong(serializedXML, "qlim");		
	}
	
	/** Return the value of the property this MeasuredCriterion subclass is intrested in. */
	protected abstract int getCriterionValue(TitoState studentAnswer);



}
