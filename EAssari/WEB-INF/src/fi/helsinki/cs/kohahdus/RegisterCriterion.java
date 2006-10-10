package fi.helsinki.cs.kohahdus;

/* Vain mallliksi kriteerien toimintalogiikan esittelyyn. Oikeasti rekisterikriteeriä 
 * ei edes peritä suoraan Criterion-luokasta vaan väliin tulee toinen abstracti luokka. */
public class RegisterCriterion extends Criterion {
	private static final int OP_EQUALS = 0;
	private static final int OP_EQUALS_OR_GREATER = 1;

	private int registerNum;
	private int comparisonValue;
	private int comparisonOperator;

	public boolean meetsCriterion(TitoState studentAnswer, TitoState modelAnswer) {
		int teacherValue = comparisonValue;
		if (modelAnswer != null)
			teacherValue = modelAnswer.getRegister(registerNum);

		int studentValue = studentAnswer.getRegister(registerNum);		
		
		switch (comparisonOperator) {
			case OP_EQUALS             : return studentValue == teacherValue;
			case OP_EQUALS_OR_GREATER  : return studentValue >= teacherValue;
			/*...*/
		}
		
		
		return false; // dead code
	}

	public String serializeToString() {
		return "<class>" + this.getClass().getName() + "</class>" + // tai getCanonicalName() 
		       "<regnum>" + registerNum + "</regnum>" +
		       "<value>" + comparisonValue + "</value>" + 
		       "<operator>" + comparisonOperator + "</value>" +
		       "<posfb>" + positiveFeedback + "</posfb>" +
		       "<negfb>" + negativeFeedback + "</negfb>" +
		       "<issecret>" + secretInputCriterion + "</issecret>";
	}

	public void init(String serialized) {
		
		
		// TODO Auto-generated method stub

	}

}
