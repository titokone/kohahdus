package fi.helsinki.cs.kohahdus;


public class Log {
	private static String context = "";
	
	public static void write(String data){
		System.out.println(context + ": " + data);		
	}
	
	public static void setContext(String context){
		Log.context = context;		
	}
}




