package fi.helsinki.cs.kohahdus.criteria;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import fi.helsinki.cs.kohahdus.Log;
import fi.hu.cs.titokone.Application;
import fi.hu.cs.titokone.BinaryInterpreter;
import fi.hu.cs.titokone.Control;
import fi.hu.cs.titokone.MemoryLine;
import fi.hu.cs.titokone.Processor;
import fi.hu.cs.titokone.RandomAccessMemory;
import fi.hu.cs.ttk91.TTK91CompileException;
import fi.hu.cs.ttk91.TTK91CompileSource;
import fi.hu.cs.ttk91.TTK91Exception;
import fi.hu.cs.ttk91.TTK91RuntimeException;

/** Capsulates the end-state of single run of TitoKone.
 *  
 * T�m� luokka toteutetaan toisessa iteraatiossa */
public class TitoState {
	private Control controller = new Control(new File("/dev/null"), new File("/dev/tty"));
	private Application app;
	private Processor cpu;
	private RandomAccessMemory mem;
	
	/** Memory reference count. Saved after execution so TitoState.getMemoryLocation(..)
	 * will not affect the counts. */
	private int memRef = 0;
	
	/** Program output. Saved after execution because readCrt() is destructive call */
	private String output ="";

	/** Compiled program code. Stored here after compilation, before exectution 
	 * (has implications on the opcodes seen in programs using self modifying code). */
	private MemoryLine code[];
	
	
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
			Log.write("Attempting to compile TTK91 program");
			app = (Application)(controller.compile(src));
			code = app.getCode();
		} catch (TTK91CompileException e) {
			Log.write("TTK91 program compilation failed");
			Log.write(e);
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
			Log.write("Attempting to run titokone with input '" + keyboardInput + "'");
			app.setKbd(keyboardInput + ","); // TitoKone does not accept empty string
			controller.run(app, maxExecutionSteps);
		} catch (TTK91RuntimeException e) {
			Log.write("Failed to run titokone");
			Log.write(e);
			return e.getMessage();
		} catch (TTK91Exception e) {		// APIdoc indicates this can never happen
			throw new RuntimeException(e);
		} finally {
			cpu = (Processor)(controller.getCpu());
			mem = (RandomAccessMemory)(controller.getMemory());
			memRef = mem.getMemoryReferences();
			output = app.readCrt();
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
		return app.getSymbolTable().toHashMap();
	}
	
	
	/** Return TitoKone screen output as String in format "1234, 1234, 1234". Returns an
	 * emptry String "" if the program produced no screen ouput. */
	String getScreenOutput() {
		String outputs[] = output.split("\n");

		// Reformat output as "124, 4242, 2335, 3535, 35325"
		StringBuffer buffer = new StringBuffer();
		for (int i=0; i<outputs.length; i++) {
			buffer.append(outputs[i]);
			if (i < outputs.length-1) {
				buffer.append(", ");
			}
		}	
		return buffer.toString();
	}
	
	
	
	
	
	/** Return maximum size of stack reached during program execution.
	 * @return stack size, measured in 32-bit words
	 */ 
	int getStackMaxSize() {
		return cpu.giveStackMaxSize();
	}

	/** Return number of executed instructions. */
	int getExecutionSteps() {
		return cpu.giveCommAmount();		
	}
	
	/** Return number of instruction words in the program code */
	int getCodeSize() {
		return mem.getCodeAreaSize();		
	}
	
	/** Return number of words in program's data-area */
	int getDataSize() {
		return mem.getDataAreaSize();
	}
	
	/** Return number data references executed during program run. The number includes
	 * memory references caused by both data reads and writes, but not memory reference
	 * caused by instruction fetches. */
	int getDataReferenceCount() {
		return memRef;
	}
	
	/** Return used opcodes in set of Strings. This does not include instructions
	 * DC, DS and EQU.  */
	Set<String> getUsedOpcodes() {
	    Set<String> opcodes = new HashSet<String>();

	    BinaryInterpreter interp = new BinaryInterpreter();
	    for(MemoryLine line : code) {
	    	String fullInstruction = interp.binaryToString(line.getBinary());
	    	if (fullInstruction.indexOf(' ') == -1) {
	    		opcodes.add(fullInstruction);		    		    
	    	} else {
	    		opcodes.add(fullInstruction.substring(0, fullInstruction.indexOf(' ')));
	    	}		
	    }
	    return opcodes;
	}
}
