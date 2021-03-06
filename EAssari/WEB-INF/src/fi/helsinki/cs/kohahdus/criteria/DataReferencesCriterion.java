package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for program data references. This measures the memory
 * references of data reads and writes. To measure total number of memory references
 * (total includes the instruction fetch reference), use MemReferencesCriterion
 * @author Mikko Kinnunen 
 */  
public class DataReferencesCriterion extends MeasuredCriterion {

	/** Empty constructor for deserialization */
	protected DataReferencesCriterion() { }

	public DataReferencesCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getDataReferenceCount();
	}

}
