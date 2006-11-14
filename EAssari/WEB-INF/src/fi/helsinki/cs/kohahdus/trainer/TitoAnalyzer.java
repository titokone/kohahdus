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
	
	//KONSTRUKTORI
	public TitoAnalyzer() {
	}
	
	//METODI
	/** Analyzes student's answercode. Returns feedback from analysis. */
	public TitoFeedback Analyze(Task task, List<Criterion> criteria, String programCode, String keyboardInput) {
		
		//Create general data
		int maxInstructions=task.getMaximumNumberOfInstructions();
		state=new TitoState();
		TitoAnalyzerFeedback feedback=new TitoAnalyzerFeedback();
		
		//compile the code, check if errors
		String compileResult=state.compile(programCode); //compile program with TitoKone
		if (!compileResult.equals(null)) {
			feedback.setCompileError(compileResult);
			return feedback;
		}
		
		//execute the successfully compiled code
		String runResult=state.execute(keyboardInput, maxInstructions); //run the compiled program in TitoKone
		if (!runResult.equals(null)) {
			feedback.setRunError(runResult);
			return feedback;
		}
		
		//criterion check
		int dfgsd=state.getRegister(int registerCode);
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
	

	
	/** private class for feedback*/
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
			return null;
		}
		
		/** Return overall feedback of this TitoAnalyzerFeedback */
		public String getOverallFeedback() {
			// TODO Auto-generated method stub
			return null;
		}

		/** Return true if task was completed successfully */
		public boolean wasSuccessful() {
			// TODO Auto-generated method stub
			return false;
		}
		
		/** Return list of CriterionFeedback objects */
		public List<TitoCriterionFeedback> getCriteriaFeedback() {
			// TODO Auto-generated method stub
			return null;
		}
		
		/** Return compile error */
		public String getCompileError() {
			// TODO Auto-generated method stub
			return null;
		}
		
		/** Return run error. */
		public String getRunError() {
			// TODO Auto-generated method stub
			return null;
		}		
	}
	
}
