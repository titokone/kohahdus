package fi.helsinki.cs.kohahdus.trainer;

import java.util.*;

import fi.helsinki.cs.kohahdus.criteria.*;

/**
 * 
 * Author Ninja
 * http://www.ebaumsworld.com/2006/06/jeopardy.html
 */


/** Class for running TitoKone and analyzing student's answer. */
public class TitoAnalyzer {
	private TitoState state;
	private TitoState stateSecret;
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
		boolean hasSecretInput=!(task.getSecretInput().equals("")||task.getSecretInput().equals(null));
		
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
				programCodeTeacher+=task.getFillInPostCode();
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
			if (hasSecretInput) {
				stateTeacherSecret=new TitoState();
				//Compile the code, check if errors.
				compileResult=stateTeacherSecret.compile(programCodeTeacher); //compile program with TitoKone
				if (!compileResult.equals(null)) {
					feedback.setCompileError(compileResult);
					return feedback;
				}
				//Execute the successfully compiled code.
				runResult=stateTeacherSecret.execute(task.getSecretInput(), maxInstructions); //run the compiled program in TitoKone
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
			programCode+=task.getFillInPostCode();
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
		
		//Check if task has secret input
		if (hasSecretInput) {
			stateSecret=new TitoState();
			compileResult=stateSecret.compile(programCode); //compile program with TitoKone
			if (!compileResult.equals(null)) {
				feedback.setCompileError(compileResult);
				return feedback;
			}
			//Execute the successfully compiled code.
			runResult=stateSecret.execute(task.getSecretInput(), maxInstructions); //run the compiled program in TitoKone
			if (!runResult.equals(null)) {
				feedback.setRunError(runResult);
				return feedback;
			} //end if
		} //end if secret input
		
		
		
		//CRITERION CHECK
		LinkedList<TitoCriterionFeedback> criterfblist=new LinkedList<TitoCriterionFeedback>();
		boolean passp, passs;
		boolean passTask=true; //will stay true until criterion fails
		
		//TODO
		for (Criterion c : criteria) {
			//Criterion feedback object and value fields for it, will be created in the end.
			TitoCriterionFeedback critfb;
			String cname="", cfeedback="";
			Boolean csuccess=true;
			
			//if has acceptance test
			if (c.hasAcceptanceTest(task.isValidateByModel())) {
				
				//first with public input
				passp=c.passesAcceptanceTest(state, stateTeacherPublic);
				if (passp==false) {
					passTask=false;
					csuccess=false;
				}
				
				//with secret input
				if (c.isSecretInputCriterion()) {
					passs=c.passesAcceptanceTest(stateSecret, stateTeacherSecret);
					if (passs==false) {
						passTask=false;
						csuccess=false;
					}
				}
				
			}
			
			//if has quality test
			if (c.hasQualityTest(task.isValidateByModel())) {
				
				
				
			}
			
			//create criterion feedback object and add it to list
			critfb=new TitoCriterionFeedback(cname, cfeedback, csuccess);
			criterfblist.add(critfb);
		} //TODO
		
		//Criterions checked, lets create the feedback
		feedback.setTitoState(state);
		feedback.setWasSuccessful(passTask);
		if (passTask) {
			feedback.setOverallFeedback(task.getPassFeedBack());
		} else {
			feedback.setOverallFeedback(task.getFailFeedBack());
		}
		feedback.setCriteriaFeedback(criterfblist);
		
		//feedback object created, return it
		return feedback;
		
		/*
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
		*/
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