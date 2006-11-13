package fi.helsinki.cs.kohahdus.languages;

import java.util.*;

public class LanguageManager {
	
	private static HashMap<String, ResourceBundle> bundles;
	
	/**
	 * Initializes all resources specified in parameter file.
	 * @param propertiesFile Contains filenames of all resources to be loaded in XML format.
	 */
	public static synchronized void loadTextResources(String propertiesFile) {
		if (bundles != null)
			return;
		
		//load all XML files and store in HashMap		
	}
	
	/**
	 * Returns a ResourceBundle object containing all language specific data
	 * on a single JSP page.
	 * @param lang "FI" or "EN"
	 * @param page Pagename specifies which XML file is used.
	 * @return
	 */
	public static ResourceBundle getTextResource(String lang, String page) {
		//get bundle from HashMap with page+lang
		return null;
	}
}
