package fi.helsinki.cs.kohahdus.trainer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mock object for testing purposes on jsp-pages. Emulates a minimal Task-object 
 * without error handling etc. 
 */

public class MockTask extends Task {

	private String language;
	
	public MockTask(String name, String category, String author, boolean inEnglish, boolean fillin) {
		super((inEnglish ? "EN" : "FI"), name, author, "description", "modelAnswer", category, (fillin ? Task.TYPE_FILL : Task.TYPE_FULL), "", "", false );

		
	}
	
	
	public MockTask(String taskid, String courseid, String moduleid, String tasktypeid, int seqno, Timestamp deadline, boolean shouldstore, boolean shouldregister, boolean shouldknowstudent, boolean shouldevaluate, int cvalue, int nooftries, Tasktype tType) {
		super(taskid, courseid, moduleid, tasktypeid, seqno, deadline, shouldstore,
				shouldregister, shouldknowstudent, shouldevaluate, cvalue, nooftries,
				tType);
		// TODO Auto-generated constructor stub
	}
	

	/** Return tasks language ("FI" or "EN") */
	public String getLanguage() {
		return this.language;
	}
	
	
	
	private static List<MockTask> tasks = null;
	private static Random rng = new Random();
	
	public static List<MockTask> getTasks() {
		if (tasks == null) {
			initTasks();
		}
		return tasks;
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
