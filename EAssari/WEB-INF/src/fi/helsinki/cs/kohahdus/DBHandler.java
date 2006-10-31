package fi.helsinki.cs.kohahdus;

import java.sql.*;
import java.util.*;

import fi.helsinki.cs.kohahdus.trainer.*;
import fi.helsinki.cs.kohahdus.criteria.*;


/** Singleton class used for database interactions. Each public method of DBHandler class
 * encapsulates one database transaction, and thus may cause multiple inserts/updates/removes
 * with one call. The atomicity of the operations is quaranteed by using the transaction model
 * provided by the SQL standard.
 *
 * @author 
 */

/* Lisäsin pari huomautusta tähän luokkaan ja ne saa poistaa kun olet lukaissut ne.
   Opiskelijan tehtävälistausta säädettäessä haluttiin merkata ne tehtävät, jotka on jo
   tehty/yritetty. Tällä hetkellä ei ole minkäänlaista keinoa kaivaa suoritustietoja
   kannasta. Task/User olioihin tätä tietoa ei kannattane lisätä, joten onnistuisikohan
   esim. metodi joka ottaa parametrikseen kurssin ja opiskelijan ja palauttaa jonkinlaisen 
   listan tai Map:in suorituksista.  
*/

public class DBHandler {
	private static final String DEFAULT_TASKTYPE = "titotask"; 
	private static final String DEFAULT_MODULE_ID = "0"; 
	private static final String DEFAULT_MODULE_TYPE = "training"; 
	private static final String ATTRIBUTE_TYPE_TASK = "T"; 
	private static final String ATTRIBUTE_VALUE_TYPE_CHARACTER = "C";	// Character value 
	private static final String ATTRIBUTE_VALUE_TYPE_NUMERIC = "N";		// Numeric value 
	
	private static DBHandler instance = new DBHandler();
	private String dbDriver = "oracle.jdbc.OracleDriver";
	private String dbServer = "jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test";
	private String dbLogin  = "kohahdus";
	private String dbPassword = "b1tt1"; 
    
   
	private DBHandler(){
		init();	
	}
	
	public static DBHandler getInstance(){
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
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		//return (Course[])courses.toArray();
		return courses;
	}
	
	/* Tämä ei ole ihan näin simppeli tapaus vaan uusi kurssi olisi hyvä saattaa käyttövalmiiseen
	   tilaan. Eli kaikki muillakin kursseilla esiintyvät tehtävät tulisi linkittää myös
	   tähän uuteen kurssiin.	
	*/
	public synchronized boolean createCourse(Course course) throws SQLException {
		if (!addCourse(course)) return false;
		
		// Add the default dummy module for the course
		if (!addModule(course.getCourseID())) return false;
		
		// Links all tasks with the course
		LinkedList<Task> tasks = getTasks();
		for (Task task : tasks) {
			if (!addTaskInModule(course.getCourseID(), task.getTaskID())) return false;
		}
		
		// Todo: rollback in case of failing of one of the methods above.
	
		return true;
	}

	/** Add new course to database. Does not check weather the course already exists in the DB. */
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
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
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
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 
	
	/** Return all tasks */
	public LinkedList<Task> getTasks() throws SQLException{
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
				//task(rs.getDate("datecreate"));			
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
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return tasks;
	}
	
	/** Return all tasks */
	public LinkedList<Task> getTasks(String courseID, String userID) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		LinkedList<Task> tasks = new LinkedList<Task>();
		try {
			st = conn.prepareStatement("select t.*, sm.lasttrynumber, sm.hassucceeded" +
									   "from studentmodel sm, taskidmodule tim, task t " +
									   "where sm.sid=? and sm.courseid=? and sm.courseid=tim.courseid " +
									   "and sm.moduleid=tim.moduleid and sm.seqno=tim.seqno " +
									   "and tim.taskid=t.taskid and tim.moduleid=?");
			st.setString(1, userID);
			st.setString(2, courseID);
			st.setString(3, DBHandler.DEFAULT_MODULE_ID);
			st.executeQuery();
			ResultSet rs = st.getResultSet();
			while (rs.next()){
				Task task = new Task();
				task.setTaskID(rs.getString("taskid"));
				task.setName(rs.getString("taskname"));
				task.setAuthor(rs.getString("author"));
				// Todo: implement these fields
				//task(rs.getDate("datecreate"));
				//task.setTasktype(rs.getString("tasktype"));
				task.deserializeFromXML(rs.getString("taskmetadata"));
				task.setNoOfTries(rs.getInt("lasttrynumber"));
				task.setShouldStore("N".equals(rs.getString("shouldstoreanswer_def")) ? false : true);
				task.setShouldRegister("N".equals(rs.getString("shouldregistertry_def")) ? false : true);
				task.setShouldKnow("N".equals(rs.getString("shouldknowstudent_def")) ? false : true);
				task.setShouldEvaluate("N".equals(rs.getString("shouldevaluate_def")) ? false : true);
				task.setCutoffvalue(rs.getInt("cutoffvalue"));
				task.setHasSucceeded("N".equals(rs.getString("hassucceded")) ? false : true);
				tasks.add(task);
			} 
			Log.write("DBHandler: Fetched " +tasks.size() + " tasks with course="+courseID+ ", user="+userID);
			rs.close();
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to fetch tasks with course="+courseID+ ", user="+userID+". " +e);
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return tasks;
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
		} finally {
			release(conn);
			if (st != null) st.close();			
		}
		return task;
	}

	/** Adds a new task to task database. The insert will affect all courses. This operation
	 * also adds all the criteria for the task to database*/ 
	public synchronized boolean createTask(Task task, List<Criterion> criteria) throws SQLException{
		Log.write("DBHandler: Creating a task and criteria to DB: name=" +task.getName()+ ", id="+task.getTaskID()+", criteriaCount="+criteria.size());
		if (!addTask(task)) return false;
		
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

	/** Adds a task to DB without any linkage to courses and modules */
	//TODO: add TitoTrainer specific data somehere
	//At least these fields from Task:
	//description; pinput; sinput; modelAnswer; category; taskType; fillInPreCode; fillInPostCode; useModel; passFeedBack; failFeedBack;
	public boolean addTask(Task task) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st1 = null;
		PreparedStatement st2 = null;
		try {
			st1 = conn.prepareStatement("insert into task (taskid, taskname, author, datecreated, cutoffvalue, tasktype, taskmetadata) " +
									   "values (common_seq.nextval,?,?,sysdate,?,?,?)"); 
			st1.setString(1, task.getName());
			st1.setString(2, task.getAuthor());
			st1.setInt(3, task.getCutoffvalue());
			st1.setString(4, DBHandler.DEFAULT_TASKTYPE);
			st1.setString(5, task.serializeToXML());
			//Log.write("DBHandler: Executing insert...");			
			if (st1.executeUpdate() > 0){
				Log.write("DBHandler: Task added to DB: "+task);
				st2 = conn.prepareStatement("select common_seq.currval from dual");
				ResultSet rs = st2.executeQuery();
				if (rs.next()){
					task.setTaskID(rs.getString(1));
				}
				rs.close();
				return true;
			} else {
				Log.write("DBHandler: Failed to add task to DB: " +task);
			}
			
		} catch (SQLException e){
			Log.write("DBHandler: Failed to add task to DB: " +task+". " +e);
		} finally {
			release(conn);
			if (st1 != null) st1.close();			
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
	//TODO: päivitetään myös ne kentät jotka tarvitsee lisätä createTask metodiin 
	private boolean updateTask(Task task) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update task set taskname=?, author=?, cutoffvalue=?, tasktype=?, taskmetadata=? " +
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
		} finally {
			release(conn);
			if (st != null) st.close();
		}	
		return false;		
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
			// TODO: Kysytään, että käytetäänkö taskeissa monenkielisiä criteereitä...
			//st.setString(3, task.getLanguage());
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
				Log.write("DBHandler: user not found with " +userID+ "/" +password);
			}
			rs.close();			
			
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
				user.setStudentNumber(rs.getString("extid"));
				user.setSocialSecurityNumber(rs.getString("extid2"));
				user.setStatus(rs.getString("status"));
			} else {
				Log.write("DBHandler: user not found with " +userID);
			}
			rs.close();
			
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
		} finally {
			release(conn);
			if (st != null) st.close();			
		}	
		return false;
	} 	
		
}



