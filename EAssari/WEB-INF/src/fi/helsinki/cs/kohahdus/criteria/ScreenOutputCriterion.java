package fi.helsinki.cs.kohahdus.criteria;

import java.util.ResourceBundle;

/** Concrete criterion class for screen output
* @author Mikko Kinnunen 
*/  
public class ScreenOutputCriterion extends Criterion {
	private String expectedOutput = "";
	private boolean compareToModel = false;


	
	/** Empty constructor for deserialization */
	protected ScreenOutputCriterion() { }

	public ScreenOutputCriterion(String id, boolean usesSecretInput) {
		super(id, usesSecretInput);
	}

	
	/** Set wheater this Variable criterion is to be used with model answer
	 * @param useWithModel any non-empty string sets compare-to-model flag true */
	public void setCompareToModel(String useWithModel) {
		compareToModel = (useWithModel != null) && (!useWithModel.equals(""));
	}
	
	
	/** Test whether this variable criterion has an acceptance test to use with
	 * model answert. This is a convenience method to be used in JSTL-code, all
	 * it does is call hasAcceptanceTest(true).
	 * @return true if this criterion has a model test
	 */
	public boolean isModelAcceptanceTest() {
		return this.hasAcceptanceTest(true);
	}	
	
	
	@Override public boolean hasAcceptanceTest(boolean usingModelAnswer) {
		boolean haveTest = false;
		if ((usingModelAnswer) && (compareToModel)) {
			haveTest = true;
		}
		if ((!usingModelAnswer) && (!expectedOutput.equals(""))) {
			haveTest = true;
		}		
		return haveTest;
	}
 

	@Override public boolean passesAcceptanceTest(TitoState studentAnswer, TitoState modelAnswer) {
		String expectedValue = expectedOutput;
		if ((modelAnswer != null) && (compareToModel)) {
			expectedValue = modelAnswer.getScreenOutput();
		}	
		return expectedValue.equals(studentAnswer.getScreenOutput());
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
				if (!outputs[i].matches("^[\\-\\+]?\\d+$"))
					continue;				
				buffer.append(outputs[i]);
				if (i < outputs.length-1) {
					buffer.append(", ");
				}
			}
			expectedOutput = buffer.toString();
		}
	}
	
	@Override protected String serializeSubClass() {
		return toXML("out", expectedOutput) +
			   toXML("cmpmodel", compareToModel);

	}

	@Override protected void initSubClass(String serializedXML) {
		expectedOutput = parseXMLString(serializedXML, "out");
		compareToModel = parseXMLBoolean(serializedXML, "cmpmodel");
	}


}
