package fi.helsinki.cs.kohahdus;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
*
* @author Taro Morimoto 
*/

public class StudentAnswers {
	private String firstname;
	private String lastname;
	private String userID;
	private String StudentNumber;
	private String SocialSecurityNumber;
	private HashMap<String, AnswerState> answers;
	
	public StudentAnswers(String firstname, String lastname, String userID, String studentnumber, String socialsecuritynumber){
		this.firstname = firstname;
		this.lastname = lastname;
		this.userID = userID;
		this.StudentNumber = studentnumber;
		this.SocialSecurityNumber = socialsecuritynumber;
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
	
	public LinkedList<AnswerState> getAnswers() {
		LinkedList<AnswerState> list = new LinkedList<AnswerState>(answers.values());
		Collections.sort(list);
		return list;
	}
	
	public void putAnswerState(String key, AnswerState value) {
		answers.put(key, value);
	}

	public String getStudentNumber() {
		return StudentNumber;
	}
	
	public void setStudentNumber(String StudentNumber) {
		this.StudentNumber = StudentNumber;
	}

	public String getSocialSecurityNumber() {
		return SocialSecurityNumber;
	}

	public void setSocialSecurityNumber(String SocialSecurityNumber) {
		this.SocialSecurityNumber = SocialSecurityNumber;
	}

	public int getCorrectAnswerCount() {
		int count = 0;
		for (AnswerState answer : answers.values()){
			if (answer.getHasSucceeded()) count++;
		}
		return count;
	}

}
