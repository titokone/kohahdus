package fi.helsinki.cs.kohahdus;

public class CritTest {

	public static void main(String[] args) {
		Criterion c = new CodeSizeCriterion("CODESIZE", false, true, "Hyvin meni", "Siis ei näin", "15");
		TitoState t = new TitoState();
		
		
		String xml = c.serializeToXML();
		
		System.out.println("aktiivinen: " + c.isActive());
		System.out.println("läpi: " + c.meetsCriterion(t, null));
		System.out.println(xml);
		
		Criterion b = Criterion.deserializeFromXML(xml);
		System.out.println("aktiivinen: " + b.isActive());
		System.out.println("läpi: " + b.meetsCriterion(t, null));
		System.out.println(b.serializeToXML());
		
		
		
	}

}
