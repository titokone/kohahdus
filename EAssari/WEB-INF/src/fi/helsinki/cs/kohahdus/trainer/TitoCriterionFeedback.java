package fi.helsinki.cs.kohahdus.trainer;

/** Class for criterion feedback information. */
public class TitoCriterionFeedback {
	private String name;
	private String feedback;
	private boolean success;
	
	
	/** Creates a new instance of TitoCriterionFeedback.  */
	public TitoCriterionFeedback(String name, String feedback, boolean success) {
		this.name = name;
		this.feedback = feedback;
		this.success = success;	
	}
	
	/** Return criterion name */
	public String getName() {
		return name;
	}
	
	/** Return feedback of this criterion  */
	public String getFeedback() {
		return feedback;
	}
	
	/** Return true if this criterion meets passing requirements */
	public boolean isPassedAcceptanceTest() {
		return success;
	}
}