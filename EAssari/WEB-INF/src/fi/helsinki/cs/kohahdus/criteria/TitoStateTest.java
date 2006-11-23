package fi.helsinki.cs.kohahdus.criteria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import static fi.hu.cs.ttk91.TTK91Cpu.*;

public class TitoStateTest extends TestCase {
	private TitoState tito;
	
	private String defs = ""
						// Define symbols A B C D
						+ "     A DC 11 \n"
						+ "     B DC 3 \n"
						+ "     C DC -2 \n"
						+ "     D DC 1337 \n"
						;
	
	
	private String code = ""
						// Call MADD-funtion to calculate A*B+C and store to D (D = 11*3-2 = 31
						// code size = 7, exec steps = 7, data references = 10
						+ "     PUSH  SP, A     \n" // 1st param used also for the return value
						+ "     PUSH  SP, B     \n"
						+ "     PUSH  SP, C     \n"
						+ "     CALL  SP, madd  \n"
						+ "     POP   SP, R0    \n"						
						+ "     STORE R0, D     \n"

						  
						// Print numbers 1, 1, 2, 3, 5, 8, 13
						// code size = 14, exec steps = 14, data references = 0
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
						// code size = 6, exec steps = 6, data references = 0
						+ "     LOAD  R0, =000   \n"
						+ "     LOAD  R1, =100   \n"
						+ "     LOAD  R2, =200   \n"
						+ "     LOAD  R3, =300   \n"
						+ "     LOAD  R4, =400   \n"
						+ "     LOAD  R5, =500   \n"

						// Exit program
						// code size = 1, exec steps = 1, mem references = 2
						+ "     SVC   SP, =HALT  \n"
						
						// Multiply-accumulate: multiply 1st param by 2nd param, add 3rd param and store back to param 1 location
						// code size = 7, exec steps = 7, mem references = 
						+ "madd PUSH  SP, R1     \n" // Save R1
						+ "     LOAD  R1, -4(FP) \n"
						+ "     MUL   R1, -3(FP) \n"
						+ "     ADD   R1, -2(FP) \n "
						+ "     STORE R1, -4(FP) \n"
						+ "     POP   SP, R1     \n" // Restore R1
						+ "     EXIT  SP, =2     \n" // Nuke 3rd and 2nd paramn, retain 1st 						
						;
	
	
	

	protected void setUp() throws Exception {
		super.setUp();
		tito = new TitoState();
		
		String compileError = tito.compile(defs + code);
		String runError = tito.execute("", 500);
		if ((compileError != null) || (runError != null)) {
			System.out.println("Failed: " + compileError + runError);			
		}		
	}

	
	

	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getRegister(int)' */
	public void testGetRegister() {
		assertEquals(000, tito.getRegister(REG_R0));
		assertEquals(100, tito.getRegister(REG_R1));
		assertEquals(200, tito.getRegister(REG_R2));
		assertEquals(300, tito.getRegister(REG_R3));
		assertEquals(400, tito.getRegister(REG_R4));
		assertEquals(500, tito.getRegister(REG_R5));
	}
	

	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getScreenOutput()' */
	public void testGetScreenOutput() {
		assertEquals("1, 1, 2, 3, 5, 8, 13", tito.getScreenOutput());
	}
	

	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getDataSize()' */
	public void testGetDataSize() {
		assertEquals(defs.split("\n").length, tito.getDataSize());
	}
	
	
	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getCodeSize()' */
	public void testGetCodeSize() {
		assertEquals(code.split("\n").length, tito.getCodeSize());
	}
	
	
	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getMemoryLocation(int)' */
	public void testGetMemoryLocation() {
		int dLocation = defs.split("\n").length + code.split("\n").length - 1;
		assertEquals(31, tito.getMemoryLocation(dLocation));
	}
	
	
	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getSymbolTable()' */
	public void testGetSymbolTable() {
		int aLocation = defs.split("\n").length + code.split("\n").length - 4;
		int bLocation = defs.split("\n").length + code.split("\n").length - 3;
		int cLocation = defs.split("\n").length + code.split("\n").length - 2;
		int dLocation = defs.split("\n").length + code.split("\n").length - 1;
		HashMap symbols = tito.getSymbolTable();
		assertEquals(aLocation, symbols.get("a"));		
		assertEquals(bLocation, symbols.get("b"));		
		assertEquals(cLocation, symbols.get("c"));		
		assertEquals(dLocation, symbols.get("d"));		
	}
	

	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getStackMaxSize()' */
	public void testGetStackMaxSize() {
		assertEquals(6, tito.getStackMaxSize()); 
	}
	

	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getExecutionSteps()' */
	public void testGetExecutionSteps() {
		assertEquals(code.split("\n").length, tito.getExecutionSteps());
	}

	
	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getMemoryAccessCount()' */
	public void testGetDataReferenceCount() {
		assertEquals(20, tito.getDataReferenceCount());
	}

	
	/* Test method for 'fi.helsinki.cs.kohahdus.criteria.TitoState.getUsedOpcodes()' */
	public void testGetUsedOpcodes() {
		Set<String> expectedOpcodes = new HashSet<String>();
		expectedOpcodes.add("LOAD");
		expectedOpcodes.add("STORE");
		expectedOpcodes.add("ADD");
		expectedOpcodes.add("OUT");
		expectedOpcodes.add("MUL");
		expectedOpcodes.add("PUSH");
		expectedOpcodes.add("CALL");
		expectedOpcodes.add("EXIT");
		expectedOpcodes.add("POP");
		expectedOpcodes.add("SVC");
		assertEquals(expectedOpcodes, tito.getUsedOpcodes());
	}

}
