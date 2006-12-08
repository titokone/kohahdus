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
 * Location of the properties file is defined in web.xml and loaded
 * during startup.
 */

public class LanguageManager {
		
	private static HashMap<String, ResourceBundle> bundles;
	
		
	/**
	 * Initializes all resources specified in parameter file. This method is now called from
	 * a filter named TitoInitializer.
	 * @param contextPath Path to TitoTrainers tomcat context
	 * @param propertiesFilePath Path to properties.xml inside TitoTrainers context.
	 */
	public static synchronized void loadTextResources(String contextPath, String propertiesFilePath) {
		if (bundles != null)
			return;

		Log.write("LanguageManager: loading from " + contextPath +propertiesFilePath);
		
		InputStream in;
		
		//path to properties.xml file
		String propertiesFile = contextPath + propertiesFilePath;
		
		try {
			File properties = new File(propertiesFile);
			in = new FileInputStream(properties);
		} catch (Exception e) {
			Log.write("LanguageManager: failed to load properties file " + propertiesFile + ". " + e);
			return;
		}
		
		//Extract locations of other xml files from properties file
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
		
		//Load all individual xml files and create all bundles
		Enumeration pages = fileList.keys();
		while (pages.hasMoreElements()) {
			String pageName = (String) pages.nextElement();
			String xmlFile = fileList.getProperty(pageName);
			loadBundle(pageName, contextPath + xmlFile);
		}		
	}
	
	/**
	 * Loads a single xml file and adds it to HashMap.
	 * File is loaded in both finnish & english.
	 * @param page Name of the jsp-page
	 * @param xmlFile Path to xml file
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
		
		//Read all data from xml file
		try {
			data.loadFromXML(in);
		} catch (InvalidPropertiesFormatException e) {
			Log.write("LanguageManager: xml file " + xmlFile + " in unknown format. " + e);
			return;
		} catch (IOException e) {
			Log.write("LanguageManager: Failed to read xml file " + xmlFile + ". " + e);
			return;
		}

		//Add bundles for all supported languages
		bundles.put(page + "_EN", new TitoBundle(data, "EN"));
		bundles.put(page + "_FI", new TitoBundle(data, "FI"));	
		
		Log.write("LanguageManager: FI & EN bundles for page " + page + " loaded successfully");
	}	
	
	/**
	 * Returns a ResourceBundle object containing all language specific data
	 * on a single JSP page.
	 * @param lang "FI" or "EN"
	 * @param page Pagename specifies which XML file is used. This should equal the name of jsp-page.
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
}
