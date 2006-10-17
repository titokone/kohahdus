package fi.helsinki.cs.kohahdus.trainer;

import java.util.*;

public class CourseComparator implements Comparator<Course> {
	
	public int compare(Course c1, Course c2) {
		if (c2 == null) return -1;
		if (c1 == null) return 1;
		
		return c1.getName().compareTo(c2.getName());
	}

}
