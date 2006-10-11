package fi.helsinki.cs.kohahdus;

/* Vain mallliksi kriteerien toimintalogiikan esittelyyn. Oikeasti rekisterikriteeri� 
 * ei edes perit� suoraan Criterion-luokasta vaan v�liin tulee toinen abstrakti luokka. */
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

	
	public String serializeSubClass() {
		return "<regnum>" + registerNum + "</regnum>" +
		       "<value>" + comparisonValue + "</value>" + 
		       "<operator>" + comparisonOperator + "</value>";
	}
	

	public void initSubClass(String serializedXML) {		
		// N�m� arvot siis katsotaan tuosta XML-stringist�
		registerNum = 0; 
		comparisonValue = 0;
		comparisonOperator = 0;
	}

}
