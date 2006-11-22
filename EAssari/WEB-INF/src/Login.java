import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import fi.helsinki.cs.kohahdus.trainer.CacheException;
import fi.helsinki.cs.kohahdus.trainer.DatabaseException;
import fi.helsinki.cs.kohahdus.trainer.TrainerServlet;
import fi.helsinki.cs.kohahdus.trainer.User;
import trainer.*;

/*
 * Login.java
 *
 * Created on 7. lokakuuta 2004, 13:37
 */

/**
 *
 * @author  Harri Laine
 */


public class Login extends TrainerServlet {

    public static AccessServer accessServer;
    
    public void init (ServletConfig config) throws ServletException  {
       super.init(config);
       if (accessServer == null) { 
         // Only created by first Login servlet to call
         // String conFile = config.getServletContext().getInitParameter("confile");
         // try {
         //    Properties p = new Properties();
         //    p.load(new FileInputStream(conFile));
         //    String dbDriver = (String) p.get("dbDriver");
         //    String dbServer = (String) p.get("dbServer");
	 //    String dbLogin  = (String) p.get("dbLogin");
	 //    String dbPassword = (String) p.get("dbPassword");
             accessServer = new AccessServer(dbDriver,dbServer,dbLogin,dbPassword);
         // }
         // catch (Exception e) {
         //    throw new ServletException("Problems with configuration file: "+conFile);
         // }    
       }
    }
    
   public void doPost(HttpServletRequest req, HttpServletResponse res)
                                throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    // Get the user's name and password
    String userid = req.getParameter("userid");
    String lng= req.getParameter("lng");
    if (lng==null) 
        lng="FI";
    String environment= req.getParameter("environment");
    if (environment==null)
        environment="TEST";
    String passwd = req.getParameter("psswd");
    String isnew = req.getParameter("newuser");
    String accessStatus="none";
    String lpref=lng;
    User user=null;
    // Check the name and password for validity
    try {
       if (userid==null) 
          // plain login form
          generateLoginForms(out,environment,lng);
       else {   
          // id provided
            user= accessServer.getUser(userid);
            if (user==null) {
               generateOldLoginForm(out,environment, "mismatch",userid,lng);
            }   
            else {
               if (!passwd.equals(user.getPassword())){
                   generateOldLoginForm(out,environment,"mismatch",userid,lng);
               }
               else
                   //store user information in session
                   accessStatus= user.getStatus();
            }
        }
    } 
    catch (CacheException ce) {
        out.println(fatalErrorNotification("LOGIN",ce.getMessage()));
    }    
    catch (DatabaseException de) {
        out.println(fatalErrorNotification("DATABASE",de.getMessage()));
    }
       
    if (!accessStatus.equals("none")) {      
       HttpSession session = req.getSession(true);
       session.setAttribute("userid", userid);  // just a marker object
       session.setAttribute("user", user);  
       // Try redirecting the client to the page he first tried to access
     try {
         String target = (String) session.getAttribute("originaltarget");
         if (target != null)
             res.sendRedirect(target);
         return;
     }
     catch (Exception ignored) { }{

       // Couldn't redirect to the target. Redirect to the error page.
        res.sendRedirect("Error.html");
       }
    } 
}

   public void doGet(HttpServletRequest req, HttpServletResponse res)
                                throws ServletException, IOException { 
       doPost(req,res);
   }
   
   public void generateLoginForms(PrintWriter out, String environment, String lng)
   throws CacheException {
      // print header
       out.println(pageHeader(taskBase.getAttribute("X",environment,"istallation",lng), 
          "login.js", "login.css"));
      //generate olduserform
       oldUserFields(out,null,null,lng);
       
      // newUserFields(out,environment,null,null,null,null,null, null,null,null,null,lng);
      // print footer
       footer();
   }  
   
   
   
   public void generateOldLoginForm(PrintWriter out, String environment, String error, String userid, String lng) 
      throws CacheException  {
       out.println( pageHeader(taskBase.getAttribute("X",environment,"istallation",lng), 
          "login.js", "login.css"));
      //generate olduserform
       oldUserFields(out,error,userid,lng);
      // print footer
       footer();   
   }
   
   public void oldUserFields(PrintWriter out, String error, String userid, String lng)
       throws CacheException {
       // depending on formtype print the form
       if (error!=null) {
           out.println("<p class=\"messg\">");
           out.println(taskBase.getAttribute("G", "login","error",lng));
           out.println("</p>");
       }
       out.println("<form name=\"loginform\" action=\"Login\" method=\"post\">");
       out.println("<div class=\"form_1\">");
       out.println("<div class=\"header_1\">");
       out.println(taskBase.getAttribute("G","login","loginheaderold",lng));
       out.println("</div>");
       // userid
       out.println("<table class=\"lf\">");
       out.println("<tr><td class=\"hd\">");
       out.println(taskBase.getAttribute("G","login","promptuserid",lng));
       out.println("</td><td class=\"inp\">");
       out.println("<input type=\"text\" id=\"userid\" name=\"userid\" size=\"16\" maxlength=\"16\" "+
          "value=\""+ (userid!=null?userid:"")+ "\">");
       out.println("</td></tr>");
           // password
       out.println("<tr><td class=\"hd\">");
       out.println(taskBase.getAttribute("G","login","password",lng));
       out.println("</td><td class=\"inp\">");
       out.println("<input type=\"text\" id=\"psswd\" name=\"psswd\" size=\"16\" maxlength=\"16\" value=\"\">");
           // change password button (reveals new fields)
       out.println("</td></tr>");
       out.println("<tr><td colspan=\"2\">");
       
       out.println("<input type=\"submit\" value=\""+taskBase.getAttribute("G","login","submitlogin",lng)+"\">");
       out.println("</td></tr>");
       out.println("</table>");
       out.println("</div>");
       out.println("</form>");
   }    
           
   
   

}

