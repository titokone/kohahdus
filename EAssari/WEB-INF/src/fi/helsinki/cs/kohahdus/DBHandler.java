package fi.helsinki.cs.kohahdus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import trainer.DatabaseException;

/** 
 * Singleton class used for database interactions. Each public method of DBHandler class
 * encapsulates one database transaction, and thus may cause multiple inserts/updates/removes
 * with one call. The atomicity of the operations is quaranteed by using the transaction model
 * provided by the SQL standard.
 *
 * @author 
 */
public class DBHandler {
	
	private static DBHandler instance = null;
	private String dbDriver =null;
	private String dbServer = null;
	private String dbLogin = null;
	private String dbPassword = null;
    
   
	private DBHandler(){
		init();
	}
	
	public static synchronized DBHandler getInstance(){
		if (instance == null) {
			instance = new DBHandler();
		}
		return instance;		
	}
	
	private boolean init(){
        dbDriver = db_Driver;
        dbServer = db_Server;
        dbLogin  = db_Login;
        dbPassword = db_Password; 
		
		// TODO: initialize db connection pool
		return true;
	}

	// TODO: DBConnectionPool
	///      !!!!!!!!!!!!!!!!!! Tosi likainen ratkaisu ennen DBConnectionPool luokkaa
	protected Connection getConnection () throws DatabaseException {
	    // load database driver if not already loaded
		Connection conn= null;
		try { 
		  Class.forName(dbDriver);               // load driver if not loaded
		  //  Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) { 
		     throw new DatabaseException ("Couldn't find the database driver "+dbDriver);
		}
		try {
		   conn= DriverManager.getConnection(dbServer, dbLogin, dbPassword);
		} catch (SQLException sex) {
		   throw new DatabaseException("Couldn't establish a repository connection. ");
        }
        return conn;
	}	

		
	/** Return all tasks of Course c */
	public Task[] getTasks(Course c) {
		return null;
	}
	
	/** Return task identified by taskID */
	public Task getTask(String taskID) {
		return null;
	}
	
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
	public User[] getUsers(Course c) {
		return null;
	}
	
	/** Return user identified by userID */
	public User getUser(String userID) {
		return null;
	}

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