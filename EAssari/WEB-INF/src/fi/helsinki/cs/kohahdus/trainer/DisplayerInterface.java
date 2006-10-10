package fi.helsinki.cs.kohahdus.trainer;

/****
 * Interface for generating the taks display
 */

public interface DisplayerInterface {
    
    /****
     * initializes the Displayer
     * Params:
     *    taskid= full task identifier 
     *    language= language used (FI,EN)
     *    initparams= parameters used for Dispalyer initialization, XML format
     */
    public void init (String taskid, String language, String initparams);
    
    /****
     * Generates the JavaSripts needed for the user interface
     */
    public String getScript () throws CacheException;
  
     /****
      * Generates the actual represewntation of the task in HTML
      * Params:
      *   initval contains the default answer
      *   params  contains the task specific generated parametervalues - the format of the string
      *           is Displayer5 specific but xml is recommended
      *   hiddens contains hidden http parameters to be includen in the form
      *   allowTry specifies whether answerbutton should be included or not  
      */
    public String getSetting(String [] initVal, String params, String hiddens,  boolean allowTry) throws CacheException;
 
   /***
    * generates help 'links'
    */
    public String getHelps() throws CacheException;
    

    /***
     * Registration of cache to be used in storing attribute values
     */
    public void registerCache(AttributeCache cache );
}

