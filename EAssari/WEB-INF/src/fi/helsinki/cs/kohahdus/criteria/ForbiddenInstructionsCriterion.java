package fi.helsinki.cs.kohahdus.criteria;

import java.util.ResourceBundle;
import java.util.Set;

/** Concrete criterion class for banned opcodes
 * @author Mikko Kinnunen 
 */  
public class ForbiddenInstructionsCriterion extends InstructionCriterion {

	/** Empty constructor for deserialization */
	protected ForbiddenInstructionsCriterion() { }

	public ForbiddenInstructionsCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		Set<String> usedOps = studentAnswer.getUsedOpcodes();
		Set<String> banOps  = opcodes;
		for (String usedOp : usedOps) {
			if (banOps.contains(usedOp))
				return false;	
		}		
		
		return true;
	}
}
