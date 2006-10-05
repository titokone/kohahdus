package fi.helsinki.cs.kohahdus;

import java.util.Hashtable;



public class User {
	//TODO: Tarkista että nämä ovat sama kuin eAssarin eauser.statuskenttä
	public static final int STATUS_STUDENT = 0;
	public static final int STATUS_PRIVLEDGED = 1;
	public static final int STATUS_TEACHER = 2;
	public static final int STATUS_ADMIN = 3;
	
	private String userid;
	private String lastname;
	private String firstname;
	private String email;
	private String externalid;
	private String externalid2;
	private String status;
	private String passwd;
	private String lpref;
	private java.sql.Timestamp lastvisit;
	private Hashtable activeTasks;

	
	public User() {
		this(null, null, null, null, null, null, null, null, null, null);
	}
	
	public User(String uid, String lname, String fname, String email, String status, String extid, 
			String extid2, String psw, String lpref, java.sql.Timestamp lastvisit) {
		this.userid = uid;
		this.lastname = lname;
		this.firstname = fname;
		this.email = email;
		this.status = status;
		this.externalid = extid;
		this.externalid2 = extid2;
		this.passwd = psw;
		this.lpref = lpref;
		this.lastvisit = lastvisit;
		this.activeTasks = new Hashtable(30);    
	}	
    
	/** Return true if userID exists in the user database */
	//public static boolean isUser(String userID) {}
	
	/** Retrieve user identified by userID from the user database */
	public User(String userID) {
		
	}
	
	/** Return last name of this user */
	public String getLastName() {
		return null;
	}
	
	/** Set last name of this user */
	public void setLastName(String name) {
	
	}
	
	/** Return first name of this user */
	public String getFirstName() {
		return null;
	}
	
	/** Set first name of this user */
	public void setFirstName(String name) {}
	
	/** Return email address of this user */
	public String getEmail() {
		return null;
	}
	
	/** Set email address of this user */
	public void setEmail(String addr) {}
	
	/** Return access level of this user - see static STATUS_* constants */ 
	public int getStatus() {
		return 0;
	}
	
	/** Return student number of this user. This identifier maps to <code>aeuser.extid</code> in the database */
	public String getStudentNumber() {
		return null;
	}
	
	/** Set student number of of this user */
	public void setStudentNumber(String studentnum) {}
	
	/** Return social security number of this user. This identifier maps to <code>aeuser.extid2</code> in the database */
	public String getSocialSecurityNumber() {
		return null;
	}
	
	/** Set social security number of this user */
	public void setSocialSecurityNumber(String ssn) {}
	
	/** Return plaintext password of this user */
	public String getPassword() {
		return null;
	}
	
	/** Set password of this user to Pass */
	public void setPassword(String pass) {}
	
	/** Return the preferred language of this user as String. The language is either "EN" or "FI" */
	public String getLanguage() {
		return null;
	}
	
	/** Set the preferred language of this user. The language is either "EN" or "FI
	 * @param lang either "FI" or "EN"
	 */
	public void setLanguage(String lang) {} 
	
	/** Return true of this user has the privelidges to add/remove/modify tasks and browse user statistics */
	public boolean isTeacher() {
		return false;
	}
}


