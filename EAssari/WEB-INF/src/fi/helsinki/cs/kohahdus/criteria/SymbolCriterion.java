package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for symbol values */  
public class SymbolCriterion extends VariableCriterion {
	private String symbolName;
	
	/** Empty constructor for deserialization */
	protected SymbolCriterion() { }

	public SymbolCriterion(String id, boolean usesSecretInput, String comparisonOperator, String symbol) {
		super(id, usesSecretInput, comparisonOperator);
		symbolName = (symbol != null) ? symbol : "";
	}
	
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
