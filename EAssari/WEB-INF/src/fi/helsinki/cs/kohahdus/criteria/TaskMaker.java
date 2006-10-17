package fi.helsinki.cs.kohahdus.criteria;

import java.util.*;

/** Creates finnish and english language task-templates */
public class TaskMaker {
	static LinkedList<Criterion> fiCriteria = new LinkedList<Criterion>();
	static LinkedList<Criterion> enCriteria = new LinkedList<Criterion>();

	public static void main(String args[]) {
		for (int i=0; i<8; i++) {
			fiCriteria.add(createRegisterCriterion_FI_pub(i));
			enCriteria.add(createRegisterCriterion_EN_pub(i));
			fiCriteria.add(createRegisterCriterion_FI_sec(i));
			enCriteria.add(createRegisterCriterion_EN_sec(i));
		}
		
		for (Criterion cr : fiCriteria) {
			System.out.println(cr.serializeToXML());
		}
		for (Criterion cr : enCriteria) {
			System.out.println(cr.serializeToXML());
		}		
	}
	
	
	
	
	
	
	
	// <rekisterikriteerit>
	static Criterion createRegisterCriterion_FI_pub(int register) {
		RegisterCriterion cr = new RegisterCriterion("PUBREG" + register, false, register);
		cr.setAcceptanceFeedback("Rekisterin R"+ register + " arvo oikein OK");
		cr.setFailureFeedback("Rekisterin R" + register + " arvo oli väärin");
		return cr;
	}	
	static Criterion createRegisterCriterion_EN_pub(int register) {
		RegisterCriterion cr = new RegisterCriterion("PUBREG" + register, false, register);
		cr.setAcceptanceFeedback("Register R"+ register + " value correct");
		cr.setFailureFeedback("Rekisterin R" + register + " value incorrect");
		return cr;
	}
	static Criterion createRegisterCriterion_FI_sec(int register) {
		RegisterCriterion cr = new RegisterCriterion("SECREG" + register, true, register);
		cr.setAcceptanceFeedback("Rekisterin R"+ register + " arvo oikein myös vaihtoehtoisella syötteellä");
		cr.setFailureFeedback("Rekisterin R" + register + " arvo oli väärin kun käytettiin vaihtoehtoista syötettä");
		return cr;
	}	
	static Criterion createRegisterCriterion_EN_sec(int register) {
		RegisterCriterion cr = new RegisterCriterion("SECREG" + register, true, register);
		cr.setAcceptanceFeedback("Register R"+ register + " value was correct when using alternate input");
		cr.setFailureFeedback("Rekisterin R" + register + " value was incorrect when using alternate input");
		return cr;
	}
	// </rekisterikriteerit>

	
		
	// <symbolikriteerit>
	static Criterion createSymbolCriterion_FI_pub() {
		SymbolCriterion cr = new SymbolCriterion("PUBSYM1", false);
		cr.setAcceptanceFeedback("Symbolin [_] arvo oikein");
		cr.setFailureFeedback("Symbolin [_] arvo oli vääri");
		return cr;		
	}	
	static Criterion createSymbolCriterion_EN_pub() {
		SymbolCriterion cr = new SymbolCriterion("PUBSYM1", false);
		cr.setAcceptanceFeedback("Symbol [_] value correct");
		cr.setFailureFeedback("Symbol [_] value incorrect");
		return cr;		
	}
	static Criterion createSymbolCriterion_FI_sec() {
		SymbolCriterion cr = new SymbolCriterion("SECSYM1", false);
		cr.setAcceptanceFeedback("Symbolin [_] arvo arvo oikein myös vaihtoehtoisella syötteellä");
		cr.setFailureFeedback("Symbolin [_] arvo oli väärin kun käytettiin vaihtoehtoista syötettä");
		return cr;		
	}	
	static Criterion createSymbolCriterion_EN_sec() {
		SymbolCriterion cr = new SymbolCriterion("SECSYM1", false);
		cr.setAcceptanceFeedback("Symbol [_] value was correct when using alternate input");
		cr.setFailureFeedback("Symbol [_] value was incorrect when using alternate input");
		return cr;		
	}
	// </symbolikriteerit>
		
	
	
	// <outputkriteerit>
	static Criterion createRequiredInstructionsCriterion_FI() {
		RequiredInstructionsCriterion cr = new RequiredInstructionsCriterion("REQOPCODES", false);
		cr.setFailureFeedback("Vastauksessa ei ole käytetty kaikkia vaadittuja käskyjä");
		return cr;		
	}	
	static Criterion createRequiredInstructionsCriterion_EN() {
		RequiredInstructionsCriterion cr = new RequiredInstructionsCriterion("REQOPCODES", false);
		cr.setFailureFeedback("Your solution does not contain");
		return cr;		
	}
	static Criterion createForbiddenInstructionsCriterion_FI() {
		ForbiddenInstructionsCriterion cr = new ForbiddenInstructionsCriterion("SECSYM1", false);
		cr.setFailureFeedback("Symbolin [_] arvo oli väärin kun käytettiin vaihtoehtoista syötettä");
		return cr;		
	}	
	static Criterion createForbiddenInstructionsCriterion_EN() {
		ForbiddenInstructionsCriterion cr = new ForbiddenInstructionsCriterion("SECSYM1", false);
		cr.setFailureFeedback("Symbol [_] value was incorrect when using alternate input");
		return cr;		
	}		
	// </outputkriteerit>
		
	
	
	
	
	
		
	// <kriteerit>
	
	// </kriteerit>
	// <kriteerit>
	
	// </kriteerit>
	// <kriteerit>
	
	// </kriteerit>
	// <kriteerit>
	
	// </kriteerit>
		
		
		
		
		
		
		
}
