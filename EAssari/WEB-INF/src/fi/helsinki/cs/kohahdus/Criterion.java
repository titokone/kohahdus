package fi.helsinki.cs.kohahdus;


/** Base class for all criterion types. The many different types of criteria in TitoTrainer
 * are all used via the interface defined here. The analyzer component does not know the
 * details of different Criterion sub-classes. Only the composer used for creating and
 * modiying Tasks is even aware that differnt types of criteria exist.
 * 
 * This class and all sub-classes provides following quarantees
 * - getters always return Strings, even if the field the getter is for is value type 
 * - getters never return null, but they may return empty strings
 * - constructors take string input, the constructor takes care of parsing value types
 * - constructors accept null values, treating them same as empty stings
 * - TODO: ID and secretInputCriterion are different
 */
public abstract class Criterion {
	/** Special case for signaling undefined numeric value. Criteria that deal with numeric
	 *  types (such as RegisterCriterium) need all 32-bits of int, but we also need a way
	 *  to represent undefined (ie. null) values. Best-but-still-ugly solution is to use
	 *  longs for all numeric types and give use special signal value for undef. */
	protected static final long UNDEFINED = Long.MIN_VALUE; 

	
	private String id;
	private String highQualityFeedback = "";
	private String acceptanceFeedback = "";
	private String failureFeedback = "";
	private boolean secretInputCriterion;
	private boolean passingCriterion;
	
	/** Empty constructor for deserialization */
	protected Criterion() { }
	
	
	/** Initialize fields common to all Criterion types */
	public Criterion(String id, boolean usesScretInput, String highQualityFeedback, String acceptanceFeedback, String failureFeedback) {
		if (id == null) {
			throw new NullPointerException("Criterion ID cannot be null");
		}		
		if (highQualityFeedback != null) {
			this.highQualityFeedback = highQualityFeedback;
		}
		if (acceptanceFeedback != null) {
			this.acceptanceFeedback = acceptanceFeedback;
		}
		if (failureFeedback != null) {
			this.failureFeedback = failureFeedback;
		}
	
		this.secretInputCriterion = usesScretInput;
		this.id = id;
	}

	
	/** Return true if this criterion is to be used with secret input */
	public boolean isSecretInputCriterion() {
		return secretInputCriterion;		
	}

	/** Return the feedback string used for acceptable solution attempts */
	public String getAcceptanceFeedback() {
		return acceptanceFeedback;
	}
	
	/** Return the feedback string used for failed solution attempts */
	public String getFailureFeedback() {
		return failureFeedback;
	}
	
	/** Return the feedback string used for high quality solution attempts */
	public String getHighQualityFeedback() {
		return highQualityFeedback;
	}

	/** Return the identifier of this Criterion */
	public String getID(){
		return id;
	}
	
	/** Return a serialized copy of this Criterion in XML-format */
	public String serializeToXML() {
		return toXML("class", this.getClass().getName()) + // TODO: vaiko getCanonicalName() ? 
			   toXML("posfb", getAcceptanceFeedback()) +
			   toXML("negfb", getFailureFeedback()) +
			   toXML("hqfb", getHighQualityFeedback()) +
			   toXML("secret", isSecretInputCriterion()) + 
			   toXML("id", getID()) +
			   serializeSubClass();
	}


	/** Return true if this criterion has test for evaluating failure/success
	 * of the student's answer. Return of false means this criterion should
	 * NOT be used to test <code>passesAcceptanceTest(..)</code>. */
	public abstract boolean hasAcceptanceTest();

	
	/** Return true if this criterion has test for evaluating the quality
	 * of the student's answer. Return of false means this criterion should
	 * NOT be used to test <code>passesQualityTest(..)</code>. */
	public abstract boolean hasQualityTest();
	
	
	/** Return true if student's solution meets the passing requirement of this Criterion */
	public abstract boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer);

	
	/** Return true if student's solution meets the high-quality requirement of this criterion. */
	public abstract boolean passesQualityTest(TitoState studentAnswer, TitoState modelAnswer);
	
	
	/** Serialize non-static data-members of Criterion sub-class to XML format. The
	 * subclass can decide the names of its XML tags but they must not collide with
	 * the tags used by this class: "class", "posfb", "negfb",* "hqfb", "secret" 
	 * and "id". The abstract Criterion class will handle the serialization of its
	 * data-members, subclasses need to deserialize only the fields they add.
	 * 
	 * NOTE: aAssari DB imposes a 2000 char limit to stored strings so subclasses
	 * should try to keep the tags and data short (without being cryptic). */
	protected abstract String serializeSubClass();	
	
	
	/** Initialize non-static data-members of this Criterion subclass instance using
	 * the serialized representation returned by <code>serializeToXML()</code>. The
	 * data-member of the abstract Criterion class will have already been deserialized
	 * when this method is called. */
	protected abstract void initSubClass(String serializedXML);

	
	/** Instantiate new Criterion object using the serialized form Xml */
	public static Criterion deserializeFromXML(String xml)  {
		try {
			String criterionClass = parseXMLString(xml, "class");
			Class concreteClass = Class.forName(criterionClass);
			if (concreteClass != null) {
				Criterion c = (Criterion) concreteClass.newInstance();
				c.acceptanceFeedback = parseXMLString(xml, "posfb");
				c.failureFeedback = parseXMLString(xml, "negfb");
				c.highQualityFeedback = parseXMLString(xml, "hqfb");
				c.secretInputCriterion = parseXMLBoolean(xml, "secret");
				c.id = parseXMLString(xml, "id");
				c.initSubClass(xml);
				return c;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
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
	
	/** Serialize long value to XML string. Helper function for serializeSubClass() */
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
		return value.indexOf(0) == 'T';
	}
	
	/** Deserialize long value from XML string. Helper function for initSubClass() */
	protected static long parseXMLLong(String XML, String tagname) {
		String value = parseXMLString(XML, tagname);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return UNDEFINED;
		}
	}	




}