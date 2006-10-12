package fi.helsinki.cs.kohahdus;

import java.sql.*;

import fi.helsinki.cs.kohahdus.trainer.Course;
import fi.helsinki.cs.kohahdus.trainer.Task;
import fi.helsinki.cs.kohahdus.trainer.User;

/** Singleton class used for database interactions. Each public method of DBHandler class
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
        dbDriver = "oracle.jdbc.OracleDriver";
        dbServer = "jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test";
        dbLogin  = "kohahdus";
        dbPassword = "b1tt1"; 
		
		// TODO: initialize db connection pool
		return true;
	}
	
	protected void release(Connection conn) throws SQLException{
		if (conn != null) conn.close();
	}

	// TODO: DBConnectionPool
	///      !!!!!!!!!!!!!!!!!! Tosi likainen ratkaisu ennen DBConnectionPool luokkaa
	protected Connection getConnection () throws SQLException {
	    // load database driver if not already loaded
		Connection conn = null;
		try { 
		  Class.forName(dbDriver);               // load driver if not loaded
		} catch (ClassNotFoundException e) { 
			throw new SQLException ("Couldn't find the database driver "+dbDriver);
		}
		try {
		   conn = DriverManager.getConnection(dbServer, dbLogin, dbPassword);
		} catch (SQLException sex) {
			throw new SQLException("Couldn't establish a repository connection. ");
        }
        return conn;
	}	

	/** Return all courses */
	public Course[] getCourses() {
		return null;
	}
	
	
	/** Return all tasks */
	public Task[] getTasks() {
		return null;
	}
	
	/** Return task identified by taskID */
	public Task getTask(String taskID) {
		return null;
	}

	/** Return the criteria of Task task */
	public Criterion[] getCriteria(Task task) {
		return null;
	}	
	
	/** Add new task to task database. The insert will affect all courses. This operation
	 * will also create the criteria for the task */ 
	public void createTask(Task task, Criterion[] criteria) {
		
	}
	
	/** Update existing task. The update will affect all courses This operation
	 * will also update the criteria for the task */  
	public void updateTask(Task task, Criterion[] criteria) {
		
	}

	/** Remove task from task database (and thus all courses). This will also remove all stored
	 * answers of the task. */
	public void removeTask(Task task) {
		
	}

	/** Return all users who have attempted to solve at least one task of Course c */
	public User[] getUsers(Course c) throws SQLException {
		return null;
	}
	
	/** Return user identified by userID and password (login) */
	public User getUser(String userID, String password) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		User user = null;
		try {
			st = conn.prepareStatement("select * from eauser where userid=? and password=?");
			st.setString(1, userID);
			st.setString(2, password);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			if (rs.next()){
 				user = new User(userID);
				user.setEmail(rs.getString("email"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setLanguage(rs.getString("lpref"));
				user.setPassword(rs.getString("password"));
				user.setStudentNumber(rs.getString("extid2"));
				user.setSocialSecurityNumber(rs.getString("extid"));
				user.setStatus(rs.getString("status"));
			} else {
				Log.write("DBHandler: user not found with " +userID+ "/" +password);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add user " +userID+ ". " +e);
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return user;
	}


	/** Return user identified by userID */
	public User getUser(String userID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		User user = null;
		try {
			st = conn.prepareStatement("select * from eauser where userid=?");
			st.setString(1, userID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			if (rs.next()){
				user = new User(userID);
				user.setEmail(rs.getString("email"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setLanguage(rs.getString("lpref"));
				user.setPassword(rs.getString("password"));
				user.setStudentNumber(rs.getString("extid2"));
				user.setSocialSecurityNumber(rs.getString("extid"));
				user.setStatus(rs.getString("status"));
			} else {
				Log.write("DBHandler: user not found with " +userID);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to get user "+userID+". " +e);
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return user;
	} 
	
	/** Add new user to user database. Does not check weather the user already exists in the DB. */
	public boolean createUser(User user) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into eauser (userid, lastname, firstname, email, status, extid, extid2, password, lpref, lastvisit) " +
									   "values (?,?,?,?,?,?,?,?,?,sysdate)"); 
			st.setString(1, user.getUserID());
			st.setString(2, user.getLastName());
			st.setString(3, user.getFirstName());
			st.setString(4, user.getEmail());
			st.setString(5, user.getStatus());
			st.setString(6, user.getStudentNumber());
			st.setString(7, user.getSocialSecurityNumber());
			st.setString(8, user.getPassword());
			st.setString(9, user.getLpref());
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: User " +user.getUserID()+ " added to DB ");
				return true;
			} else {
				Log.write("DBHandler: Failed to add user " +user.getUserID());
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to create user "+user.getUserID()+". " +e);
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 
	
	/** Update existing user to DB with userID. Does not check weather the user exists or not. */
	public boolean updateUser(User user) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update eauser set lastname=?, firstname=?, email=?, status=?, extid=?, extid2=?, password=?, lpref=?, lastvisit=sysdate) " +
									   "where userid=?"); 
			st.setString(1, user.getLastName());
			st.setString(2, user.getFirstName());
			st.setString(3, user.getEmail());
			st.setString(4, user.getStatus());
			st.setString(5, user.getStudentNumber());
			st.setString(6, user.getSocialSecurityNumber());
			st.setString(7, user.getPassword());
			st.setString(8, user.getLpref());
			st.setString(9, user.getUserID());
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: User " +user.getUserID()+ " updated to DB ");
				return true;
			} else {
				Log.write("DBHandler: Failed to update user " +user.getUserID()+ ". User probably does not exists in the DB.");
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to update user "+user.getUserID()+". " +e);
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 	
		
	/** Add criterion c to task */
	private void addCriterion(Task t, Criterion c) {}
	
	/** Remove criteria form task */
	private void removeCriteria(Task t) {}
		
	/** Add task to Course */
	private void addTask(Course c, Task task) {}
}



