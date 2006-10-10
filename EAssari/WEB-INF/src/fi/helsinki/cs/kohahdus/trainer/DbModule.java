/*
 * DbModule.java
 *
 * Created on 8. lokakuuta 200

 *
 */

package fi.helsinki.cs.kohahdus.trainer;

/**
 *
 * @author  Harri Laine
 */

import java.util.*;
import java.sql.*;
import java.math.*;

public class DbModule {
    
    String dbDriver =null;
    String dbServer = null;
    String dbLogin = null;
    String dbPassword = null;
    
    /** Creates a new instance of DbModule */
    public DbModule(String db_Driver, String db_Server, String db_Login, String db_Password) {
        dbDriver = db_Driver;
        dbServer = db_Server;
	dbLogin  = db_Login;
	dbPassword = db_Password; 
    }
    
    protected Connection getConnection () throws DatabaseException {
        // load database driver if not already loaded
        Connection conn= null;
        try { 
          Class.forName(dbDriver);               // load driver if not loaded
          //  Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) { 
             throw new DatabaseException ("Couldn't find the database driver "+dbDriver);
        }
        try {
           conn= DriverManager.getConnection(dbServer, dbLogin, dbPassword);
        } catch (SQLException sex) {
           throw new DatabaseException("Couldn't establish a repository connection. ");
        }
        return conn;
   }
    
}
