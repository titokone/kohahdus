package fi.helsinki.cs.kohahdus.criteria;

import java.util.ResourceBundle;
import static fi.hu.cs.ttk91.TTK91Cpu.*;

/** Concrete criterion class for register values
 * @author Mikko Kinnunen 
 */  
public class RegisterCriterion extends VariableCriterion {
	private int registerNumber;
	
	/** Maps register number to TitoKone register constant (eg. 5 -> REG_R5) */
	private static int registerCodes[] = { REG_R0, REG_R1, REG_R2, REG_R3, REG_R4, REG_R5, REG_R6, REG_R7};

	
	/** Empty constructor for deserialization */
	protected RegisterCriterion() { }
	
	public RegisterCriterion(String id, boolean usesSecretInput, int register) {
		super(id, usesSecretInput);
		if ((register < 0) || (register > 7)) {
			throw new IllegalArgumentException("Invalid register number: " + register);
		}		
		registerNumber = register;
	}

	/** Return human-readable name of this criterion. Unlike other Criterion types, 
	 * this does not lookup the languageBundle with it's ID, but with a constant
	 * String "REG". Space and register number are appended to the string returned
	 * by the bundle. */
	@Override public String getName(ResourceBundle languageBundle) {
		return languageBundle.getString("REG") + " " + "R" + registerNumber;
	}

	@Override protected long getCriterionValue(TitoState answer) {
		return answer.getRegister(registerCodes[registerNumber]);
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
