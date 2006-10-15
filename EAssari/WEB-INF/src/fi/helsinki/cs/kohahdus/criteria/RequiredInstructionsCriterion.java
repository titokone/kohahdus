package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for required opcodes */
public class RequiredInstructionsCriterion extends InstructionCriterion {

	/** Empty constructor for deserialization */
	protected RequiredInstructionsCriterion() { }

	public RequiredInstructionsCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return false;
	}

}
