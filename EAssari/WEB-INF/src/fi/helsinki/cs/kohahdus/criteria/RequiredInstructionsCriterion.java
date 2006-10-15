package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for required opcodes */
public class RequiredInstructionsCriterion extends InstructionCriterion {

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return false;
	}

}
