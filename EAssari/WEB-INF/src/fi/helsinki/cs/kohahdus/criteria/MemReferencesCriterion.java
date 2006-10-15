package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for program memory references. This measures the
 * total number of memory references, which includes the reference to read
 * an the next instruction to be executed. To measure data-references only, 
 * use DataReferenceCriterion. */
public class MemReferencesCriterion extends MeasuredCriterion {

	/** Empty constructor for deserialization */
	protected MemReferencesCriterion() { }

	
	public MemReferencesCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getMemoryAccessCount();
	}

}
