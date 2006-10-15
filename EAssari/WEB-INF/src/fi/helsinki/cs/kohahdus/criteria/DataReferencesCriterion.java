package fi.helsinki.cs.kohahdus.criteria;

/** Concrete criterion class for program date references. This measures the
 * memory references of data reads and writes. To measure total number of
 * memory references, use MemReferencesCriterion */
public class DataReferencesCriterion extends MeasuredCriterion {

	/** Empty constructor for deserialization */
	protected DataReferencesCriterion() { }

	public DataReferencesCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getMemoryAccessCount() - studentAnswer.getInstructionCount();
	}

}
