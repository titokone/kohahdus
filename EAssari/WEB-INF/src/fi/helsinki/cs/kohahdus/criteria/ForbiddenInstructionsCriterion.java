package fi.helsinki.cs.kohahdus.criteria;

import java.util.ResourceBundle;

/** Concrete criterion class for banned opcodes */
public class ForbiddenInstructionsCriterion extends InstructionCriterion {

	/** Empty constructor for deserialization */
	protected ForbiddenInstructionsCriterion() { }

	public ForbiddenInstructionsCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return false;
	}
}
