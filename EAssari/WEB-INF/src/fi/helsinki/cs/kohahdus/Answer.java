package fi.helsinki.cs.kohahdus;

import java.io.*;
import javax.servlet.http.*;

import fi.helsinki.cs.kohahdus.trainer.AnalyserInterface;
import fi.helsinki.cs.kohahdus.trainer.CacheException;
import fi.helsinki.cs.kohahdus.trainer.DisplayerInterface;
import fi.helsinki.cs.kohahdus.trainer.Feedback;
import fi.helsinki.cs.kohahdus.trainer.Tasktype;
import fi.helsinki.cs.kohahdus.trainer.TrainerParameters;
import fi.helsinki.cs.kohahdus.trainer.TrainerServlet;

import java.util.*;
import java.sql.*;
import trainer.*;


// Author Harri Laine
public class Answer extends TrainerServlet {

public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {
    // Parameter ses informs whether session is required or not
    // Language, student id, taskid and course
    // are known by session is one exists.
    // They may also be delivered as request parameters
    //Session session= req.getSession(false);
    String language= "EN"; 
   // String student=  null; 
    int taskNo;
    String courseId= null;
    String contextId= null;
    String anon=null;
    
    
    //Task tsk= null;
    //Course course = null;
    
    String errorCode=null;
    String eMessage=" ";
    String fullTID=null; 
    String ems=null;
    int attempts=0;
    
    Feedback feedback= null;
    String taskscript =null;
    String taskbody= null;
    
    // parameters
    
    boolean shouldCheck=false;
    boolean shouldRegisterState=false;
    boolean shouldStoreAnswer=false;
    boolean allowTry=true;
    //prepare for output
    res.setContentType("text/html");
    PrintWriter out= res.getWriter();
   // boolean parameterErrors =false;

    // find out the task parameters and check them
    
    String anonymous= null;
    HttpSession session= null; 
    User student=null;
    boolean registrationNeeded=true;
    TrainerParameters trp= new TrainerParameters(req);
    fullTID= trp.getFullTaskID();
    taskNo=   trp.getTaskNo();
    courseId= trp.getCourseID();
    contextId= trp.getModuleID(); 
     // falty request parameters
     if (trp.isFaulty()) {
       try {
          ems= taskBase.getAttribute("E",trp.getParameterErrors(),"MESSAGE",language);
       }
        catch (CacheException cex) {
          ems = cex.getMessage();
       } 
       out.println(fatalErrorNotification(fullTID, ems));
       return;
    }
    
    registrationNeeded=trp.registrationNeeded();
    
  
    if (registrationNeeded) {  
       // Student must have logged on the course 
       // Studendt information is stored in an existing session object
       String sesCourse=null;
       String sesModule=null;
       boolean okToStudy=false;
       session= req.getSession(true);   
       if (!session.isNew()) {
          sesCourse= (String) session.getAttribute("courseid");
          sesModule= (String) session.getAttribute("moduleid");
          language= (String) session.getAttribute("lang");
          student= (User) session.getAttribute("user");
          //if course has changed check aceess rights
          if (sesCourse==null || !sesCourse.equals(courseId)) {
             //check access to this course
             if (student!=null) { 
               // try { 
                   okToStudy= taskBase.checkCourseAccess(student.getUserid(),courseId);
               // } catch (cacheException ce) {
               //    okToStudy=false;
               // } 
                          
                if (okToStudy) {
                   session.setAttribute("courseid",courseId);
                   session.setAttribute("moduleid",contextId);
                }
                else {
                   session.invalidate();
                   okToStudy=false;
                }
            }
            else {
                 session.invalidate();
                 okToStudy=false;
            }
         }
         else {
             if (student!=null)
                 okToStudy=true;     
             else {
                 session.invalidate();
                 okToStudy=false;
             }    
         }
       }    
       else {
           // new session redirect to Login
           session.setAttribute("courseid",courseId);
           session.setAttribute("moduleid",contextId);
           session.setMaxInactiveInterval(3600);
           StringBuffer target= req.getRequestURL(); 
           String par= req.getQueryString();
           if (par!=null) {
              target.append("?");
              target.append(par);
           }    
           // if get then  paste querystring
           session.setAttribute("originaltarget",target.toString());
           // here should be url construction if call is post 
           res.sendRedirect("Login");
           return;  
      }
      if (!okToStudy) {
         try {
           ems=taskBase.getAttribute("E","NOSESSION","MESSAGE",language);
         }  
         catch (CacheException c) { 
            ems= c.getMessage();
         }  
         out.println(fatalErrorNotification(fullTID, ems+':'+okToStudy));
         return;
      }    
    } 
    //if language is given as requirement parameter it overwrites
    //the one in session 
    
    if (trp.getLanguage()!=null)  
       language=trp.getLanguage();

    //there was a session but no student - impossible?
    //if (student==null && registrationNeeded) {
    //  try {  
    //     ems= taskBase.getAttribute("E","NOSTUDENT","MESSAGE",language); 
    //  }  
    //  catch (CacheException c) { 
    //    ems= c.getMessage();
    //  }     
    //  out.println(fatalErrorNotification(fullTID, ems));
    //  return;
    //}

    
     
  // parameters are OK and registration done if needed --  continue
   try {   
      Task tsk= taskBase.taskById(trp.getTaskNo(), trp.getCourseID(), trp.getModuleID());
      if (tsk==null) {         
         ems= taskBase.getAttribute("E","TASKUNKNOWN","MESSAGE",language);
         out.println(fatalErrorNotification(fullTID, ems));
         return;   
      }
      // get the analyser
      boolean wasAnalysed = false;
      boolean wassuccess=false;
      boolean wasintime=false;
      String[] ans = trp.getAnswer();
      String params= trp.getParams();
      Tasktype tpe=tsk.getTasktype();
      
      //    activation parameters override task parameters
      if (trp.analysisDenied())
         shouldCheck=false;
      else
         shouldCheck=tsk.shouldBeAnalysed();
      if (trp.stateRegistrationDenied()) 
          shouldRegisterState=false;
      else
          shouldRegisterState=tsk.shouldRegisterTry();
      if (trp.answerStorageDenied())
          shouldStoreAnswer=false;
      else
          shouldStoreAnswer= tsk.shouldStoreAnswer();
      int currentcredit=0;
      Calendar now=null;
      Timestamp when=null;
      if (shouldCheck) {
         AnalyserInterface ani= tsk.getAnalyser(language);
         if (ani==null) {
            ems= taskBase.getAttribute("E","NOANALYSER","MESSAGE",language)+ tpe.getAnalyserClass(); 
            out.println(fatalErrorNotification(fullTID, ems));
            return;
         }
         ani.registerCache(taskBase);      
         feedback= ani.analyse(ans,params);
         wasAnalysed= true;
         if (feedback.causedFatalError()) {
            ems= feedback.getMsgText();
            out.println(fatalErrorNotification(fullTID, ems));
            return;
         }
         currentcredit= feedback.getEvaluation();
         wassuccess= tsk.wasSuccess(currentcredit);  
         now=Calendar.getInstance();
         when= new Timestamp(now.getTimeInMillis());
         wasintime= tsk.isInTime(when);
      }
      else
         feedback= new Feedback(trp.getFb());
               
      // tallennetaan tila
      // hae nykytila
      // mihin p‰‰sty (nykytila, task.cutoffvalue, feedback.getvalue)
    
     
     
      if (student!=null && shouldRegisterState) {    
         if (wassuccess)   
            student.registerTry(fullTID, wassuccess, wasintime, currentcredit);
      }
      if (student!=null && shouldStoreAnswer){ 
         String done= storeStateAndAnswer(shouldStoreAnswer, student, 
                 courseId, contextId, taskNo, fullTID, 
                 ans, params, 
                 wassuccess, wasintime, currentcredit,
                 when, language, feedback);
         if (done!=null) {
            out.println(done); 
            return;
         }
      }
      
      if (trp.answerRequired() || student==null)
         allowTry=true;
      else {
          attempts= student.getAttempts(fullTID);
          allowTry= tsk.shouldAllowRetry(attempts);
      }    
     
      // build the page
      // first show the task
       DisplayerInterface disp= tsk.getDisplayer(language);
       if (disp==null) {
          ems= taskBase.getAttribute("E","NODISPLAYER","MESSAGE",language); 
          out.println(fatalErrorNotification(fullTID, ems));
          return;
       } 
       disp.registerCache(taskBase);
       taskbody= disp.getSetting(ans, params, trp.getHiddens(),true);
       if (taskbody==null) {
           ems= taskBase.getAttribute("E","DISPLAYFAILED","MESSAGE",language);
           out.println(fatalErrorNotification(trp.getFullTaskID(), ems));
           return;
       }    
       out.println(pageHeader(fullTID, disp.getScript(), tsk.getStyle()));
       out.println(bodyBegin());
       out.println(taskbody);   
       if (wasAnalysed) {
           feedbackbox(tsk.getCutoffvalue(), out, feedback);
           String extra = feedback.getExtra();
           if (extra!=null) 
               out.println(extra);
       }    
       out.println(bodyEnd());
       out.println(footer());
   }    
   catch (CacheException c) {
      ems= c.getMessage();
      out.println(fatalErrorNotification(trp.getFullTaskID(), ems));
      return;
   }   
}

public void doPost (HttpServletRequest req, HttpServletResponse res) throws IOException {
   doPost(req,res);
}

private String storeStateAndAnswer  (boolean shouldStore, 
        User student, 
        String courseId, String moduleID, int taskNo, String fullTaskID,
        String [] answers,  String params, 
        boolean accepted, boolean intime, int credit,
        Timestamp when, String lang, Feedback feedback) throws CacheException
{
    String insertStatus=
            "insert into studentmodel values (?,?,?,?,?,1,?,?,?)";
    String updateStatus=
         "update studentmodel set "+
          "  lasttrynumber=?, "+
          "  currentresult=?, "+
          "  hassucceeded=?, "+
          "  wascreditedintime=? "+
          "  where studentid=? and" +
            "courseid=? and " +
            "moduleid=? and" +
            "seqno=?";
    String insertanswer= "insert into answer values (" +
            "?,?,?,?,?,?,?,?,?,?)";
     
    Connection connection=null;
    PreparedStatement ps1=null;
    PreparedStatement ps2=null;
    String done=null;

    
     try {
        connection =getConnection();
        if (connection!=null) {
            int answercount= student.getAttempts(fullTaskID);
            int cc=student.getCurrentCredit(fullTaskID);
            String stval= student.getState(fullTaskID);
            String studentId= student.getUserid();
                    
            if (answercount==1) {
                ps1=connection.prepareStatement(insertStatus);
                ps1.setString(1,studentId);
                ps1.setString(2,courseId);
                ps1.setString(3,moduleID);
                ps1.setInt(4,taskNo);
                ps1.setString(5,params);
                ps1.setInt(6,cc);
                if (stval.equals("YES")) {
                    ps1.setString(7,"Y");
                    ps1.setString(8,"Y");
                }
                else {
                    if (stval.equals("LATE")) {
                        ps1.setString(7,"Y");
                        ps1.setString(8,"N");
                    }    
                    else {
                        ps1.setString(7,"N");
                        ps1.setString(8,"N");
                    }                      
                }    
                ps1.executeUpdate();                 
            }
            else {
                ps1=connection.prepareStatement(insertStatus);
                ps1.setInt(1,answercount);
                ps1.setInt(2,cc);
                if (stval.equals("YES")) {
                    ps1.setString(3,"Y");
                    ps1.setString(4,"Y");
                }
                else {
                    if (stval.equals("LATE")) {
                        ps1.setString(3,"Y");
                        ps1.setString(4,"N");
                    }    
                    else {
                        ps1.setString(3,"N");
                        ps1.setString(4,"N");
                    }                      
                }    
                
                ps1.setString(5,studentId);
                ps1.setString(6,courseId);
                ps1.setString(6,moduleID);
                ps1.setInt(8,taskNo);
                ps1.executeUpdate();                 
            }
            
            
            if (shouldStore) {
                String packedanswer = pack(answers);
                ps2=connection.prepareStatement(insertanswer);
                ps2.setString(1,studentId);
                ps2.setString(2,courseId);
                ps2.setString(3,moduleID);
                ps2.setInt(4,taskNo);
                ps2.setInt(5,answercount);
                ps2.setInt(6,feedback.getEvaluation());
                ps2.setTimestamp(7,when);
                ps2.setString(8,packedanswer);
                ps2.setString(9,lang);
                ps2.setString(10,feedback.toString()); 
                ps2.executeUpdate();    
            }
        }
        else {
            String em1= taskBase.getAttribute("E","DATABASEERROR","MESSAGE",lang); 
            done =fatalErrorNotification(fullTaskID, em1);
        }
    }
    catch (SQLException e) {
          String em2= taskBase.getAttribute("E","DATABASEERROR","MESSAGE",lang)+ e.getMessage(); 
          done= fatalErrorNotification(fullTaskID, em2);
    }  
    finally {
           try {
             if (ps1!=null) ps1.close();
             if (ps2!=null) ps2.close();
             connection.close();
           }  
             catch (SQLException fex) {}      
    }
    return done;
 }
     
 //      

private String pack (String[] ans){
    //packs the answer to a single string
    //items#length#data#length#....
    StringBuffer buf= new StringBuffer();
    buf.append(ans.length);
    buf.append('#');
    for (int i=0;i<ans.length;i++) {
        buf.append(ans[i].length());
        buf.append('#');
        buf.append(ans[i]);
        buf.append('#');
    }    
    return buf.toString();
}   
  
private void feedbackbox(int coff, PrintWriter out, Feedback fb) {  
    if (fb.wasPassed(coff)) 
       out.println("<P CLASS=\"okbox\">");
    else   
       out.println("<P CLASS=\"failbox\">"); 
    out.println(fb.getMsgText());
    out.println("</P>");
}

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
}

