package fi.helsinki.cs.kohahdus;

/** Base class for criteria that measure runtime or compile time values of the student's answer.
 * These can be used as pass-fail criteria and/or as quality evaluators.
 * @author mkinnune
 *
 */

public abstract class MeasuredCriterion extends Criterion {
	protected long limit = UNDEFINED;
	
	/** Empty constructor for deserialization */
	protected MeasuredCriterion() {
		super();
	}
	
	/** Initialize fields common to all MeasuredCriterion types */
	public MeasuredCriterion(String id, boolean usesSecretInput, boolean mustPass,
			 				 String passingFeedback, String negativeFeedback, String limit) {
		super(id, usesSecretInput, mustPass, passingFeedback, negativeFeedback);

		// Attempt parsing the limit (failure leaves limit UNDEFINED)
		if (limit != null) {
			try {
				this.limit = Integer.parseInt(limit);
			} catch (NumberFormatException e) {}
		}
	}	
	
	/** Return true if the limit is met. */
	public boolean meetsCriterion(TitoState studentAnswer, TitoState modelAnswer) {
		return getCriterionValue(studentAnswer) <= limit;
	}
	
	/** Serialize non-static MeasuredCriterion data-members. If any subclass overrides
	 * this method it must append the result of <code>super.serializeSubClass();<code>
	 * to the String it returns. */
	protected String serializeSubClass() {
		return toXML("lim", limit);
	}	
	
	/** Initialize non-static MeasuredCriterion data-members. If any subclass overrides
	 * this method it must call <code>super.initSubClass(serializedXML);<code>. */
	protected void initSubClass(String serializedXML) {
		limit = parseXMLLong(serializedXML, "lim");		
	}
	
	public boolean isActive() {
		return limit != UNDEFINED;
	}	
	
	/** Return the value of the property this MeasuredCriterion subclass is intrested in. */
	protected abstract int getCriterionValue(TitoState studentAnswer);
}
