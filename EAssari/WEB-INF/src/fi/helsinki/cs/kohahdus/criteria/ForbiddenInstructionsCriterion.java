package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for banned opcodes */
public class ForbiddenInstructionsCriterion extends InstructionCriterion {

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return false;
	}

}
