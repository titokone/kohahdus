/*
 * AttributeCache.java
 *
 * Created on 8. lokakuuta 2003, 10:04
 */

package fi.helsinki.cs.kohahdus.trainer;

/**
 * This interface defines the methods for getting attribute values from the cache
 * @author  laine
 * @version 
 */
public interface AttributeCache {

    /****
     * provides the value in the specified language for the given attribute
     * Params:
     *   objType = type of object (T=task, D= displayer, E=Error, A=analyser, C=Course)
     *   objID= key used in fetching value from hash map:
     *          full task id, class name, error name, courseid
     *   attributename = name of attribute
     *   language = (FI,EN, language independent values may be obtained with whatever language attribute)
     */  
    
    public String getAttribute (String objType, String objID, String attributename, String language) throws CacheException;
    
}

