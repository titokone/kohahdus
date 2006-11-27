package fi.helsinki.cs.kohahdus;

import java.util.HashMap;

public class StudentAnswers extends HashMap<String, AnswerState> {
	private String firstname;
	private String lastname;
	private String userID;
	
	public StudentAnswers(String firstname, String lastname, String userID){
		this.firstname = firstname;
		this.lastname = lastname;
		this.userID = userID;
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
	
	

}
