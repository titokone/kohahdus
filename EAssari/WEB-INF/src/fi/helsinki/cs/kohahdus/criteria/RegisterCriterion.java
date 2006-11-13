package fi.helsinki.cs.kohahdus.criteria;

import java.util.ResourceBundle;

/** Concrete criterion class for register values */  
public class RegisterCriterion extends VariableCriterion {
	private int registerNumber;
	
	/** Empty constructor for deserialization */
	protected RegisterCriterion() { }
	
	public RegisterCriterion(String id, boolean usesSecretInput, int register) {
		super(id, usesSecretInput);
		if ((register < 0) || (register > 7)) {
			throw new IllegalArgumentException("Invalid register number: " + register);
		}		
		registerNumber = register;
	}
	
	@Override public String getName(ResourceBundle languageBundle) {
		return languageBundle.getString("REG") + " " + "R" + registerNumber;
	}

	@Override protected long getCriterionValue(TitoState answer) {
		return answer.getRegister(registerNumber);
	}
	
	@Override protected String serializeSubClass() {
		return super.serializeSubClass() +
			   toXML("reg", registerNumber);
	}

	@Override protected void initSubClass(String serializedXML) {
		super.initSubClass(serializedXML);
		registerNumber = (int)parseXMLLong(serializedXML, "reg");
	}
	

}
