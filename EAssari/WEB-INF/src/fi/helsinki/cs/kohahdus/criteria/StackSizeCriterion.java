package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for maximum stack size */
public class StackSizeCriterion extends MeasuredCriterion {

	/** Empty constructor for deserialization */
	protected StackSizeCriterion() {
		// TODO Auto-generated constructor stub
	}

	public StackSizeCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getStackMaxSize();
	}
}
