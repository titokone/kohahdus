
/*
 * TrainerParameters.java
 *
 * Created on 30. tammikuuta 2004, 8:38
 */

package fi.helsinki.cs.kohahdus.trainer;
/**
 *
 * @author  laine
 * @version 
 */

import javax.servlet.*;
import javax.servlet.http.*;

public class TrainerParameters extends Object {

   String parameterErrors;
   String language="EN";       //optional
   String courseID;            //must
   String moduleID;            //must
   String taskSNo;             //must
   int taskNo;                 //must
   String check;               //optional
   String studentID;           //optional
   String answer[];            //optional
   String params;              //optional
   String fb;                  //optional
   String store;
   String amust;
   String idmode; 
   int storemode=0;      
    /** Creates new TrainerParameters */
    public TrainerParameters(HttpServletRequest req) {
    
        // get the parameters from request
        parameterErrors=null;
        if (req.getParameter("lang") != null)          // use default if not specified
           language= req.getParameter("lang");
       // studentID= req.getParameter("sid");       
        taskSNo= req.getParameter("tno");          // task sequence number within module *
        courseID =  req.getParameter("cid");       // course identifier *
        moduleID = req.getParameter("mid");        // module identifier *
        params= req.getParameter("par");           // task specific parameters XML
        answer= req.getParameterValues("answer");  // answer array
        check= req.getParameter("check");          // check override deny request: value 'denied'
                                                   // missing: use task definition
        store= req.getParameter("store");          // storage mode override
                                                   // 0=store nothing
                                                   // 1=register try, don't store answer
                                                   // 2=register try and store answer
                                                   // missing: use task definition
        fb= req.getParameter("fb");                // previous feedback
        amust=req.getParameter("amust");           // answer possibility override
                                                   // missing: use task definition
        idmode=req.getParameter("idmode");           // ANON anonymousmode
                                                   // IDREQ should be known
                                                   // null: = ANON        
        // check 
        if (courseID==null) { 
           parameterErrors="NOCOURSEID";
           return;
        }   
        if (moduleID==null) {
           parameterErrors="NOCONTEXTID";
           return;
        }   
        if (taskSNo==null) {
           parameterErrors="NOTASKID";
           return;
        }   
        try {
           taskNo= Integer.parseInt(taskSNo);
        } 
        catch (NumberFormatException nfe) {
           parameterErrors="TASKIDERROR";
           return;
        }
        storemode=0;
        if (store!=null)
            if (store.equals("1"))
                storemode=1;
            else
                if (store.equals("2"))
                   storemode=2;
        
    }
    
    public String getFullTaskID() {
        return courseID+'.'+moduleID+'.'+taskSNo;
    }    
    
    public int getTaskNo() {
        return taskNo;
    }    
    
    public String getCourseID() {
        return courseID;
    }    
    
    public String getModuleID() {
        return moduleID;
    }    
       
    public String [] getAnswer() {
        return answer;
    }    
    
    public String getParams() {
        return params;
    }    
    
    public String getLanguage() {
        return language;
    }    
    
    public String getParameterErrors() {
        return parameterErrors;
    }    
    
    public String getFb() {
        return fb;
    }
  
    public boolean isFaulty() { 
        return parameterErrors!=null;
    } 
    
    public String getHiddens() {
       StringBuffer hiddens =new StringBuffer();
       hiddens.append("<input type=\"HIDDEN\" name=\"lang\" value=\"");
       hiddens.append(language);
       hiddens.append("\">");
       //if (studentID!=null) {
       //   hiddens.append("<input type=\"HIDDEN\" name=\"sid\" value=\"");
       //   hiddens.append(studentID);
       //   hiddens.append("\">");
       //}   
       hiddens.append("<input type=\"HIDDEN\" name=\"cid\" value=\"");
       hiddens.append(courseID);
       hiddens.append("\">");
       hiddens.append("<input type=\"HIDDEN\" name=\"mid\" value=\"");
       hiddens.append(moduleID);
       hiddens.append("\">");
       hiddens.append("<input type=\"HIDDEN\" name=\"tno\" value=\"");
       hiddens.append(taskSNo);
       hiddens.append("\">");
       return hiddens.toString();
   }   
 
public boolean analysisDenied() {
    if (check==null)
        return false;
    else
        if (check.equals("denied"))
           return true;
        else
           return false;
}    
public boolean stateRegistrationDenied() {
    if (storemode==0)
        return true;
    else
        return false;
}    
public boolean answerStorageDenied() {
    if (storemode<2)
        return true;
    else
        return false;
}    

public boolean answerRequired() {
    if (amust==null)
        return false;
    else
        if (amust.equals("yes"))
           return true;
        else
           return false;
}    
public boolean registrationNeeded() {
    if (idmode==null)
        return false;
    else 
        if (idmode.equals("ANON"))
           return false;
        else 
           return true;
    
}

}
