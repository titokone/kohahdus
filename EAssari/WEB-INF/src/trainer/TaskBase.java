package trainer;

import java.lang.reflect.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.*;

/****
 * Taskbase manages the cache of tasks, taskbodies, and their attributes.
 * It also knows the the tasktype specific Analysers and  Displayers and 
 * loads them when necessary
 */

public class TaskBase implements AttributeCache {

    // static TaskBase INSTANCE;
    // static DBConnectionBroker taskBasepool; 
    
    Hashtable loadedAttributes;
    //Hashtable loadedAnalysers;
    //Hashtable loadedDisplayers;
    Hashtable loadedTasks;
    Hashtable loadedTasktypes;
    
    String dbDriver =null;
    String dbServer = null;
    String dbLogin = null;
    String dbPassword = null;
    // String helpURL=null;
    
    /** 
    *  Creates new TaskBase object, A single program may use many taskbases 
    */
    public TaskBase(String db_Driver, String db_Server, String db_Login, String db_Password) {
        dbDriver = db_Driver;
        dbServer = db_Server;
	dbLogin  = db_Login;
	dbPassword = db_Password;
        loadedAttributes = new Hashtable(1000);
    //    loadedAnalysers= new Hashtable(20);
    //    loadedDisplayers= new Hashtable(20);
        loadedTasktypes= new Hashtable(20);
        loadedTasks= new Hashtable(100);
        
	// int minConns   = Integer.parseInt((String) p.get("minConns"));
	// int maxConns   = Integer.parseInt((String) p.get("maxConns"));
	// String logFileString = (String) p.get("logFileString");
	// double maxConnTime   =
	//    (new Double((String)p.get("maxConnTime"))).doubleValue();
        //initialize repository 
        // Class.forName(dbDriver);
       //    helpURL= (String) p.get("helpURL");
        // dbURL=   (String) p.get("dbURL");
	// myBroker = new
	//	    DbConnectionBroker(dbDriver,dbServer,dbLogin,dbPassword,
	//		       minConns,maxConns,logFileString,maxConnTime);
    }
    

   /****
    *  Return the value of the the specified course, module or task attribute in a requested language.
    *  Also language independent values are returned.
    *  Returns null if the requested value is not available.
    *  If the attribute values in the language specified are not in cache they are loaded.
    *  
    */  
    
    public String getAttribute (String objType, String objID, String attributename, String language) throws
        CacheException {
        // are the attribute values in this language already loaded 
        
        String keyPrefixLang= objType+"."+objID+"."+language;
        String keyPrefixAll= objType+"."+objID+".ALL.";
        String key = keyPrefixLang+"."+attributename;
        String value= null;
        // StringBuffer v = new StringBuffer();
        // check if the values are loaded
        // check if objects's attributen in current language are in cache
        String loadStatus = (String) loadedAttributes.get(keyPrefixLang);
        if (loadStatus==null) {
           // values are not loaded yet, so load them
          // v.append(keyPrefixLang +" not loaded yet, load all<br>"); 
           loadStatus = loadAllAttributes(objType,objID, language); 
          // v.append(loadStatus);
          // v.append("br>");
        } 
        if (loadStatus=="LOADED") {
           // now it is loaded if it exists
           value = (String) loadedAttributes.get(key);
           if (value==null) {
              // there were no value in this language, but there might be one
              // common to all languages, load that
              // v.append(key + "not found<br>"); 
              String anyLangKey= keyPrefixAll+attributename;
              // try to find a common value for all languages
              value = (String) loadedAttributes.get(anyLangKey);
           }    
           //if (value !=null) 
           //    v.append(value);
           return value;
        }   
        else {
          throw new CacheException(loadStatus);
        }  
    }   
    
 /*
    
   public String getScript(String tasktype) { 
      // provides the javascript needed in processing the task form
      // of task iloId thet is of type tasktype
      
         return null;
      else {
         DisplayerInterface taskDisplayer= null;
         taskDisplayer = (DisplayerInterface) getPlugin(tasktype, loadedDisplayers, "DISPLAYER");
         String script=null;
         if (taskDisplayer!=null)
            taskDisplayer.registerCache(this); 
      net   script= taskDisplayer.getScript(language);
         return script;
      } 
         
   }


   public String getSetting (String taskID, String tasktype, String language) {
      String body= "<b>Task "+ taskID + " of type "+tasktype + " in language " + language +" requested</b>";   
           DisplayerInterface taskDisplayer=null;
           taskDisplayer= (DisplayerInterface) getPlugin(tasktype, loadedDisplayers, "DISPLAYER");
           String body=null;
           if (taskDisplayer!=null)
              taskDisplayer.registerCache(this);
           body= taskDisplayer.getSetting(taskID,language);
    
     return body;
     
       
   }
  */  
 /*  public Task taskByIdTest (String taskID, String courseID, String contextID) {
       return new Task(taskID, courseID, contextID, "TEST", 1, null, "N","N","N",-1);
   }    
 */ 
   
   /** 
    * Returns the module specific and general information about the task.
    * First referenve to the task creates the object and attaches it to the task pool
    */
   
   public synchronized Task taskById  (int seqNo, String courseID, String moduleID) throws CacheException
     {
       Task task=null;
       String fullTaskID= courseID+"."+moduleID+"."+ seqNo;
       
       // full id consists of courseid, moduleid and task sequence number
       
       task= (Task) loadedTasks.get(fullTaskID);
       
       if (task==null) {  
             // the task has not been loaded yet
          String query= "select taskinmodule.taskid, tasktype, numberoftries, shouldstoreanswer, "+
                     " shouldregistertry, shouldknowstudent, shouldevaluate, iscreditable, creditsupto, "+
                     "taskinmodule.cutoffvalue, "+
                     " displayer, displayerinit, analyser, analyserinit, tasktypestyle " + 
                     "from module, task, taskinmodule,tasktype "+
                     "where taskinmodule.taskid= task.taskid and "+
                     "module.courseid=taskinmodule.courseid and "+ 
                     "module.moduleid=taskinmodule.moduleid and "+
                     "tasktype.typename=task.tasktype and "+
                     "taskinmodule.seqno="+seqNo +" and taskinmodule.courseid= '" +courseID+ 
                        "' and taskinmodule.moduleid= '"+moduleID+"'"; 
       
          Connection con = getConnection();
          Statement stm=null;
          ResultSet rs=null;
          try {                                
                 stm= con.createStatement();
                 rs= stm.executeQuery(query);
                 if (rs.next()) {
                    String tid= rs.getString("taskid"); 
                    String tty=rs.getString("tasktype");      
                    int  tries= rs.getInt("numberoftries");
                    String shouldreg= rs.getString("shouldregistertry");
                    String shouldstore= rs.getString("shouldstoreanswer");
                    String shouldknowstudent=rs.getString("shouldknowstudent");
                    String shouldevaluate=rs.getString("shouldevaluate");
                    int cvalue= rs.getInt("cutoffvalue");
                    boolean breg=false;
                    boolean bstore=false;
                    boolean bknow=false;
                    boolean beval=false;
                    if (shouldreg!=null && shouldreg.equals("Y"))
                        breg=true;
                    if (shouldstore!=null && shouldstore.equals("Y"))
                        bstore=true;
                    if (shouldknowstudent!=null && shouldknowstudent.equals("Y"))
                        bknow=true;
                    if (shouldevaluate!=null && shouldevaluate.equals("Y"))
                        beval=true;
                    String isC= rs.getString("iscreditable");
                    Timestamp creditsupto= rs.getTimestamp("creditsupto");
                    Tasktype tType = (Tasktype) loadedTasktypes.get(tty);
                    
                    if (tType==null) {
                        // tasktype has not been loaded 
                        String displayer= rs.getString("displayer");
                        String displayerinit= rs.getString("displayerinit");
                        String analyser= rs.getString("analyser");
                        String analyserinit= rs.getString("analyserinit");
                        String mStyle= rs.getString("tasktypestyle");
                        tType= new Tasktype (tty, displayer, displayerinit, analyser, analyserinit, mStyle);
                        loadedTasktypes.put(tty,tType);
                    }
                    task= new Task(tid, courseID, moduleID, tty, seqNo, creditsupto,
                           bstore, breg, bknow, beval, cvalue, tries, tType);
                    loadedTasks.put(fullTaskID, task);
                 }
                 else {
                    task=null;
                 } 
             } 
             catch (SQLException ex) {
                   throw new CacheException ("Repository failure: "+ex.getMessage()); 
             }
             finally {    
                try {
                   if (rs!=null)
                      rs.close();
                   if (stm!=null)
                      stm.close();
                   con.close();
                }    
                catch (SQLException sex) {}
             }             
       }    
       return task;
    }     
   
   public boolean checkCourseAccess(String userid, String courseid) {
       //here should be a query to test the access privilege
       return true;
   }
   
    /****
     * Establishes a database connection according to the parameters
     * loaded from configuration file
     */
    
  private Connection getConnection () throws CacheException {
        // load database driver if not already loaded
        Connection conn= null;
        try { 
          Class.forName(dbDriver);               // load driver
          //  Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) { 
             throw new CacheException ("Couldn't find the driver "+dbDriver);
        }
        try {
           conn= DriverManager.getConnection(dbServer, dbLogin, dbPassword);
        } catch (SQLException sex) {
           throw new CacheException("Couldn't establish repository connection. ");
        }
        return conn;
   }

    
 

   /****
    * Loads object's attribute values in a given language and also values common to all
    * languages
    */
          
    private synchronized String loadAllAttributes(String objType, String objID, String language)
    throws CacheException {    
       // create connection to taskdb
       String fb= null;
       // Feedback fx= new Feedback();
       Connection con = getConnection();
       // retrieve all taskattribues in this language
       if (con==null) {
           fb="FAILED no connection";
           return fb;
       }    
           
       String attQuery= "select attributename, attributevalue, valuetype,language "+
                        "from attributevalues "+
                        "where objecttype='"+objType+ 
                        "' and objectid='"+objID+"' and (language ='"+language+
                        "' or language='ALL')";
       Statement stm=null;
       ResultSet rs=null;
       boolean attributesExist =false;
       String keyPrefix= objType+"."+objID+".";
       String keyPrefixAllLang= objType+"."+objID+".ALL";
       // check if attribute values common to all languages have already been loaded
       boolean commonLoaded =false;
       String commonL= (String) loadedAttributes.get(keyPrefixAllLang);
       if (commonL!=null) {
           commonLoaded= commonL.equals("LOADED");
       }
       
       String bkey=keyPrefix+language;
         
       try {
           stm=con.createStatement();
           rs= stm.executeQuery(attQuery);
           while (rs.next()) { 
               attributesExist=true;
               String aName= rs.getString("ATTRIBUTENAME");
               String aValue= rs.getString("ATTRIBUTEVALUE");
               String aLang= rs.getString("LANGUAGE");
               if (aValue!=null){
                  String akey=keyPrefix+aLang+"."+aName; 
                  loadedAttributes.put(akey,aValue);
               }   
           }   
         //  if (attributesExist) {
              fb="LOADED";
              // mark language dependent values as loade
              loadedAttributes.put(bkey,fb);
              //
              if (!commonLoaded)
                 loadedAttributes.put(keyPrefixAllLang, fb);
           //}   
           //else {
           //   fb= "UNAVAILABLE";
           //   loadedAttributes.put(bkey,fb);
           //}   
       }        
       catch (SQLException ex) { 
          fb=  "Repository failure :"+ ex.getMessage();
          throw new CacheException(fb); 
       }  
       finally {
           try {
             if (rs!=null)
                rs.close();
             if (stm!=null)
                stm.close();
             con.close();
           }  
             catch (SQLException fex) {}      
       }
       return fb;
    } 
}
