package fi.helsinki.cs.kohahdus.criteria;

import java.util.*;

public class CritTest {

	public static void main(String[] args) {
		List<Criterion> criteria = new LinkedList<Criterion>();
		Criterion cr;
		
		
		cr = new CodeSizeCriterion("CODESIZE", false);
		cr.setHighQualityFeedback("Softasi oli pieni & kaunis 3>");
		cr.setAcceptanceFeedback("Kelpaa, olisit kyll‰ pienemm‰nkin voinut tehd‰");
		cr.setFailureFeedback("Ett‰ bloattia on kiva tehd‰ vai? Koitapa uudestaan");
		criteria.add(cr);
		
		cr = new RegisterCriterion("REG3", false, 3);
		criteria.add(cr);
		
		SymbolCriterion scr = new SymbolCriterion("SYM1", false);
		scr.setSymbolName("X");
		scr.setComparisonOperator("==");
		criteria.add(scr);
		
		
		
		

		
		TitoState t = new TitoState();
		for (Criterion c : criteria) {
			String xml = c.serializeToXML();
			c = null;
			System.out.println(xml);
			c = Criterion.deserializeFromXML(xml);
			
			System.out.println("sis‰lt‰‰ laatukriteerin: "      + c.hasQualityTest(false));
			System.out.println("laatukriteeri l‰pi: "           + c.passesQualityTest(t, null));
			System.out.println("sis‰lt‰‰ hyv‰ksymiskriteerin: " + c.hasAcceptanceTest(false));
			System.out.println("hyv‰ksymiskriteeri l‰pi: "      + c.passesAcceptanceTest(t, null));
			System.out.println("korkean laadun palaute: "       + c.getHighQualityFeedback());
			if (c.getId().equals("SYM1")) {
				SymbolCriterion c2 = (SymbolCriterion)c; 
				System.out.println("symbolin nimi: " + c2.getSymbolName());
				System.out.println("vertailu oper: " + c2.getComparisonOperator());
			}			
			System.out.println();
			
			
			
		}
		
		
	}

}
