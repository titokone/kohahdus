package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for program code size
 * @author Mikko Kinnunen 
 */  
public class DataAreaSizeCriterion extends MeasuredCriterion {
	
	/** Empty constructor for deserialization */
	protected DataAreaSizeCriterion() { }

	public DataAreaSizeCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getDataSize();
	}

}
