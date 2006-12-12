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

	/** Return human-readable name of this criterion. Unlike other Criterion types, 
	 * this does not lookup the languageBundle with it's ID, but with a constant
	 * String "SYM". Space and symbol name are appended to the string returned
	 * by the bundle. */
	@Override public String getName(ResourceBundle languageBundle) {
		return languageBundle.getString("SYM") + " " + symbolName.toUpperCase();
	}

	
	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return !symbolName.equals("") && super.hasAcceptanceTest(usingModelAnswer);  
	}	
	
	
	/** Set the symbol name this SymbolCriterion will examine */
	public void setSymbolName(String symbol) {
		if (symbol == null) { 
			symbolName = "";
		} else {
			symbolName = symbol.toLowerCase(); // Titokone always returns symbols in lowercase			
		}		
	}
	
	/** Return the symbol name this SymbolCriterion will examine. The symbol name is in
	 * lowercase characters, regardless of what was used in setSymbolName(..). */
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
		setSymbolName(parseXMLString(serializedXML, "symbol"));
	}
}
