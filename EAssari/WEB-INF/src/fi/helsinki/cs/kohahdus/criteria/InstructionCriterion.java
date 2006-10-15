package fi.helsinki.cs.kohahdus.criteria;

/** Base class for banned and required opcode criteria */
public abstract class InstructionCriterion extends Criterion {
	
	/** Empty constructor for deserialization */
	protected InstructionCriterion() {	}

	
	public InstructionCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	protected String opcodes = "";


	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return !opcodes.equals("");
	}

	@Override public String getAcceptanceTest() {
		return opcodes;
	}
	
	@Override public void setAcceptanceTest(String test) {
	}


	@Override protected String serializeSubClass() {
		return toXML("opcodes", opcodes);
	}
	
	@Override protected void initSubClass(String serializedXML) {
		opcodes = parseXMLString(serializedXML, "opcodes");
	}
}
