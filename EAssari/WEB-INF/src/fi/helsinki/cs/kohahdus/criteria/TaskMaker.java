package fi.helsinki.cs.kohahdus.criteria;

import static fi.helsinki.cs.kohahdus.criteria.Criterion.*; 
import java.util.*;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import fi.helsinki.cs.kohahdus.trainer.*;
import fi.helsinki.cs.kohahdus.*;



/** Copy-paste koodauksella aikaansaatu template-tehtävän luonti hässäkkä.
 * Kriteerit on tehty, task-ilmentymät pitäisi vielä vääntää. Palautteet 
 * pitää tietysti vielä hioa, nämä kelpaavat vain testaukseen. 
 * 
 * TaskMaker creates a Task object and all its criteria from HttpRequest.
 * All fields in Task and Criteria should be initialized to appropriate values.*/

public class TaskMaker {
	static LinkedList<Criterion> fiCriteria = new LinkedList<Criterion>();
	static LinkedList<Criterion> enCriteria = new LinkedList<Criterion>();

	
	//These fields are equal with their HTML counterparts. To access a parameter in request
	//one has to append ID (from Criteria) with one of these.
	private static final String ACCEPTANCE_VAL = "_value";
	private static final String ACCEPTANCE_FB = "_acceptance_feedback";
	private static final String FAILURE_FB = "_failure_feedback";
	private static final String COMPARISON = "_comparison_op";
	private static final String CHECKED = "_checked";
	
	private static final String OUTPUT_VAL = "output_value";
	private static final String OUTPUT_ACCEPTANCE_FB = "output_acceptance_feedback";
	private static final String OUTPUT_FAILURE_FB = "output_failure_feedback";
	
	private static final String ACCEPTANCE_LIMIT = "_acceptance_limit";
	private static final String QUALITY_LIMIT = "_quality_limit";
	private static final String QUALITY_FB = "_quality_feedback";
	
	private static final String OPCODE_INSTRUCTIONS = "_instructions";
	private static final String OPCODE_FB = "_feedback";
	
	private static final String SYMBOL_NAME = "_name";
	
	private static final String PRE_CODE = "partial_code1";
	private static final String POST_CODE = "partial_code2";
	private static final String EX_CODE = "example_code";
	
	private static final String TASK_TYPE = "task_type";
	private static final String TASK_PROGRAM = "whole_program";
	private static final String TASK_FILLIN = "partial";
	
	private static final String VALIDATING_MODEL = "correctness_by";
	private static final String VALIDATE_BY_EXAMPLE = "example_program";
	
	private static final String MAX_INSTRUCTIONS = "maximum_number_of_executed_instructions";
	
	
	private static final String TASK_NAME = "task_name";
	private static final String LANGUAGE = "language";
	private static final String CATEGORY = "category";
	private static final String INSTRUCTIONS = "instructions";
	private static final String SECRET_INPUT = "secret_input";
	private static final String PUBLIC_INPUT = "public_input";
	private static final String PASS_FEEDBACK = "feedback_pass";
	private static final String FAIL_FEEDBACK = "feedback_fail";
	
	private static final int DEFAULT_CUTOFF = 100;
		
	
	/** Task object that matches given values in request */
	private Task task;
	/** Criteria linked to the Task. */
	private List<Criterion> criteria;
	
	//TEMP
	private Map<String, String> params;
	
	/**
	 * Creates a TaskMaker object from qualified HttpRequest. All parameters
	 * required in task/criterion creation are assumed to be included in request.
	 * @param req Request that contains all of Tasks data.
	 */
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
		
		
		//Add criteria...
		for (int i=0; i<8; i++) {
			addRegisterCriterion(i, req, false);
			addRegisterCriterion(i, req, true);
		}
		
		addSymbolCriteria(req);
		
		
		addOutputCriterion(req, true);
		addOutputCriterion(req, false);		
		addInstructionCriteria(req);		
		addQualityCriteria(req);
	
		
		
		//Add values that are equal to all task types to the task
		task.setName(req.getParameter(TASK_NAME));
		task.setLanguage(req.getParameter(LANGUAGE));
		task.setCategory(req.getParameter(CATEGORY));
		task.setDescription(req.getParameter(INSTRUCTIONS));
		task.setSecretInput(req.getParameter(SECRET_INPUT));
		task.setPublicInput(req.getParameter(PUBLIC_INPUT));
		task.setPassFeedBack(req.getParameter(PASS_FEEDBACK));			
		task.setFailFeedBack(req.getParameter(FAIL_FEEDBACK));		
		task.setFillInPostCode(req.getParameter(POST_CODE));
		task.setFillInPreCode(req.getParameter(PRE_CODE));
		task.setModelAnswer(req.getParameter(EX_CODE));
		
		Log.write("TaskMaker: Max no. instructions = "+req.getParameter(MAX_INSTRUCTIONS));
		task.setMaximumNumberOfInstructions(req.getParameter(MAX_INSTRUCTIONS));
		
		task.setCutoffvalue(DEFAULT_CUTOFF);
		
		//Determine task type
		if (TASK_FILLIN.equals(req.getParameter(TASK_TYPE))) {
			task.setFillInTask(true);
			task.setTitoTaskType(Task.TYPE_FILL);
		} else {
			task.setFillInTask(false);
			task.setTitoTaskType(Task.TYPE_FULL);
		}
		
		//Determine if Analyzer should use model answer
		if (VALIDATE_BY_EXAMPLE.equals(req.getParameter(VALIDATING_MODEL))) {
			task.setValidateByModel(true);
		} else {
			task.setValidateByModel(false);
		}		
	}
	
	/** Returns the fully initialized Task */
	public Task getTask() {
		return task;
	}
	
	/** Returns all Criteria of the Task */
	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	/** 
	 * Creates a new RegisterCriterion for a given register number.
	 * Criterion attributes are set to the values defined in request.
	 * The new criterion object is added to criteria list.
	 *  
	 * @param register
	 * @param req
	 * @param isSecret
	 */
	private void addRegisterCriterion(int register, HttpServletRequest req, boolean isSecret) {
		String id = (isSecret ? Criterion.ID_SECRET_REGISTER_PREFIX : Criterion.ID_PUBLIC_REGISTER_PREFIX) + register;
		RegisterCriterion crit = new RegisterCriterion(id, isSecret, register);
		
		crit.setComparisonOperator(req.getParameter(id + COMPARISON));
		crit.setAcceptanceTestValue(req.getParameter(id + ACCEPTANCE_VAL));
		crit.setAcceptanceFeedback(req.getParameter(id + ACCEPTANCE_FB));
		crit.setFailureFeedback(req.getParameter(id + FAILURE_FB));
		crit.setCompareToModel(req.getParameter(id + CHECKED));

		Log.write(id + CHECKED + " checked: " + req.getParameter(id + CHECKED));
		Log.write(id + ".hasAcceptanceTest(true) = " + crit.hasAcceptanceTest(true));

		criteria.add(crit);
	}

	/**
	 * Creates a new ScreenOutputCriterion and adds it to criterion list.
	 * All values are set to the values defined in request.
	 * 
	 * @param req
	 * @param isSecret
	 */
	private void addOutputCriterion(HttpServletRequest req, boolean isSecret) {
		String id = (isSecret ? ID_SECRET_OUTPUT : ID_PUBLIC_OUTPUT);
		ScreenOutputCriterion oc = new ScreenOutputCriterion(id, isSecret);
		
		oc.setAcceptanceTestValue(req.getParameter(id + OUTPUT_VAL));
		oc.setAcceptanceFeedback(req.getParameter(id + OUTPUT_ACCEPTANCE_FB));
		oc.setFailureFeedback(req.getParameter(id + OUTPUT_FAILURE_FB));
		
		criteria.add(oc);
	}
	
	/**
	 * Creates InstructionCriterion for required and forbidden instructions and adds them to criterion list.
	 * All values are set to the values defined in request.
	 * @param req
	 */
	private void addInstructionCriteria(HttpServletRequest req) {
		RequiredInstructionsCriterion required = new RequiredInstructionsCriterion(Criterion.ID_REQUIRED_INSTRUCTIONS, false);
		required.setAcceptanceTestValue(req.getParameter(ID_REQUIRED_INSTRUCTIONS + OPCODE_INSTRUCTIONS));
		required.setFailureFeedback(req.getParameter(ID_REQUIRED_INSTRUCTIONS + OPCODE_FB));
		
		
		
		ForbiddenInstructionsCriterion forbidden = new ForbiddenInstructionsCriterion(Criterion.ID_FORBIDDEN_INSTRUCTIONS, false);
		forbidden.setAcceptanceTestValue(req.getParameter(ID_FORBIDDEN_INSTRUCTIONS + OPCODE_INSTRUCTIONS));
		forbidden.setFailureFeedback(req.getParameter(ID_FORBIDDEN_INSTRUCTIONS + OPCODE_FB));
		
		Log.write("TaskMaker: forbidden opcodes - "+forbidden.getAcceptanceTestValue());
		Log.write("TaskMaker: required opcodes - "+required.getAcceptanceTestValue());
		
		criteria.add(required);
		criteria.add(forbidden);
	}
	
	/**
	 * Creates appropriate Criterion objects for each quality criteria and adds them to criterion list.
	 * Quality criteria includes size of code, data area and stack; number of executed instructions,
	 * data references and memory references.
	 * All values are set to the values defined in request.
	 * @param req
	 */
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

	/**
	 * Helper method for addSymbolCriteria.
	 * Sets all required fields of a Criterion to the values defined in request.
	 * @param crit
	 * @param prefix
	 * @param req
	 */
	private void addQualityValues(Criterion crit, String prefix, HttpServletRequest req) {
		crit.setAcceptanceTestValue(req.getParameter(prefix + ACCEPTANCE_LIMIT));
		crit.setQualityTestValue(req.getParameter(prefix + QUALITY_LIMIT));
		crit.setAcceptanceFeedback(req.getParameter(prefix + ACCEPTANCE_FB));
		crit.setHighQualityFeedback(req.getParameter(prefix + QUALITY_FB));
		crit.setFailureFeedback(req.getParameter(prefix + FAILURE_FB));
	}
	
	/**
	 * Creates Criterion objects for each symbol criterion included in request and adds them
	 * to criteria list.
	 * @param req
	 */
	private void addSymbolCriteria(HttpServletRequest req) {
		String count_str = req.getParameter("symbol_criterion_count");
		if (count_str == null || count_str.equals("")) {
			Log.write("TaskMaker: Symbol criteria count invalid");
			return;
		}
		int count = Integer.parseInt(count_str);
		Log.write("TaskMaker: Symbolcriteria count = "+count);
		for (int i=0; i<count; i++) {
			addSymbolCriterion(req, true, i);
			addSymbolCriterion(req, false, i);
		}
	}
	
	
	private void addSymbolCriterion(HttpServletRequest req, boolean isSecret, int num) {
		String id = (isSecret ? Criterion.ID_SECRET_SYMBOL_PREFIX : Criterion.ID_PUBLIC_SYMBOL_PREFIX) + num;
		SymbolCriterion crit = new SymbolCriterion(id, isSecret);
		
		crit.setSymbolName(req.getParameter("SYM" + num + SYMBOL_NAME));
		crit.setComparisonOperator(req.getParameter(id + COMPARISON));
		crit.setAcceptanceTestValue(req.getParameter(id + ACCEPTANCE_VAL));
		crit.setAcceptanceFeedback(req.getParameter(id + ACCEPTANCE_FB));
		crit.setFailureFeedback(req.getParameter(id + FAILURE_FB));
		crit.setCompareToModel(req.getParameter(id + CHECKED));

		Log.write(id + CHECKED + " checked: " + req.getParameter(id + CHECKED));
		Log.write(id + ".hasAcceptanceTest(true) = " + crit.hasAcceptanceTest(true));
		
		Log.write("TaskMaker: new SymbolCriteria created - symName="+crit.getSymbolName()+" CompOp="+crit.getComparisonOperator()+
				  " TestValue="+crit.getAcceptanceTestValue()+" AccFB="+crit.getAcceptanceFeedback()+" FFB="+crit.getFailureFeedback());
		
		criteria.add(crit);
	}
	
	

	
	//TEMP
	public Map<String, String> getParams() {
		return params;
	}
	
	
	
	
	
	
	public static void main(String args[]) throws SQLException {
		
		if (args.length < 3){
			System.out.println("Failed to create new template tasks.");
			System.out.println("Usage: java fi.helsinki.cs.kohahdus.criteria.TaskMaker jdbc:oracle:thin:kohahdus/b1tt1@bodbacka.cs.helsinki.fi:1521:test kohahdus b1tt1");
			return;
		}
		
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
		et.setFailFeedBack("Solution attempt failed");
		et.setPassFeedBack("Task was solved successfully");
		et.setTitoTaskType(Task.TYPE_FULL); // oletuksena vaikka näin
		et.setLanguage("EN");
		et.setMaximumNumberOfInstructions(10000);
		

		Task ft = new Task("FI_TEMPLATE");
		ft.setFailFeedBack("Ratkaisuyritys epäonnistui");
		ft.setPassFeedBack("Tehtävä ratkaistu hyväksytysti");
		ft.setCutoffvalue(100);
		ft.setTitoTaskType(Task.TYPE_FULL);
		ft.setLanguage("FI");
		ft.setMaximumNumberOfInstructions(10000);
		
		
		// Viedään tietokantaan:
		DBHandler.initialize(args[0], args[1], args[2]);
		DBHandler handler=DBHandler.getInstance();
		handler.removeTask(et);
		handler.removeTask(ft);
		boolean enCreate=handler.createTask(et, enCriteria, "EN_TEMPLATE");
		boolean fiCreate=handler.createTask(ft, fiCriteria, "FI_TEMPLATE");
		
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
		cr.setAcceptanceFeedback("Symbolin arvo oikein");
		cr.setFailureFeedback("Symbolin arvo oli väärin");
		return cr;		
	}	
	static Criterion createSymbolCriterion_EN_pub() {
		SymbolCriterion cr = new SymbolCriterion(ID_PUBLIC_SYMBOL_PREFIX + 0, false);
		cr.setAcceptanceFeedback("Symbol value correct");
		cr.setFailureFeedback("Symbol value incorrect");
		return cr;		
	}
	static Criterion createSymbolCriterion_FI_sec() {
		SymbolCriterion cr = new SymbolCriterion(ID_SECRET_SYMBOL_PREFIX + 0, true);
		cr.setAcceptanceFeedback("Symbolin arvo arvo oikein myös vaihtoehtoisella syötteellä");
		cr.setFailureFeedback("Symbolin arvo oli väärin kun käytettiin vaihtoehtoista syötettä");
		return cr;		
	}	
	static Criterion createSymbolCriterion_EN_sec() {
		SymbolCriterion cr = new SymbolCriterion(ID_SECRET_SYMBOL_PREFIX + 0, true);
		cr.setAcceptanceFeedback("Symbol value was correct when using alternate input");
		cr.setFailureFeedback("Symbol value was incorrect when using alternate input");
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
		cr.setFailureFeedback("Your solution contains at least on forbidden instruction");
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
		cr.setHighQualityFeedback("Ohjelma koko täyttää hyväksymisrajan");
		cr.setAcceptanceFeedback("Ohjelman koko täyttää hyväksymisrajan, mutta voisi olla pienempikin");
		cr.setFailureFeedback("Ohjelma on liian suuri (sisältää liian monta käskyä)");
		return cr;		
	}	
	static Criterion createCodeSizeCriterion_EN() {
		Criterion cr = new CodeSizeCriterion(ID_CODE_SIZE, false);
		cr.setHighQualityFeedback("Program size size meets the requirement");
		cr.setAcceptanceFeedback("Program size meets the passing requirement but you could do even better");
		cr.setFailureFeedback("Program is too large (contains too many instructions)");
		return cr;		
	}
	
	static Criterion createDataAreaSizeCriterion_FI() {
		Criterion cr = new DataAreaSizeCriterion(ID_DATA_AREA_SIZE, false);
		cr.setHighQualityFeedback("Data-alueen koko on erittäin kompakti :)");
		cr.setAcceptanceFeedback("Data-alueen koko on hyväksyttävä, mutta voisi olla pienempikin");
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
		cr.setAcceptanceFeedback("Pinon huippukorkeus on hyväksyttävä, mutta voisi olla pienempikin");
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
		cr.setAcceptanceFeedback("Suoritettuja käskyjen määrä on hyväksyttävä, mutta voisi olla pienempikin");
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
		cr.setAcceptanceFeedback("Muistiviittausten lukumäärä oli hyväksyttävä, mutta voisi olla pienempikin");
		cr.setFailureFeedback("Muistiviittauksia oli liikaa");
		return cr;		
	}	
	static Criterion createMemReferencesCriterion_EN() {
		Criterion cr = new MemReferencesCriterion(ID_MEMORY_REFERENCES, false);
		cr.setHighQualityFeedback("Number of memory references meets requirements");
		cr.setAcceptanceFeedback("Number of memory references is acceptable but you could do even better");
		cr.setFailureFeedback("Too many memory references");
		return cr;		
	}		
	
	static Criterion createDataReferencesCriterion_FI() {
		Criterion cr = new DataReferencesCriterion(ID_DATA_REFERENCES, false);
		cr.setHighQualityFeedback("Suoritettuja data-viittauksia on vähän :)");
		cr.setAcceptanceFeedback("Suoritettujen data-viittausten lukumäärä on hyväksyttävä, mutta voisi olla pienempikin");
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
