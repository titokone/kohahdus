package fi.helsinki.cs.kohahdus.trainer;

import java.util.HashMap;
import java.util.List;

import fi.helsinki.cs.kohahdus.criteria.*;

/**
 * 
 * Author Ninja
 * http://www.ebaumsworld.com/2006/06/jeopardy.html
 */


/** Class for running TitoKone and analyzing student's answer. */
public class TitoAnalyzer {
	private TitoState state;
	private TitoState stateTeacherPublic;
	private TitoState stateTeacherSecret;
	
	//CONSTRUCTOR
	public TitoAnalyzer() {
	}
	
	//METHOD
	/** Analyzes student's answercode. Returns feedback from analysis. */
	public TitoFeedback Analyze(Task task, List<Criterion> criteria, String programCode, String keyboardInput) {
		
		//Create general data.
		int maxInstructions=task.getMaximumNumberOfInstructions();
		TitoAnalyzerFeedback feedback=new TitoAnalyzerFeedback();
		
		//TEACHER
		//Check if task is validated by model answer
		//if so, creates new TitoState for teacher
		if (task.isValidateByModel()) {
			
			//First let's set the program code
			String programCodeTeacher;
			//Check if task is fill-in or programming task.
			if (task.isFillInTask()) {
				programCodeTeacher=task.getFillInPreCode();
				programCodeTeacher+=task.getModelAnswer();
				programCodeTeacher+=task.getFillinPostCode();
			} else {
				programCodeTeacher=task.getModelAnswer();
			}
			
			//First public let's run the code with public input
			stateTeacherPublic=new TitoState();
			//Compile the code, check if errors.
			String compileResult=stateTeacherPublic.compile(programCodeTeacher); //compile program with TitoKone
			if (!compileResult.equals(null)) {
				feedback.setCompileError(compileResult);
				return feedback;
			}
			//Execute the successfully compiled code.
			String runResult=stateTeacherPublic.execute(task.getPublicInput(), maxInstructions); //run the compiled program in TitoKone
			if (!runResult.equals(null)) {
				feedback.setRunError(runResult);
				return feedback;
			}
			
			//Let's check if task has secret input
			if (!(task.getSecretInput().equals("")||task.getSecretInput().equals(null))) {
				stateTeacherSecret=new TitoState();
				//Compile the code, check if errors.
				String compileResult=stateTeacherSecret.compile(programCodeTeacher); //compile program with TitoKone
				if (!compileResult.equals(null)) {
					feedback.setCompileError(compileResult);
					return feedback;
				}
				//Execute the successfully compiled code.
				String runResult=stateTeacherSecret.execute(task.getSecretInput(), maxInstructions); //run the compiled program in TitoKone
				if (!runResult.equals(null)) {
					feedback.setRunError(runResult);
					return feedback;
				} //end if
			} //end if
			
		} //end if task.isValidateByModel()
		
		
		
		//STUDENT
		state=new TitoState();
		
		//Check if task is fill-in or programming task.
		if (task.isFillInTask()) {
			String help=programCode;
			programCode=task.getFillInPreCode();
			programCode+=help;
			programCode+=task.getFillinPostCode();
		}
		
		//Compile the student's code, check if errors.
		String compileResult=state.compile(programCode); //compile program with TitoKone
		if (!compileResult.equals(null)) {
			feedback.setCompileError(compileResult);
			return feedback;
		}
		
		//Execute the successfully compiled code.
		String runResult=state.execute(keyboardInput, maxInstructions); //run the compiled program in TitoKone
		if (!runResult.equals(null)) {
			feedback.setRunError(runResult);
			return feedback;
		}
		
		
		
		
		//Criterion check.
		//registers

		for (Criterion c : criteria) {

			if (c.passesAcceptanceTest(state, null)) {
				
			}
		}
		
		int r=state.getRegister(int registerCode);
		
		
		int wrerwe=state.getMemoryLocation(int address);
		HashMap ewrrew=state.getSymbolTable();
		
		//allowed/forbidden instructions check
		String usedInstructions[]=state.getUsedOpcodes();
		
		//quality criterion check
		int ererw=state.getStackMaxSize();
		int sdsdf=state.getExecutionSteps();
		int sdfs=state.getCodeSize();
		int sdfsd=state.getDataSize();
		int sadfs=state.getMemoryAccessCount();
		
		//all systems clear
		return feedback;
	}
	

	
	/** Private class for feedback*/
	private class TitoAnalyzerFeedback implements TitoFeedback {
		private TitoState titostate;
		private String overallfeedback;
		private boolean wassuccess;
		private List<TitoCriterionFeedback> criterionfeedback;
		private String compileerror;
		private String runerror;

		//CONSTRUCTOR
		public TitoAnalyzerFeedback(){
		}
		
		//SET METHODS
		
		/** Set TitoState object */
		public void setTitoState(TitoState state) {
			titostate=state;
		}
		
		/** Set overall feedback of this TitoAnalyzerFeedback */
		public void setOverallFeedback(String overall) {
			overallfeedback=overall;
		}

		/** Set boolean value if task was completed successfully */
		public void setWasSuccessful(boolean value) {
			wassuccess=value;
		}
		
		/** Set list of CriterionFeedback objects */
		public void setCriteriaFeedback(List<TitoCriterionFeedback> criteria) {
			criterionfeedback=criteria;
		}
		
		/** Set compile error */
		public void setCompileError(String error) {
			compileerror=error;
		}
		
		/** Set run error. */
		public void setRunError(String error) {
			runerror=error;
		}		
		
		//GET METHODS
		
		/** Return TitoState object */
		public TitoState getTitoState() {
			return titostate;
		}
		
		/** Return overall feedback of this TitoAnalyzerFeedback */
		public String getOverallFeedback() {
			return overallfeedback;
		}

		/** Return true if task was completed successfully */
		public boolean wasSuccessful() {
			return wassuccess;
		}
		
		/** Return list of CriterionFeedback objects */
		public List<TitoCriterionFeedback> getCriteriaFeedback() {
			return criterionfeedback;
		}
		
		/** Return compile error */
		public String getCompileError() {
			return compileerror;
		}
		
		/** Return run error. */
		public String getRunError() {
			return runerror;
		}		
	}	
}