package fi.helsinki.cs.kohahdus.criteria;

import java.util.Set;

/** Concrete criterion class for required opcodes
 * @author Mikko Kinnunen 
 */  
public class RequiredInstructionsCriterion extends InstructionCriterion {

	/** Empty constructor for deserialization */
	protected RequiredInstructionsCriterion() { }

	public RequiredInstructionsCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		Set<String> usedOps = studentAnswer.getUsedOpcodes();
		Set<String> reqOps  = opcodes;
		return usedOps.containsAll(reqOps);
	}

}
