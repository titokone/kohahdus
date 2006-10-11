package fi.helsinki.cs.kohahdus;

/** Capsulates the end-state of single run of TitoKone.
 *  
 * Tämä luokka toteutetaan toisessa iteraatiossa */
public class TitoState {
	
	/** Return the contents of register Num (0 for "R0", etc). */
	int getRegister(int num) {
		return 0;
	}
	
	int getMemoryLocation(int address) {
		return 0;
	}
	
	int getSymbolAddress(String symbol) {
		return 0;
	}
	
	int[] getOutput() {
		return null;
	}
	
	int getStackMaxSize() {
		return 0;
	}
	
	int getInstructionCount() {
		return 0;		
	}
	
	int getCodeSize() {
		return 0;		
	}
	
	int getDataSize() {
		return 0;
	}
	
	int getMemoryAccessCount() {
		return 0;
	}
	
	int getDataMemoryAccessCount() {
		return 0;
	}
	
	String[] getUsedOpcodes() {
		return null;
	}	
}
