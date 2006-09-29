package trainer;

/****
 * ErrorMSG.java specifies the errormessages used in HLTrainer
 *
 * Created on 15. October 2003,
 * @author Harri Laine
 */
public class ErrorMSG {
   
    public static final int EB                   =1000;
    public static final int NO_COURSEID          =10;
    public static final int NO_TASKID            =11;
    public static final int TASK_UNKNOWN         =12;
    public static final int TASK_DISPLAY_FAILED  =13;
    public static final int NO_DISPLAYER         =14;
    public static final int NO_ANALYSER          =15; 
    public static final int NO_STUDENT           =16;
    public static final int EMPTY_ANSWER         =17;
    
   
    public ErrorMSG() {}
    
    public static String getMessage(int errorcode, String language, String extra) {
       String em=null;
       String fb=null;
       boolean finnish= language.equals("FI");
       switch (errorcode) {
          case NO_COURSEID:
             em= (finnish?"Kurssitunnus puuttuu. ":"Course identifier missing. ");
             break;
          case NO_TASKID:
             em= (finnish?"Tehtävätunnus puuttuu. ":"Task identifier missing. ");
             break;
          case TASK_UNKNOWN:
             em= (finnish?"Ei löydy määritystä tehtävälle: ":"No specification for task: ");
             break;
          case TASK_DISPLAY_FAILED:
             em= (finnish?"Tehtävän esittäminen ei onnistu: ":"Task display failed: ");
             break;
          case NO_DISPLAYER:   
             em= (finnish?"Näyttömoduulia ei löydy ":"Displayer module is missing");
             break;
           case NO_ANALYSER:   
             em= (finnish?"Tarkistusmoduulia ei löydy ":"Evaluation module is missing");
             break;
           case NO_STUDENT:   
             em= (finnish?"Opiskelija tuntematon ":"Student unidentified");
             break;
           case EMPTY_ANSWER:  
             em= (finnish?"Tyhjä vastaus ei käy!":"Please, give a non-empty answer.");  
        }
        if (extra!=null)
            fb= em + extra;
        else
            fb= em;
        return fb;
    }    
    
    public static String reportAsHTML(int errorcode, String language, String extra){ 
        StringBuffer sb= new StringBuffer();
        sb.append("<TABLE WIDTH=\"100%\" BORDER=1 BGCOLOR=\"#EED8AE\"><TD><B>");
        sb.append(getMessage(errorcode,language,extra));
        sb.append("</B></TD></TABLE>");
        return sb.toString();
    }    
}
