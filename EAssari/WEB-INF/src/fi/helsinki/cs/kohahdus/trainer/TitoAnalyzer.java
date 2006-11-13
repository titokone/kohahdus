package fi.helsinki.cs.kohahdus.trainer;

import java.util.List;

import fi.helsinki.cs.kohahdus.criteria.TitoState;

public class TitoAnalyzer {

	public TitoAnalyzer() {
	}
	
	
	
	public TitoFeedback Analyze(Task task, String programCode, String keyboardInput) {
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
