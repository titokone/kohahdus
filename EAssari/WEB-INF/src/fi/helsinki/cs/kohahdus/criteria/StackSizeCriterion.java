package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for maximum stack size
 * @author Mikko Kinnunen 
 */  
public class StackSizeCriterion extends MeasuredCriterion {

	/** Empty constructor for deserialization */
	protected StackSizeCriterion() { }

	public StackSizeCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getStackMaxSize();
	}
}
