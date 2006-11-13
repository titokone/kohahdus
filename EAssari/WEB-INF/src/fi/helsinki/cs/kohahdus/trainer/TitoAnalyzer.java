package fi.helsinki.cs.kohahdus.trainer;

import java.util.List;

import fi.helsinki.cs.kohahdus.criteria.TitoState;


/** Class for running TitoKone and analyzing student's answer. */
public class TitoAnalyzer {
	
	/** Analyzes student's answercode. Returns feedback from analysis. */
	public TitoFeedback Analyze(Task task, String programCode, String keyboardInput) {
		TitoState state=new TitoState();
		state.
		
		return new TitoAnalyzerFeedback();
	}
	

	
	
	private class TitoAnalyzerFeedback implements TitoFeedback {

		public TitoState getTitoState() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getOverallFeedback() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean wasSuccessful() {
			// TODO Auto-generated method stub
			return false;
		}

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
