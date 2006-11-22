package fi.helsinki.cs.kohahdus;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.helsinki.cs.kohahdus.criteria.*;
import fi.helsinki.cs.kohahdus.trainer.*;


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
public class Answer extends TrainerServlet {

	/**
	 * Handles the incoming request described in this class's description.
	 */
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
				
		// Get the task in question from the session
		Task task = (Task)req.getSession().getAttribute("task");
		CriterionMap criteriaMap = (CriterionMap)req.getSession().getAttribute("criteria");
		List<Criterion> criteria = criteriaMap.getList();
		
		
		// Get the student's answer from the request
		String programCode = req.getParameter("programCode");
		String keyboardInput = req.getParameter("keyboardInput");
		
		// Use the analyzer to process the answer
		TitoAnalyzer analyser = new TitoAnalyzer();
		TitoFeedback feedback = analyser.Analyze(task, criteria, programCode, keyboardInput);
		
		// Store the answer and the result to database
		// TODO: DBHandler.storeStateAndAnswer(task, criteria, feedback)
		
		// Store the result for later use on the jsp-page
		req.getSession().setAttribute("feedback", feedback);
		
		// Forward the request to the answer jsp page
		res.sendRedirect(req.getContextPath()+"www/student/answer_task.jsp?analyzed=true&keyboardInput="+keyboardInput+
				         "&programCode="+programCode);
	}
	
}

