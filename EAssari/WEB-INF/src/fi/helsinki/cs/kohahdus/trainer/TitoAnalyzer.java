package fi.helsinki.cs.kohahdus.trainer;

import java.util.List;

import fi.helsinki.cs.kohahdus.criteria.*;


/** Class for running TitoKone and analyzing student's answer. */
public class TitoAnalyzer {
	private TitoState state;
	
	//KONSTRUKTORI
	public TitoAnalyzer() {
	}
	
	//METODI
	/** Analyzes student's answercode. Returns feedback from analysis. */
	public TitoFeedback Analyze(Task task, List<Criterion> criteria, String programCode, String keyboardInput) {
		
		int maxInstructions=task.getMaximumNumberOfInstructions(); //get max instructions
		state=new TitoState(); //creates new TitoState
		state.compile(programCode); //compile the code
		
		TitoAnalyzerFeedback feedback=new TitoAnalyzerFeedback();
		
		
		String result=state.execute(keyboardInput, maxInstructions); //run the compiled program in TitoKone
		if (!result.equals(null)) {
			
		}
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

		public String getCompileError() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getRunError() {
			// TODO Auto-generated method stub
			return null;
		}		
	}
	
}
