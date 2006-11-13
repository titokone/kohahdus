package fi.helsinki.cs.kohahdus.criteria;

import java.util.ResourceBundle;

/** Base class for criteria that measure runtime or compile time values of the student's answer.
 * These can be used as pass-fail criteria and/or as quality evaluators.
 * @author mkinnune
 *
 */

public abstract class MeasuredCriterion extends Criterion {
	protected long passingLimit = UNDEFINED;
	protected long qualityLimit = UNDEFINED;
	
	/** Empty constructor for deserialization */
	protected MeasuredCriterion() { }
	
	public MeasuredCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}
	
	@Override public String getName(ResourceBundle languageBundle) {
		return languageBundle.getString(this.getId());
	}
	

	
	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return passingLimit != UNDEFINED;
	}

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return getCriterionValue(studentAnswer) <= passingLimit;
	}
	
	@Override public String getAcceptanceTestValue() {
		String value = "";
		if (passingLimit != UNDEFINED) {
			value = Long.toString(passingLimit);
		}
		return value;
	}

	/** Attempt setting the passing limit (failure sets limit UNDEFINED) */
	@Override public void setAcceptanceTestValue(String test) {
		try {
			passingLimit = Integer.parseInt(test);
		} catch (Exception e) { // NumberFormatException, NullPointerException
			passingLimit = UNDEFINED;
		}
	}
	
	
	
	@Override public boolean hasQualityTest(boolean usingModelAnswer) {
		return qualityLimit != UNDEFINED;
	}

	@Override public boolean passesQualityTest(TitoState studentAnswer, TitoState modelAnswer) {
		return getCriterionValue(studentAnswer) <= qualityLimit;
	}

	@Override public String getQualityTestValue() {
		String value = "";
		if (qualityLimit != UNDEFINED) {
			value = Long.toString(qualityLimit);
		}
		return value;
	}	

	/** Attempt setting the quality limit (failure sets limit UNDEFINED) */
	@Override public void setQualityTestValue(String test) {
		try {
			qualityLimit = Integer.parseInt(test);
		} catch (Exception e) { // NumberFormatException, NullPointerException
			qualityLimit = UNDEFINED;
		}
	}

	
	/** Serialize fields common to all MeasuredCriterion types. If any subclass overrides
	 * this method it must append restult of <code>super.serializeSubClass()</code> to
	 * the retured string */	
	@Override protected String serializeSubClass() {
		return toXML("plim", passingLimit) +
			   toXML("qlim", qualityLimit);
	}	

	/** Initialize fields common to all MeasuredCriterion types. If any subclass overrides
	 * this method it must call <code>super.initSubClass(serializedXML)</code> */	
	@Override protected void initSubClass(String serializedXML) {
		passingLimit = parseXMLLong(serializedXML, "plim");		
		qualityLimit = parseXMLLong(serializedXML, "qlim");		
	}
	
	/** Return the value of the property this MeasuredCriterion subclass is intrested in. */
	protected abstract int getCriterionValue(TitoState studentAnswer);
}
