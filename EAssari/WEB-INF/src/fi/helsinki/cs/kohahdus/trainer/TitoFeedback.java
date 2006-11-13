package fi.helsinki.cs.kohahdus.trainer;
import fi.helsinki.cs.kohahdus.criteria.*;


/** Interface for feedback data. */
public interface TitoFeedback {
	
	/** Return the end-state of TitoKone run */
	public TitoState getTitoState();

	/** Return the overall task feedback */
	public String getOverallFeedback();
	
	/** Return compile-time error message (null if no errors) */ 
	public String getCompileError();
	
	/** Return run-time error message (null if no errors) */ 
	public String getRunError();
	
	/** Return true if program was compiled and executed successfully, and all acceptance crietia were met */ 
	public boolean wasSuccessful();
	
	/** Return a list criteria feedbacks */ 
	public java.util.List<TitoCriterionFeedback> getCriteriaFeedback();	
}
