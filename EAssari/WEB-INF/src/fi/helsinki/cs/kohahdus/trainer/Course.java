package fi.helsinki.cs.kohahdus.trainer;


public class Course {
	
	/** Create new Course instance using the specified name and ID */
	public Course(String name, String id) {	
	}
	
    /** Return name of this course */
    public String getName() {
    	return null;
    }

    /** Set name of this course */
    public void setName(String name) {
    }

    /** Return course ID of this course */
    public String getID() {
    	return null;
    }
    
    /** Return all users who have solved or have tried to solve tasks from this course */
    public User[] getUsersDB() {
    	return null;
    } // Delegate to DBHandler    
}
