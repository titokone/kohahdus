package fi.helsinki.cs.kohahdus;

import java.util.Collection;
import java.util.HashMap;

public class StudentAnswers {
	private String firstname;
	private String lastname;
	private String userID;
	private String extID;
	private String extID2;
	private HashMap<String, AnswerState> answers;
	
	public StudentAnswers(String firstname, String lastname, String userID, String extID, String extID2){
		this.firstname = firstname;
		this.lastname = lastname;
		this.userID = userID;
		this.extID = extID;
		this.extID2 = extID2;
		answers = new HashMap<String, AnswerState>();
	}
	
	public StudentAnswers(){
		answers = new HashMap<String, AnswerState>();
	}
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
		
	public HashMap<String, AnswerState> getAnswerMap() {
		return answers;
	}
	
	public Collection<AnswerState> getAnswers() {
		return answers.values();
	}
	
	public void putAnswerState(String key, AnswerState value) {
		answers.put(key, value);
	}

	public String getExtID() {
		return extID;
	}

	public void setExtID(String extID) {
		this.extID = extID;
	}

	public String getExtID2() {
		return extID2;
	}

	public void setExtID2(String extID2) {
		this.extID2 = extID2;
	}
	

}
