/*
 * AccessServer.java
 *
 * Created on 11. lokakuuta 2004, 8:35
 */

/**
 *
 * @author  laine
 */
import trainer.*;
import java.sql.*;

import fi.helsinki.cs.kohahdus.trainer.DatabaseException;
import fi.helsinki.cs.kohahdus.trainer.DbModule;
import fi.helsinki.cs.kohahdus.trainer.User;


public class AccessServer extends DbModule {
    
    /** Creates a new instance of AccessServer */
    public AccessServer(String db_Driver, String db_Server, String db_Login, String db_Password) {
        super(db_Driver, db_Server,db_Login, db_Password);
    }

    public boolean accountInUse (String account) throws DatabaseException {
        String acQuery= "select userid from eauser where userid=?";
        boolean found=false;
        Connection cn = getConnection();
        PreparedStatement stm= null;
        ResultSet rs= null;
        try {
           if (cn!=null) {
              stm= cn.prepareStatement(acQuery);
              stm.setString(1,account);
              rs= stm.executeQuery();
              if (rs.next()) {
                 found=true; 
              }
           }   
        } 
        catch (SQLException ex) {
          throw new DatabaseException(ex.getMessage());
        }
        finally {
            try {
             if (rs!=null)
                rs.close();
             if (stm!=null)
                stm.close();
             cn.close();
           }  
             catch (SQLException fex) {}      
        }
        return found;
     }
 
  public User getUser(String uid) throws DatabaseException {
      
      String uQuery= "select userid, lastname, firstname, email, status, " +
              "extid, extid2, password, lpref, lastvisit "+
                     "from eauser "+
                     "where userid=?";
      String uUpdate="update eauser set lastvisit=? where userid=?";
      
      Connection cn = getConnection();
      PreparedStatement stm= null;
      PreparedStatement stm2=null;
      ResultSet rs= null;
      User user=null;
      try {
         if (cn!=null) {
            stm= cn.prepareStatement(uQuery);
            stm.setString(1,uid);
            rs= stm.executeQuery();
            
            if (rs.next()) {
               String account= rs.getString("userid");
               String lastname=rs.getString("lastname");
               String firstname=rs.getString("firstname");
               String email=rs.getString("email");
               String status= rs.getString("status");
               String extid= rs.getString("extid");
               String extid2= rs.getString("extid2");
               String psw= rs.getString("password");
               String lpref= rs.getString("lpref");
               java.sql.Timestamp lastvisit= rs.getTimestamp("lastvisit");
               user= new User(account, lastname, firstname, email, 
                              status, extid, extid2, psw, lpref, lastvisit);
               // should add updating the lastvisit
               stm2=cn.prepareStatement(uUpdate);
               stm2.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
               stm2.setString(2,uid);
               stm2.executeUpdate();
            }
         }   
     } 
     catch (SQLException ex) {
          throw new DatabaseException(ex.getMessage());
     }
     finally {
        try {
           if (rs!=null)
              rs.close();
           if (stm!=null)
              stm.close();
           if (stm2!=null)
              stm2.close(); 
           cn.close();
        }  
        catch (SQLException fex) {}      
     }
     return user;
  }

  public User registerUser (String userId, String lastname, 
                        String firstname,  String psswd, 
                           String extid, String extid2,String email, String lpref,String status) 
  throws DatabaseException {
      // insert a new user into the database and returns a user object
      String uInsert= "insert into eauser "+
                       "(userid, lastname, firstname, email, status, extid, extid2, passwd, lpref, lastvisit) " +
                       "values (?,?,?,?,?,?,?,?,?,?,?)";
      User user=null;    
      boolean found=false;
      Connection cn = getConnection();
      PreparedStatement stm= null;
      if (cn!=null) {
         try { 
            stm= cn.prepareStatement(uInsert);
            stm.setString(1,userId);
            stm.setString(2,lastname);
            stm.setString(3,firstname);
            if (email!=null)
                stm.setString(4,email);
            stm.setString(5,status);
            stm.setString(6,extid);
           stm.setString(7,extid2);
            stm.setString(8,psswd);
               stm.setString(9,lpref);
            java.sql.Timestamp lastvisit= new java.sql.Timestamp(System.currentTimeMillis());
            stm.setTimestamp(10,new Timestamp(System.currentTimeMillis()));
            // put the current date
            stm.executeUpdate();
            user= new User (userId,lastname,firstname, email,status,extid,extid2,psswd,lpref,lastvisit);
         }
         catch (SQLException ex) {
               throw new DatabaseException(ex.getMessage());
         }
         finally {
           try {
              if (stm!=null)
                 stm.close();
              cn.close();
           }  
           catch (SQLException fex) {}      
         }   
      }
      return user;
  }
}
