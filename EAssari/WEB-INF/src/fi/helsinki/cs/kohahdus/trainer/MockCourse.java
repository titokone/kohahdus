package fi.helsinki.cs.kohahdus.trainer;

import java.util.List;
import java.util.ArrayList;

/**
 * Mock object for testing jsp-pages. Emulates functions of a normal course
 * object with minimal implementation.
 */


public class MockCourse extends Course {

	private String name;
	private String id;
		
	public MockCourse(String name, String id) {
		super(name, id);
		
		this.name = name;
		this.id = id;
	}
    /** Return name of this course */
    public String getName() {
    	return name;
    }

    /** Set name of this course */
    public void setName(String name) {
    	this.name = name;
    }

    /** Return course ID of this course */
    public String getCourseID() {
    	return id;
    }
    
    /**
     * Returns an array with some mock objects for testing purposes.
     * More or less equivalent with DBHandler.getCourses()
     * @return an array of predefined MockCourse objects
     */
    private static List<MockCourse> courses = null;
    
    public static List<MockCourse> getCourses() {
    	if (courses == null) {
    		initCourses();
    	}
    	return courses;
    }
    
    public static void createCourse(MockCourse c) {
    	if (courses == null) {
    		initCourses();
    	}
    	courses.add(c);
    }
    
    private static void initCourses() {
    	courses = new ArrayList<MockCourse>();
    	for (int i=0; i<5; i++) {   		
    		courses.add(new MockCourse("Spring "+(2006+i), "ID"+i));
    		courses.add(new MockCourse("Fall "+(2006+i), "ID"+(i+5)));
    	}
    }   
}
