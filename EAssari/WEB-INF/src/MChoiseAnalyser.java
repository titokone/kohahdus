import fi.helsinki.cs.kohahdus.trainer.AnalyserInterface;
import fi.helsinki.cs.kohahdus.trainer.AttributeCache;
import fi.helsinki.cs.kohahdus.trainer.CacheException;
import fi.helsinki.cs.kohahdus.trainer.Feedback;
import fi.helsinki.cs.kohahdus.trainer.ParameterString;
import trainer.*;

public class MChoiseAnalyser implements AnalyserInterface {

    final static String ECODE="E";
    final static String TCODE="T";
    final static int PARAMETER_ERROR=2;
    
    AttributeCache cache;
    String myName;
    String taskID;
    String language;
    ParameterString initP;
     
    
    public MChoiseAnalyser() {
       cache=null;
       myName="MchoiseAnalyser";
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

     currentParameter="CORRECTNESS#"+answer[0];
  try {   
     String cInfo= cache.getAttribute(TCODE,taskID,currentParameter,language);
     if (cInfo!=null) {         
         int cInfoInt= Integer.parseInt(cInfo);
         currentParameter="MSG#"+answer[0];                                    
         String fMessage= cache.getAttribute(TCODE,taskID,"MSG#"+answer[0],language);       
         fb= new Feedback(0,cInfoInt,fMessage,null);
         return fb;
     }
     else {
         fb= new Feedback(PARAMETER_ERROR,
         cache.getAttribute(ECODE,"NOTASKATTRIBUTE", "MESSAGE",language)+
                myName+' '+taskID+'.'+currentParameter);
         return fb;
     }
  }   
  catch (NumberFormatException ex) {
         fb= new Feedback (PARAMETER_ERROR,
                  cache.getAttribute(ECODE,"ATTRIBUTETYPEERROR","MESSAGE",language) + 
                     myName+ ' '+ taskID+'.'+currentParameter);
         return fb;
  }
}
  
public void registerCache( AttributeCache c) {
   cache=c;
}

}

