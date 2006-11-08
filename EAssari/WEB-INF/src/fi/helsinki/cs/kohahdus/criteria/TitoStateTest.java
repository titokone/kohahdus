package fi.helsinki.cs.kohahdus.criteria;

import junit.framework.TestCase;
import static fi.hu.cs.ttk91.TTK91Cpu.*;

public class TitoStateTest extends TestCase {
	private TitoState tito;
	private String source = "     LOAD  R1, =0     \n"
						  + "     LOAD  R2, =1     \n"
						  + "     LOAD  R3, =3     \n"
						  + "Loop ADD   R1, R2     \n"
						  + "     ADD   R3, =100   \n"
						  + "     COMP  R1, =15000 \n"
						  + "     JNEQU R1, Loop   \n"
						  + "     SVC   SP, =HALT  \n";

	

	protected void setUp() throws Exception {
		super.setUp();
		tito = new TitoState();
		
		String compileError = tito.compile(source);
		String runError = tito.run(500);
		if ((compileError != null) || (runError != null)) {
			System.out.println("Failed: " + compileError + runError);			
		}		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getRegister(int)'
	 */
	public void testGetRegister() {
		System.out.println(tito.getRegister(REG_R1));
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getMemoryLocation(int)'
	 */
	public void testGetMemoryLocation() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getSymbolAddress(String)'
	 */
	public void testGetSymbolAddress() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getScreenOutput()'
	 */
	public void testGetScreenOutput() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getStackMaxSize()'
	 */
	public void testGetStackMaxSize() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getExecutionSteps()'
	 */
	public void testGetExecutionSteps() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getCodeSize()'
	 */
	public void testGetCodeSize() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getDataSize()'
	 */
	public void testGetDataSize() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getMemoryAccessCount()'
	 */
	public void testGetMemoryAccessCount() {

	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getUsedOpcodes()'
	 */
	public void testGetUsedOpcodes() {

	}

}
