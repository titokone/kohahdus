package fi.helsinki.cs.kohahdus;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AnswerState {
	
	private int lastTryNumber = 0;
	private int currentResult = 0;
	private boolean hasSucceeded = false;
	private String taskName;
	private String firstname;
	private String lastname;
	private String userID;
	private Timestamp answerTime;
	
	
	
	public int getCurrentResult() {
		return currentResult;
	}
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	public boolean hasSucceeded() {
		return hasSucceeded;
	}
	public void setHasSucceeded(boolean hasSucceeded) {
		this.hasSucceeded = hasSucceeded;
	}
	public int getLastTryNumber() {
		return lastTryNumber;
	}
	public void setLastTryNumber(int lastTryNumber) {
		this.lastTryNumber = lastTryNumber;
	}
	
	public void incrementLastTryNumber() {
		this.lastTryNumber++;
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getAnswerTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");
		return sdf.format(answerTime);
	}
	public void setAnswerTime(Timestamp answerTime) {
		this.answerTime = answerTime;
	}
	
}
