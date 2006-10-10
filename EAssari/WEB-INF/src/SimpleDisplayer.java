import fi.helsinki.cs.kohahdus.trainer.AttributeCache;
import fi.helsinki.cs.kohahdus.trainer.CacheException;
import fi.helsinki.cs.kohahdus.trainer.DisplayerInterface;
import fi.helsinki.cs.kohahdus.trainer.ParameterString;
import trainer.*;

public class SimpleDisplayer implements DisplayerInterface {

    final static String DCODE="D";
    final static String TCODE="T";
    
    
    AttributeCache cache;
    String myName;
    String taskID;
    String language;
    ParameterString initP;
     
    
    public SimpleDisplayer() {
       cache=null;
       myName="SimpleDisplayer";
       taskID=null;
       language="EN";
    }
 
    public void init (String taskid, String lang, String initparams) {
        taskID= taskid;
        language= lang;
        initP= new ParameterString(initparams);
    };
    
    /***
     * loads the URL of the stylesheet attached to the displayer
     */
    public String getStyle () throws CacheException {
       String style= cache.getAttribute(DCODE,myName,"STYLESHEET",language);
       return style;
    }
    
    /****
     * generates the tasktype specific javascript for processing the task form
     */
    public String getScript ()  throws CacheException {
       String script= cache.getAttribute(DCODE, myName, "JAVASCRIPT", language);
       return script;
    }

     /****
     * generates the body of the form bt simply loading it from the database
     */
    public String getSetting(String [] initVal, String params, String hiddens, boolean allowTry) throws CacheException {
       StringBuffer st = new StringBuffer();
       st.append("<div class=\"taskframe\">");
       st.append("<form name=\"answerform\" action=\"Answer\">\n");
       st.append("<p class=\"taskheader\">\n");
       st.append(taskID+": ");
       st.append(cache.getAttribute(TCODE, taskID, "NAME", language));
       st.append("</p>\n");
       st.append("<fieldset class=\"settingframe\">\n");
       st.append("<legend>");
       st.append(cache.getAttribute(DCODE, myName, "TASK", language));
       st.append("</legend>\n");
       st.append("<div class=\"setting\">\n");
       st.append(cache.getAttribute(TCODE,taskID,"SETTING",language));
       st.append("</div>\n"); 
       st.append("</fieldset>\n");
       st.append("<fieldset class=\"answerframe\">\n");
       st.append("<legend>");
       st.append(cache.getAttribute(DCODE,myName,"ANSWER",language));
       st.append("</legend>\n");
       st.append("<div class=\"answer\">\n");
       if (initP.nextElementByName("ANSWER")) {
          String tp= initP.getAttributevalue("TYPE");
          if (tp!=null && tp.equals("FIELD")) {
              String arg1=initP.getAttributevalue("SIZE"); 
              String arg2=initP.getAttributevalue("MAXLENGTH");
              st.append("<input type=\"text\" name=\"answer\" size=\"");
              if (arg1!=null) 
                 st.append(arg1);
              else 
                 st.append("20");
              st.append("\"");
              if (arg2!=null) {
                 st.append("maxlength=");
                 st.append(arg2);
                 st.append("\"");
              }                
              st.append(" value=\"");
              if (initVal!=null) 
                  st.append(initVal);
              st.append("\">\n");
          }    
          else {
              String arg3=initP.getAttributevalue("COLS");
              String arg4=initP.getAttributevalue("ROWS");
              st.append("<textarea name=\"answer\" cols=\"");
              if (arg3!=null)
                 st.append(arg3);
              else
                 st.append("60");
              st.append("\" rows=\"");
              if (arg4!=null)
                 st.append(arg4);
              else
                 st.append("5");
              st.append("\">");
              if (initVal!=null) 
                  st.append(initVal[0]);
              st.append("</textarea>\n");
          }                    
       }   
       else {
          st.append("<input type=\"text\" name=\"answer\" size=\"20\" value=\"");
          if (initVal!=null) 
                  st.append(initVal[0]);
          st.append("\">\n");
       }
       
       st.append("</div>\n");
       
       if (allowTry) {
          st.append("<div class=\"buttons\">\n");
          st.append("<input type=\"submit\" name=\"submit\" value=\"");
          st.append(cache.getAttribute(DCODE,myName,"SUBMIT",language));
          st.append("\">\n");
          st.append("<input type=\"BUTTON\" value=\"");
          st.append(cache.getAttribute(DCODE,myName,"RESET",language));
          st.append("\" onClick=\"document.answerform.answer.value='';\">\n");
          st.append("</div>");
          if (hiddens!= null)
            st.append(hiddens);
       }
       st.append("</fieldset>\n");
       st.append("</form>\n"); 
       st.append("</div>");
       return st.toString();
    }   
  
     /****
     * generates a link to technical instructions
     */  
    
    public String getHelps() throws CacheException {
        StringBuffer hb= new StringBuffer();
        String helpURL= cache.getAttribute(DCODE,myName,"HOWTOHELPURI",language);
        String helpText=null;
        String helpText2=null;
        if (helpURL!=null) {
            helpText =cache.getAttribute(DCODE,myName,"HOWTOHELPLINK",language);
            if (helpText==null) 
               helpText = helpURL;
        }    
           
        String helpURL2=null;
        helpURL2= cache.getAttribute(DCODE,myName,"TOPICHELPURI",language);
        if (helpURL2!=null) {
            helpText2 =cache.getAttribute(DCODE,myName,"TOPICHELPLINK",language);
            if (helpText==null) 
               helpText2= helpURL2; 
        } 
        if (helpURL!=null ||helpURL2!=null) {
            hb.append("<div class=\"helpframe\">");
            hb.append("<a href=\""+helpURL+"\" target=\"_blank\">"+helpText+"</a> ");
            hb.append("<a href=\""+helpURL+"\" target=\"_blank\">"+helpText+"</a> ");
            hb.append("</div>");
        }
        return hb.toString();
    }
 


    public void registerCache( AttributeCache c) {
        cache=c;
    }
}

