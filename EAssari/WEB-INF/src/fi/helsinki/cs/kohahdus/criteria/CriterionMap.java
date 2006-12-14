package fi.helsinki.cs.kohahdus.criteria;

import java.util.*;
import fi.helsinki.cs.kohahdus.*;

public class CriterionMap {
	private HashMap<String,Criterion> criteriaMap = new HashMap<String,Criterion>();
	
	public HashMap<String,Criterion> getMap(){
		return criteriaMap;
	}
	
	public SortedMap<String, RegisterCriterion> getRegisterCriteria(String idStartsWith){
		SortedMap<String, RegisterCriterion> criteria = new TreeMap<String, RegisterCriterion>();
		for (Criterion c : criteriaMap.values()) {
			if (c instanceof RegisterCriterion && c.getId().startsWith(idStartsWith)) {
				criteria.put(c.getId(), (RegisterCriterion)c);
			}
		}
		return criteria;
	}
	
	public boolean isEmpty(){
		return criteriaMap.isEmpty();
	}
	
	public SortedMap<String, SymbolCriterion> getSymbolCriteria(String idStartsWith){
		SortedMap<String, SymbolCriterion> criteria = new TreeMap<String, SymbolCriterion>();
		for (Criterion c : criteriaMap.values()) {
			if (c instanceof SymbolCriterion && c.getId().startsWith(idStartsWith)) {
				criteria.put(c.getId(), (SymbolCriterion)c);
			}
		}
		return criteria;
	}
	
	public int getSymbolCriterionCount(){
		int count = 0;
		for (Criterion c : criteriaMap.values()) {
			if (c instanceof SymbolCriterion) {
				count++;
			}
		}
		return count/2;
	}
	
	public int getCriterionCount(){
		int count = criteriaMap.size();
		//Log.write("getCriterionCount:"+count);
		return count;
	}
	
	public List<Criterion> getList() {
		return new LinkedList<Criterion>(criteriaMap.values());
	}
	
}