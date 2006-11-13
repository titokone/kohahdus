package fi.helsinki.cs.kohahdus.trainer;
import fi.helsinki.cs.kohahdus.criteria.*;

public interface TitoFeedback {
	
	public TitoState getTitoState();

	public String getOverallFeedback();
	
	public String getCompileError();
	
	public String getRunError();
	
	// TODO: Parempi nimi	
	public boolean wasSuccessful();
	
	public java.util.List<TitoCriterionFeedback> getCriteriaFeedback();	
}
