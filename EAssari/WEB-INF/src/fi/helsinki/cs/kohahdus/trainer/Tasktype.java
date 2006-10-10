/*
 * Tasktype.java
 *
 * Created on 26. marraskuuta 2003, 10:47
 */

package fi.helsinki.cs.kohahdus.trainer;

/**
 *
 * @author  laine
 * @version 
 */
public class Tasktype extends Object {

    String tasktypeName;
    String displayerClass;
    String displayerInit;
    String analyserClass;
    String analyserInit;
    String style;


/** Creates new Tasktype */
    public Tasktype(String name, String displayer, String displayerinit, String analyser, String analyserinit, String tStyle) {
        tasktypeName=   name;
        displayerClass= displayer;
        displayerInit=  displayerinit;
        analyserClass=  analyser;
        analyserInit=   analyserinit;
        style=          tStyle;
    }
   
    public DisplayerInterface getDisplayer(String language, String taskID) {
        try {
           Class DI = Class.forName(displayerClass);
           if (DI!=null) {
              DisplayerInterface disp = (DisplayerInterface) DI.newInstance();
              disp.init(taskID,language, displayerInit);
              return disp;
           }
           return null;
        }   
        catch (Exception e) {
           return null;
        }      
    }
    
    public AnalyserInterface getAnalyser(String language, String taskID ) {
        try {
           Class AC = Class.forName(analyserClass);
           if (AC!=null) {
              AnalyserInterface an = (AnalyserInterface) AC.newInstance();
              an.init(taskID, language, analyserInit);
              return an;
           }
           else
              return null;
           
        }   
        catch (Exception e) {
           return null;
        }      
    } 

    public String getDisplayerClass() {
        return displayerClass;
    }    
    
    public String getAnalyserClass() {
        return analyserClass;
    }  
    
    public String getStyle () {
        if (style==null)
            return "../styles/defaulttask.css";
        else
            return "../styles/"+style;
    }    
}
