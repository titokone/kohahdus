package fi.helsinki.cs.kohahdus.trainer;

import java.sql.Timestamp;
import java.util.*;

import fi.helsinki.cs.kohahdus.Criterion;

// Laajennetaan olemassa olevaa Task luokkaa
public class Task {
	
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
	

	
	

	
// ***** Old code begins here *****	

	
    String taskID;
    String courseID;
    String moduleID;
    String tasktypeID;
    Tasktype tasktype;
    int seqNo;
    Timestamp deadLine;
    boolean shouldStore;
    boolean shouldRegister;
    boolean shouldKnow;
    boolean shouldEvaluate;
    int cutoffvalue;
    int noOfTries;
	String style;	
	
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
    
	
	

}
