/*
 * User.java
 *
 * Created on 21. lokakuuta 2004, 15:37
 */

package trainer;

import java.util.*;
/**
 *
 * @author  laine
 */
public class User {
    String userid;
    String lastname;
    String firstname;
    String email;
    String externalid;
    String externalid2;
    String status;
    String passwd;
    String lpref;
    java.sql.Timestamp lastvisit;
    Hashtable activeTasks;
    
    /** Creates a new instance of User */
    public User(String uid, String lname, String fname, String email, String status, String extid, 
                String extid2, String psw, String lpref, java.sql.Timestamp lastvisit) {
       this.userid=uid;
       this.lastname=lname;
       this.firstname=fname;
       this.email=email;
       this.status=status;
       this.externalid=extid;
       this.externalid2=extid2;
       this.passwd=psw;
       this.lpref=lpref;
       this.lastvisit=lastvisit;
       this.activeTasks = new Hashtable(30);    
    }
    
    public String getPassword() { return passwd;}
    public String getName() {return firstname+ ' '+ lastname;}
    public String getLastname() {return lastname;}
    public String getFirstName() {return firstname;}
    public String getEmail() {return email;}
    public String getExternalid() {return externalid;}
    public String getExternalid2() {return externalid2;}
    public String getStatus() {return status;}
    public String getLpref() {return lpref;}
    public java.sql.Timestamp getLastVisit() {return lastvisit;}
    public String getUserid() {return userid;}
    
    public int registerTry(String target, boolean wassuccess, boolean wasintime, int currentcredit) {
        String state= (String) activeTasks.get(target);
        String newState=null;
        int trc=0;
        int points=0;
        if (state==null) {  
            if (wasintime)
                newState="YES";
            else
                if (wassuccess)
                    newState="LATE";
                else
                    newState="NO";
            trc=1;
            activeTasks.put(target,"1:"+currentcredit+":"+newState);
        }
        else {
            int colon= state.indexOf(":");
            trc= Integer.parseInt(state.substring(0,colon));
            int col2= state.indexOf(":",colon+1);
            points= Integer.parseInt(state.substring(colon+1,col2));
            String oldstate=state.substring(col2+1);
            newState=oldstate;
            if (wasintime && !state.equals("YES"))
                newState="YES";
            else
                if (wassuccess && state.equals("NO"))
                    newState="LATE";
            trc++;
            if (points<currentcredit)
                points=currentcredit;
            activeTasks.put(target,trc+":"+points+":"+newState);
        }
        return trc;
    }
    
   public String getState(String target) {
       String st= (String) activeTasks.get(target);
       String state=null;
       if (st==null)
           state="NONE";
       else {
           int colon= st.indexOf(":");
           int col2=st.indexOf(":",colon+1);
           state=st.substring(col2+1);
       }
       return state;
   } 
   
   public int getAttempts(String target) {
       String state= (String) activeTasks.get(target);
       if (state==null)
           return 0;
       else {
          int colon= state.indexOf(":");
          int trc= Integer.parseInt(state.substring(0,colon));
          return trc;
       }
}
   
public int getCurrentCredit(String target) {
       String state= (String) activeTasks.get(target);
       if (state==null)
           return 0;
       else {
          int colon= state.indexOf(":");
          int colon2= state.indexOf(":",colon+1);
          int trc= Integer.parseInt(state.substring(colon+1,colon2));
          return trc;
       }
}

}