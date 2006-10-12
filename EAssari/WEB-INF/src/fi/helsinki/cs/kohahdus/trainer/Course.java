package fi.helsinki.cs.kohahdus.trainer;


public class Course {
	
    private int modules = 0;                     // number of modules
    private String courseID = null;              //  unique course identifier
    private String courseName = null;            // 
    private String courseMetadata = null;        // 
    private String courseLogo = null;            // 
    private String courseStyle = null;           // are credits counted
    private String courseType = null;            // type of course

	public Course() {		
	}
	public Course(int modules, String courseID, String  coursetype, String courseStyle) {
	    this.modules = modules;
	    this.courseID = courseID;
	    this.courseType = coursetype;
	    this.courseStyle = courseStyle;
	}
 
	public String getCourseLogo() {
		return courseLogo;
	}
	public void setCourseLogo(String courseLogo) {
		this.courseLogo = courseLogo;
	}
	public String getCourseMetadata() {
		return courseMetadata;
	}
	public void setCourseMetadata(String courseMetadata) {
		this.courseMetadata = courseMetadata;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public void setCourseStyle(String courseStyle) {
		this.courseStyle = courseStyle;
	}
	public int getModules() {
		return modules;
	}
	public String getCourseID() {
		return courseID;
	}
	public String getCourseType() {
		return courseType;
	}
	public String getCourseStyle() {
		return courseStyle;
	}
	
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
    
    public String toString(){
    	return courseName + "(" + courseID +")";    	
    }
    
}
