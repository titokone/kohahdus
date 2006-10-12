package fi.helsinki.cs.kohahdus.trainer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Mock object for testing purposes on jsp-pages. Emulates a minimal Task-object 
 * without error handling etc. 
 */

public class MockTask extends Task {

	//Fields are not implemented in Task at the moment
	private String name;
	private String author;
	private String category;
	private String language;
	private boolean isFillIn;
	
	
	public MockTask(String name, String category, String author, boolean inEnglish, boolean fillin) {
		super(null, null, null, null, 0, null, false, false, false, false, 0, 0, null);
		this.name = name;
		this.category = category;
		this.author = author;
		this.isFillIn = fillin;	
		this.language = inEnglish ? "EN" : "FI";
	}
	
	
	public MockTask(String taskid, String courseid, String moduleid, String tasktypeid, int seqno, Timestamp deadline, boolean shouldstore, boolean shouldregister, boolean shouldknowstudent, boolean shouldevaluate, int cvalue, int nooftries, Tasktype tType) {
		super(taskid, courseid, moduleid, tasktypeid, seqno, deadline, shouldstore,
				shouldregister, shouldknowstudent, shouldevaluate, cvalue, nooftries,
				tType);
		// TODO Auto-generated constructor stub
	}
	
	/** Return the name of this task */
	public String getName() {
		return name;
	}
	
	/** Set the name of this task */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Return name of the last person who has modified this task */
	public String getAuthor() {
		return this.author;
	}

	/** Set "last task modification by" attribute to Name,
	 *  set last-modification-timestamp to current data and time */
	public void setAuthor(String name) {
		this.author = name;
	}
	
	/** Return the date and time this task was last modified */
	public Date getModificationDate() {
		return null;
	}
	
	
	/** Return code of the model answer provided by teacher */
	public String getModelAnswer() {
		return null;
	}
	
	/** Set model answer code */
	public void setModelAnswer(String code) {
		
	}
	
	/** Return true if this task is to be validated by comparing results of the student's answer
	 * to results of teacher's answer */
	public boolean isValidateByModel() {
		return false;
	}
	
	/** Set the validation method of this task */
	public void setValidateByModel(boolean useModel){
		
	}

	/** Return the task category of this task */
	public String getCategory() {
		return this.category;
	}
	
	/** Set task category of this task */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/** Return tasktype as a String (fill-in or programming) */
	public String getTaskTypeString() {
		return isFillIn ? "Fill-in" : "Programming";
	}	
	
	/** Return true if this is a fill-in task */
	public boolean isFillInTask() {
		return false;
	}
	
	/** Set this task as fill-in or create-full-program  */
	public void setFillInTask(boolean fillIn) {
		
	}

	/** Return the code that is prepended before student's code in a fill-in task */
	public String getFillInPreCode() {
		return null;
	}
	
	/** Set the code that is prepended before student's code in a fill-in task */
	public void SetFillInPreCode(String code) {
		
	}
	
	/** Return the code that is appended to student's code in a fill-in task */
	public String getFillInPostCode() {
		return null;
	}
	
	/** Set the code that is appended to student's code in a fill-in task */
	public void SetFillInPostCode(String code) {
		
	}

	/** Return the description (tehtävänanto) of this task */
	public String getDescription() {
		return null;
	}

	/** Set description (tehtävänanto) of this task */
	public void setDescription() {
		
	}

	/** Return tasks language ("FI" or "EN") */
	public String getLanguage() {
		return this.language;
	}
	
	
	
	private static List<MockTask> tasks = null;
	private static Random rng = new Random();
	
	public static Task[] getTasks() {
		if (tasks == null) {
			initTasks();
		}
		return tasks.toArray(new MockTask[0]);
	}
	
	private static void initTasks() {
		tasks = new ArrayList<MockTask>();
		for (int i=0; i<10; i++) {
			tasks.add(new MockTask("Task "+i, rndCategory(), rndAuthor(), rng.nextBoolean(), rng.nextBoolean()));
		}
	}
	
	private static String[] categories = { "Easy task", "Moderate task", "Hard task", "Some other category" };
	
	private static String rndCategory() {
		return categories[rng.nextInt(categories.length)];
	}
	
	private static String rndAuthor() {
		return rng.nextBoolean() ? "Teemu" : "Päivi";
	}
		
}
