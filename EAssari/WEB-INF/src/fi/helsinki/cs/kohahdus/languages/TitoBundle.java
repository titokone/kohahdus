package fi.helsinki.cs.kohahdus.languages;

import java.util.*;

public class TitoBundle extends ResourceBundle {

	private Properties xmlData;
	private String language;
	
	/**
	 * Initalize a new TitoBundle, which returns data from
	 * parameter Properties object in a given language.
	 * @param data Properties
	 * @param lang
	 */
	public TitoBundle(Properties data, String lang) {
		//TODO: parameter check
		this.xmlData = data;
		this.language = lang;
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
	 * TODO
	 * Returns all available key values.
	 */
	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}
}
