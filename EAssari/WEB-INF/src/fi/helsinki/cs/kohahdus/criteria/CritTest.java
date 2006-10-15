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
			
			System.out.println("sisältää laatukriteerin: " + c.hasQualityTest(false));
			System.out.println("laatukriteeri läpi: " + c.passesQualityTest(t, null));
			System.out.println("sisältää hyväksymiskriteerin: " + c.hasAcceptanceTest(false));
			System.out.println("hyväksymiskriteeri läpi: " + c.passesAcceptanceTest(t, null));
			System.out.println();
		}
		
		
	}

}
