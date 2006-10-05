/** 
 * Singleton class used for database interactions. Each public method of DBHandler class
 * encapsulates one database transaction, and thus may cause multiple inserts/updates/removes
 * with one call. The atomicity of the operations is quaranteed by using the transaction model
 * provided by the SQL standard.
 *
 * @author 
 */





public class DBHandler {
	
	/** Return all tasks of Course c */
	public Task[] getTasks(Course c) {}
	
	/** Return task identified by taskID */
	public Task getTask(String taskID) {}
	
	/** Add new task to task database. The insert will affect all courses. This operation
	 * will also create the criteria for the task */ 
	public void createTask(Task task, Criterion[] criteria) {}
	
	/** Update existing task. The update will affect all courses This operation
	 * will also update the criteria for the task */  
	public void updateTask(Task task, Criterion[] criteria) {}

	/** Remove task from task database (and thus all courses). This will also remove all stored
	 * answers of the task. */
	public void removeTask(Task task) {}
	

	/** Return all users who have attempted to solve at least one task of Course c */
	public User[] getUsers(Course c) {}
	
	/** Return user identified by userID */
	public User getUser(String userID) {}

	/** Add new user to user database */
	public void createUser(User user) {} 
	
	/** Update existing user */
	public void updateUser(User user) {} 

	
		
	/** Add criterion c to task */
	private void addCriterion(Task t, Criterion c) {}
	
	/** Remove criteria form task */
	private void removeCriteria(Task t) {}
	
	
	
	/** Add task to Course */
	private void addTask(Course c, Task task) {}
}