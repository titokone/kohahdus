package fi.helsinki.cs.kohahdus;

import java.io.*;
import javax.servlet.http.*;

import fi.helsinki.cs.kohahdus.trainer.AnalyserInterface;
import fi.helsinki.cs.kohahdus.trainer.CacheException;
import fi.helsinki.cs.kohahdus.trainer.DisplayerInterface;
import fi.helsinki.cs.kohahdus.trainer.Feedback;
import fi.helsinki.cs.kohahdus.trainer.Task;
import fi.helsinki.cs.kohahdus.trainer.Tasktype;
import fi.helsinki.cs.kohahdus.trainer.TrainerParameters;
import fi.helsinki.cs.kohahdus.trainer.TrainerServlet;
import fi.helsinki.cs.kohahdus.trainer.User;

import java.util.*;
import java.sql.*;


/**
 * This servlet is called from student's task answer jsp-page.
 * It receives the answer and the task id in the request.
 * The task and the criteria are fetched from the database.
 * Student's answer is then analyzed by the Analyzer and the 
 * result is stored into database. Finally the execution is redirected
 * to the student's task answer jsp-page.  
 * 
 *  Author Kohahdus, Taro Morimoto
 */
public class Answer extends TrainerServlet {


	/**
	 * Handles the incoming request described in this class's description.
	 */
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
	}
	
	/**
	 * Stores the answer of the student into database table storedanswer. 
	 * Also stores the state of the student's answered task into table studentmodel.
	 * 
	 * @param shouldStore
	 * @param student
	 * @param courseId
	 * @param taskID
	 * @param answers
	 * @param accepted
	 * @param lang
	 * @param feedback
	 * @return
	 * @throws SQLException
	 */
	private String storeStateAndAnswer(boolean shouldStore, User student, String courseId, 
									   String taskID, String[] answers, boolean accepted,
									   String lang, Feedback feedback) throws SQLException {
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
}

