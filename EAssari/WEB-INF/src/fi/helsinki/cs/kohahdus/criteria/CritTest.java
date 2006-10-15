package fi.helsinki.cs.kohahdus.criteria;

import java.util.*;

public class CritTest {

	public static void main(String[] args) {
		List<Criterion> criteria = new LinkedList<Criterion>();
		Criterion cr;
		
		cr = new CodeSizeCriterion("CODESIZE", false);
		criteria.add(cr);
		
		cr = new RegisterCriterion("REG3", false, "==", 3);
		criteria.add(cr);
		

		
		TitoState t = new TitoState();
		for (Criterion c : criteria) {
			String xml = c.serializeToXML();
			System.out.println(xml);
			c = Criterion.deserializeFromXML(xml);
			
			System.out.println("sis‰lt‰‰ laatukriteerin: " + c.hasQualityTest(false));
			System.out.println("laatukriteeri l‰pi: " + c.passesQualityTest(t, null));
			System.out.println("sis‰lt‰‰ hyv‰ksymiskriteerin: " + c.hasAcceptanceTest(false));
			System.out.println("hyv‰ksymiskriteeri l‰pi: " + c.passesAcceptanceTest(t, null));
			System.out.println();
		}
		
		
	}

}
