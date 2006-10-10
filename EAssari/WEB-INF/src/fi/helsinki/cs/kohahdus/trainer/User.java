package fi.helsinki.cs.kohahdus.trainer;

import java.util.Hashtable;



public class User {
	public static final String STATUS_STUDENT = "student";
//	public static final String STATUS_PRIVLEDGED = "privileged";
	public static final String STATUS_TEACHER = "teacher";
//	public static final String STATUS_ADMIN = "adm";
	


	
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
	//TODO - what if userID isn't found from db?
	//		 Do we create a new object with null fields or throw an execption?
	public User(String userID) {
		this.userid = userID;	
	}
	
	
	/** Set last name of this user */
	public void setLastName(String name) {
		lastname = name;
	}
	
	
	/** Set first name of this user */
	public void setFirstName(String name) {
		firstname = name;
	}
	

	
	/** Set email address of this user */
	public void setEmail(String addr) {
		email = addr;
	}
	

	/** Return student number of this user. This identifier maps to <code>aeuser.extid</code> in the database */
	public String getStudentNumber() {
		return externalid;
	}
	
	/** Set student number of of this user */
	public void setStudentNumber(String studentnum) {
		externalid = studentnum;
	}
	
	/** Return social security number of this user. This identifier maps to <code>aeuser.extid2</code> in the database */
	public String getSocialSecurityNumber() {
		return externalid2;
	}
	
	/** Set social security number of this user */
	public void setSocialSecurityNumber(String ssn) {
		externalid2 = ssn;
	}
	
	
	/** Set password of this user to Pass */
	public void setPassword(String pass) {
		passwd = pass;
	}
	
	
	/** Set the preferred language of this user. The language is either "EN" or "FI
	 * @param lang either "FI" or "EN"
	 */
	public void setLanguage(String lang) {
		
	} 
	
	/** Return true of this user has the privelidges to add/remove/modify tasks and browse user statistics */
	public boolean isTeacher() {
		
	}

	public void setStatus(String status) {
	}
	
		
	
	
	public boolean isValid(){
		if (userid == null) return false;
		return true;
	}
	

	
	
	
	
	
	
	
	
// **** N�M� AINAKIN OVAT VANHASTA EASSARISTA *****	

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
	
	/** Return email address of this user */
	public String getEmail() {
		return email;
	}
	/** Return plaintext password of this user */
	public String getPassword() {
		return passwd;
	}
	/** Return last name of this user */
	public String getLastname() {
		return lastname;
	}
	/** Return the preferred language of this user as String. The language is either "EN" or "FI" */
	public String getLpref() {
		return lpref;
	}
	/** Return first name of this user */
    public String getFirstName() {
    	return firstname;
    }
	
	
	
	
    public String getUserid() {
    	return userid;
    }
    
    public int registerTry(String target, boolean wassuccess, boolean wasintime, int currentcredit) {
		String state = (String) activeTasks.get(target);
		String newState = null;
		int trc = 0;
		int points = 0;
		if (state == null) {
			if (wasintime)
				newState = "YES";
			else if (wassuccess)
				newState = "LATE";
			else
				newState = "NO";
			trc = 1;
			activeTasks.put(target, "1:" + currentcredit + ":" + newState);
		} else {
			int colon = state.indexOf(":");
			trc = Integer.parseInt(state.substring(0, colon));
			int col2 = state.indexOf(":", colon + 1);
			points = Integer.parseInt(state.substring(colon + 1, col2));
			String oldstate = state.substring(col2 + 1);
			newState = oldstate;
			if (wasintime && !state.equals("YES"))
				newState = "YES";
			else if (wassuccess && state.equals("NO"))
				newState = "LATE";
			trc++;
			if (points < currentcredit)
				points = currentcredit;
			activeTasks.put(target, trc + ":" + points + ":" + newState);
		}
		return trc;
	}

	public int getAttempts(String target) {
		String state = (String) activeTasks.get(target);
		if (state == null)
			return 0;
		else {
			int colon = state.indexOf(":");
			int trc = Integer.parseInt(state.substring(0, colon));
			return trc;
		}
	}

	public String getState(String target) {
		String st = (String) activeTasks.get(target);
		String state = null;
		if (st == null)
			state = "NONE";
		else {
			int colon = st.indexOf(":");
			int col2 = st.indexOf(":", colon + 1);
			state = st.substring(col2 + 1);
		}
		return state;
	}

	public int getCurrentCredit(String target) {
		String state = (String) activeTasks.get(target);
		if (state == null)
			return 0;
		else {
			int colon = state.indexOf(":");
			int colon2 = state.indexOf(":", colon + 1);
			int trc = Integer.parseInt(state.substring(colon + 1, colon2));
			return trc;
		}
	}
    
	
	
}


