package fi.helsinki.cs.kohahdus.languages;

import java.util.*;
import fi.helsinki.cs.kohahdus.Log;

/**
 * TitoBundle object contains language specific data for a given page in a given language.
 *  
 * One should note that LanguageManager handles the creation of TitoBundles instead of ResourceBundle.
 */

public class TitoBundle extends ResourceBundle {

	private Properties xmlData;
	private String language;
	
	/**
	 * Initalize a new TitoBundle, which returns data from
	 * parameter Properties object in a given language.
	 * @param data Properties
	 * @param lang "FI" or "EN"
	 */
	public TitoBundle(Properties data, String lang) {
		if (data == null || lang == null || !(lang.equals("EN") || lang.equals("FI"))) {
			Log.write("TitoBundle: Constructor failed");
			throw new IllegalArgumentException("Invalid data for TitoBundle: lang "+lang+" data "+data);
		}
		this.xmlData = data;
		this.language = "_"+lang;
	}
	
	/**
	 * Returns an object, which matches the key, from xmlData.
	 * It's preferable to use getString method from parent class instead.
	 */
	@Override
	protected Object handleGetObject(String key) {
		return xmlData.getProperty(key + language);
	}

	/**
	 * Returns all available key values.
	 * 
	 * This is required by the super class, but it isn't used anywhere.
	 */
	@Override
	public Enumeration<String> getKeys() {		
		return null;
	}
}
