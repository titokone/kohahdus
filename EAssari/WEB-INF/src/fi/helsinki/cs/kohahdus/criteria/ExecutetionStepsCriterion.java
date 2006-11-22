package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for program execution steps. This measures the
 * number of _executed_ instructions, whereas CodeSizeCriterion measures
 * the size of program code. */
public class ExecutetionStepsCriterion extends MeasuredCriterion {

	/** Empty constructor for deserialization */
	protected ExecutetionStepsCriterion() { }

	public ExecutetionStepsCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	/** Return the number of executed instructions */
	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getExecutionSteps();
	}

}
