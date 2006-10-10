import fi.helsinki.cs.kohahdus.trainer.AnalyserInterface;
import fi.helsinki.cs.kohahdus.trainer.AttributeCache;
import fi.helsinki.cs.kohahdus.trainer.CacheException;
import fi.helsinki.cs.kohahdus.trainer.Feedback;
import fi.helsinki.cs.kohahdus.trainer.ParameterString;
import trainer.*;

public class SimpleAnalyser implements AnalyserInterface {

    final static String ECODE="E";
    final static String TCODE="T";
    final static int PARAMETER_ERROR=2;
    
    AttributeCache cache;
    String myName;
    String taskID;
    String language;
    ParameterString initP;
     
    
    public SimpleAnalyser() {
       cache=null;
       myName="SimpleAnalyser";
       taskID=null;
       language="EN";
       initP=null;
    }
 
    public void init (String taskid, String lang, String initparams) {
        taskID= taskid;
        language= lang;
        if (initparams!=null)
            initP= new ParameterString(initparams);
    };


/****
*  Analysis method, checks the answer and gives the feedback
*  Assumes: answer array contains only one string, if there are more only the first
*           is analysed.
*/

        
public Feedback analyse(String [] answer, String params ) throws CacheException {

  Feedback fb=null;
  String currentParameter=null;

  // check that the answer is not empty
  if (answer==null  || answer[0]==null || answer[0].length()==0) {
     fb= new Feedback(1,0,cache.getAttribute(ECODE,"NOANSWER","MESSAGE", language),null);
     return fb;
  }
      
  // compare the answer with check values until full match or all values checked
  // generate feedback for matching value

    currentParameter="VALUECOUNT";
    String vc= cache.getAttribute(TCODE,taskID,"VALUECOUNT",language);
    if (vc!=null) { 
      try {
        int valuesToCheck = Integer.parseInt(vc); 
        for (int i=1; i<=valuesToCheck; i++) {
          currentParameter="VALUE#"+i;
          String checkvalue= cache.getAttribute(TCODE,taskID,currentParameter,language);
          if (checkvalue!=null) {
            if (answer[0].equals(checkvalue)) {
              currentParameter="CORRECTNESS#"+i;
              String cInfo= cache.getAttribute(TCODE,taskID,currentParameter,language);
              if (cInfo!=null) {         
                int cInfoInt= Integer.parseInt(cInfo);
                currentParameter="MSG#"+i;                                    
                String fMessage= cache.getAttribute(TCODE,taskID,"MSG#"+i,language);       
                fb= new Feedback(0,cInfoInt,fMessage,null);
                return fb;
              }
              else {
                fb= new Feedback(PARAMETER_ERROR,
                         cache.getAttribute(ECODE,"NOTASKATTRIBUTE", "MESSAGE",language)+
                         myName+' '+taskID+'.'+"CORRECTNESS#"+i);
                return fb;
              }
            }
          } 
          else {
             fb= new Feedback(PARAMETER_ERROR,
                         cache.getAttribute(ECODE,"NOTASKATTRIBUTE","MESSAGE",language)+
                         myName+' '+taskID+'.'+"VALUE#"+i);
             return fb;
          }
        }
      }
      catch (NumberFormatException ex) {
         fb= new Feedback (PARAMETER_ERROR,
                  cache.getAttribute(ECODE,"ATTRIBUTETYPEERROR","MESSAGE",language) + 
                     myName+ ' '+ taskID+'.'+currentParameter);
         return fb;
      }
    }
    else {
      fb= new Feedback(PARAMETER_ERROR,
               cache.getAttribute(ECODE,"NOTASKATTRIBUTE","MESSAGE",language)+
                         myName+ ' '+ taskID+'.'+"VALUECOUNT");
             return fb;
    }
                
    // if no match or errors were found generate feedback for 'no match'

   String oMessage= cache.getAttribute(TCODE,taskID,"OTHERS",language);
   if (oMessage!=null) {
      fb= new Feedback(0,0,oMessage,null);
      return fb;
   }   
   else {
      fb = new Feedback (PARAMETER_ERROR, 
                cache.getAttribute(ECODE,"NOTASKATTRIBUTE","MESSAGE",language)+
                         myName+ ' ' + taskID+'.'+"OTHERS");
                return fb;
   }
} 

public void registerCache( AttributeCache c) {
   cache=c;
}

}

