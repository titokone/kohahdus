package fi.helsinki.cs.kohahdus;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import fi.helsinki.cs.kohahdus.languages.LanguageManager;


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
     * This method is called when the context is loading.s
     */
    public void init(FilterConfig filterConfig) {
    	this.filterConfig = filterConfig;
    	Log.write("Initializing servlet context.");
    	
    	String propertiesFile = filterConfig.getInitParameter("language-properties");
    	LanguageManager.loadTextResources(propertiesFile);
    	
    }



}

