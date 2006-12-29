package fi.helsinki.cs.kohahdus;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import fi.helsinki.cs.kohahdus.languages.LanguageManager;


/**
*
* @author Taro Morimoto 
*/

public final class TitoInitializer implements Filter {

    private FilterConfig filterConfig = null;

    /**
     * This method is called in the beginning of every http request to the context.
     * This behavior can be configured in the /WEB-INF/web.xml file.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
    }


    public void destroy() {
    }

    /**
     * This method is called when the context is loading.
     */
    public void init(FilterConfig filterConfig) {
    	this.filterConfig = filterConfig;
    	Log.setContext(filterConfig.getInitParameter("context-name"));

    	Log.write("");
    	Log.write("*********** Initializing servlet context...");
    	
    	String propertiesFile = filterConfig.getInitParameter("language-properties");
    	String contextPath = filterConfig.getInitParameter("context-path");
    	LanguageManager.loadTextResources(contextPath, propertiesFile);
    	
    	DBHandler.initialize(filterConfig.getInitParameter("db-string"),
				 filterConfig.getInitParameter("db-username"),
				 filterConfig.getInitParameter("db-password"));

    	Emailer.initialize(filterConfig.getInitParameter("smtp-server"), Integer.parseInt(filterConfig.getInitParameter("smtp-port")));

    	Log.write("*********** Servlet context initialized.");
    	Log.write("");
    }



}

