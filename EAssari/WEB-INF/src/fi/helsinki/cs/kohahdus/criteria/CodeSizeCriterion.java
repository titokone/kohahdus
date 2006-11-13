package fi.helsinki.cs.kohahdus.criteria;

import java.util.ResourceBundle;

/** Concrete criterion class for code size */  
public class CodeSizeCriterion extends MeasuredCriterion {
	
	/** Empty constructor for deserialization */
	protected CodeSizeCriterion() { }

	public CodeSizeCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	/** Return number of instructions in the student's answer */
	@Override protected int getCriterionValue(TitoState studentAnswer) {
		return studentAnswer.getCodeSize();
	}
}
