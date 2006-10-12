package fi.helsinki.cs.kohahdus.trainer;

import java.sql.Timestamp;
import java.util.*;

import fi.helsinki.cs.kohahdus.Criterion;

// Laajennetaan olemassa olevaa Task luokkaa
public class Task {
	
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

	public Task(){
		
	}
	
	/** Return the name of this task */
	public String getName() {
		return null;
	}
	
	/** Set the name of this task */
	public void setName(String name) {
	}
	
	/** Return name of the last person who has modified this task */
	public String getAuthor() {
		return null;
	}

	/** Set "last task modification by" attribute to Name,
	 *  set last-modification-timestamp to current data and time */
	public void setAuthor(String name) {
	}
	
	/** Return the date and time this task was last modified */
	public Date getModificationDate() {
		return null;
	}
	
	
	/** Return code of the model answer provided by teacher */
	public String getModelAnswer() {
		return null;
	}
	
	/** Set model answer code */
	public void setModelAnswer(String code) {
		
	}
	
	/** Return true if this task is to be validated by comparing results of the student's answer
	 * to results of teacher's answer */
	public boolean isValidateByModel() {
		return false;
	}
	
	/** Set the validation method of this task */
	public void setValidateByModel(boolean useModel){
		
	}

	/** Return the task category of this task */
	public String getCategory() {
		return null;
	}
	
	/** Set task category of this task */
	public void setCategory(String category) {
		
	}
	
	/** Return tasktype as a String (fill-in or programming) */
	public String getTaskTypeString() {
		return null;
	}
	
	/** Return true if this is a fill-in task */
	public boolean isFillInTask() {
		return false;
	}
	
	/** Set this task as fill-in or create-full-program  */
	public void setFillInTask(boolean fillIn) {
		
	}

	/** Return the code that is prepended before student's code in a fill-in task */
	public String getFillInPreCode() {
		return null;
	}
	
	/** Set the code that is prepended before student's code in a fill-in task */
	public void SetFillInPreCode(String code) {
		
	}
	
	/** Return the code that is appended to student's code in a fill-in task */
	public String getFillInPostCode() {
		return null;
	}
	
	/** Set the code that is appended to student's code in a fill-in task */
	public void SetFillInPostCode(String code) {
		
	}

	/** Return the description (tehtävänanto) of this task */
	public String getDescription() {
		return null;
	}

	/** Set description (tehtävänanto) of this task */
	public void setDescription() {
		
	}
	
	/** Return tasks language ("FI" or "EN") */
	public String getLanguage() {
		return null;
	}
	
	
	

	
// ***** Old code begins here *****	

	
	
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
