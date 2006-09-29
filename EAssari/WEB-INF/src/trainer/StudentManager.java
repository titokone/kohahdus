/*
 * StudentManager.java
 *
 * Created on 30. elokuuta 2006, 8:36
 *
 * Manages students answers.
 */

package trainer;

/**
 *
 * @author laine
 */
public class StudentManager {
    
    String dbDriver =null;
    String dbServer = null;
    String dbLogin = null;
    String dbPassword = null;
    
    /** Creates a new instance of StudentManager */
    public StudentManager(String db_Driver, String db_Server, String db_Login, String db_Password) {dbDriver = db_Driver;
        dbServer = db_Server;
	dbLogin  = db_Login;
	dbPassword = db_Password;
    }
  
    public int registerTry(Student student) {
        //if student has status for the task update it
        //else create status record
        //return the trial count
        int thusfar= student.getCounter(fulltaskID);
        
        if (thusfar=0) {
            
        } 
            
        
                
    }
    
    
   private Connection getConnection () throws CacheException {
        // load database driver if not already loaded
        Connection conn= null;
        try { 
          Class.forName(dbDriver);               // load driver
          //  Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) { 
             throw new CacheException ("Couldn't find the driver "+dbDriver);
        }
        try {
           conn= DriverManager.getConnection(dbServer, dbLogin, dbPassword);
        } catch (SQLException sex) {
           throw new CacheException("Couldn't establish repository connection. ");
        }
        return conn;
   } 
}
