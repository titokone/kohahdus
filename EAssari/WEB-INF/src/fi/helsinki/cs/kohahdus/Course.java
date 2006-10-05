package fi.helsinki.cs.kohahdus;


public class Course {
    /** Retrieve course identified by courseID from the course database */
    Course(String courseID) {}
    
    /** Return name of this course */
    public String getName() {
    	return null;
    }

    /** Set name of this course */
    public String setName(String name) {
    	return null;
    }

    /** Return all users who have solved or have tried to solve tasks from this course */
    public User[] getUsersDB() {
    	return null;
    } // Delegate to DBHandler

    // Implement if needed
    //void addTask(Task task)
    //void removeTask(Task task)
    //String getId()
}