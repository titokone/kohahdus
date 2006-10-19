package fi.helsinki.cs.kohahdus.criteria;

import static fi.helsinki.cs.kohahdus.criteria.Criterion.*; 
import java.util.*;
import java.sql.SQLException;
import fi.helsinki.cs.kohahdus.trainer.Task;
import fi.helsinki.cs.kohahdus.DBHandler;

/** Copy-paste koodauksella aikaansaatu template-tehtävän luonti hässäkkä.
 * Kriteerit on tehty, task-ilmentymät pitäisi vielä vääntää. Palautteet 
 * pitää tietysti vielä hioa, nämä kelpaavat vain testaukseen. */

public class TaskMaker {
	static LinkedList<Criterion> fiCriteria = new LinkedList<Criterion>();
	static LinkedList<Criterion> enCriteria = new LinkedList<Criterion>();

	public static void main(String args[]) throws SQLException {
		// Suomenkieliset kriteerit:
		for (int i=0; i<8; i++) {
			fiCriteria.add(createRegisterCriterion_FI_pub(i));
			fiCriteria.add(createRegisterCriterion_FI_sec(i));
		}
		fiCriteria.add(createSymbolCriterion_FI_pub());
		fiCriteria.add(createSymbolCriterion_FI_sec());
		fiCriteria.add(createRequiredInstructionsCriterion_FI());
		fiCriteria.add(createForbiddenInstructionsCriterion_FI());
		fiCriteria.add(createScreenOutputCriterion_FI_pub());
		fiCriteria.add(createScreenOutputCriterion_FI_sec());
		fiCriteria.add(createCodeSizeCriterion_FI());
		fiCriteria.add(createDataAreaSizeCriterion_FI());
		fiCriteria.add(createDataReferencesCriterion_FI());
		fiCriteria.add(createExecutetionStepsCriterion_FI());
		fiCriteria.add(createMemReferencesCriterion_FI());
		fiCriteria.add(createStackSizeCriterion_FI());

		
		// Englanninkieliset kriteerit:
		for (int i=0; i<8; i++) {
			enCriteria.add(createRegisterCriterion_EN_pub(i));
			enCriteria.add(createRegisterCriterion_EN_sec(i));
		}
		enCriteria.add(createSymbolCriterion_EN_pub());
		enCriteria.add(createSymbolCriterion_EN_sec());
		enCriteria.add(createRequiredInstructionsCriterion_EN());
		enCriteria.add(createForbiddenInstructionsCriterion_EN());
		enCriteria.add(createScreenOutputCriterion_EN_pub());
		enCriteria.add(createScreenOutputCriterion_EN_sec());
		enCriteria.add(createCodeSizeCriterion_EN());
		enCriteria.add(createDataAreaSizeCriterion_EN());
		enCriteria.add(createDataReferencesCriterion_EN());
		enCriteria.add(createExecutetionStepsCriterion_EN());
		enCriteria.add(createMemReferencesCriterion_EN());
		enCriteria.add(createStackSizeCriterion_EN());
		
		// Ihan vaan testinä
		for (Criterion cr : fiCriteria) {
			String xml1 = cr.serializeToXML();
			cr = Criterion.deserializeFromXML(xml1);
			String xml2 = cr.serializeToXML();
//			System.out.println(xml2);
			assert(xml1.equals(xml2));
		}
		for (Criterion cr : enCriteria) {
			String xml1 = cr.serializeToXML();
			cr = Criterion.deserializeFromXML(xml1);
			String xml2 = cr.serializeToXML();
//			System.out.println(xml2);
			assert(xml1.equals(xml2));
		}
		
		
		// Tehdään task-ilmentymät:
		Task et = new Task("EN_TEMPLATE");
		et.setFailFeedBack("Task wasn't solved");
		et.setPassFeedBack("Task was solved");
		et.setTitoTaskType(Task.TYPE_FULL); // oletuksena vaikka näin
		et.setLanguage("EN");
		

		Task ft = new Task("FI_TEMPLATE");
		ft.setFailFeedBack("Ratkaisuyritys epäonnistui");
		ft.setPassFeedBack("Tehtävä ratkaistu hyväksytysti");
		ft.setCutoffvalue(100);
		ft.setTitoTaskType(Task.TYPE_FULL);
		ft.setLanguage("FI");
		
		
		// Viedään tietokantaan:
		DBHandler handler=DBHandler.getInstance();
		System.out.println("Meneekö?");
		boolean enCreate=handler.createTask(et, enCriteria);
	System.out.println("Meni " + enCreate);
		boolean fiCreate=handler.createTask(ft, fiCriteria);
		
		if (enCreate&&fiCreate) {
			System.out.println("Tasks were added to database succesfully.");
		} else {
			System.out.println("There were errors with DBHandler. Tasks not added succesfully.");
		}		
	}
	
	
	
	
	
	
	
	// <rekisterikriteerit>
	static Criterion createRegisterCriterion_FI_pub(int register) {
		RegisterCriterion cr = new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + register, false, register);
		cr.setAcceptanceFeedback("Rekisterin R"+ register + " arvo oikein");
		cr.setFailureFeedback("Rekisterin R" + register + " arvo oli väärin");
		return cr;
	}	
	static Criterion createRegisterCriterion_EN_pub(int register) {
		RegisterCriterion cr = new RegisterCriterion(ID_PUBLIC_REGISTER_PREFIX + register, false, register);
		cr.setAcceptanceFeedback("Register R"+ register + " value correct");
		cr.setFailureFeedback("Register R" + register + " value incorrect");
		return cr;
	}
	static Criterion createRegisterCriterion_FI_sec(int register) {
		RegisterCriterion cr = new RegisterCriterion(ID_SECRET_REGISTER_PREFIX + register, true, register);
		cr.setAcceptanceFeedback("Rekisterin R"+ register + " arvo oikein myös vaihtoehtoisella syötteellä");
		cr.setFailureFeedback("Rekisterin R" + register + " arvo oli väärin kun käytettiin vaihtoehtoista syötettä");
		return cr;
	}	
	static Criterion createRegisterCriterion_EN_sec(int register) {
		RegisterCriterion cr = new RegisterCriterion(ID_SECRET_REGISTER_PREFIX + register, true, register);
		cr.setAcceptanceFeedback("Register R"+ register + " value was correct when using alternate input");
		cr.setFailureFeedback("Register R" + register + " value was incorrect when using alternate input");
		return cr;
	}
	// </rekisterikriteerit>

	
		
	// <symbolikriteerit>
	static Criterion createSymbolCriterion_FI_pub() {
		SymbolCriterion cr = new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 0, false);
		cr.setAcceptanceFeedback("Symbolin _ arvo oikein");
		cr.setFailureFeedback("Symbolin _ arvo oli väärin");
		return cr;		
	}	
	static Criterion createSymbolCriterion_EN_pub() {
		SymbolCriterion cr = new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 0, false);
		cr.setAcceptanceFeedback("Symbol _ value correct");
		cr.setFailureFeedback("Symbol _ value incorrect");
		return cr;		
	}
	static Criterion createSymbolCriterion_FI_sec() {
		SymbolCriterion cr = new SymbolCriterion(ID_SECRET_SYMBOL_PREFIX + 0, true);
		cr.setAcceptanceFeedback("Symbolin _ arvo arvo oikein myös vaihtoehtoisella syötteellä");
		cr.setFailureFeedback("Symbolin _ arvo oli väärin kun käytettiin vaihtoehtoista syötettä");
		return cr;		
	}	
	static Criterion createSymbolCriterion_EN_sec() {
		SymbolCriterion cr = new SymbolCriterion(ID_SECRET_SYMBOL_PREFIX + 0, true);
		cr.setAcceptanceFeedback("Symbol _ value was correct when using alternate input");
		cr.setFailureFeedback("Symbol _ value was incorrect when using alternate input");
		return cr;		
	}
	// </symbolikriteerit>
		
	
	
	// <käskykriteerit>
	static Criterion createRequiredInstructionsCriterion_FI() {
		RequiredInstructionsCriterion cr = new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false);
		cr.setFailureFeedback("Vastauksessa ei ole käytetty kaikkia vaadittuja käskyjä");
		return cr;		
	}	
	static Criterion createRequiredInstructionsCriterion_EN() {
		RequiredInstructionsCriterion cr = new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false);
		cr.setFailureFeedback("Your solution does not contain all required instructions");
		return cr;		
	}
	static Criterion createForbiddenInstructionsCriterion_FI() {
		ForbiddenInstructionsCriterion cr = new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		cr.setFailureFeedback("Vastauksessa on käytetty ainakin yhtä kiellettyä käskyä");
		return cr;		
	}	
	static Criterion createForbiddenInstructionsCriterion_EN() {
		ForbiddenInstructionsCriterion cr = new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		cr.setFailureFeedback("Your solution contains forbidden instruction(s)");
		return cr;		
	}		
	// </käskykriteerit>
	
		
	
	// <tulostekriteerit>
	static Criterion createScreenOutputCriterion_FI_pub() {
		ScreenOutputCriterion cr = new ScreenOutputCriterion(ID_PUBLIC_OUTPUT, false);
		cr.setAcceptanceFeedback("Ohjelman tuottama tuloste oli oikein");
		cr.setFailureFeedback("Ohjelman tuottama tuloste poikkesi odotetusta");
		return cr;		
	}	
	static Criterion createScreenOutputCriterion_EN_pub() {
		ScreenOutputCriterion cr = new ScreenOutputCriterion(ID_PUBLIC_OUTPUT, false);
		cr.setAcceptanceFeedback("Screen output produced by the program was correct");
		cr.setFailureFeedback("Screen output produced by the program was incorrect");
		return cr;		
	}
	static Criterion createScreenOutputCriterion_FI_sec() {
		ScreenOutputCriterion cr = new ScreenOutputCriterion(ID_SECRET_OUTPUT, true);
		cr.setAcceptanceFeedback("Ohjelman tuottama tuloste oli oikein myös vaihtoehtoisella syötteellä");
		cr.setFailureFeedback("Ohjelman tuottama tuloste poikkesi odotetusta kun käytettiin vaihtoehtoista syötettä");
		return cr;		
	}	
	static Criterion createScreenOutputCriterion_EN_sec() {
		ScreenOutputCriterion cr = new ScreenOutputCriterion(ID_SECRET_OUTPUT, true);
		cr.setAcceptanceFeedback("Screen output produced by the program was correct when using alternate input");
		cr.setFailureFeedback("Screen output produced by the program was correct when using alternate input");
		return cr;		
	}
	// </tulostekriteerit>
	
		
	
	// <mittauskriteerit>
	static Criterion createCodeSizeCriterion_FI() {
		Criterion cr = new CodeSizeCriterion(ID_CODE_SIZE, false);
		cr.setHighQualityFeedback("Ohjelma on erittäin kompaktin kokoinen :)");
		cr.setAcceptanceFeedback("Ohjelman koko täyttää vaatimukset [mutta voisi olla pienempikin]");
		cr.setFailureFeedback("Ohjelma on liian suuri (sisältää liian monta käskyä)");
		return cr;		
	}	
	static Criterion createCodeSizeCriterion_EN() {
		Criterion cr = new CodeSizeCriterion(ID_CODE_SIZE, false);
		cr.setHighQualityFeedback("Program size is very compact :)");
		cr.setAcceptanceFeedback("Program size meets the requirements [could be smaller though]");
		cr.setFailureFeedback("Program is too large (contains too many instructions)");
		return cr;		
	}
	
	static Criterion createDataAreaSizeCriterion_FI() {
		Criterion cr = new DataAreaSizeCriterion(ID_DATA_AREA_SIZE, false);
		cr.setHighQualityFeedback("Data-alueen koko on erittäin kompakti :)");
		cr.setAcceptanceFeedback("Data-alueen koko on hyväksyttävä");
		cr.setFailureFeedback("Data-alueen koko on liian suuri");
		return cr;		
	}	
	static Criterion createDataAreaSizeCriterion_EN() {
		Criterion cr = new DataAreaSizeCriterion(ID_DATA_AREA_SIZE, false);
		cr.setHighQualityFeedback("Data-area size is very compact :) ");
		cr.setAcceptanceFeedback("Data-area size is acceptable");
		cr.setFailureFeedback("Data-area size is too large");
		return cr;		
	}
	
	static Criterion createStackSizeCriterion_FI() {
		Criterion cr = new StackSizeCriterion(ID_STACK_SIZE, false);
		cr.setHighQualityFeedback("Pinon koko on erittäin kompakti :)");
		cr.setAcceptanceFeedback("Pinon koko on hyväksyttävä");
		cr.setFailureFeedback("Pinon koko on liian suuri");
		return cr;		
	}	
	static Criterion createStackSizeCriterion_EN() {
		Criterion cr = new StackSizeCriterion(ID_STACK_SIZE, false);
		cr.setHighQualityFeedback("Stack size is very compact :)");
		cr.setAcceptanceFeedback("Stack size is acceptable");
		cr.setFailureFeedback("Stack size is too large");
		return cr;		
	}	
	
	static Criterion createExecutetionStepsCriterion_FI() {
		Criterion cr = new ExecutetionStepsCriterion(ID_EXECUTION_STEPS, false);
		cr.setHighQualityFeedback("Suoritettuja käskyjä on vähän :)");
		cr.setAcceptanceFeedback("Suoritettuja käskyjä määrä on hyväksyttävä");
		cr.setFailureFeedback("Käskyjä suoritettiin liian paljon");
		return cr;		
	}	
	static Criterion createExecutetionStepsCriterion_EN() {
		Criterion cr = new ExecutetionStepsCriterion(ID_EXECUTION_STEPS, false);
		cr.setHighQualityFeedback("Number of executed instructions  is low :)");
		cr.setAcceptanceFeedback("Number of executed instructions is acceptable");
		cr.setFailureFeedback("Too many executed instructions");
		return cr;		
	}
	
	static Criterion createMemReferencesCriterion_FI() {
		Criterion cr = new MemReferencesCriterion(ID_MEMORY_REFERENCES, false);
		cr.setHighQualityFeedback("Muistiviittauksia oli erittäin vähän :)");
		cr.setAcceptanceFeedback("Muistiviittausten lukumäärä oli hyväksyttävä");
		cr.setFailureFeedback("Muistiviittauksia oli liikaa");
		return cr;		
	}	
	static Criterion createMemReferencesCriterion_EN() {
		Criterion cr = new MemReferencesCriterion(ID_MEMORY_REFERENCES, false);
		cr.setHighQualityFeedback("Number of memory references is low :)");
		cr.setAcceptanceFeedback("Number of memory references is acceptable");
		cr.setFailureFeedback("Too many memory references");
		return cr;		
	}		
	
	static Criterion createDataReferencesCriterion_FI() {
		Criterion cr = new DataReferencesCriterion(ID_DATA_REFERENCES, false);
		cr.setHighQualityFeedback("Suoritettuja data-viittauksia on vähän :)");
		cr.setAcceptanceFeedback("Suoritettujen data-viittausten lukumäärä on hyväksyttävä");
		cr.setFailureFeedback("Suoritettuja data-viittauksia on liikaa");
		return cr;		
	}	
	static Criterion createDataReferencesCriterion_EN() {
		Criterion cr = new DataReferencesCriterion(ID_DATA_REFERENCES, false);
		cr.setHighQualityFeedback("Number of executed data references is low :)");
		cr.setAcceptanceFeedback("Number of executed data references is acceptable");
		cr.setFailureFeedback("Too many executed data references");
		return cr;		
	}	
	// </mittauskriteerit>
}
