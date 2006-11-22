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
	public static final int SORT_BY_STATUS = 5;
	public static final int SORT_BY_TRIES = 6;
	

	private int sortBy;
	
	public TaskComparator(int sort) {
		this.sortBy = sort;
	}
	
	
	private int compareName(Task t1, Task t2) {	
		if (t2.getName() == null) return -1;
		if (t1.getName() == null) return 1;
		return t1.getName().compareTo(t2.getName());
	}
	
	private int compareType(Task t1, Task t2) {
		if (t2.getTitoTaskType() == null) return -1;
		if (t1.getTitoTaskType() == null) return 1;
		return t1.getTitoTaskType().compareTo(t2.getTitoTaskType());
	}
	
	private int compareCategory(Task t1, Task t2) {
		if (t2.getCategory() == null) return -1;
		if (t1.getCategory() == null) return 1;
		return t1.getCategory().compareTo(t2.getCategory());
	}

	private int compareLanguage(Task t1, Task t2) {
		if (t2.getLanguage() == null) return -1;
		if (t1.getLanguage() == null) return 1;
		return t1.getLanguage().compareTo(t2.getLanguage());
	}

	private int compareAuthor(Task t1, Task t2) {
		if (t2.getLanguage() == null) return -1;
		if (t1.getLanguage() == null) return 1;
		return t1.getAuthor().compareTo(t2.getAuthor());
	}
	
	private int compareStatus(Task t1, Task t2) {
		int t1status = t1.isHasSucceeded()    ? 2 
				     : t1.getNoOfTries() == 0 ? 1
				     :                          0
				     ;
		int t2status = t2.isHasSucceeded()    ? 2 
					 : t2.getNoOfTries() == 0 ? 1
					 :                          0
					 ;
		
//		if (t1.isHasSucceeded() || t2.getNoOfTries() == 0) return -1;
//		if (t2.isHasSucceeded()) return 1;		
		return t2status - t1status;
	}
	
	private int compareTries(Task t1, Task t2) {
		if (t1.getNoOfTries() < t2.getNoOfTries()) return -1;
		return (t1.getNoOfTries() == t2.getNoOfTries()) ? 0 : 1;
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
		case SORT_BY_STATUS:
			return compareStatus(t1, t2);
		case SORT_BY_TRIES:
			return compareTries(t1, t2);
		}
		return 0;
	}
}