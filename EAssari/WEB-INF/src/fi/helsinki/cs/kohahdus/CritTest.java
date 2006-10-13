package fi.helsinki.cs.kohahdus;

public class CritTest {

	public static void main(String[] args) {
		Criterion c = new CodeSizeCriterion("CODESIZE", false, "Tosi jee", "Hyvin meni", "Siis ei n‰in", "15", "20");
		TitoState t = new TitoState();
		
		
		String xml = c.serializeToXML();
		
		System.out.println("sis‰lt‰‰ laatukriteerin: " + c.hasQualityTest());
		System.out.println("laatukriteeri l‰pi: " + c.passesQualityTest(t, null));
		System.out.println("sis‰lt‰‰ hyv‰ksymiskriteerin: " + c.hasAcceptanceTest());
		System.out.println("hyv‰ksymiskriteeri l‰pi: " + c.passesAcceptanceTest(t, null));
		System.out.println(xml);
		
	}

}
