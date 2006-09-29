/*
 * AnalyserInterface.java
 *
 * Created on 26. marraskuuta 2003, 17:31
 */

package trainer;

/**
 *
 * @author  laine
 * @version 
 */
public interface AnalyserInterface {

    /*****
     * initializes the analyser
     * Params:  
     *    taskID =full task identifier (couse.module.task)
     *    lanquage = language used (currently FI,EN)
     *    initparams = initialization parameters, xml, mäy be null
     */
    public void init (String taskID, String language, String initparams);
    
   /****
    * Analysis of the answer
    *    return Feedback
    *    answer = an array of answer items
    *             analyser knows how to interpret
    *    params= generated task parameters, displayer and analyser together determine the form
    *            xml may be used
    * 
    */  
   
    public Feedback analyse (String [] answer, String params) throws CacheException;
  
    /***
     * Registration of cache to be used in storing attribute values
     */
    public void registerCache(AttributeCache cache );
}

