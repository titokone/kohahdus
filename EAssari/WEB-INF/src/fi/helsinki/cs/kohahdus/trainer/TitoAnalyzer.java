package fi.helsinki.cs.kohahdus.trainer;

import java.util.*;

import fi.helsinki.cs.kohahdus.criteria.*;
import fi.helsinki.cs.kohahdus.languages.LanguageManager;

/**
 * 
 * Author Ninja
 * 
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
		boolean hasSecretInput = !(task.getSecretInput() == null ||task.getSecretInput().equals(""));
		feedback.setOverallFeedback(task.getFailFeedBack());
		
		//Check if code has file-modifying comman DEF
		//If it has, end Analyzing and return compile error
		if (DrParser(programCode, "def")) {
			feedback.setCompileError("TitoTrainer doesn't support command DEF");
			return feedback;
		}
		
		
		
		//TEACHER
		//Check if task is validated by model answer
		//if so, creates new TitoState for teacher
		if (task.isValidateByModel()) {
			
			//First let's set the program code
			String programCodeTeacher;
			//Check if task is fill-in or programming task.
			if (task.isFillInTask()) {
				programCodeTeacher=task.getFillInPreCode();
				programCodeTeacher+="\n";
				programCodeTeacher+=task.getModelAnswer();
				programCodeTeacher+="\n";
				programCodeTeacher+=task.getFillInPostCode();
			} else {
				programCodeTeacher=task.getModelAnswer();
			}
			
			//First public let's run the code with public input
			stateTeacherPublic=new TitoState();
			//Compile the code, check if errors.
			String compileResult=stateTeacherPublic.compile(programCodeTeacher); //compile program with TitoKone
			if (compileResult != null) {
				compileResult = "Error in teacher's code, task is corrupted and can't be solved: " + compileResult;
				feedback.setCompileError(compileResult);
				return feedback;
			}
			
			//Execute the successfully compiled code.
			String runResult=stateTeacherPublic.execute(task.getPublicInput(), maxInstructions); //run the compiled program in TitoKone
			if (runResult != null) {
				runResult = "Error in teacher's code, task is corrupted and can't be solved: " + runResult;
				feedback.setRunError(runResult);
				return feedback;
			}
			
			//Let's check if task has secret input
			if (hasSecretInput) {
				stateTeacherSecret=new TitoState();
				//Compile the code, check if errors.
				compileResult=stateTeacherSecret.compile(programCodeTeacher); //compile program with TitoKone
				if (compileResult != null) {
					compileResult = "Error in teacher's code, task is corrupted and can't be solved: " + compileResult;
					feedback.setCompileError(compileResult);
					return feedback;
				}
				//Execute the successfully compiled code.
				runResult=stateTeacherSecret.execute(task.getSecretInput(), maxInstructions); //run the compiled program in TitoKone
				if (runResult != null) {
					runResult = "Error in teacher's code, task is corrupted and can't be solved: " + runResult;
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
			programCode+="\n";
			programCode+=help;
			programCode+="\n";
			programCode+=task.getFillInPostCode();
		}
		
		//Compile the student's code, check if errors.
		String compileResult=state.compile(programCode); //compile program with TitoKone
		if (compileResult != null) {
			feedback.setCompileError(compileResult);
			return feedback;
		}
		
		//Execute the successfully compiled code.
		feedback.setTitoState(state);
		String runResult=state.execute(keyboardInput, maxInstructions); //run the compiled program in TitoKone
		if (runResult != null) {
			feedback.setRunError(runResult);
			return feedback;
		}
		
		//Check if task has secret input
		if (hasSecretInput) {
			stateSecret=new TitoState();
			compileResult=stateSecret.compile(programCode); //compile program with TitoKone
			if (compileResult != null) {
				feedback.setCompileError(compileResult);
				return feedback;
			}
			//Execute the successfully compiled code.
			runResult=stateSecret.execute(task.getSecretInput(), maxInstructions); //run the compiled program in TitoKone
			if (runResult != null) {
				feedback.setRunError(runResult);
				return feedback;
			} //end if
		} //end if secret input
		//CRITERION CHECK
		LinkedList<TitoCriterionFeedback> criterfblist=new LinkedList<TitoCriterionFeedback>();
		boolean passp=true, passs=true;
		boolean passTask=true; //will stay true until criterion fails
		
		//Checks each criterion
		for (Criterion c : criteria) {
			//Criterion feedback object and value fields for it, will be created in the end.
			TitoCriterionFeedback critfb;
			String cname="", cfeedback="";
			Boolean csuccess=true;
			
			//get the name
			cname=c.getName(LanguageManager.getTextResource(task.getLanguage() ,"criterion"));
			//cname="TESTINIMI";
			
			// if criterion is meant for secret input
			if (c.isSecretInputCriterion()) {
				if (c.hasAcceptanceTest(task.isValidateByModel())) {
					/* It won't be used in any quality tests.
					   It always has acceptance test. stateTeacherSecret can be null. */
					if (stateSecret!=null) {
						passs=c.passesAcceptanceTest(stateSecret, stateTeacherSecret);
					}
					// criterion requirement is met
					if (passs) {
						cfeedback=c.getAcceptanceFeedback();
					} else { // fails
						cfeedback=c.getFailureFeedback();					
						csuccess=false;
						passTask=false;
					}
				}
			}
			// if criterion is meant for public input
			else {
				// Check if criterion has quality test
				boolean passedQuality=false; // to check if it passed quality test
				if (c.hasQualityTest(task.isValidateByModel())) {
					// it has, let's check it out. stateTeacherPublic can be null.
					passp=c.passesQualityTest(state, stateTeacherPublic);
					if (passp) {
						cfeedback=c.getHighQualityFeedback();
						passedQuality=true;
					}
				}
				/* Check if criterion has acceptace test
				   won't be checked if it passed the quality test */
				if (c.hasAcceptanceTest(task.isValidateByModel()) && passedQuality==false) {
					//it has, let's check it out. stateTeacherPublic can be null.
					passp=c.passesAcceptanceTest(state, stateTeacherPublic);
					if (passp) { //student passes
						cfeedback=c.getAcceptanceFeedback();
					} else {  //student fails
						cfeedback=c.getFailureFeedback();
						csuccess=false;
						passTask=false;
					}
				}
			} // end else, for public input
			
			//create criterion feedback object and add it to list if it had tests
			if (c.hasQualityTest(task.isValidateByModel()) ||
				c.hasAcceptanceTest(task.isValidateByModel())) {
				critfb=new TitoCriterionFeedback(cname, cfeedback, csuccess);
				criterfblist.add(critfb);
			}
		}
		
		//Criterions checked, lets create the feedback
		feedback.setWasSuccessful(passTask);
		if (passTask) {
			feedback.setOverallFeedback(task.getPassFeedBack());
		} else {
			feedback.setOverallFeedback(task.getFailFeedBack());
		}
		feedback.setCriteriaFeedback(criterfblist);
		
		//feedback object created, return it
		return feedback;
	}
	
	
	/** Private method for finding word in programCode*/
	private boolean DrParser(String programCode, String word) {
		programCode=programCode.toLowerCase();
		word=word.toLowerCase();
		int len=word.length();
		
		for (int i=0; i<programCode.length(); ++i) {
			if ((i+len+1) < (programCode.length()) ) { //checks there's enough space for word
				
				if ( programCode.charAt(i)==' ' || programCode.charAt(i)=='\t' ) { //Check if it has space
					//Lets find the word
					int j=1;
					boolean ok=true;
					while (j<=len && ok==true ) {
						if (!(programCode.charAt(i+j)==word.charAt(j-1)) ) {
							ok=false;
						}
						j=j+1;
					}
					//found a sequence, see if it ends to space
					if ( ok==true && (programCode.charAt((i+len+1))==' ' || programCode.charAt((i+len+1))=='\t') ) {
						return true; //found a word that's in the String
					}
				}
			}
		}
		return false; //didn't find word
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
		public boolean isSuccessful() {
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