package fi.helsinki.cs.kohahdus;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.helsinki.cs.kohahdus.criteria.Criterion;
import fi.helsinki.cs.kohahdus.criteria.CriterionMap;
import fi.helsinki.cs.kohahdus.trainer.Task;
import fi.helsinki.cs.kohahdus.trainer.TitoAnalyzer;
import fi.helsinki.cs.kohahdus.trainer.TitoFeedback;
import fi.helsinki.cs.kohahdus.trainer.User;


/**
 * This servlet is called from student's task answer jsp-page.
 * It receives the answer in the request.
 * The task and the criteria are fetched from the session.
 * Student's answer is then analyzed by the Analyzer and the 
 * result is stored into database. Finally the execution is redirected
 * to the student's task answer jsp-page.  
 * 
 *  Author Kohahdus, Taro Morimoto
 */
public class Answer extends HttpServlet {

	/**
	 * Handles the incoming request described in this class's description.
	 */
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			User user = (User)req.getSession().getAttribute("user");
			String courseID = (String)req.getSession().getAttribute("course");
			String language = (String)req.getSession().getAttribute("language");
	
			// Get the task in question from the session
			Task task = (Task)req.getSession().getAttribute("task");
			CriterionMap criteriaMap = (CriterionMap)req.getSession().getAttribute("criteria");
			List<Criterion> criteria = criteriaMap.getList();		
			
			// Get the student's answer from the request
			String programCode = req.getParameter("programCode");
			String keyboardInput = req.getParameter("keyboardInput");
			
			Log.write("Answering with code " + programCode + " and input " + keyboardInput);
			
			// Use the analyzer to process the answer
			TitoAnalyzer analyser = new TitoAnalyzer();
			TitoFeedback feedback = analyser.Analyze(task, criteria, programCode, keyboardInput);
			
			// Store the answer and the result to database
			if (user.isStudent()) {
				DBHandler.getInstance().storeStateAndAnswer(user.getUserID(), courseID, task.getTaskID(), programCode,
															keyboardInput, feedback, language);
			}
			
			// Store the result for later use on the jsp-page
			req.getSession().setAttribute("feedback", feedback);
			req.getSession().setAttribute("programCode", programCode);
			req.getSession().setAttribute("keyboardInput", keyboardInput);
			
			// Forward the request to the answer jsp page
			Log.write("Answer: Redirecting to www/answer_task.jsp?analyzed=true#code");
			res.sendRedirect("www/answer_task.jsp?analyzed=true#code");
					    
		} catch (Exception e){
			Log.write("Answer: Failed to process student's answer. " +e);
			Log.write(e);
			res.sendRedirect("www/error.jsp?errorMsg=answer-failed");
		}
	}
	
}

