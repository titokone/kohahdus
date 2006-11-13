package fi.helsinki.cs.kohahdus.criteria;

import java.io.File;
import java.util.HashMap;

import fi.hu.cs.ttk91.*;
import fi.hu.cs.titokone.Control;

/** Capsulates the end-state of single run of TitoKone.
 *  
 * Tämä luokka toteutetaan toisessa iteraatiossa */
public class TitoState {
	private Control controller = new Control(new File("/dev/null"), new File("/dev/tty"));
	private TTK91Application app;
	private TTK91Cpu cpu;
	private TTK91Memory mem;

	
	
	/** Silly String wrapper-class required by TitoKone */
	private class Source implements TTK91CompileSource {
		private String source;
		public Source(String sourceCode) {
			source = sourceCode;
		}
		public String getSource() {
			return source;
		}	
	}
	
	

	/** Compiles given source code. If this method executes successfully, the program
	 * may be run with the <code>run()</code> method.
	 * @param sourceCode TKK91 program source code as a string
	 * @return compile error message, <code>null</code> if no errors
	 */
	public String compile(String sourceCode) {
		TTK91CompileSource src = new Source(sourceCode);
		try {
			this.app = controller.compile(src);
		} catch (TTK91CompileException e) {
			return e.getMessage();
		} catch (TTK91Exception e) { 		// APIdoc indicates this can never happen, and initial  
			throw new RuntimeException(e);  // check of source code seems to confirm it
		}		
		return null;
	}
	
	
	
	/** Runs previously compiled program.
	 * @param keyboardInput keyboard input for the program, as [ \n\r\t\f,.:;] separated list of numbers
	 * @param maxExecutionSteps maximum number instruction to execute (prevent inifinite loops)
	 * @return runtime error message, <code>null</code> if no errors
	 */
	public String execute(String keyboardInput, int maxExecutionSteps) {
		try {
			app.setKbd(keyboardInput);
			controller.run(app, maxExecutionSteps);
			cpu = controller.getCpu();
			mem = controller.getMemory();
		} catch (TTK91RuntimeException e) {
			return e.getMessage();
		} catch (TTK91Exception e) {		// APIdoc indicates this can never happen
			throw new RuntimeException(e);
		}		
		return null;
	}
	
	
	
	/** Return contents of register
	 * @param registerCode TKK91Cpu.REG_*
	 * @throws IllegalArgumentException invalid register code is given
	 * @see fi.hu.cs.ttk91.TTK91Cpu#REG_R0 */
	int getRegister(int registerCode) {
		return cpu.getValueOf(registerCode);
	}
	
	
	

	/** Return contents of specified memory address.
	 * @throws IllegalArgumentException if Address > (CodeSize + DataSize - 1) 
	 */
	int getMemoryLocation(int address) {
		return mem.getValue(address);
	}
	
	/** Return symbol table that maps symbol names to symbol value addresses
	 */
	HashMap getSymbolTable() {
		return mem.getSymbolTable();
	}
	
	/** Return TitoKone screen output as String in format "1234, 1234, 1234". Returns and
	 * emptry String "" if the program produced no screen ouput. */
	String getScreenOutput() {
		return app.readCrt();
	}
	
	/** Return maximum size of stack reached during program execution.
	 * @return stack size, measured in 32-bit words
	 */ 
	int getStackMaxSize() {
		return 0;
	}

	/** Return number of executed instructions. */
	int getExecutionSteps() {
		return 0;		
	}
	
	/** Return number of instruction words in the program code */
	int getCodeSize() {
		return 0;		
	}
	
	/** Return number of words in program's data-area */
	int getDataSize() {
		return 0;
	}
	
	/** Return number memory references executed during program run. This number
	 * includes references caused by both data and instruction fetches. */
	int getMemoryAccessCount() {
		return 0;
	}
	
	String[] getUsedOpcodes() {
		return null;
	}	
}
