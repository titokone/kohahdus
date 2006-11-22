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
	
	private static List<MockTask> tasks = null;
	private static Random rng = new Random();
	private static String[] categories = { "Easy task", "Moderate task", "Hard task", "Some other category" };

	public MockTask(String name) {
		this.setName(name);
		this.setCategory(rndCategory());
		this.setAuthor(rndAuthor());
		this.setLanguage(rng.nextBoolean() ? "EN" : "FI");
		this.setTitoTaskType(rng.nextBoolean() ? Task.TYPE_FILL : Task.TYPE_FULL);
		this.setHasSucceeded(rng.nextBoolean());
		this.setNoOfTries(rng.nextInt(10));
	}
	
	public MockTask(String name, String category, String author, boolean inEnglish, boolean fillin) {
		super();
		this.setName(name);
		this.setCategory(category);
		this.setAuthor(author);
		this.setLanguage(inEnglish ? "EN" : "FI");
		this.setTitoTaskType(fillin ? Task.TYPE_FILL : Task.TYPE_FULL);
	}
	
	public static List<MockTask> getTasks() {
		if (tasks == null) {
			initTasks();
		}
		return tasks;
	}
	
	private static void initTasks() {
		tasks = new ArrayList<MockTask>();
		for (int i=0; i<10; i++) {
			tasks.add(new MockTask("Task "+i));
		}
	}

	private static String rndCategory() {
		return categories[rng.nextInt(categories.length)];
	}
	
	private static String rndAuthor() {
		return rng.nextBoolean() ? "Teemu" : "Päivi";
	}
		
}
