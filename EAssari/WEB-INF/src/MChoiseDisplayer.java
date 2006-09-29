import trainer.*;

public class MChoiseDisplayer implements DisplayerInterface {

    final static String DCODE="D";
    final static String TCODE="T";
    final static String ECODE="E";
    
    
    AttributeCache cache;
    String myName;
    String taskID;
    String language;
    ParameterString initP=null;
     
    
    public MChoiseDisplayer() {
       cache=null;
       myName="MChoiseDisplayer";
       taskID=null;
       language="EN";
    }
 
    public void init (String taskid, String lang, String initparams) {
        taskID= taskid;
        language= lang;
        if (initparams!=null)
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
     * generates the body of the form by simply loading it from the database
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
    
       int checked=1;
       String currentParameter="VALUECOUNT";
       String vc= cache.getAttribute(TCODE,taskID,"VALUECOUNT",language);
       if (vc!=null) { 
          try {
             if (initVal!=null)
                checked=Integer.parseInt(initVal[0]);	
             int valuesToCheck = Integer.parseInt(vc);
             st.append("<table>"); 
             for (int i=1; i<=valuesToCheck; i++) {
                 currentParameter="VALUE#"+i;
                 String checkvalue= cache.getAttribute(TCODE,taskID,currentParameter,language);
                 if (checkvalue!=null) {
                    st.append("<tr><td>\n");
                    st.append("<input type=\"radio\" name=\"answer\" value=\"");
                    st.append(i);
                    st.append("\"");
                    st.append((checked==i)?"checked ":" ");
                    st.append("></td>\n");
                    st.append("<td>");
                    st.append(checkvalue);
                    st.append("</td></tr>");
                 }       
             } 
             st.append("</table>");
          } 
          catch (NumberFormatException ex) {
             st.append(cache.getAttribute(ECODE,"ATTRIBUTETYPEERROR","MESSAGE",language) + 
                     myName+ ' '+ taskID+'.'+currentParameter);
          }
       }
       else {
          st.append(cache.getAttribute(ECODE,"NOTASKATTRIBUTE","MESSAGE",language)+
                         myName+ ' '+ taskID+'.'+"VALUECOUNT");
       }
       st.append("</div>\n");
       
       if (allowTry) {
          st.append("<div class=\"buttons\">\n");
          st.append("<input type=\"submit\" name=\"submit\" value=\"");
          st.append(cache.getAttribute(DCODE,myName,"SUBMIT",language));
          st.append("\">\n");
          st.append("<input type=\"RESET\" value=\"");
          st.append(cache.getAttribute(DCODE,myName,"RESET",language));
          st.append("\">\n");
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

