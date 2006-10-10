package fi.helsinki.cs.kohahdus.trainer;
/**
 * Trainer servlet superclass
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

/**
 * Creates a TaskBase object to be used as the cache for all task related information
 * 
 */

public class TrainerServlet extends HttpServlet {

    protected static TaskBase taskBase=null;
    protected static String dbDriver=null;
    protected static String dbServer=null;
    protected static String dbLogin=null;
    protected static String dbPassword=null;

    public void init (ServletConfig config) throws ServletException  {
       super.init(config);
       if (taskBase == null) { 
         // Only created by first servlet to call
         String conFile = config.getServletContext().getInitParameter("confile");
         try {
             Properties p = new Properties();
             p.load(new FileInputStream(conFile));
             dbDriver = (String) p.get("dbDriver");
             dbServer = (String) p.get("dbServer");
	     dbLogin  = (String) p.get("dbLogin");
	     dbPassword = (String) p.get("dbPassword");
             taskBase = new TaskBase(dbDriver,dbServer,dbLogin,dbPassword);
         }
         catch (Exception e) {
             throw new ServletException("Problems with configuration file: "+conFile);
         }    
       }
    }
  
    protected String pageHeader(String titleAttachment, String jscript, String css) {
        StringBuffer header= new StringBuffer();
        header.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0Transitional//EN\"> \n");
        header.append("<html><head>\n");
        header.append("<title>eAssari (1.0) ");
        if (titleAttachment!=null)
            header.append(titleAttachment);
        header.append("</title>\n");
        if (css!=null) {
           header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
           header.append(css);
           header.append("\">");
        }  
        if (jscript!=null) {
           header.append("<script type=\"text/javascript\" src= \"");
           header.append(jscript);
           header.append("\"></script>");
        }   
        header.append("</head>");
        return header.toString();
    }       

    protected String bodyBegin() {
       return "<body>\n";
    }    
    
    protected String bodyEnd() {
        return "</body>";
    }    
    
    protected String footer() {
        return "</html>";
    } 
    
    protected String errorBox(String msg) {
        StringBuffer sb= new StringBuffer();
        sb.append("<table>");
        sb.append("<tr><th>eAssari error</th></tr>");
        sb.append("<tr><td>");
        sb.append(msg);
        sb.append("</td></table><p></p>");
        return sb.toString();
    }  
       
    protected String fatalErrorNotification(String taskInfo, String msg) {
        StringBuffer page= new StringBuffer();
        page.append(pageHeader("ERROR",null,"../styles/errorpage.css"));
        page.append(bodyBegin());
        page.append(errorBox(msg+"<br>"+taskInfo));
        page.append(bodyEnd());
        page.append(footer());
        return page.toString();
    }    
      
    
}

