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
	private String positiveFeedback = "";
	private String negativeFeedback = "";
	private boolean secretInputCriterion;
	private boolean passingCriterion;
	
	/** Empty constructor for deserialization */
	protected Criterion() {		
	}
	
	
	/** Initialize fields common to all Criterion types */
	public Criterion(String id, boolean usesScretInput, boolean mustPass, String positiveFeedback, String negativeFeedback) {
		if (id == null) {
			throw new NullPointerException("Criterion ID cannot be null");
		}		
		if (positiveFeedback != null) {
			this.positiveFeedback = positiveFeedback;
		}
		if (negativeFeedback != null) {
			this.negativeFeedback = negativeFeedback;
		}
		
		this.secretInputCriterion = usesScretInput;
		this.passingCriterion = mustPass;
		this.id = id;
	}

	
	/** Return true if this criterion is to be used with secret input */
	public boolean isSecretInputCriterion() {
		return secretInputCriterion;		
	}
	
	
	/** Return true if meeting this criterion mandatory for passing the task. Return of false
	 *  means this criterion does not affect the passing of the task, but may still be tested
	 *  with <code>meetsCriterion(..)</code> to evaluate the quality of the answer. */
	public boolean isPassingCriterion() {
		return passingCriterion;		
	}	

	
	/** Return the positive feedback string of this Criterion. If the positive feedback of an
	 * active task is an empty string, the student trying to solve a task will not given any
	 * positive feedback from this particular Criterion. */
	public String getPositiveFeedback() {
		return positiveFeedback;
	}

	
	/** Return the negative feedback string of this Criterion. Even if the negative feedback of
	 * an active task is an empty string, the student trying to solve a task should be given
	 * <i>some</i> indication of what part of the solution attempt failed. For example a canned
	 * error message: <code>crit.getClass().getName() + " failed (feedback: not defined)"</code> */
	public String getNegativeFeedback() {
		return negativeFeedback;
	
	}

	/** Return the identifier of this String */
	public String getID(){
		return id;
	}
	
	/** Return a serialized copy of this Criterion in XML-format */
	public String serializeToXML() {
		return toXML("class", this.getClass().getName()) + // TODO: vaiko getCanonicalName() ? 
			   toXML("posfb", getPositiveFeedback()) +
			   toXML("negfb", getNegativeFeedback()) +
			   toXML("secret", isSecretInputCriterion()) + 
			   toXML("pass", isPassingCriterion()) + 
			   toXML("id", getID()) +
			   serializeSubClass();
	}


	/** Return true if this Criterion is active. If Criterion is active the student's
	 * answer must be tested with the meetsCriterion(...) method. Conversly, if this
	 * method returns false, meetsCriterion(...) must not be called (result of such
	 * call is not defined. */
	public abstract boolean isActive();	

	
	/** Return true if student's answer meets the condition(s) of this criterion. */
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
			String criterionClass = parseXMLString(xml, "class");
			Class concreteClass = Class.forName(criterionClass);
			if (concreteClass != null) {
				Criterion c = (Criterion) concreteClass.newInstance();
				c.positiveFeedback = parseXMLString(xml, "posfb");
				c.negativeFeedback = parseXMLString(xml, "negfb");
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