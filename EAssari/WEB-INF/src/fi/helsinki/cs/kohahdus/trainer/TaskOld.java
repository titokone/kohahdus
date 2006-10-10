package fi.helsinki.cs.kohahdus.trainer;

import java.util.Date;
import java.sql.*;

//---------------------------------------------
// Course specific use of a learning object
//
public class TaskOld {

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
    
    

  // analysis of the answer if necessary 
     
  public TaskOld (String taskid, String courseid, String moduleid, 
               String tasktypeid, int seqno, Timestamp deadline,
               boolean shouldstore, boolean shouldregister, boolean shouldknowstudent, 
               boolean shouldevaluate, int cvalue, int nooftries, Tasktype tType) {
       
    taskID= taskid;
    courseID= courseid;
    moduleID= moduleid;
    tasktypeID= tasktypeid;
    seqNo =seqno;
    deadLine= deadline;
    shouldStore = shouldstore;
    shouldRegister= shouldregister;
    shouldKnow= shouldknowstudent;
    shouldEvaluate = shouldevaluate;
    noOfTries= nooftries;
    cutoffvalue=cvalue;
    tasktype= tType;
  }  
  
  //public Feedback analyse(String [] answers, String language) {   
  //   if (shouldevaluate.equals("Y")) {  
  //      feedback= ilo.analyse(answers,language);   
  //   }
  

   //returns the scripts attached to the taskpage
   /*public String getScript(language) {
        String taskscript = null;
        taskscript= ilo.getScript(language);
        return taskscript;
   }
   */
  
   //returns the tasksetting 
  /*
   public String getSetting(language) {
        String setting = null;
        setting= ilo.getSetting(language);
        return setting;
   }
  */

  public DisplayerInterface getDisplayer(String language) {
      DisplayerInterface disp = tasktype.getDisplayer(language,taskID);
      return disp;
  }    
  public AnalyserInterface getAnalyser(String language) {
      AnalyserInterface an = tasktype.getAnalyser(language,taskID);
      return an;
  }    

  //returns true if further tries are allowed
   public boolean shouldAllowRetry(int triesSoFar) {
      return noOfTries>triesSoFar;
   }

   // returns true if the try should be registered otherwise false
   public boolean shouldRegisterTry() {
       return shouldRegister;
    }

    // returns true if the answer should be registered otherwise false
    public boolean shouldStoreAnswer() {
        return shouldStore;
    }
  
    public boolean shouldBeAnalysed() {
        return shouldEvaluate;
    }    

    public boolean shouldKnowStudent() {
        return shouldKnow;
    }    
        
    public String getTasktypeID () {
        return tasktypeID;
    }   
    
    public Tasktype getTasktype() {
        return tasktype;
    }    
    
    public String getTaskID() {
        return taskID;
    }  
    
    public int getCutoffvalue() {
        return cutoffvalue;
    }    
    
    public String getStyle() {
        return tasktype.getStyle();
    }    
    
    public boolean wasSuccess(int points) {
        return points>=cutoffvalue;
    }
    public boolean isInTime(Timestamp when) {
        return deadLine.after(when);
    }
    // 
}