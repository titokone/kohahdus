package fi.helsinki.cs.kohahdus.criteria;

/** Capsulates the end-state of single run of TitoKone.
 *  
 * Tämä luokka toteutetaan toisessa iteraatiossa */
public class TitoState {

	/** Return contents of register Num (0 for "R0", etc).
	 * @throws IllegalArgumentException if Num > 7 or Num < 0 */
	int getRegister(int num) {
		return 0;
	}

	/** Return contents of specified memory address.
	 * @throws IllegalArgumentException if Address > (CodeSize + DataSize - 1) 
	 */
	int getMemoryLocation(int address) {
		return 0;
	}
	
	/** Return memory address of specified symbol
	 * @param symbolName Name of the symbol 
	 * @return address or -1 if symbolName does not exist in the symbol table
	 */
	int getSymbolAddress(String symbolName) {
		return 0;
	}
	
	/** Return TitoKone screen output as String in format "1234, 1234, 1234". Returns and
	 * emptry String "" if the program produced no screen ouput. */
	String getScreenOutput() {
		return null;
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
