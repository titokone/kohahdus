package fi.helsinki.cs.kohahdus;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Log {
	private static String context = "";
	
	public synchronized static void write(String data){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String aika = sdf.format(new Date());
		System.out.println("[" + aika +"][" + context + "] " + data);		
	}
	
	public synchronized static void write(Exception e){
		e.printStackTrace(System.out);
	}
	
	public synchronized static void setContext(String context){
		Log.context = context;		
	}
}




