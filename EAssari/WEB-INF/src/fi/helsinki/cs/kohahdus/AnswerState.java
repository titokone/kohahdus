package fi.helsinki.cs.kohahdus;

public class AnswerState {
	
	private int lastTryNumber = 0;
	private int currentResult = 0;
	private boolean hasSucceeded = false;
	
	
	
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
	
}
