package fi.helsinki.cs.kohahdus.trainer;

import java.sql.Timestamp;
import java.util.*;

import fi.helsinki.cs.kohahdus.Criterion;
/**
*
* @author  jari
*/
//Pitäisi olla kaikki mitä suunnitteludokumentissa pyydetään.
//Muutoksien jälkeen pitäisi kutsua metodeja setAuthor(author)
//ja setModificationDate().

// Laajennetaan olemassa olevaa Task luokkaa
public class Task {
	public static final String TYPE_FULL = "programming";
	public static final String TYPE_FILL = "fill-in";
	
	private String taskName;
	private String author;
	private String description;
	private String modelAnswer;
	private String category;
	private String taskType;
	private String fillInPreCode;
	private String fillInPostCode;
	private boolean useModel;
	
	private Date modificationdate;
    
	
//CONSTRUCTORS
	/** Creates a new instance of Task */
	public Task(){
		this.modificationdate=new Date();
	}
	
	/** Creates a new instance of Task */
	public Task(String taskname, String auth, String desc,
			String modelanswer, String categ, String type,
			String fillInPre, String fillInPost, boolean useModel) {
		this.taskName=taskname;
		this.author=auth;
		this.description=desc;
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
	}
	
	
//SET-METHODS
	/** Set the name of this task */
	public void setName(String name) {
		this.taskName=name;
	}
	
	/** Set "last task modification by" attribute to Name,
	 *  set last-modification-timestamp to current data and time */
	public void setAuthor(String name) {
		this.author=name;
	}
	
	/** Set description (tehtävänanto) of this task */
	public void setDescription(String desc) {
		this.description=desc;
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
	public void setTaskType(String type) {
		if (type == null || !(type.equalsIgnoreCase(TYPE_FULL) || type.equalsIgnoreCase(TYPE_FILL))) {
			throw new IllegalArgumentException("Given category "+type+" not valid");
		}
		this.taskType=type;
	}
	
	/** Set the code that is prepended before student's code in a fill-in task */
	public void SetFillInPreCode(String code) {
		this.fillInPreCode=code;
	}
	
	/** Set the code that is appended to student's code in a fill-in task */
	public void SetFillInPostCode(String code) {
		this.fillInPostCode=code;
	}
	
	/** Set this task as fill-in or create-full-program  */
	public void setFillInTask(boolean fillIn) {
		if (fillIn)
			this.taskType=TYPE_FILL;
	}
	
	/** Set the validation method of this task */
	public void setValidateByModel(boolean useModel){
		this.useModel=useModel;
	}
	
	/** Set the date and time this task was last modified */
	public void setModificationDate() {
		this.modificationdate=new Date();  //sets the current date and time
	}
	
	
//GET-METHODS
	/** Return the name of this task */
	public String getName() {
		return taskName;
	}
	
	/** Return name of the last person who has modified this task */
	public String getAuthor() {
		return author;
	}
	
	/** Return the description (tehtävänanto) of this task */
	public String getDescription() {
		return description;
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
	public String getTaskType() {
		return taskType;
	}
	//sama metodi periaatteessa, mitä halutaan?
	/** Return tasktype as a String (fill-in or programming) */
	public String getTaskTypeString() {
		if (taskType==TYPE_FULL) return "programming";
		if (taskType==TYPE_FILL) return "fill-in";
		return "not specified";
	}
	
	/** Return the code that is prepended before student's code in a fill-in task */
	public String getFillInPreCode() {
		return fillInPreCode;
	}
	
	/** Return the code that is appended to student's code in a fill-in task */
	public String getFillInPostCode() {
		return fillInPostCode;
	}
	
	/** Return true if this is a fill-in task */
	public boolean isFillInTask() {
		return taskType==TYPE_FILL;
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
	
//OTHER METHODS
	/** Returns String of the date and time */
	public String getDateAsString() {
		return modificationdate.toString();
	}
	
	

	
	
	
//	 **** NÄMÄ AINAKIN OVAT VANHASTA EASSARISTA *****	
	private String taskID;
    private String courseID;
    private String moduleID;
    private String tasktypeID;
    private String metadata;
    private Tasktype tasktype;
    private int seqNo;
    private Timestamp deadLine;
    private boolean shouldStore;
    private boolean shouldRegister;
    private boolean shouldKnow;
    private boolean shouldEvaluate;
    private int cutoffvalue;
    private int noOfTries;
    private String style;	
	
	
	public Task(String taskid, String courseid, String moduleid, String tasktypeid, int seqno, Timestamp deadline,
				   boolean shouldstore, boolean shouldregister, boolean shouldknowstudent, boolean shouldevaluate, int cvalue,
				   int nooftries, Tasktype tType) {
		
		taskID = taskid;
		courseID = courseid;
		moduleID = moduleid;
		tasktypeID = tasktypeid;
		seqNo = seqno;
		deadLine = deadline;
		shouldStore = shouldstore;
		shouldRegister = shouldregister;
		shouldKnow = shouldknowstudent;
		shouldEvaluate = shouldevaluate;
		noOfTries = nooftries;
		cutoffvalue = cvalue;
		tasktype = tType;
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
    public Tasktype getTasktype() {
        return tasktype;
    }    
    public AnalyserInterface getAnalyser(String language) {
        AnalyserInterface an = tasktype.getAnalyser(language,taskID);
        return an;
    }    
    public boolean wasSuccess(int points) {
        return points>=cutoffvalue;
    }
    public boolean isInTime(Timestamp when) {
        return deadLine.after(when);
    }
    public boolean shouldAllowRetry(int triesSoFar) {
        return noOfTries>triesSoFar;
     }
    public DisplayerInterface getDisplayer(String language) {
        DisplayerInterface disp = tasktype.getDisplayer(language,taskID);
        return disp;
    }    
    public int getCutoffvalue() {
        return cutoffvalue;
    }    
    public String getStyle() {
        return tasktype.getStyle();
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

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public void setCutoffvalue(int cutoffvalue) {
		this.cutoffvalue = cutoffvalue;
	}

	public void setTasktype(Tasktype tasktype) {
		this.tasktype = tasktype;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public int getNoOfTries() {
		return noOfTries;
	}

	public void setNoOfTries(int noOfTries) {
		this.noOfTries = noOfTries;
	}    
    
	
	

}
