package fi.helsinki.cs.kohahdus.criteria;

import java.util.*;

public class CriterionMap extends HashMap<String,Criterion> {
	
	public SortedMap<String, RegisterCriterion> getRegisterCriteria(String idStartsWith){
		SortedMap criteria = new TreeMap();
		for (Criterion c : this.values()) {
			if (c instanceof RegisterCriterion && c.getID().startsWith(idStartsWith)) {
				criteria.put(c.getID(), c);
			}
		}
		return criteria;
	}
	
	public SortedMap<String, SymbolCriterion> getSymbolCriteria(String idStartsWith){
		SortedMap criteria = new TreeMap();
		for (Criterion c : this.values()) {
			if (c instanceof SymbolCriterion && c.getID().startsWith(idStartsWith)) {
				criteria.put(c.getID(), c);
			}
		}
		return criteria;
	}
	
	
}