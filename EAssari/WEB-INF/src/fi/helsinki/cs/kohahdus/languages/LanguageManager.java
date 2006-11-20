package fi.helsinki.cs.kohahdus.languages;

import java.util.*;
import java.io.*;

import fi.helsinki.cs.kohahdus.Log;

/**
 * LanguageManager handles all requests for language specific resources.
 * Resources are stored in xml files. During startup class loads all
 * resources specified in properties file. Then class proceeds to load 
 * individual resources specified in properties file.
 * 
 * One should note that all pathnames should originate from directory kohahdus/.
 *
 * Default properties file: EAssari/WEB-INF/xml/properties.xml
 */

public class LanguageManager {
		
	private static HashMap<String, ResourceBundle> bundles;
	
	/**
	 * Ensure that all bundles are loaded when this class is referenced
	 */
	static {
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//Toimii vain db:llä - HUOM konteksti
		loadTextResources("tomcat/webapps/paula/WEB-INF/xml/properties.xml");
		
		//Toimii vain workspacessa
		//loadTextResources("EAssari/WEB-INF/xml/testProperties.xml");
	}
		
	/**
	 * Initializes all resources specified in parameter file.
	 * @param propertiesFile Contains filenames of all resources to be loaded in XML format.
	 */
	private static synchronized void loadTextResources(String propertiesFile) {
		if (bundles != null)
			return;

		File path = new File(".");
		Log.write("LanguageManager: path = " + path.getAbsolutePath());
		
		File properties = new File(propertiesFile);
		InputStream in;
		
		try {
			in = new FileInputStream(properties);
		} catch (FileNotFoundException e) {
			Log.write("LanguageManager: failed to load properties file " + propertiesFile + ". " + e);
			return;
		}
		
		Properties fileList = new Properties();
		try {
			fileList.loadFromXML(in);
		} catch (InvalidPropertiesFormatException e) {
			Log.write("LanguageManager: properties file " + propertiesFile + " in unknown format. " + e);
			return;
		} catch (IOException e) {
			Log.write("LanguageManager: Failed to read properties file " + propertiesFile + ". " + e);
			e.printStackTrace();
			return;
		}
		
		bundles = new HashMap<String, ResourceBundle>();
		
		Enumeration pages = fileList.keys();
		while (pages.hasMoreElements()) {
			//TODO: casting error?
			String pageName = (String) pages.nextElement();
			String xmlFile = fileList.getProperty(pageName);
			loadBundle(pageName, xmlFile);
		}		
	}
	
	/**
	 * Loads a single xml file and adds it to HashMap.
	 * File is loaded in both finnish & english.
	 * @param page
	 * @param xmlFile
	 */
	private static void loadBundle(String page, String xmlFile) {
		File file = new File(xmlFile);
		InputStream in;
		
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Log.write("LanguageManager: failed to load xml file " + xmlFile + 
						" for page " + page + ". " + e);
			return;
		}

		Properties data = new Properties();
		
		try {
			data.loadFromXML(in);
		} catch (InvalidPropertiesFormatException e) {
			Log.write("LanguageManager: xml file " + xmlFile + " in unknown format. " + e);
			return;
		} catch (IOException e) {
			Log.write("LanguageManager: Failed to read xml file " + xmlFile + ". " + e);
			return;
		}

		bundles.put(page + "_EN", new TitoBundle(data, "EN"));
		bundles.put(page + "_FI", new TitoBundle(data, "FI"));	
		
		Log.write("LanguageManager: FI & EN bundles for page " + page + " loaded successfully");
	}	
	
	/**
	 * Returns a ResourceBundle object containing all language specific data
	 * on a single JSP page.
	 * @param lang "FI" or "EN"
	 * @param page Pagename specifies which XML file is used.
	 * @return
	 */
	public static ResourceBundle getTextResource(String lang, String page) {
		if (bundles.containsKey(page + "_" + lang)) {
			return bundles.get(page + "_" + lang);
		}
		System.out.println("Resource [page "+page+", lang "+lang+"] not found");
		//TODO: throw ?exception
		throw new RuntimeException("Requested language is not supported");
	}
	
	public static void main(String[] args) {
		LanguageManager lm = new LanguageManager();
		
		ResourceBundle fi = lm.getTextResource("FI", "studentTaskList");
		ResourceBundle en = lm.getTextResource("EN", "studentTaskList");
		
		System.out.println("testi FI: "+fi.getString("testi"));
		System.out.println("testi EN: "+en.getString("testi"));
	}
}
