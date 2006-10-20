package fi.helsinki.cs.kohahdus.criteria;

public class ScreenOutputCriterion extends Criterion {
	private String expectedOutput = "";

	/** Empty constructor for deserialization */
	protected ScreenOutputCriterion() { }

	public ScreenOutputCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}


	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return !expectedOutput.equals("");
	}
 

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return expectedOutput.equals(studentAnswer.getScreenOutput());
	}

	@Override public String getAcceptanceTestValue() {
		return expectedOutput;
	}

	@Override public void setAcceptanceTestValue(String test) {
		if (test == null) {		
			expectedOutput = "";
		} else {
			// reformat test as "124, 4242, 2335, 3535, 35325"
			String[] outputs = test.split("[ \t\r\f\n,;]+");
			StringBuffer buffer = new StringBuffer();
			for (int i=0; i<outputs.length; i++) {
				buffer.append(outputs[i]);
				if (i < outputs.length-1) {
					buffer.append(", ");
				}
			}
			expectedOutput = buffer.toString();
		}
	}
	
	@Override protected String serializeSubClass() {
		return toXML("out", expectedOutput);
	}

	@Override protected void initSubClass(String serializedXML) {
		expectedOutput = parseXMLString(serializedXML, "out");
	}


}
