package fi.helsinki.cs.kohahdus.criteria;

import java.util.*;

public class CriterionMap extends HashMap<String,Criterion> {
	
	public SortedMap<String, RegisterCriterion> getRegisterCriteria(String idStartsWith){
		SortedMap<String, RegisterCriterion> criteria = new TreeMap<String, RegisterCriterion>();
		for (Criterion c : this.values()) {
			if (c instanceof RegisterCriterion && c.getID().startsWith(idStartsWith)) {
				criteria.put(c.getID(), (RegisterCriterion)c);
			}
		}
		return criteria;
	}
	
	public SortedMap<String, SymbolCriterion> getSymbolCriteria(String idStartsWith){
		SortedMap<String, SymbolCriterion> criteria = new TreeMap<String, SymbolCriterion>();
		for (Criterion c : this.values()) {
			if (c instanceof SymbolCriterion && c.getID().startsWith(idStartsWith)) {
				criteria.put(c.getID(), (SymbolCriterion)c);
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