package fi.helsinki.cs.kohahdus;

/** Base class for all criterion types.
 * 
 * @author mkinnune
 *
 */
public abstract class Criterion {
	protected String positiveFeedback;
	protected String negativeFeedback;
	protected boolean secretInputCriterion;


	/** Return the positive feedback string of this Criterion. Criterion types that also
	 * evaluate quality of the answer should overide this method so they can return a 
	 * different string depending on the quality of the student's answer. */
	public String getPositiveFeedback(TitoState studentAnswer) {
		return positiveFeedback;
	}

	/** Return the negative feedback string of this Criterion */
	public String getNegativeFeedback() {
		return negativeFeedback;
	}

	/** Return true if this criterion is to be used with secret input */
	public boolean isSecretInputCriterion() {
		return secretInputCriterion;		
	}
	
	
	/** Return true if students answer meets the condition(s) of this criterion. Criterion
	 * types that also evaluate quality of the answer return <code>true</code> if the 
	 * answer fullfills the passing requirement, even if answer was deemed low quality. */
	public abstract boolean meetsCriterion(TitoState studentAnswer, TitoState modelAnswer);

	/** Serialize and return a string representation of this criterion */
	public abstract String serializeToString();
	
	/** Initialize fields of this criterion using the serialized representation returned
	 * by <code>serializeToString()</code> */
	public abstract void init(String serialized);
	
	
}