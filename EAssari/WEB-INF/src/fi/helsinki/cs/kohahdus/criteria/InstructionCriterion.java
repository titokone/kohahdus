package fi.helsinki.cs.kohahdus.criteria;

/** Base class for banned and required opcode criteria */
public abstract class InstructionCriterion extends Criterion {
	private String opcodes = "";
	
	/** Empty constructor for deserialization */
	protected InstructionCriterion() {	}

	
	public InstructionCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return !opcodes.equals("");
	}

	@Override public String getAcceptanceTestValue() {
		return opcodes;
	}
	
	@Override public void setAcceptanceTestValue(String test) {
		if (test == null) {		
			opcodes = "";
		} else {
			// reformat test as "NOP, MUL, DIV"
			String[] instructions = test.split("[ \t\r\f\n,;]+");
			StringBuffer buffer = new StringBuffer();
			for (int i=0; i<instructions.length; i++) {
				buffer.append(instructions[i]);
				if (i < instructions.length-1) {
					buffer.append(", ");
				}
			}
			opcodes = buffer.toString();
		}
	}


	@Override protected String serializeSubClass() {
		return toXML("opcodes", opcodes);
	}
	
	@Override protected void initSubClass(String serializedXML) {
		opcodes = parseXMLString(serializedXML, "opcodes");
	}
}
