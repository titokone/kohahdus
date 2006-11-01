package fi.helsinki.cs.kohahdus.criteria;


/** Base class for all criterion types. The many different types of criteria in
 * TitoTrainer are all used via the interface defined here. The analyzer component
 * does not know the details of different Criterion sub-classes. Only the composer
 * used for creating and modiying Tasks is aware that differnt types of criteria exist.
 * <p>
 * This class and all sub-classes shall provide following quarantees
 * <ul>
 * <li> getters always return Strings, regarless of the field type 
 * <li> getters never return null, but they may return empty strings
 * <li> setters always take Strings, regarless of the field type.
 *      Invalid strings, (eg. non-numeric string for numeric field),
 *      empty strings, and null values are acceptable and will clear
 *      the field OR set the field to some default value.
 * <li> setters do not throw exceptions
 * <li> Criterion objects are never in an invalid state. This is done
 *      by setting and validating mandatory fields in the constructor.
 * <li> However, Criterion object deserialized from the database are
 *      not subject to validation, they are assumed to be always valid.
 * </ul>   
 */
public abstract class Criterion {
	public static final String ID_PUBLIC_REGISTER_PREFIX = "PUBREG";
	public static final String ID_SECRET_REGISTER_PREFIX = "SECREG";
	public static final String ID_PUBLIC_SYMBOL_PREFIX   = "PUBSYM";
	public static final String ID_SECRET_SYMBOL_PREFIX   = "SECSYM";
	public static final String ID_PUBLIC_OUTPUT          = "PUBOUT";
	public static final String ID_SECRET_OUTPUT          = "SECOUT";
	public static final String ID_REQUIRED_INSTRUCTIONS  = "REQOPCODES";
	public static final String ID_FORBIDDEN_INSTRUCTIONS = "BANOPCODES";
	public static final String ID_CODE_SIZE              = "CODESIZE";
	public static final String ID_DATA_AREA_SIZE         = "DATASIZE";
	public static final String ID_STACK_SIZE             = "STACKSIZE";
	public static final String ID_EXECUTION_STEPS        = "STEPS";
	public static final String ID_MEMORY_REFERENCES      = "MEMREF";
	public static final String ID_DATA_REFERENCES        = "DATAREF";
	
	
	
	
	/** Special case for signaling undefined numeric value. Criteria that deal with numeric
	 *  types (such as RegisterCriterium) need all 32-bits of int, but we also need a way
	 *  to represent undefined (IOW null) values. Best-but-still-ugly solution is to use
	 *  longs for all numeric types and use a special signal value for undef. */
	protected static final long UNDEFINED = Long.MIN_VALUE; 
	
	private String id;
	private String highQualityFeedback = "";
	private String acceptanceFeedback = "";
	private String failureFeedback = "";
	private boolean secretInputCriterion;
	
	
	/** Empty constructor for deserialization */
	protected Criterion() { }
	
	
	/** Initialize mandatory data members of Criterion 
	 * @param id Identifier that can used to distinguish the different criteria of one task
	 * @param usesSecretInput true if criterion is to be use in conjunktion to secret input */
	protected Criterion(String id, boolean usesSecretInput) {
		if (id == null) {
			throw new NullPointerException("Criterion ID cannot be null");
		}		
		if (id.equals("")) {
			throw new IllegalArgumentException("Criterion ID cannot be empty string");
		}		
		this.secretInputCriterion = usesSecretInput;
		this.id = id;
	}

	
	/** Return the identifier of this Criterion */
	public String getId(){
		return id;
	}

	
	/** Return true if this criterion is to be used with secret input */
	public boolean isSecretInputCriterion() {
		return secretInputCriterion;		
	}

	
	/** Return the feedback string used for high quality solution attempts */
	public String getHighQualityFeedback() {
		return highQualityFeedback;
	}
	
	/** Set the feedback string used for high quality solution attempts */
	public void setHighQualityFeedback(String feedback) {
		highQualityFeedback = (feedback != null) ? feedback : "";
	}
	
	
	/** Return the feedback string used for acceptable solution attempts */
	public String getAcceptanceFeedback() {
		return acceptanceFeedback;
	}
	
	/** Set the feedback string used for acceptable solution attempts */
	public void setAcceptanceFeedback(String feedback) {
		acceptanceFeedback = (feedback != null) ? feedback : "";
	}

	
	/** Return the feedback string used for failed solution attempts */
	public String getFailureFeedback() {
		return failureFeedback;
	}
	
	/** Set the feedback string used for failed solution attempts */
	public void setFailureFeedback(String feedback) {
		failureFeedback = (feedback != null) ? feedback : "";
	}	

	
	/** Return a serialized copy of this Criterion in XML-format */
	public String serializeToXML() {
		return toXML("class", this.getClass().getName()) + 
			   toXML("posfb", getAcceptanceFeedback()) +
			   toXML("negfb", getFailureFeedback()) +
			   toXML("hqfb", getHighQualityFeedback()) +
			   toXML("secret", isSecretInputCriterion()) + 
			   toXML("id", getId()) +
			   serializeSubClass();
	}

	
	/** Instantiate new Criterion object using the serialized form XML.
	 * @throws RuntimeException exceptions in the deserialization are
	 * caught and rethrown as uncheckced exception. */
	public static Criterion deserializeFromXML(String xml)  {
		try {
			String criterionClass = parseXMLString(xml, "class");
			Class concreteClass = Class.forName(criterionClass);
			Criterion c = (Criterion) concreteClass.newInstance();
			c.acceptanceFeedback = parseXMLString(xml, "posfb");
			c.failureFeedback = parseXMLString(xml, "negfb");
			c.highQualityFeedback = parseXMLString(xml, "hqfb");
			c.secretInputCriterion = parseXMLBoolean(xml, "secret");
			c.id = parseXMLString(xml, "id");
			c.initSubClass(xml);
			return c;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// TODO: tämä takaisin VariableCriterion luokkaan?
	public String getComparisonOperator() {
		return "";
	}
	// TODO: tämä takaisin VariableCriterion luokkaan?
	public void setComparisonOperator(String operator) {
		
	}
	
	
	/** Return true if this criterion has test for evaluating failure/success
	 * of the student's answer. Return of false means this criterion should
	 * NOT be used to test <code>passesAcceptanceTest(..)</code>. */
	public abstract boolean hasAcceptanceTest(boolean usingModelAnswer);
	
	/** Return true if student's solution meets the passing requirement of this Criterion. 
	 * 
	 * @param studentAnswer end state of TitoKone for student's answer
	 * @param modelAnswer end state of TitoKone for teacher's answer. If the task is
	 *        NOT validated against teacher's model answer, pass <code>null</code> param. */
	public abstract boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer);
	
	/** Return the value the student's answer will be compared to */
	public abstract String getAcceptanceTestValue();
	
	/** Set the value the student's answer will be compared to. */
	public abstract void setAcceptanceTestValue(String test);

	
	/** Return true if this criterion has test for evaluating the quality
	 * of the student's answer. Return of false means this criterion should
	 * NOT be used to test <code>passesQualityTest(..)</code>.
	 * 
	 * Criterion class provides a default implementation that always returns false. */
	public boolean hasQualityTest(boolean usingModelAnswer) {
		return false;
	}
	
	/** Return true if student's solution meets the high-quality requirement of this criterion.
	 * <p>
	 * Criterion class provides a default implementation that always returns false. */
	public boolean passesQualityTest(TitoState studentAnswer, TitoState modelAnswer) {
		return false;
	}
	
	/** Return the value the student's answer will be compared to.
	 *  
	 * Criterion class provides a default implementation that always returns an empty string. */
	public String getQualityTestValue() {
		return "";
	}

	/** Set the value the student's answer will be compared to. 
	 * 
	 * Criterion class provides a default implementation that is a no-op. */
	public void setQualityTestValue(String test) {
	}
	
	
	
	
	
	/** Serialize non-static data-members of Criterion sub-class to XML format. The
	 * subclass can decide the names of its XML tags but they must not collide with
	 * the tags used by this class: "class", "posfb", "negfb", "hqfb", "secret" 
	 * and "id". The abstract Criterion class will handle the serialization of its
	 * data-members, subclasses need to deserialize only the fields they add.
	 * <p>
	 * NOTE: aAssari DB imposes a 2000 char limit to stored strings so subclasses
	 * should try to keep both tags and data short (without being cryptic). */
	protected abstract String serializeSubClass();	
	
	
	/** Initialize non-static data-members of this Criterion subclass instance using
	 * the serialized representation returned by <code>serializeToXML()</code>. The
	 * data-member of the abstract Criterion class will have already been deserialized
	 * when this method is called. */
	protected abstract void initSubClass(String serializedXML);

	
	/** Serialize String value to XML string. Helper function for serializeSubClass() */
	protected static String toXML(String tagname, String value) {
		value = value.replaceAll("&", "&amp;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("<", "&lt;");
		return "<" + tagname + ">" + value + "</" + tagname + ">";
	}
	
	/** Serialize boolean value to XML string. Helper function for serializeSubClass() */
	protected static String toXML(String tagname, boolean value) {
		return "<" + tagname + ">" + (value ? 'T' : 'F') + "</" + tagname + ">";
	}
	
	/** Serialize int value to XML string. Helper function for serializeSubClass() */
	protected static String toXML(String tagname, long value) {
		String xml = "<" + tagname + ">";
		if (value != UNDEFINED) { // Empty tag is better and shorter representation of undefined
			xml = xml + value;
		}
		return xml + "</" + tagname + ">";
	}	
	
	/** Deserialize String value from XML string. Helper function for initSubClass() */
	protected static String parseXMLString(String XML, String tagname) {
		int begin = XML.indexOf("<" + tagname + ">") + tagname.length() + 2;
		int end   = XML.indexOf("</" + tagname + ">");
		String value = XML.substring(begin, end);
		value = value.replaceAll("&gt;", ">");
		value = value.replaceAll("&lt;", "<");
		value = value.replaceAll("&amp;", "&");
		return value;
	}

	/** Deserialize boolean value from XML string. Helper function for initSubClass() */
	protected static boolean parseXMLBoolean(String XML, String tagname) {
		String value = parseXMLString(XML, tagname);
		return value.charAt(0) == 'T';
	}
	
	/** Deserialize long value from XML string. Helper function for initSubClass()
	 * @return value or UNDEFINED */
	protected static long parseXMLLong(String XML, String tagname) {
		String value = parseXMLString(XML, tagname);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return UNDEFINED;
		}
	}	
}	
