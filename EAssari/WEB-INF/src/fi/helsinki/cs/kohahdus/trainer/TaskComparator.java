package fi.helsinki.cs.kohahdus.trainer;

import java.util.*;

/**
 * Compares Task objects lexicographicly. This class is used in Teacher/StudentTaskLists to sort
 * task listing by name, category etc.
 *
 */

public class TaskComparator implements Comparator<Task> {

	public static final int SORT_BY_NAME = 0;
	public static final int SORT_BY_TYPE = 1;
	public static final int SORT_BY_CATEGORY = 2;
	public static final int SORT_BY_LANGUAGE = 3;
	public static final int SORT_BY_AUTHOR = 4;
	

	private int sortBy;
	
	public TaskComparator(int sort) {
		this.sortBy = sort;
	}
	
	
	private int compareName(Task t1, Task t2) {		
		return t1.getName().compareTo(t2.getName());
	}
	
	private int compareType(Task t1, Task t2) {
		return t1.getTitoTaskType().compareTo(t2.getTitoTaskType());
	}
	
	private int compareCategory(Task t1, Task t2) {
		return t1.getCategory().compareTo(t2.getCategory());
	}

	private int compareLanguage(Task t1, Task t2) {
		return t1.getLanguage().compareTo(t2.getLanguage());
	}

	private int compareAuthor(Task t1, Task t2) {
		return t1.getAuthor().compareTo(t2.getAuthor());
	}
	
	public int compare(Task t1, Task t2) {
		if (t2 == null) return -1;
		if (t1 == null) return 1;
		
		switch (sortBy) {
		case SORT_BY_NAME:
			return compareName(t1, t2);
		case SORT_BY_TYPE:
			return compareType(t1, t2);
		case SORT_BY_CATEGORY:
			return compareCategory(t1, t2);
		case SORT_BY_LANGUAGE:
			return compareLanguage(t1, t2);
		case SORT_BY_AUTHOR:
			return compareAuthor(t1, t2);
		}
		return 0;
	}
}