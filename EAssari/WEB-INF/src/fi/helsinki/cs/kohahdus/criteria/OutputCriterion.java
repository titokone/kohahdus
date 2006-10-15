package fi.helsinki.cs.kohahdus.criteria;

public class OutputCriterion extends Criterion {
	private String expectedOutput = "";


	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		return !expectedOutput.equals("");
	}
 

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		return expectedOutput.equals(studentAnswer.getOutput());
	}

	@Override public String getAcceptanceTest() {
		return expectedOutput;
	}

	@Override public void setAcceptanceTest(String test) {
		if (test == null) {		
			expectedOutput = "";
		} else {
			// reformat test as "124, 4242, 2335, 3535, 35325"
//			String[] outputs = test.split("[ \t\r\f\n,;]+");
			String[] outputs = test.split("\\D+");
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
