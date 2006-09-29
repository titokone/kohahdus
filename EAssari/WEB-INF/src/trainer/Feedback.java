/*
 * Feedback.java
 *
 * Created on 10. lokakuuta 2003, 8:42
 */

package trainer;

/**
 *
 * @author  laine
 * @version 
 */
public class Feedback extends java.lang.Object {
   
 
    int errorCode;   // zero if no system errors
    int evaluation;  // task dependent correctness 0 faulty, 100 perfect
                     // use of values between 0 and 100 is analyser dependent
    String msgText;  // feedback message
    String extra;    // suplementary feedeback

    /** Creates new Feedback */
    
    /***
     * creates emty feedback
     */
    public Feedback () {
        errorCode=0;
        evaluation=0;
        msgText=null;
        extra=null;
    }    
   
    /***
     * creates feedback for fatal errors
     * Params:
     *   eCode = error code (0, 1, 2)
     *   eMsg  =Eroor message
     */
    public Feedback(int eCode, String eMsg) {
        errorCode= eCode;
        evaluation=0;
        msgText= eMsg;
        extra=null; 
    }
   
    /***
     * creates general feedback
     *   eCode = error code (0, 1, 2)
     *   eval  = point obtained (0-100)
     *   evalMsg = evaluation message
     *   evalExtra = additional feedback, for example the result of sql query
     */
    public Feedback(int eCode, int eval, String evalMsg, String evalExtra) {
        errorCode=eCode;
        evaluation=eval;
        msgText=evalMsg;
        extra=evalExtra;
    }

   public Feedback(String fbstr) {
       if (fbstr==null) {
          errorCode=0;
          evaluation=0;
          msgText=null;
          extra=null;
       }
       else {
           String [] osat= fbstr.split("#");
           try {
              errorCode= Integer.parseInt(osat[0]);
              evaluation= Integer.parseInt(osat[1]);
              msgText= osat[2];
              extra= osat[3];
           }
           catch (NumberFormatException n) {
              errorCode=0;
              evaluation=0;
              msgText=null;
              extra=null;
           }   
       }
   }    
              
    
    public int getErrorCode() {
        return errorCode;
    }    
   
    public int getEvaluation() {
        return evaluation;
    }
 
    public String getMsgText() {
        return msgText;
    }
    
    public String getExtra() {
        return extra;
    }    
    
    public void setErrorCode(int e) {
      errorCode=e;
    }  
    
    public void setMsgText(String msg) {
      msgText= msg;
    } 
    

    public boolean causedFatalError () {
      return errorCode>1;
    }
    
    public boolean wasPassed(int cutoff) {
       return evaluation>=cutoff;
    }  
    
    public String toString() {
       if (errorCode==0 && evaluation==0 && msgText==null && extra==null)
           return null;
       else    
           return errorCode+"#"+evaluation+"#"+msgText+"#"+extra;
    }
}
