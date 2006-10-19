package fi.helsinki.cs.kohahdus.criteria;

import java.util.*;

public class CriterionMap extends HashMap<String,Criterion> {
	
	public SortedMap<String, RegisterCriterion> getRegisterCriteria(String idStartsWith){
		SortedMap<String, RegisterCriterion> criteria = new TreeMap<String, RegisterCriterion>();
		for (Criterion c : this.values()) {
			if (c instanceof RegisterCriterion && c.getId().startsWith(idStartsWith)) {
				criteria.put(c.getId(), (RegisterCriterion)c);
			}
		}
		return criteria;
	}
	
	public SortedMap<String, SymbolCriterion> getSymbolCriteria(String idStartsWith){
		SortedMap<String, SymbolCriterion> criteria = new TreeMap<String, SymbolCriterion>();
		for (Criterion c : this.values()) {
			if (c instanceof SymbolCriterion && c.getId().startsWith(idStartsWith)) {
				criteria.put(c.getId(), (SymbolCriterion)c);
			}
		}
		return criteria;
	}
	
	public int getSymbolCriteriaCount(){
		int count = 0;
		for (Criterion c : this.values()) {
			if (c instanceof SymbolCriterion) {
				count++;
			}
		}
		return count/2;
	}
	
	
}