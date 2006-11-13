package fi.helsinki.cs.kohahdus.trainer;

public class TitoCriterionFeedback {
	private String name;
	private String feedback;
	private boolean success;
	
	
	
	public TitoCriterionFeedback(String name, String feedback, boolean success) {
		this.name = name;
		this.feedback = feedback;
		this.success = success;	
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public String getFeedback() {
		return feedback;
	}
	
	
	// TODO: Parempi nimi
	public boolean wasSuccessful() {
		return success;
	}
}
