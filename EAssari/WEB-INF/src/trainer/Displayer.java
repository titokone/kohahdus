package trainer;
/***
 * Anstract superclass for displayers
 */

public class Displayer implements DisplayerInterface {

    final static String DCODE="D";
    final static String TCODE="T";
    
    
    AttributeCache cache;
    String myName;
     
    
    public Displayer() {
       cache=null;
       myName="Displayer";
    }
 
    public void init (String initparams) {
    };
    
    /***
     * loads the URL of the stylesheet attached to the displayer
     */
    public String getStyle (String language ) {
       String style= cache.getAttribute(DCODE,myName,"STYLESHEET",language);
       return style;
    }
    
    /****
     * generates the tasktype specific javascript for processing the task form
     */
    public String getScript (String language) {
       String script= cache.getAttribute(DCODE, myName, "JAVASCRIPT", language);
       return script;
    }

     /****
     * generates the body of the form bt simply loading it from the database
     */
    public String getSetting( String taskID, String language) {
       String setting = cache.getAttribute(TCODE, taskID, "SETTING", language);
       return setting;
    }   
  
     /****
     * generates a link to technical instructions
     */  
    public String getTechnicalHelp(String language) {
        String helpURL= cache.getAttribute(DCODE,myName,"HOWTOHELP",language);
        String helpText=null;
        String helpLink=null;
        //       String helpImg=null;
        //       String helpTE
        if (helpURL!=null) {
            helpText =cache.getAttribute(DCODE,myName,"HOWTOHELPTEXT",language);
            if (helpText==null) 
               helpText = (language.equals("FI")?"Ohje":"Help");
            helpLink="<A HREF=\""+helpURL+"\">"+helpText+"</A>";
        }    
        return helpLink;
    }
    
    /****
     * generates a link to topic specifig help
     */   
    public String getTopicHelp(String taskID, String language) {
        String helpURL= cache.getAttribute(TCODE, taskID,"TOPICHELP",language);
        String helpText=null;
        String helpLink=null;
        //       String helpImg=null;
        //       String helpTE
        if (helpURL!=null) {
            helpText =cache.getAttribute(TCODE,taskID,"TOPICHELPTEXT",language);
            if (helpText==null) 
               helpText = (language.equals("FINNISH")?"Ohje":"Help");
            helpLink="<A HREF=\""+helpURL+"\">"+helpText+"</A>";
        }    
        return helpLink;
    } 



    public void registerCache( AttributeCache c) {
        cache=c;
    }
}

