package fi.helsinki.cs.kohahdus.criteria;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.HashSet;


/** Base class for banned and required opcode criteria */
public abstract class InstructionCriterion extends Criterion {
	protected Set<String> opcodes = new HashSet<String>();
	
	/** Empty constructor for deserialization */
	protected InstructionCriterion() {	}

	
	public InstructionCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}
	
	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return !opcodes.isEmpty();
	}

	@Override public String getAcceptanceTestValue() {
		String codes = opcodes.toString(); // Formated: [CODE1, CODE2, CODE3] 
		return codes.substring(1, codes.length() - 2);
	}
	
	@Override public void setAcceptanceTestValue(String test) {
		opcodes = new HashSet<String>();
		if (test != null) {
			String[] instructions = test.split("[ \t\r\f\n,;]+");
			opcodes.addAll(Arrays.asList(instructions));
		}
	}


	@Override protected String serializeSubClass() {
		return toXML("opcodes", getAcceptanceTestValue());
	}
	
	@Override protected void initSubClass(String serializedXML) {
		setAcceptanceTestValue(parseXMLString(serializedXML, "opcodes"));
	}
}
