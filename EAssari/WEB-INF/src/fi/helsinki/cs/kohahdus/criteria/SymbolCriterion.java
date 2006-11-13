package fi.helsinki.cs.kohahdus.criteria;

import java.util.HashMap;
import java.util.ResourceBundle;

/** Concrete criterion class for symbol values */  
public class SymbolCriterion extends VariableCriterion {
	private String symbolName = "";
	
	/** Empty constructor for deserialization */
	protected SymbolCriterion() { }

	public SymbolCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override public String getName(ResourceBundle languageBundle) {
		return languageBundle.getString("SYM") + " " + symbolName;
	}

	
	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return !symbolName.equals("") && super.hasAcceptanceTest(usingModelAnswer);  
	}	
	
	
	/** Set the symbol name this SymbolCriterion will examine
	 * @param symbol string containing characters: A-Z, a-z, 0-9, _ */
	public void setSymbolName(String symbol) {
		if ((symbol == null) || ((symbol.matches("\\W")))) { 
			symbolName = "";
		} else {
			symbolName = symbol;			
		}		
	}
	
	/** Return the symbol name this SymbolCriterion will examine */
	public String getSymbolName() {
		return symbolName;
	}

	@Override protected long getCriterionValue(TitoState answer) {
		HashMap symbols = answer.getSymbolTable();
		Integer addr = (Integer)symbols.get(symbolName);
		return (addr == null) ? UNDEFINED : answer.getMemoryLocation(addr);
	}	

	@Override protected String serializeSubClass() {
		return super.serializeSubClass() +
			   toXML("symbol", symbolName);
	}

	@Override protected void initSubClass(String serializedXML) {
		super.initSubClass(serializedXML);
		symbolName = parseXMLString(serializedXML, "symbol");
	}
}
