package fi.helsinki.cs.kohahdus.trainer;

import java.util.*;

/**
 * This class compares two Course objects lexicographicly by name.
 */

public class CourseComparator implements Comparator<Course> {
	
	/**
	 * Sorts two courses in alphabetic order by their name. Parameters may be null. Non-null
	 * objects are expected to be fully initialized.
	 * @param c1 
	 * @param c2
	 * @return -1 if c1 < c2 or c2 == null
	 * 			0 if c1 equals c2
	 * 			1 if c1 > c2 or c1 == null
	 */
	public int compare(Course c1, Course c2) {
		if (c2 == null) return -1;
		if (c1 == null) return 1;
		
		return c1.getName().compareTo(c2.getName());
	}

}
