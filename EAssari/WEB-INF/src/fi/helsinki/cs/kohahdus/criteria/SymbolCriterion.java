package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for symbol values */  
public class SymbolCriterion extends VariableCriterion {
	private String symbolName;
	
	/** Empty constructor for deserialization */
	protected SymbolCriterion() { }

	public SymbolCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
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
		long value = UNDEFINED;
		int addr = answer.getSymbolAddress(symbolName);
		if (addr != -1) {
			value = answer.getMemoryLocation(addr);
		}	
		return value;
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
