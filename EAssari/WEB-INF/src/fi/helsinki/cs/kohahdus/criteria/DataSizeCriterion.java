package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for program code size */
public class DataSizeCriterion extends MeasuredCriterion {
	
	/** Empty constructor for deserialization */
	protected DataSizeCriterion() { }

	public DataSizeCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getDataSize();
	}

}
