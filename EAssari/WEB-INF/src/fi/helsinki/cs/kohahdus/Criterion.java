package fi.helsinki.cs.kohahdus;


/** Base class for all criterion types. The many different types of criteria in TitoTrainer
 * are all used via the interface defined here. The analyzer component does not know the
 * details of different Criterion sub-classes. Only the composer used for creating and
 * modiying Tasks is even aware that differnt types of criteria exist. * 
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

	/** Return a serialized copy of this Criterion in XML-format */
	public String serializeToXML() {
		return "<class>" + this.getClass().getName() + "</class>" + // TODO: vaiko getCanonicalName() ? 
			   "<posfb>" + positiveFeedback + "</posfb>" +			// TODO: convert <> to &lt; &gt;
			   "<negfb>" + negativeFeedback + "</negfb>" +
			   "<secret>" + secretInputCriterion + "</secret>" + 
			   serializeSubClass();
	}
	
	
	/** Return true if student's answer meets the condition(s) of this criterion. Criterion
	 * types that also evaluate quality of the answer must return <code>true</code> if the 
	 * answer fullfills the passing requirement, even if answer was deemed low quality. */
	public abstract boolean meetsCriterion(TitoState studentAnswer, TitoState modelAnswer);

	/** Serialize non-static data-members of Criterion sub-class to XML format. The subclass
	 * can freely decide the names of the XML tags. The abstract Criterion class will handle
	 * the serialization of its data-members.
	 * 
	 * The serialized string is stored in the eAssari database in a 2000-char field so
	 * subclasses should try to keep the tags and data short (without being cryptic). */
	protected abstract String serializeSubClass();
	
	/** Initialize non-static data-members of this Criterion subclass instance using the 
	 * serialized representation returned by <code>serializeToXML()</code>. The data-member
	 * of the abstract Criterion class will have already been deserialized when this method
	 * is called. */
	protected abstract void initSubClass(String serializedXML);

	
	
	/** Instantiate new Criterion object using the serialized form Xml */
	public static Criterion deserializeFromXML(String xml)  {
		try {
			String criterionClass = "Konkreettisen luokan nimi XML-muutujasta";
			Class concreteClass = Class.forName(criterionClass);
			if (concreteClass != null) {
				Criterion c = (Criterion) concreteClass.newInstance();
				c.positiveFeedback = "Alustetaan xml-muuttujan tiedoilla";
				c.negativeFeedback = "Alustetaan xml-muuttujan tiedoilla";
				c.secretInputCriterion = false; //Alustetaan xml-muuttujan tiedoilla
				c.initSubClass(xml);
				return c;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}	
}