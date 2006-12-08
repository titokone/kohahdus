package fi.helsinki.cs.kohahdus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import fi.helsinki.cs.kohahdus.criteria.Criterion;
import fi.helsinki.cs.kohahdus.criteria.CriterionMap;
import fi.helsinki.cs.kohahdus.trainer.Course;
import fi.helsinki.cs.kohahdus.trainer.Task;
import fi.helsinki.cs.kohahdus.trainer.TitoFeedback;
import fi.helsinki.cs.kohahdus.trainer.User;


/** Singleton class used for database interactions. Each public method of DBHandler class
 * encapsulates one database transaction, and thus may cause multiple inserts/updates/removes
 * with one call. The atomicity of the operations is quaranteed by using the transaction model
 * provided by the SQL standard.
 *
 * @author 
 */

public class DBHandler {
	private static final String DEFAULT_LANGUAGE = "EN"; 
	private static final String DEFAULT_TASKTYPE = "titotask"; 
	private static final String DEFAULT_MODULE_ID = "0"; 
	private static final String DEFAULT_MODULE_TYPE = "training"; 
	private static final String ATTRIBUTE_TYPE_TASK = "T"; 
	private static final String ATTRIBUTE_TYPE_CATEGORY = "Q"; 
	private static final String ATTRIBUTE_ID_CATEGORY = "Q"; 
	private static final String ATTRIBUTE_NAME_CATEGORY = "Q"; 
	private static final String ATTRIBUTE_VALUE_TYPE_CHARACTER = "C";	// Character value 
	private static final String ATTRIBUTE_VALUE_TYPE_NUMERIC = "N";		// Numeric value 
	
	private static DBHandler instance = null;
	private String dbDriver;
	private String dbServer;
	private String dbLogin;
	private String dbPassword; 
    
   
	private DBHandler(){
	}
	
	public static DBHandler getInstance(){
		return instance;		
	}
	
	public static boolean initialize(String connectionString, String username, String password){
		if (instance != null){
			Log.write("Failed to initialize. DBHandler already initialized.");
			return false;
		}
		instance = new DBHandler();
		return instance.init(connectionString, username, password);
	}
	
	private boolean init(String connectionString, String username, String password){
		dbDriver = "oracle.jdbc.OracleDriver";
		dbServer = connectionString;
		dbLogin  = username;
		dbPassword = password; 
        
		// TODO: initialize db connection pool
		
		Log.write("DBHandler initialized.");
		return true;
	}
	
	protected void release(Connection conn) throws SQLException{
		if (conn != null) conn.close();
	}

	// TODO: DBConnectionPool
	///      !!!!!!!!!!!!!!!!!! Tosi likainen ratkaisu ennen DBConnectionPool luokkaa
	protected Connection getConnection () throws SQLException {
		// load database driver if not already loaded
		//Log.write("DBHandler: Getting connection...");
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
		//Log.write("DBHandler: Got connection.");
        return conn;
	}	

	/** Return all courses */
	public LinkedList<Course> getCourses() throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<Course> courses = new LinkedList<Course>();
		try {
			st = conn.prepareStatement("select * from course");
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				Course course = new Course();
				course.setCourseID(rs.getString("courseid"));
				course.setCourseName(rs.getString("coursename"));
				course.setCourseMetadata(rs.getString("coursemetadata"));
				course.setCourseLogo(rs.getString("courselogo"));
				course.setCourseStyle(rs.getString("coursestyle"));
				courses.add(course);
			}
			Log.write("DBHandler: Fetched " +courses.size() + " courses from DB.");
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch courses from DB. " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		//return (Course[])courses.toArray();
		return courses;
	}
	
	/** Return all courses */
	public String getCourseName(String courseID) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		String courseName = null;
		try {
			st = conn.prepareStatement("select coursename from course where courseid=?");
			st.setString(1, courseID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			if (rs.next()){
				courseName = rs.getString("coursename");
			}
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch course name from DB. " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return courseName;
	}
	
	/**
	 * Adds a new course to database and linkes the course to all tasks.
	 */
	public synchronized boolean createCourse(Course course) throws SQLException {
		if (!addCourse(course)) return false;
		
		// Add the default dummy module for the course
		if (!addModule(course.getCourseID())) return false;
		
		// Links all tasks with the course
		List<Task> tasks = getTasks();
		for (Task task : tasks) {
			if (!addTaskInModule(course.getCourseID(), task.getTaskID())) return false;
		}
		
		// Todo: rollback in case of failing of one of the methods above.
	
		return true;
	}

	/** 
	 * Add new course to database. Does not check weather the course already exists in the DB. 
	 */
	private boolean addCourse(Course course) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into course (courseid, coursename, coursemetadata, courselogo, coursestyle) " +
									   "values (common_seq.nextval,?,?,?,?)"); 
			st.setString(1, course.getCourseName());
			st.setString(2, course.getCourseMetadata());
			st.setString(3, course.getCourseLogo());
			st.setString(4, course.getCourseStyle());
			int c = st.executeUpdate();
			if (c > 0){
				st = conn.prepareStatement("select common_seq.currval as courseid from dual");
				ResultSet rs = st.executeQuery();
				if (rs.next()){
					course.setCourseID(rs.getString("courseid"));
					Log.write("DBHandler: course " +course+ " added to DB ");
					rs.close();
					return true;
				}
				rs.close();
			}
			Log.write("DBHandler: Failed to add course " +course);
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to create course "+course+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 

	/** 
	 * Remove course from database (and thus all refering taskinmodules).
	 */
	public boolean removeCourse(String courseID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from taskinmodule where courseid=?");
			st.setString(1, courseID);			
			st.executeUpdate();
			st.close();
			
			st = conn.prepareStatement("delete from module where courseid=?");
			st.setString(1, courseID);			
			st.executeUpdate();
			st.close();
			
			st = conn.prepareStatement("delete from course where courseid=?");
			st.setString(1, courseID);
			st.executeUpdate();
			return true;
		} catch (SQLException e){
			Log.write("DBHandler: Failed to remove course " +courseID+". " +e);
			e.printStackTrace();
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();
		}	
		// Todo: rollback in case of failing of one of the methods above.	
	}
	
	
	
	/** Add a default module to course. */
	private boolean addModule(String courseID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into module (courseid, moduleid, moduletype) values (?,?,?)"); 
			st.setString(1, courseID);
			st.setString(2, DBHandler.DEFAULT_MODULE_ID);
			st.setString(3, DBHandler.DEFAULT_MODULE_TYPE);
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: default module added to course " +courseID);
				return true;
			} else {
				Log.write("DBHandler: Failed to add module to course " +courseID);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add module to course "+courseID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 

	/** Adds a taskinmodule entry so that the task is linked with the course. */
	private boolean addTaskInModule(String courseID, String taskID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into taskinmodule (courseid, moduleid, taskid, seqno, numberoftries) " +
									   "values (?,?,?,common_seq.nextval,999999)"); 
			st.setString(1, courseID);
			st.setString(2, DBHandler.DEFAULT_MODULE_ID);
			st.setString(3, taskID);
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: taskinmodule added: course=" +courseID+", task="+taskID);
				return true;
			} else {
				Log.write("DBHandler: Failed to add taskinmodule: course=" +courseID+", task="+taskID);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add taskinmodule: course=" +courseID+", task="+taskID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 
	
	
	/** Returns templates for tasks in finnish and english. Adding more templates shouldn't interfere
	 *  with implementation on teacherTaskList.jsp */
	public List<Task> getTemplates() throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<Task> tasks = new LinkedList<Task>();
		try {
			st = conn.prepareStatement("select * from task where taskid = 'EN_TEMPLATE' or taskid = 'FI_TEMPLATE'");
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				String taskID = rs.getString("taskid");				
				Task task = new Task();
				task.setTaskID(taskID);
				task.setName(rs.getString("taskname"));
				task.setAuthor(rs.getString("author"));
				task.setModificationDate((rs.getDate("datecreated")));			
				task.deserializeFromXML(rs.getString("taskmetadata"));
				task.setNoOfTries(rs.getInt("numberoftries_def"));
				task.setShouldStore("N".equals(rs.getString("shouldstoreanswer_def")) ? false : true);
				task.setShouldRegister("N".equals(rs.getString("shouldregistertry_def")) ? false : true);
				task.setShouldKnow("N".equals(rs.getString("shouldknowstudent_def")) ? false : true);
				task.setShouldEvaluate("N".equals(rs.getString("shouldevaluate_def")) ? false : true);
				task.setCutoffvalue(rs.getInt("cutoffvalue"));
				tasks.add(task);
			} 
			Log.write("DBHandler: Fetched " +tasks.size() + " templates from DB.");
			rs.close();			
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch templates from DB. " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return tasks;
	}
	
	
	
	/** Return all tasks */
	public List<Task> getTasks() throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<Task> tasks = new LinkedList<Task>();
		try {
			st = conn.prepareStatement("select * from task");
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				String taskID = rs.getString("taskid");
				if (("EN_TEMPLATE".equals(taskID)) || ("FI_TEMPLATE".equals(taskID))) {
					continue;
				}				
				Task task = new Task();
				task.setTaskID(taskID);
				task.setName(rs.getString("taskname"));
				task.setAuthor(rs.getString("author"));
				// Todo: implement these fields
				task.setModificationDate((rs.getDate("datecreated")));			
				//task.setTasktype(rs.getString("tasktype"));
				task.deserializeFromXML(rs.getString("taskmetadata"));
				task.setNoOfTries(rs.getInt("numberoftries_def"));
				task.setShouldStore("N".equals(rs.getString("shouldstoreanswer_def")) ? false : true);
				task.setShouldRegister("N".equals(rs.getString("shouldregistertry_def")) ? false : true);
				task.setShouldKnow("N".equals(rs.getString("shouldknowstudent_def")) ? false : true);
				task.setShouldEvaluate("N".equals(rs.getString("shouldevaluate_def")) ? false : true);
				task.setCutoffvalue(rs.getInt("cutoffvalue"));
				tasks.add(task);
			} 
			Log.write("DBHandler: Fetched " +tasks.size() + " tasks from DB.");
			rs.close();			
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch tasks from DB. " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return tasks;
	}
	
	/** Return all tasks with students status info */
	public List<Task> getTasks(String courseID, String userID) throws SQLException{
		List<Task> tasks = getTasks();
		HashMap<String, AnswerState> states = getAnswerStates(userID, courseID, DEFAULT_MODULE_ID);
		
		for (Task t : tasks) {
			AnswerState s = states.get(t.getTaskID());
			if (s != null) {
				t.setNoOfTries(s.getLastTryNumber());
				t.setHasSucceeded(s.getHasSucceeded());
			}
		}
		
		return tasks;
	}
	
	/** Add answer state to a task object */
	private HashMap<String, AnswerState> getAnswerStates(String userID, String courseID, String moduleID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		HashMap<String, AnswerState> states = new HashMap<String, AnswerState>();
		
		try {
			st = conn.prepareStatement("select seqno, lasttrynumber, hassucceeded from studentmodel " +
									   "where sid=? and courseid=? and moduleid=? ");
			
			st.setString(1, userID);
			st.setString(2, courseID);
			st.setString(3, moduleID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()) {
				AnswerState state = new AnswerState();
				
				state.setLastTryNumber(rs.getInt("lasttrynumber"));
				state.setHasSucceeded("Y".equals(rs.getString("hassucceeded")));
				
				states.put(rs.getString("seqno"), state);
			} 
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to get number of tries for user "+userID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}
		return states;
	} 
	
	/** Return task identified by taskID */
	public Task getTask(String taskID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		Task task = null;
		try {
			st = conn.prepareStatement("select * from task where taskid=?");
			st.setString(1, taskID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			if (rs.next()){
				task = new Task();
				task.setTaskID(taskID);
				task.setName(rs.getString("taskname"));
				task.setAuthor(rs.getString("author"));
				// TODO: implement these fields
				//most of these are needed in composer to fill default values.
				//task.setFillInPostCode();
				//task.setFillInPreCode();
				//task.setFillInTask();
				//task.setTitoTaskType();
				//task(rs.getDate("datecreate"));			
				//task.setTasktype(rs.getString("tasktype"));
				task.deserializeFromXML(rs.getString("taskmetadata"));
				task.setNoOfTries(rs.getInt("numberoftries_def"));
				task.setShouldStore("N".equals(rs.getString("shouldstoreanswer_def")) ? false : true);
				task.setShouldRegister("N".equals(rs.getString("shouldregistertry_def")) ? false : true);
				task.setShouldKnow("N".equals(rs.getString("shouldknowstudent_def")) ? false : true);
				task.setShouldEvaluate("N".equals(rs.getString("shouldevaluate_def")) ? false : true);
				task.setCutoffvalue(rs.getInt("cutoffvalue"));
				Log.write("DBHandler: Fetched " +task+ " from DB.");
			} 
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch tasks from DB. " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}
		return task;
	}

	/** 
	 * Adds a new task to task database. The insert will affect all courses. This operation
	 * also adds all the criteria for the task to database
	 */ 
	public boolean createTask(Task task, List<Criterion> criteria) throws SQLException{
		return createTask(task, criteria, null);
	}
	public synchronized boolean createTask(Task task, List<Criterion> criteria, String defaultTaskID) throws SQLException{
		Log.write("DBHandler: Creating a task and criteria to DB: name=" +task.getName()+ ", id="+task.getTaskID()+", criteriaCount="+criteria.size());
		if (!addTask(task, defaultTaskID)) return false;
		
		// Link the new task with all existing courses 
		LinkedList<Course> courses = getCourses();
		for (Course c : courses){
			if (!addTaskInModule(c.getCourseID(), task.getTaskID())) return false;
		}
		
		// Add criterions to the db
		for (Criterion c : criteria) {
			if (!addCriterion(task, c)) return false;
		}
		
		// Todo: rollback in case of failing of one of the methods above.
	
		return true;
	}

	/**
	 * Adds a task to DB without any linkage to courses and modules 
	 */
	public boolean addTask(Task task) throws SQLException{
		return addTask(task, null);
	}
	public boolean addTask(Task task, String defaultTaskID) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st1 = null;
		PreparedStatement st2 = null;
		try {
			if (defaultTaskID == null) {
				st1 = conn.prepareStatement("insert into task (taskname, author, datecreated, cutoffvalue, tasktype, taskmetadata, taskid) " +
											"values (?,?,sysdate,?,?,?,common_seq.nextval)");
			} else {
				st1 = conn.prepareStatement("insert into task (taskname, author, datecreated, cutoffvalue, tasktype, taskmetadata, taskid) " +
				   							"values (?,?,sysdate,?,?,?,?)");
				st1.setString(6, defaultTaskID);
			}
			st1.setString(1, task.getName());
			st1.setString(2, task.getAuthor());
			st1.setInt(3, task.getCutoffvalue());
			st1.setString(4, DBHandler.DEFAULT_TASKTYPE);
			st1.setString(5, task.serializeToXML()); // Contains TitoTrainer specific data
			
			if (st1.executeUpdate() > 0){
				Log.write("DBHandler: Task added to DB: "+task);
				if (defaultTaskID == null) { 
					st2 = conn.prepareStatement("select common_seq.currval from dual");
					ResultSet rs = st2.executeQuery();
					if (rs.next()){
						task.setTaskID(rs.getString(1));
					}
					rs.close();
				} else {
					task.setTaskID(defaultTaskID);
				}
				return true;
			} else {
				Log.write("DBHandler: Failed to add task to DB: " +task);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add task to DB: " +task+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st1 != null) st1.close();			
			if (st2 != null) st2.close();			
		}	
		return true;
	}
	
	/** Update existing task. The update will affect all courses. This operation
	 * will also update the criteria for the task */  
	public boolean updateTask(Task task, List<Criterion> criteria) throws SQLException{
		if (!updateTask(task)) return false;
		
		// Update criterions to the db
		removeCriteria(task);
		for (Criterion c : criteria) {
			if (!addCriterion(task, c)) return false;
		}
		
		// Todo: rollback in case of failing of one of the methods above.
	
		return true;
	}

	/** Updates a task to DB without modifying any courses or modules */
	private boolean updateTask(Task task) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update task set taskname=?, author=?, cutoffvalue=?, tasktype=?, taskmetadata=?, datecreated=sysdate " +
									   "where taskid=?"); 
			st.setString(1, task.getName());
			st.setString(2, task.getAuthor());
			st.setInt(3, task.getCutoffvalue());
			st.setString(4, DBHandler.DEFAULT_TASKTYPE);
			st.setString(5, task.serializeToXML());
			st.setString(6, task.getTaskID());
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: Task updated to DB: name=" +task.getName()+ ", id="+task.getTaskID());
				return true;
			} else {
				Log.write("DBHandler: Failed to update task to DB: name=" +task.getName()+ ", id="+task.getTaskID());
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to update task to DB: name=" +task.getName()+ ", id="+task.getTaskID()+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	}
		
	/** Updates a task's metadata to DB */
	private boolean updateTaskMetadata(Task task) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update task set taskmetadata=? where taskid=?"); 
			st.setString(1, task.serializeToXML());
			st.setString(2, task.getTaskID());
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: Task metadata updated to DB: name=" +task.getName()+ ", id="+task.getTaskID());
				return true;
			} else {
				Log.write("DBHandler: Failed to update task metadata to DB: name=" +task.getName()+ ", id="+task.getTaskID());
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to update task metadata to DB: name=" +task.getName()+ ", id="+task.getTaskID()+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	}
		
	/** Remove task from task database (and thus all courses). This will also remove all stored
	 * answers of the task. */
	public boolean removeTask(Task task) throws SQLException {
		// TODO: Tämä metodi on Mikon vääntämä, Taro saa korjata
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
/*			st = conn.prepareStatement("delete from storedanswer where taskid=?");
			st.setString(1, task.getTaskID());
			st.executeUpdate();
			
			st = conn.prepareStatement("delete from studentmodel where taskid=?");
			st.setString(1, task.getTaskID());
			st.executeUpdate();
*/			
			// Seuraava SQL-lause poistaa myös kriteerit
			removeCriteria(task); 
			
			st = conn.prepareStatement("delete from attributevalues where objecttype=? and objectid=?");
			st.setString(1, "T");
			st.setString(2, task.getTaskID());
			st.executeUpdate();

			st = conn.prepareStatement("delete from taskinmodule where taskid=?");
			st.setString(1, task.getTaskID());			
			st.executeUpdate();			
			
			st = conn.prepareStatement("delete from task where taskid=?");
			st.setString(1, task.getTaskID());			
			st.executeUpdate();
			return true;
		} catch (SQLException e){
			Log.write("DBHandler: Failed to remove task '" +task.getName()+"', taskid="+task.getTaskID()+". " +e);
			e.printStackTrace();
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();
		}	
		// Todo: rollback in case of failing of one of the methods above.	
	}
	
	
	/** Adds criterion c to task */
	private boolean addCriterion(Task t, Criterion c) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into attributevalues (objecttype, objectid, attributename, language, valuetype, attributevalue) " +
									   "values (?,?,?,?,?,?)"); 
			st.setString(1, DBHandler.ATTRIBUTE_TYPE_TASK);
			st.setString(2, t.getTaskID());
			st.setString(3, c.getId());
			st.setString(4, t.getLanguage());
			st.setString(5, DBHandler.ATTRIBUTE_VALUE_TYPE_CHARACTER);
			st.setString(6, c.serializeToXML());
			if (st.executeUpdate() > 0){
				Log.write("DBHandler: Criterion added: task=" +t.getName()+", criterion="+c.getId());
				return true;
			} else {
				Log.write("DBHandler: Failed to add criterion: task=" +t.getName()+", criterion="+c.getId());
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add criterion: task=" +t.getName()+", criterion="+c.getId()+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	}
	
	/** Updates criterion c of task */
	private boolean updateCriterion(Task t, Criterion c) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update attributevalues set attributevalue=? " +
									   "where objecttype=? and objectid=? and attributename=? and language=?"); 
			st.setString(1, c.serializeToXML());
			st.setString(2, DBHandler.ATTRIBUTE_TYPE_TASK);
			st.setString(3, t.getTaskID());
			st.setString(4, c.getId());
			st.setString(5, t.getLanguage());
			if (st.executeUpdate() > 0){
				Log.write("DBHandler: Criterion updated: task=" +t.getName()+", criterion="+c.getId());
				return true;
			} else {
				Log.write("DBHandler: Failed to update criterion: task=" +t.getName()+", criterion="+c.getId());
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to update criterion: task=" +t.getName()+", criterion="+c.getId()+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	}
	
	/** Remove criteria form task */
	private void removeCriteria(Task t) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from attributevalues " +
									   "where objecttype=? and objectid=? and language=?"); 
			st.setString(1, DBHandler.ATTRIBUTE_TYPE_TASK);
			st.setString(2, t.getTaskID());
			st.setString(3, t.getLanguage());
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: "+c+" criterions deleted: task=" +t.getName()+", taskid="+t.getTaskID());
			} else {
				Log.write("DBHandler: Failed to delete criterion: task=" +t.getName()+", taskid="+t.getTaskID());
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to delete criterion: task=" +t.getName()+", taskid="+t.getTaskID()+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
	}

	/** Return the criteria of a task */
	public LinkedList<Criterion> getCriteria(Task task) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<Criterion> criteria = new LinkedList<Criterion>();
		try {
			st = conn.prepareStatement("select * from attributevalues " +
									   "where objecttype=? and objectid=?");
			st.setString(1, DBHandler.ATTRIBUTE_TYPE_TASK);
			st.setString(2, task.getTaskID());
			//st.setString(3, task.getLanguage());
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				criteria.add(Criterion.deserializeFromXML(rs.getString("attributevalue")));
			} 
			Log.write("DBHandler: Fetched " +criteria.size() + " criteria with task="+task.getName()+ ", taskid="+task.getTaskID());
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch criteria with task="+task.getName()+", taskid="+task.getTaskID()+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return criteria;
	}	
	
	/** Return the criteria of a task in a map */
	public CriterionMap getCriteriaMap(Task task) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		CriterionMap criteria = new CriterionMap();
		try {
			st = conn.prepareStatement("select * from attributevalues " +
									   "where objecttype=? and objectid=?");
			st.setString(1, DBHandler.ATTRIBUTE_TYPE_TASK);
			st.setString(2, task.getTaskID());
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				Criterion c = Criterion.deserializeFromXML(rs.getString("attributevalue"));
				criteria.put(c.getId(), c);
			} 
			Log.write("DBHandler: Fetched " +criteria.size() + " criteria with task="+task.getName()+ ", taskid="+task.getTaskID());
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch criteria with task="+task.getName()+", taskid="+task.getTaskID()+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}
		return criteria;
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
				user.setStudentNumber(rs.getString("extid"));
				user.setSocialSecurityNumber(rs.getString("extid2"));
				user.setStatus(rs.getString("status"));
			} else {
				Log.write("DBHandler: user not found with " +userID+ " and with given password");
			}
			rs.close();			
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add user " +userID+ ". " +e);
			throw e;
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
				user.setStudentNumber(rs.getString("extid"));
				user.setSocialSecurityNumber(rs.getString("extid2"));
				user.setStatus(rs.getString("status"));
			} else {
				Log.write("DBHandler: user not found with " +userID);
			}
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to get user "+userID+". " +e);
			throw e;
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
			throw e;
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
			st = conn.prepareStatement("update eauser set lastname=?, firstname=?, email=?, status=?, extid=?, extid2=?, password=?, lpref=?, lastvisit=sysdate " +
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
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 	
		
	
	/** Remove selected user */
	public void removeUser(String userID) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete eauser where userid=?"); 
			st.setString(1, userID);
			st.executeUpdate();
			if (st != null) st.close();			
			
			st = conn.prepareStatement("delete storedanswer where sid=?"); 
			st.setString(1, userID);
			st.executeUpdate();
			if (st != null) st.close();			
			
			st = conn.prepareStatement("delete studentmodel where sid=?"); 
			st.setString(1, userID);
			st.executeUpdate();
			
			Log.write("DBHandler: User " +userID+ " removed from database ");
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to remove user "+userID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
	} 	
	
	
	
	
	/** Return user identified by name or a fragment of a name (lastname or firstname) */
	public LinkedList<User> getUsers(String query) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<User> users = new LinkedList<User>();
		try {
			st = conn.prepareStatement("select * from eauser where lower(firstname) like ? or lower(lastname) like ? " +
									   "or lower(extid) like ? or lower(extid2) like ? or lower(userid) like ?");
			StringTokenizer names = new StringTokenizer(query);
			while (names.hasMoreTokens()) {
				String name = names.nextToken().toLowerCase();
				st.setString(1, "%"+name+"%");
				st.setString(2, "%"+name+"%");
				st.setString(3, "%"+name+"%");
				st.setString(4, "%"+name+"%");
				st.setString(5, "%"+name+"%");
				st.executeQuery();
				ResultSet rs = st.getResultSet();
				while (rs.next()){
					User user = new User(rs.getString("userid"));
					user.setEmail(rs.getString("email"));
					user.setFirstName(rs.getString("firstname"));
					user.setLastName(rs.getString("lastname"));
					user.setLanguage(rs.getString("lpref"));
					user.setPassword(rs.getString("password"));
					user.setStudentNumber(rs.getString("extid"));
					user.setSocialSecurityNumber(rs.getString("extid2"));
					user.setStatus(rs.getString("status"));
					users.add(user);
				} 
				rs.close();
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to get users with query "+query+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return users;
	} 

	
	/**
	 * Stores the answer of the student into database table storedanswer. 
	 * Also stores the state of the student's answered task into table studentmodel.
	 * 
	 */
	public void storeStateAndAnswer(String userID, String courseID, String taskID, String answer, 
									   String input, TitoFeedback feedback, String language) throws SQLException {
		int seqNo = getTaskSequenceNumber(courseID, taskID);
		AnswerState state = getAnswerState(userID, courseID, DBHandler.DEFAULT_MODULE_ID, seqNo);
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into storedanswer (sid, courseid, moduleid, seqno, trynumber, " +
									   "correctness, whenanswered, answer, feedbacklanguage, feedback) " +
									   "values (?,?,?,?,?,?,sysdate,?,?,?)"); 
			st.setString(1, userID);
			st.setString(2, courseID);
			st.setString(3, DBHandler.DEFAULT_MODULE_ID);
			st.setInt(4, seqNo);
			st.setInt(5, state.getLastTryNumber() + 1);
			st.setInt(6, feedback.isSuccessful() ? 100 : 0);
			st.setString(7, answer);
			st.setString(8, language);
			st.setString(9, feedback.getOverallFeedback());
			int c = st.executeUpdate();
			if (c > 0){
				Log.write("DBHandler: Answer stored for user " +userID);
				state.incrementLastTryNumber();
				if (!state.getHasSucceeded()) state.setHasSucceeded(feedback.isSuccessful());
				if (state.getCurrentResult() == 0) state.setCurrentResult(feedback.isSuccessful() ? 100 : 0);
			} else {
				Log.write("DBHandler: Failed store answer for user " +userID);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to store answer for user "+userID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		
		// Finally store the answer state
		storeAnswerState(userID, courseID, seqNo, state);
	}

	
	/**
	 * Stores the state of an answer into database table studentmodel. 
	 */
	private void storeAnswerState(String userID, String courseID, int seqNo, AnswerState state) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			if (state.getLastTryNumber() ==  1){
				st = conn.prepareStatement("insert into studentmodel (sid, courseid, moduleid, seqno, lasttrynumber, " +
										   "currentresult, hassucceeded, wascreditedintime) values (?,?,?,?,?,?,?,'Y')");
				st.setString(1, userID);
				st.setString(2, courseID);
				st.setString(3, DBHandler.DEFAULT_MODULE_ID);
				st.setInt(4, seqNo);
				st.setInt(5, state.getLastTryNumber());
				st.setInt(6, state.getCurrentResult());
				st.setString(7, state.getHasSucceeded() ? "Y" : "N");
				int c = st.executeUpdate();
				if (c > 0){
					Log.write("DBHandler: Answer state inserted for user " +userID);
				} else {
					Log.write("DBHandler: Failed to insert answer state for user " +userID);
				}
			} else {
				st = conn.prepareStatement("update studentmodel set lasttrynumber=?, currentresult=?, hassucceeded=? " +
										   "where sid=? and courseid=? and moduleid=? and seqno=?");
				st.setInt(1, state.getLastTryNumber());
				st.setInt(2, state.getCurrentResult());
				st.setString(3, state.getHasSucceeded() ? "Y" : "N");
				st.setString(4, userID);
				st.setString(5, courseID);
				st.setString(6, DBHandler.DEFAULT_MODULE_ID);
				st.setInt(7, seqNo);
				int c = st.executeUpdate();
				if (c > 0){
					Log.write("DBHandler: Answer state updated for user " +userID);
				} else {
					Log.write("DBHandler: Failed to update answer state for user " +userID);
				}
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to update/insert answer state for user "+userID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
	}

	
	/**
	 *  Returns a list of tasks (as AnswerStates) that student has answered.
	 *  If courseID == null, return all answers of a student. 
	 */
	public StudentAnswers getStudentAnswers(String userID) throws SQLException {
		return getStudentAnswers(userID, null);
	}
	public StudentAnswers getStudentAnswers(String userID, String courseID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		StudentAnswers studentsAnswers = new StudentAnswers();
		
		try {
			String sql = "select u.firstname, u.lastname, sm.hassucceeded, sm.lasttrynumber, t.taskname, sa.whenanswered, t.taskid, c.coursename, c.courseid " +
			   			 "from studentmodel sm, eauser u, task t, storedanswer sa, taskinmodule tim, course c " +
			   			 "where sm.sid=? and sm.sid=u.userid and sm.seqno=tim.seqno and sm.courseid=c.courseid and " +
			   			 "sa.trynumber=sm.lasttrynumber and tim.taskid=t.taskid and sa.seqno=sm.seqno and sm.sid=sa.sid";
			if (courseID != null){
				sql += " and sm.courseid=?"; 
	   			st = conn.prepareStatement(sql);
	   			st.setString(2, courseID);
			} else {
	   			st = conn.prepareStatement(sql);
			}
			st.setString(1, userID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				AnswerState m = new AnswerState();
				m.setFirstname(rs.getString("firstname"));
				m.setLastname(rs.getString("lastname"));
				m.setHasSucceeded("Y".equals(rs.getString("hassucceeded")));
				m.setLastTryNumber(rs.getInt("lasttrynumber"));
				m.setTaskName(rs.getString("taskname"));
				m.setAnswerTime(rs.getTimestamp("whenanswered"));
				m.setUserID(userID);
				m.setCourseName(rs.getString("coursename"));
				studentsAnswers.putAnswerState(rs.getString("courseid")+"_"+rs.getString("taskid"), m);
			} 
			rs.close();
			

		} catch (SQLException e){
			Log.write("DBHandler: Failed to get student answers: "+userID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	

		return studentsAnswers;
	}	
	
	/** Return answer state for a task */
	private AnswerState getAnswerState(String userID, String courseID, String moduleID, int seqNo) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		AnswerState state = new AnswerState();
		try {
			st = conn.prepareStatement("select lasttrynumber, currentresult, hassucceeded from studentmodel " +
									   "where sid=? and courseid=? and moduleid=? and seqno=?");
			st.setString(1, userID);
			st.setString(2, courseID);
			st.setString(3, moduleID);
			st.setInt(4, seqNo);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			if (rs.next()){
				state.setLastTryNumber(rs.getInt("lasttrynumber"));
				state.setCurrentResult(rs.getInt("currentresult"));
				state.setHasSucceeded("Y".equals(rs.getString("hassucceeded")));
			} 
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to get number of tries for user "+userID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return state;
	} 
	
	/** Returns a sequence number for a task in a module */
	private int getTaskSequenceNumber(String courseID, String taskID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		int seqNo = 0;
		try {
			st = conn.prepareStatement("select seqno from taskinmodule where courseid=? and taskid=? and moduleid=?");
			st.setString(1, courseID);
			st.setString(2, taskID);
			st.setString(3, DBHandler.DEFAULT_MODULE_ID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			if (rs.next()){
				seqNo = rs.getInt("seqno");
			} 
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to get seqno. " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return seqNo;
	} 

	/**
	 *  Returns a list of students containing all the answer states that student has answered. 
	 */
	public LinkedList<StudentAnswers> getAllStudentAnswers(String courseID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<StudentAnswers> students = new LinkedList<StudentAnswers>();
		
		try {
			st = conn.prepareStatement("select u.userid, u.lastname, u.firstname, u.extid, u.extid2, t.taskname, t.taskid, sm.hassucceeded " +
									   "from studentmodel sm, course c, task t, taskinmodule tim, eauser u " +
									   "where sm.courseid=? and sm.courseid=c.courseid and t.taskid=tim.taskid and " +
									   "sm.seqno=tim.seqno and u.userid=sm.sid order by 1");
			st.setString(1, courseID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			String userID = null;
			StudentAnswers studentAnswers = null; 
			while (rs.next()){
				AnswerState m = new AnswerState();
				m.setFirstname(rs.getString("firstname"));
				m.setLastname(rs.getString("lastname"));
				m.setHasSucceeded("Y".equals(rs.getString("hassucceeded")));
				m.setTaskName(rs.getString("taskname"));
				m.setUserID(rs.getString("userid"));
				m.setstudentnumber(rs.getString("extid"));
				m.setsocialsecuritynumber(rs.getString("extid2"));
				
				
				if (!rs.getString("userid").equals(userID)){
					userID = rs.getString("userid");
					studentAnswers = new StudentAnswers(m.getFirstname(), m.getLastname(), m.getUserID(), m.getstudentnumber(), m.getsocialsecuritynumber());
					students.addLast(studentAnswers);
				}
				studentAnswers.getAnswerMap().put(rs.getString("taskname"), m);
			} 
			rs.close();
			

		} catch (SQLException e){
			Log.write("DBHandler: Failed to get all student answers: "+courseID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	

		return students;
	}	
	
	/**
	 *  Returns a list of task names that have been answered. 
	 */
	public LinkedList<String> getAnsweredTaskNames(String courseID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<String> taskNames = new LinkedList<String>();
		
		try {
			st = conn.prepareStatement("select distinct t.taskname from task t, studentmodel sm, taskinmodule tim " +
									   "where sm.seqno=tim.seqno and tim.taskid=t.taskid and sm.courseid=? order by taskname");
			st.setString(1, courseID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				taskNames.add(rs.getString("taskname"));
			} 
			rs.close();
			

		} catch (SQLException e){
			Log.write("DBHandler: Failed to get answered tasks names: "+courseID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	

		return taskNames;
	}	

	
	/** Return a answer a student has given on last try */
	public String getStudentAnswer(String userID, String courseID, String taskID) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		String answer = "";
		try {
			String sql = "select sa.answer from storedanswer sa, taskinmodule tim " +
			   			 "where sa.sid=? and sa.seqno=tim.seqno and tim.taskid=? and " +
			   			 "sa.courseid=? order by sa.whenanswered desc";
   			st = conn.prepareStatement(sql);
			st.setString(1, userID);
   			st.setString(2, taskID);
   			st.setString(3, courseID);
			ResultSet rs = st.executeQuery();
			if (rs.next()){
				answer = rs.getString("answer");
				Log.write("DBHandler: Got student answer. userID="+userID+" taskID="+taskID);
			} else {
				Log.write("DBHandler: Failed get student answer. userID="+userID+" taskID="+taskID);
			}
			rs.close();
			

		} catch (SQLException e){
			Log.write("DBHandler: Failed to get student answer: "+userID+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	

		return answer;
	}	
	
	
	/** Return a list of categories */
	public LinkedList<String> getCategories() throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<String> categories = new LinkedList<String>();
		try {
			st = conn.prepareStatement("select * from attributevalues " +
									   "where objecttype=? and objectid=?");
			st.setString(1, DBHandler.ATTRIBUTE_TYPE_CATEGORY);
			st.setString(2, DBHandler.ATTRIBUTE_ID_CATEGORY);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				categories.add(rs.getString("attributename"));
			} 
			Log.write("DBHandler: Fetched " +categories.size() + " categories");
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch categories with. " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return categories;
	}	
	
	/** Adds a new category  */
	public void addCategory(String name) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into attributevalues (objecttype, objectid, attributename, valuetype, language) " +
									   "values (?,?,?,?,?)"); 
			st.setString(1, DBHandler.ATTRIBUTE_TYPE_CATEGORY);
			st.setString(2, DBHandler.ATTRIBUTE_ID_CATEGORY);
			st.setString(3, name);
			st.setString(4, DBHandler.ATTRIBUTE_VALUE_TYPE_CHARACTER);
			st.setString(5, DBHandler.DEFAULT_LANGUAGE);
			if (st.executeUpdate() > 0){
				Log.write("DBHandler: Category added: " +name);
			} else {
				Log.write("DBHandler: Failed to add category: " +name);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add category: " +name+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
	}
	
	/** Deletes a category. Before deletion check if the category is being used in a task.  */
	public boolean checkAndDeleteCategory(String name) throws SQLException{
		for (Task task : getTasks()) {
			if (task.getCategory().equalsIgnoreCase(name)) {
				Log.write("DBHandler: Category already in use. Failed to delete category: " +name);
				return false;
			}
		}
		
		// If the category is not in use then delete it from db
		return deleteCategory(name);
	}
	
	/** Deletes a category  */
	private boolean deleteCategory(String name) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from attributevalues where objecttype=? and objectid=? and attributename=?"); 
			st.setString(1, DBHandler.ATTRIBUTE_TYPE_CATEGORY);
			st.setString(2, DBHandler.ATTRIBUTE_ID_CATEGORY);
			st.setString(3, name);
			if (st.executeUpdate() > 0){
				Log.write("DBHandler: Category deleted: " +name);
				return true;
			} else {
				Log.write("DBHandler: Failed to delete category: " +name);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to delete category: " +name+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	}
	
	/** updates category table and update all matching categories within tasks  */
	public boolean updateCategoryAndTasks(String oldCategory, String newCategory) throws SQLException{
		if (!updateCategory(oldCategory, newCategory)) return false; 
		for (Task task : getTasks()) {
			if (task.getCategory().equalsIgnoreCase(oldCategory)) {
				task.setCategory(newCategory);
				updateTaskMetadata(task);
			}
		}
		return true;
	}
	/** updates a category  */
	private boolean updateCategory(String oldCategory, String newCategory) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update attributevalues set attributename=? where objecttype=? and objectid=? and attributename=?"); 
			st.setString(1, newCategory);
			st.setString(2, DBHandler.ATTRIBUTE_TYPE_CATEGORY);
			st.setString(3, DBHandler.ATTRIBUTE_ID_CATEGORY);
			st.setString(4, oldCategory);
			if (st.executeUpdate() > 0){
				Log.write("DBHandler: Category updated to: " +newCategory);
				return true;
			} else {
				Log.write("DBHandler: Failed to update category: " +oldCategory);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to update category: " +oldCategory+". " +e);
			throw e;
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	}
	
	
}



