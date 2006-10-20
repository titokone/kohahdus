package fi.helsinki.cs.kohahdus.criteria;

import static fi.helsinki.cs.kohahdus.criteria.Criterion.*; 
import java.util.*;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import fi.helsinki.cs.kohahdus.trainer.*;
import fi.helsinki.cs.kohahdus.*;



/** Copy-paste koodauksella aikaansaatu template-teht�v�n luonti h�ss�kk�.
 * Kriteerit on tehty, task-ilmentym�t pit�isi viel� v��nt��. Palautteet 
 * pit�� tietysti viel� hioa, n�m� kelpaavat vain testaukseen. */

public class TaskMaker {
	static LinkedList<Criterion> fiCriteria = new LinkedList<Criterion>();
	static LinkedList<Criterion> enCriteria = new LinkedList<Criterion>();

	
	//These fields are equal with their HTML counterparts. To access a parameter in request
	//one has to append ID (from Criteria) with one of these.
	private static final String ACCEPTANCE_VAL = "_value";
	private static final String ACCEPTANCE_FB = "_acceptance_feedback";
	private static final String FAILURE_FB = "_failure_feedback";
	private static final String COMPARISON = "_comparison_op";
	
	private static final String OUTPUT_VAL = "output_value";
	private static final String OUTPUT_ACCEPTANCE_FB = "output_acceptance_feedback";
	private static final String OUTPUT_FAILURE_FB = "output_failure_feedback";
	
	private static final String ACCEPTANCE_LIMIT = "_acceptance_limit";
	private static final String QUALITY_LIMIT = "_quality_limit";
	private static final String QUALITY_FB = "_quality_feedback";
	
	private static final String OPCODE_INSTRUCTIONS = "_instructions";
	private static final String OPCODE_FB = "_feedback";
	
	private static final String PRE_CODE = "partial_code1";
	private static final String POST_CODE = "partial_code2";
	private static final String EX_CODE = "example_code";
	
	private static final String TASK_TYPE = "task_type";
	private static final String TASK_PROGRAM = "whole_program";
	private static final String TASK_FILLIN = "partial";
	
	private static final String VALIDATING_MODEL = "correctness_by";
	private static final String VALIDATE_BY_EXAMPLE = "example_program";
	
	private static final String MAX_INSTRUCTIONS = "maximum_number_of_executed_instructions";

	//TODO: tallennetaanko ^ silmukan esto kriteerin� vai taskin ominaisuutena? Oletan j�lkimm�ist�.
	
	private Task task;
	private List<Criterion> criteria;
	
	//TEMP
	private Map<String, String> params;
	
	
	public TaskMaker(HttpServletRequest req) {
		
		//TEMP
		params = new HashMap<String, String>();
		
		Enumeration names = req.getParameterNames();
		
		while (names.hasMoreElements()) {
			String elem = (String) names.nextElement();
			String value = req.getParameter(elem);
			params.put(elem, value);				
		}
		//END TEMP
		
		this.task = new Task();
		this.criteria = new LinkedList<Criterion>();
		
		for (int i=0; i<8; i++) {
			addRegisterCriterion(i, req, false);
			addRegisterCriterion(i, req, true);
		}
		
		addOutputCriterion(req, true);
		addOutputCriterion(req, false);		
		addInstructionCriteria(req);		
		addQualityCriteria(req);
	
		//TODO: addSymbolCriteria();
		
		
		
		//Add values that are equal to all task types to the task
		//User user = (User) req.getSession().getAttribute("user"); <- Tehd��n save_task.jsp sivulla
		//task.setAuthor(user.getFirstName()+" "+user.getLastName());
		task.setName(req.getParameter("task_name"));
		task.setCategory(req.getParameter("category"));
		task.setDescription(req.getParameter("instructions"));
		task.setSecretInput(req.getParameter("secret_input"));
		task.setPublicInput(req.getParameter("public_input"));
					
		task.setFillInPostCode(req.getParameter(POST_CODE));
		task.setFillInPreCode(req.getParameter(PRE_CODE));
		task.setModelAnswer(req.getParameter(EX_CODE));
		
		task.setCutoffvalue(100);
		
		if (TASK_FILLIN.equals(req.getParameter(TASK_TYPE))) {
			task.setFillInTask(true);
			task.setTitoTaskType(Task.TYPE_FILL);
		} else {
			task.setFillInTask(false);
			task.setTitoTaskType(Task.TYPE_FULL);
		}
		
		if (VALIDATE_BY_EXAMPLE.equals(req.getParameter(VALIDATING_MODEL))) {
			task.setValidateByModel(true);
		} else {
			task.setValidateByModel(false);
		}
		
		//TODO:
		//task.setTaskID(); <- tehd��n save_task.jsp sivulla?
		
		Log.write("Task created with following criteria:");
		for (Criterion c : criteria) {
			Log.write(c.serializeToXML());
		}
	}
	
	public Task getTask() {
		return task;
	}
	
	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	
	private void addRegisterCriterion(int register, HttpServletRequest req, boolean isSecret) {
		String id = (isSecret ? Criterion.ID_SECRET_REGISTER_PREFIX : Criterion.ID_PUBLIC_REGISTER_PREFIX) + register;
		RegisterCriterion crit = new RegisterCriterion(id, isSecret, register);
		
		crit.setComparisonOperator(req.getParameter(id + COMPARISON));
		crit.setAcceptanceTestValue(req.getParameter(id + ACCEPTANCE_VAL));
		crit.setAcceptanceFeedback(req.getParameter(id + ACCEPTANCE_FB));
		crit.setFailureFeedback(req.getParameter(id + FAILURE_FB));
		
		criteria.add(crit);
	}

	private void addOutputCriterion(HttpServletRequest req, boolean isSecret) {
		String id = (isSecret ? ID_SECRET_OUTPUT : ID_PUBLIC_OUTPUT);
		ScreenOutputCriterion oc = new ScreenOutputCriterion(id, isSecret);
		
		oc.setAcceptanceTestValue(req.getParameter(id + OUTPUT_VAL));
		oc.setAcceptanceFeedback(req.getParameter(id + OUTPUT_ACCEPTANCE_FB));
		oc.setFailureFeedback(req.getParameter(id + OUTPUT_FAILURE_FB));
		
		criteria.add(oc);
	}
	
	//FIXME: k�skyt eiv�t taida l�yty� tuolta. Joko javascript ongelma tai katsotaan v��r�st� paikasta...
	private void addInstructionCriteria(HttpServletRequest req) {
		RequiredInstructionsCriterion required = new RequiredInstructionsCriterion(Criterion.ID_REQUIRED_INSTRUCTIONS, false);
		required.setAcceptanceTestValue(req.getParameter(ID_REQUIRED_INSTRUCTIONS + OPCODE_INSTRUCTIONS));
		required.setFailureFeedback(req.getParameter(ID_REQUIRED_INSTRUCTIONS + OPCODE_FB));
		
		ForbiddenInstructionsCriterion forbidden = new ForbiddenInstructionsCriterion(Criterion.ID_FORBIDDEN_INSTRUCTIONS, false);
		forbidden.setAcceptanceTestValue(req.getParameter(ID_FORBIDDEN_INSTRUCTIONS + OPCODE_FB));
		
		criteria.add(required);
		criteria.add(forbidden);
	}
	
	private void addQualityCriteria(HttpServletRequest req) {
		CodeSizeCriterion codeSize = new CodeSizeCriterion(ID_CODE_SIZE, false);
		addQualityValues(codeSize, ID_CODE_SIZE, req);
		
		DataAreaSizeCriterion dataSize = new DataAreaSizeCriterion(ID_DATA_AREA_SIZE, false);
		addQualityValues(dataSize, ID_DATA_AREA_SIZE, req);
		
		StackSizeCriterion stackSize = new StackSizeCriterion(ID_STACK_SIZE, false);
		addQualityValues(stackSize, ID_STACK_SIZE, req);
		
		ExecutetionStepsCriterion execSteps = new ExecutetionStepsCriterion(ID_EXECUTION_STEPS, false);
		addQualityValues(execSteps, ID_EXECUTION_STEPS, req);
		
		DataReferencesCriterion dataRefs = new DataReferencesCriterion(ID_DATA_REFERENCES, false);
		addQualityValues(dataRefs, ID_DATA_REFERENCES, req);
		
		MemReferencesCriterion memRefs = new MemReferencesCriterion(ID_MEMORY_REFERENCES, false);
		addQualityValues(memRefs, ID_MEMORY_REFERENCES, req);
		
		criteria.add(codeSize);
		criteria.add(dataSize);
		criteria.add(stackSize);
		criteria.add(execSteps);
		criteria.add(dataRefs);
		criteria.add(memRefs);
		
	}
	
	private void addQualityValues(Criterion crit, String prefix, HttpServletRequest req) {
		crit.setAcceptanceTestValue(req.getParameter(prefix + ACCEPTANCE_LIMIT));
		crit.setQualityTestValue(req.getParameter(prefix + QUALITY_LIMIT));
		crit.setAcceptanceFeedback(req.getParameter(prefix + ACCEPTANCE_FB));
		crit.setHighQualityFeedback(req.getParameter(prefix + QUALITY_FB));
		crit.setFailureFeedback(req.getParameter(prefix + FAILURE_FB));
	}
	
	//TEMP
	public Map<String, String> getParams() {
		return params;
	}
	
	
	
	
	
	
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
		
		// Ihan vaan testin�
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
		
		
		// Tehd��n task-ilmentym�t:
		Task et = new Task("EN_TEMPLATE");
		et.setFailFeedBack("Task wasn't solved");
		et.setPassFeedBack("Task was solved");
		et.setTitoTaskType(Task.TYPE_FULL); // oletuksena vaikka n�in
		et.setLanguage("EN");
		

		Task ft = new Task("FI_TEMPLATE");
		ft.setFailFeedBack("Ratkaisuyritys ep�onnistui");
		ft.setPassFeedBack("Teht�v� ratkaistu hyv�ksytysti");
		ft.setCutoffvalue(100);
		ft.setTitoTaskType(Task.TYPE_FULL);
		ft.setLanguage("FI");
		
		
		// Vied��n tietokantaan:
		DBHandler handler=DBHandler.getInstance();
		handler.removeTask(et);
		handler.removeTask(ft);
		boolean enCreate=handler.createTask(et, enCriteria);
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
		cr.setFailureFeedback("Rekisterin R" + register + " arvo oli v��rin");
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
		cr.setAcceptanceFeedback("Rekisterin R"+ register + " arvo oikein my�s vaihtoehtoisella sy�tteell�");
		cr.setFailureFeedback("Rekisterin R" + register + " arvo oli v��rin kun k�ytettiin vaihtoehtoista sy�tett�");
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
		cr.setFailureFeedback("Symbolin _ arvo oli v��rin");
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
		cr.setAcceptanceFeedback("Symbolin _ arvo arvo oikein my�s vaihtoehtoisella sy�tteell�");
		cr.setFailureFeedback("Symbolin _ arvo oli v��rin kun k�ytettiin vaihtoehtoista sy�tett�");
		return cr;		
	}	
	static Criterion createSymbolCriterion_EN_sec() {
		SymbolCriterion cr = new SymbolCriterion(ID_SECRET_SYMBOL_PREFIX + 0, true);
		cr.setAcceptanceFeedback("Symbol _ value was correct when using alternate input");
		cr.setFailureFeedback("Symbol _ value was incorrect when using alternate input");
		return cr;		
	}
	// </symbolikriteerit>
		
	
	
	// <k�skykriteerit>
	static Criterion createRequiredInstructionsCriterion_FI() {
		RequiredInstructionsCriterion cr = new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false);
		cr.setFailureFeedback("Vastauksessa ei ole k�ytetty kaikkia vaadittuja k�skyj�");
		return cr;		
	}	
	static Criterion createRequiredInstructionsCriterion_EN() {
		RequiredInstructionsCriterion cr = new RequiredInstructionsCriterion(ID_REQUIRED_INSTRUCTIONS, false);
		cr.setFailureFeedback("Your solution does not contain all required instructions");
		return cr;		
	}
	static Criterion createForbiddenInstructionsCriterion_FI() {
		ForbiddenInstructionsCriterion cr = new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		cr.setFailureFeedback("Vastauksessa on k�ytetty ainakin yht� kielletty� k�sky�");
		return cr;		
	}	
	static Criterion createForbiddenInstructionsCriterion_EN() {
		ForbiddenInstructionsCriterion cr = new ForbiddenInstructionsCriterion(ID_FORBIDDEN_INSTRUCTIONS, false);
		cr.setFailureFeedback("Your solution contains forbidden instruction(s)");
		return cr;		
	}		
	// </k�skykriteerit>
	
		
	
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
		cr.setAcceptanceFeedback("Ohjelman tuottama tuloste oli oikein my�s vaihtoehtoisella sy�tteell�");
		cr.setFailureFeedback("Ohjelman tuottama tuloste poikkesi odotetusta kun k�ytettiin vaihtoehtoista sy�tett�");
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
		cr.setHighQualityFeedback("Ohjelma on eritt�in kompaktin kokoinen :)");
		cr.setAcceptanceFeedback("Ohjelman koko t�ytt�� vaatimukset [mutta voisi olla pienempikin]");
		cr.setFailureFeedback("Ohjelma on liian suuri (sis�lt�� liian monta k�sky�)");
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
		cr.setHighQualityFeedback("Data-alueen koko on eritt�in kompakti :)");
		cr.setAcceptanceFeedback("Data-alueen koko on hyv�ksytt�v�");
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
		cr.setHighQualityFeedback("Pinon koko on eritt�in kompakti :)");
		cr.setAcceptanceFeedback("Pinon koko on hyv�ksytt�v�");
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
		cr.setHighQualityFeedback("Suoritettuja k�skyj� on v�h�n :)");
		cr.setAcceptanceFeedback("Suoritettuja k�skyj� m��r� on hyv�ksytt�v�");
		cr.setFailureFeedback("K�skyj� suoritettiin liian paljon");
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
		cr.setHighQualityFeedback("Muistiviittauksia oli eritt�in v�h�n :)");
		cr.setAcceptanceFeedback("Muistiviittausten lukum��r� oli hyv�ksytt�v�");
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
		cr.setHighQualityFeedback("Suoritettuja data-viittauksia on v�h�n :)");
		cr.setAcceptanceFeedback("Suoritettujen data-viittausten lukum��r� on hyv�ksytt�v�");
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
