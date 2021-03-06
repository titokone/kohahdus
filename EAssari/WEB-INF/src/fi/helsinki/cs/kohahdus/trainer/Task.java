package fi.helsinki.cs.kohahdus.trainer;

import java.util.Date;

import fi.helsinki.cs.kohahdus.Log;

//import fi.helsinki.cs.kohahdus.Criterion;
/**
*
* @author  jari
*/
//Pit�isi olla kaikki mit� suunnitteludokumentissa pyydet��n.
//Muutoksien j�lkeen pit�isi kutsua metodeja setAuthor(author)
//ja setModificationDate().

// Laajennetaan olemassa olevaa Task luokkaa
public class Task {
	public static final String TYPE_FULL = "programming";
	public static final String TYPE_FILL = "fill-in";
	
	private String taskID;
	private String language;
	private String taskName;
	private String author;
	private String description;
	private String pinput;
	private String sinput;
	private String modelAnswer;
	private String category;
	private String taskType;
	private String fillInPreCode;
	private String fillInPostCode;
	private int maxInstructions;
	private boolean useModel;
	
	private String passFeedback;
	private String failFeedback;
	
	private boolean hasSucceeded;		// If the student has completed the task successfully
	private int noOfTries;			// How many times student has tried to complete the task
	
	private Date modificationdate;
    
    private String courseID;
    private boolean shouldStore;
    private boolean shouldRegister;
    private boolean shouldKnow;
    private boolean shouldEvaluate;
    private int cutoffvalue;
	
	
//CONSTRUCTORS
	/** Creates a new instance of Task */
	public Task(){
		this.modificationdate=new Date();
		this.hasSucceeded=false;
		this.noOfTries=0;
	}
	
	/** Creates a new instance of Task*/
	public Task(String taskID){
		this.taskID=taskID;
		this.modificationdate=new Date();
		this.hasSucceeded=false;
		this.noOfTries=0;
	}
	
	/** Creates a new instance of Task */
	public Task(String language, String taskname, String auth, String desc,
			String pinput, String sinput,
			String modelanswer, String categ, String type,
			String fillInPre, String fillInPost, boolean useModel,
			boolean succeed, int tries, String passFeedBack, String failFeedBack) {
		if (language == null || !(language.equalsIgnoreCase("EN") || language.equalsIgnoreCase("FI"))) {
			throw new IllegalArgumentException("Language param "+language);
		}
		this.language=language.toUpperCase();
		this.taskName=taskname;
		this.author=auth;
		this.description=desc;
		this.pinput=pinput;
		this.sinput=sinput;
		this.modelAnswer=modelanswer;
		this.category=categ;
		if (!(type.equalsIgnoreCase(TYPE_FULL) || type.equalsIgnoreCase(TYPE_FILL))) {
			throw new IllegalArgumentException("Given category "+type+" not valid");
		}
		this.taskType=type;
		this.fillInPreCode=fillInPre;
		this.fillInPostCode=fillInPost;
		this.useModel=useModel;
		this.modificationdate=new Date();
		this.hasSucceeded=succeed;
		this.noOfTries=tries;
		this.passFeedback=passFeedBack;
		this.failFeedback=failFeedBack;
	}
	
	
//SET-METHODS
	/** Set ID for this task */
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	
	/** Set the preferred language of this user. The language is either "EN" or "FI
	 * @param lang either "FI" or "EN"
	 */
	public void setLanguage(String lang) {
		if (lang == null || !(lang.equalsIgnoreCase("EN") || lang.equalsIgnoreCase("FI"))) {
			throw new IllegalArgumentException("Language param "+lang);
		}
		this.language = lang.toUpperCase();
	}
	
	/** Set the name of this task */
	public void setName(String name) {
		this.taskName=name;
	}
	
	/** Set "last task modification by" attribute to Name,
	 *  set last-modification-timestamp to current data and time */
	public void setAuthor(String name) {
		this.author=name;
	}
	
	/** Set description (teht�v�nanto) of this task */
	public void setDescription(String desc) {
		this.description=desc;
	}
	
	/** Set the public input as String of this task */
	public void setPublicInput(String input) {
		this.pinput=input;
	}
	
	/** Set the secret input as String of this task */
	public void setSecretInput(String input) {
		this.sinput=input;
	}
	
	/** Set model answer code */
	public void setModelAnswer(String code) {
		this.modelAnswer=code;
	}
	
	/** Set task category of this task */
	public void setCategory(String categ) {
		this.category=categ;
	}
	
	/** Set task type of this task */
	public void setTitoTaskType(String type) {
		if (type == null || !(type.equalsIgnoreCase(TYPE_FULL) || type.equalsIgnoreCase(TYPE_FILL))) {
			throw new IllegalArgumentException("Given task type "+type+" not valid");
		}
		this.taskType=type.toLowerCase();
	}
	
	/** Set the code that is prepended before student's code in a fill-in task */
	public void setFillInPreCode(String code) {
		this.fillInPreCode=code;
	}
	
	/** Set the code that is appended to student's code in a fill-in task */
	public void setFillInPostCode(String code) {
		this.fillInPostCode=code;
	}
	
	/** Set this task as fill-in or create-full-program  */
	public void setFillInTask(boolean fillIn) {
		if (fillIn)
			this.taskType=TYPE_FILL;
	}
	
	/** Sets number of tries for student */
	public void setNoOfTries(int noOfTries) {
		if (noOfTries<0) {
			throw new IllegalArgumentException("Given number of tries "+noOfTries+" not valid");
		}
		this.noOfTries = noOfTries;
	}
	
	/** Set the validation method of this task */
	public void setValidateByModel(boolean useModel){
		this.useModel=useModel;
	}
	
	/** Set the date and time this task was last modified */
	public void setModificationDate() {
		this.modificationdate=new Date();  //sets the current date and time
	}
	
	public void setModificationDate(Date date) {
		this.modificationdate = date;
	}
	
	/** Set the hasSucceeded true or false depending the student has passed the task */
	public void setHasSucceeded(boolean hasSucceeded) {
		this.hasSucceeded = hasSucceeded;
	}    
    
	/** Set return feedback if student passes the task */
	public void setPassFeedBack(String pass) {
		this.passFeedback=pass;
	}
	
	/** Set return feedback if student fails the task */
	public void setFailFeedBack(String fail) {
		this.failFeedback=fail;
	}
	
	/** Endless loop prevention */
	public void setMaximumNumberOfInstructions(int num) {
		this.maxInstructions = num;
	}
	
	/** Endless loop prevention */
	public void setMaximumNumberOfInstructions(String num) {
		try {
			this.maxInstructions = Integer.parseInt(num);
		} catch (NumberFormatException e) {
			this.maxInstructions = 0;
		}
	}
	
//GET-METHODS
	/** Return id of this task */
	public String getTaskID() {
		return taskID;
	}
	
	/** Return the language of this task */
	public String getLanguage() {
		return language;
	}
	
	/** Return the name of this task */
	public String getName() {
		return taskName;
	}
	
	/** Return name of the last person who has modified this task */
	public String getAuthor() {
		return author;
	}
	
	/** Return the description (teht�v�nanto) of this task */
	public String getDescription() {
		return description;
	}
	
	/** Return the public input as String of this task */
	public String getPublicInput() {
		return pinput;
	}
	
	/** Return the secret input as String of this task */
	public String getSecretInput() {
		return sinput;
	}
	
	/** Return code of the model answer provided by teacher */
	public String getModelAnswer() {
		return modelAnswer;
	}
	
	/** Return the task category of this task */
	public String getCategory() {
		return category;
	}
	
	/** Return the task type of this task */
	public String getTitoTaskType() {
		return taskType;
	}
	
	/** Return the code that is prepended before student's code in a fill-in task */
	public String getFillInPreCode() {
		return fillInPreCode;
	}
	
	/** Return the code that is appended to student's code in a fill-in task */
	public String getFillInPostCode() {
		return fillInPostCode;
	}
	
	/** Return the number of tries student has tried to make this task */
	public int getNoOfTries() {
		return noOfTries;
	}
	
	/** Return true if this is a fill-in task */
	public boolean isFillInTask() {
		return TYPE_FILL.equals(taskType);
	}
	
	/** Return true if this is a programming task */
	public boolean isProgrammingTask() {
		return TYPE_FULL.equals(taskType);
	}

	/** Return true if this task is to be validated by comparing results of the student's answer
	 * to results of teacher's answer */
	public boolean isValidateByModel() {
		return useModel;
	}
	

	
	/** Return the date and time this task was last modified */
	public Date getModificationDate() {
		return modificationdate;
	}
	
	/** Return true if student has made the task, false if she/he hasn't  */
	public boolean isHasSucceeded() {
		return hasSucceeded;
	}
	
	/** Return feedback if student passes the task */
	public String getPassFeedBack() {
		return passFeedback;
	}
	
	/** Return feedback if student fails the task */
	public String getFailFeedBack() {
		return failFeedback;
	}
	
	/** Returns loop prevention value */
	public int getMaximumNumberOfInstructions() {
		return maxInstructions;
	}
	
//OTHER METHODS
	/** Returns String of the date and time */
	public String getDateAsString() {
		return modificationdate.toString();
	}
	
	/** Deserialize String value from XML string. Helper function for initSubClass() */
	protected static String parseXMLString(String XML, String tagname) {
		if (XML == null) return "";
		int begin = XML.indexOf("<" + tagname + ">") + tagname.length() + 2;
		int end   = XML.indexOf("</" + tagname + ">");
		
		if (begin < 0 || end < 0) {
			Log.write("Task: failed to parseXML tag "+tagname);
			return "";
		}
		
		String value = XML.substring(begin, end);
		value = value.replaceAll("&gt;", ">");
		value = value.replaceAll("&lt;", "<");
		value = value.replaceAll("&amp;", "&");
		return value;
	}
	
	/** Deserialize boolean value from XML string. Helper function for initSubClass() */
	protected static boolean parseXMLBoolean(String XML, String tagname) {
		String value = parseXMLString(XML, tagname);
		if (value == null) return false;
		return value.equals("T");
	}
	
	/** Deserialize int value from XML string. Helper function for initSubClass()
	 * @return value or 0 */
	protected static int parseXMLint(String XML, String tagname) {
		String value = parseXMLString(XML, tagname);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/** Serialize long value to XML string. Helper function for serializeSubClass() */
	protected static String toXML(String tagname, int value) {
		String xml = "<" + tagname + ">";
		xml = xml + value;
		return xml + "</" + tagname + ">";
	}
	
	/** Serialize String value to XML string. Helper function for serializeSubClass() */
	protected static String toXML(String tagname, String value) {
		if (value == null) value = "";
		value = value.replaceAll("&", "&amp;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("<", "&lt;");
		return "<" + tagname + ">" + value + "</" + tagname + ">";
	}
	
	/** Serialize boolean value to XML string. Helper function for serializeSubClass() */
	protected static String toXML(String tagname, boolean value) {
		return "<" + tagname + ">" + (value ? "T" : "F") + "</" + tagname + ">";
	}
	
	/** Return a serialized this Task class into XML-format */
	public String serializeToXML() {
		return toXML("language", language) +
			   toXML("description", description) +
			   toXML("pinput", pinput) +
			   toXML("sinput", sinput) +
			   toXML("modelAnswer", modelAnswer) +
			   toXML("category", category) +
			   toXML("taskType", taskType) +
			   toXML("fillInPreCode", fillInPreCode) +
			   toXML("fillInPostCode", fillInPostCode) +
			   toXML("useModel", useModel) +
			   toXML("passFeedback", passFeedback) +
			   toXML("failFeedback", failFeedback) +
			   toXML("maxInstructions", maxInstructions);
	}

	
	/** Instantiate the Task class's parameters using the serialized form XML.
	 * @throws RuntimeException exceptions in the deserialization are
	 * caught and rethrown as uncheckced exception. */
	public void deserializeFromXML(String xml)  {
		if (xml == null) return;
		try {
			language = parseXMLString(xml, "language");
			description = parseXMLString(xml, "description");
			pinput = parseXMLString(xml, "pinput");
			sinput = parseXMLString(xml, "sinput");
			modelAnswer = parseXMLString(xml, "modelAnswer");
			category = parseXMLString(xml, "category");
			taskType = parseXMLString(xml, "taskType");
			fillInPreCode = parseXMLString(xml, "fillInPreCode");
			fillInPostCode = parseXMLString(xml, "fillInPostCode");
			useModel = parseXMLBoolean(xml, "useModel");
			passFeedback = parseXMLString(xml, "passFeedback");
			failFeedback = parseXMLString(xml, "failFeedback");
			maxInstructions = parseXMLint(xml, "maxInstructions");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Returns String from the task */
	public String toString(){
		String ret = "id:"+ taskID;
		ret += ", lang:" +language;
		ret += ", name:" +taskName;
		ret += ", author:" +author;
		ret += ", pubin:" +pinput;
		ret += ", secin:" +sinput;
		ret += ", useModel:" +useModel;
		ret += ", modelAnswer:" +modelAnswer;
		ret += ", category:" +category;
		ret += ", type:" +taskType;
		ret += ", succeeded:" +hasSucceeded;
		ret += ", noOfTries:" +noOfTries;
		ret += ", modDate:" +modificationdate;
		return ret;
	}

    public boolean shouldBeAnalysed() {
        return shouldEvaluate;
    }    
    public boolean shouldRegisterTry() {
    	return shouldRegister;
    }
    public boolean shouldStoreAnswer() {
        return shouldStore;
    }
    public int getCutoffvalue() {
        return cutoffvalue;
    }    

    public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public boolean isShouldEvaluate() {
		return shouldEvaluate;
	}

	public void setShouldEvaluate(boolean shouldEvaluate) {
		this.shouldEvaluate = shouldEvaluate;
	}

	public boolean isShouldKnow() {
		return shouldKnow;
	}

	public void setShouldKnow(boolean shouldKnow) {
		this.shouldKnow = shouldKnow;
	}

	public boolean isShouldRegister() {
		return shouldRegister;
	}

	public void setShouldRegister(boolean shouldRegister) {
		this.shouldRegister = shouldRegister;
	}

	public boolean isShouldStore() {
		return shouldStore;
	}

	public void setShouldStore(boolean shouldStore) {
		this.shouldStore = shouldStore;
	}

	public void setCutoffvalue(int cutoffvalue) {
		this.cutoffvalue = cutoffvalue;
	}


}
