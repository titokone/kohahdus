/*
 * DatabaseException.java
 *
 * Created on 8. lokakuuta 2004, 13:48
 */

package fi.helsinki.cs.kohahdus.trainer;

/**
 *
 * @author  Harri Laine
 */
public class DatabaseException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>DatabaseException</code> without detail message.
     */
    public DatabaseException() {
    }
    
    
    /**
     * Constructs an instance of <code>DatabaseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DatabaseException(String msg) {
        super("DATABASE ERROR: "+msg);
    }
}
