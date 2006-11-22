package fi.helsinki.cs.kohahdus.criteria;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import static fi.hu.cs.ttk91.TTK91Cpu.*;

public class TitoStateTest extends TestCase {
	private TitoState tito;
	
	private String defs = ""
						// Define symbol X
						+ "     X DC 21\n";
	
	private String code = ""		
						// Create symbol X and set it value 42
						// code size = 3, exec steps = 3, mem references = 6
						+ "     LOAD  R0, X      \n"
						+ "     ADD   R0, X      \n"
						+ "     STORE R0, X      \n"
						  
						  
						// Print numbers 1, 1, 2, 3, 5, 8, 13
						// code size = 14, exec steps = 14, mem references = 14
						+ "     LOAD  R0, =1     \n"
						+ "     OUT   R0, =CRT   \n"
						+ "     LOAD  R0, =1     \n"
						+ "     OUT   R0, =CRT   \n"
						+ "     LOAD  R0, =2     \n"
						+ "     OUT   R0, =CRT   \n"
						+ "     LOAD  R0, =3     \n"
						+ "     OUT   R0, =CRT   \n"
						+ "     LOAD  R0, =5     \n"
						+ "     OUT   R0, =CRT   \n"
						+ "     LOAD  R0, =8     \n"
						+ "     OUT   R0, =CRT   \n"					  
						+ "     LOAD  R0, =13    \n"
						+ "     OUT   R0, =CRT   \n"
						
						// Set registers
						// code size = 6, exec steps = 6, mem references = 6
						+ "     LOAD  R0, =000   \n"
						+ "     LOAD  R1, =100   \n"
						+ "     LOAD  R2, =200   \n"
						+ "     LOAD  R3, =300   \n"
						+ "     LOAD  R4, =400   \n"
						+ "     LOAD  R5, =500   \n"
	
						// Exit program
						// code size = 1, exec steps = 1, mem references = 1
						+ "     SVC   SP, =HALT  \n";
	
	
	

	protected void setUp() throws Exception {
		super.setUp();
		tito = new TitoState();
		
		String compileError = tito.compile(defs + code);
		String runError = tito.execute("", 500);
		if ((compileError != null) || (runError != null)) {
			System.out.println("Failed: " + compileError + runError);			
		}		
	}

	
	

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getRegister(int)'
	 */
	public void testGetRegister() {
		assertEquals(000, tito.getRegister(REG_R0));
		assertEquals(100, tito.getRegister(REG_R1));
		assertEquals(200, tito.getRegister(REG_R2));
		assertEquals(300, tito.getRegister(REG_R3));
		assertEquals(400, tito.getRegister(REG_R4));
		assertEquals(500, tito.getRegister(REG_R5));
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getScreenOutput()'
	 */
	public void testGetScreenOutput() {
		assertEquals("1, 1, 2, 3, 5, 8, 13", tito.getScreenOutput());
	}
	

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getDataSize()'
	 */
	public void testGetDataSize() {
		assertEquals(defs.split("\n").length, tito.getDataSize());
	}
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getCodeSize()'
	 */
	public void testGetCodeSize() {
		assertEquals(code.split("\n").length, tito.getCodeSize());
	}

	
	
	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getMemoryLocation(int)'
	 */
	public void testGetMemoryLocation() {
		int xLocation = code.split("\n").length;
		assertEquals(42, tito.getMemoryLocation(xLocation));
	}
	

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getStackMaxSize()'
	 */
	public void testGetStackMaxSize() {
		// TODO: Add stack code to test program
		assertEquals(0, tito.getStackMaxSize()); 
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getExecutionSteps()'
	 */
	public void testGetExecutionSteps() {
		assertEquals(24, tito.getExecutionSteps());
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getMemoryAccessCount()'
	 */
	public void testGetMemoryAccessCount() {
		assertEquals(27, tito.getMemoryAccessCount());
	}

	/*
	 * Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getUsedOpcodes()'
	 */
	public void testGetUsedOpcodes() {
		Set<String> expectedOpcodes = new HashSet<String>();
		expectedOpcodes.add("LOAD");
		expectedOpcodes.add("STORE");
		expectedOpcodes.add("ADD");
		expectedOpcodes.add("OUT");
		expectedOpcodes.add("SVC");
		assertEquals(expectedOpcodes, tito.getUsedOpcodes());
	}

}
