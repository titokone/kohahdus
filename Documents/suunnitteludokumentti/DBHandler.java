





public class DBHandler {
	
	/** Return all tasks of Course c */
	public Task[] getTasks(Course c) {}
	
	/** Return task identified by taskID */
	public Task getTask(String taskID) {}
	
	/** Update existing task or add new task. The update/insert will affect all courses */ 
	public void updateTask(Task task) {} // Update or insert
	
	/** Remove task from task database (and thus all courses). This will also remove all stored
	 * answers of the task. */
	public void removeTask(Task task) {}
	
	/** Return all users who have attempted to solve at least one task of Course c */
	public User[] getUsers(Course c) {}
	
	/** Return user identified by userID */
	public User getUser(String userID) {}
	
	/** Update existing user or add new user. */
	public void updateUser(User user) {} 
}