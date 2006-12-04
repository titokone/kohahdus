package fi.helsinki.cs.kohahdus;

import java.util.Random;

import org.apache.commons.mail.SimpleEmail;


/**
* 
*/
public class Emailer {
	private static String hostName = "mail.cs.helsinki.fi";
	
	private Emailer() {}
	
	public static void initialize(String hostName) {
		if (hostName == null) return;
		Emailer.hostName = hostName;
	}
	
	/** Send a single email. */
	public static void sendNewPasswordEmail(String toEmailAddr){
	    try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(hostName);
			email.addTo(toEmailAddr);
			email.setFrom("TitoTrainer@cs.helsinki.fi", "TitoTrainer");
			email.setSubject("New password for TitoTrainer");
			email.setMsg("Your new password is: " +randomstring(6,12));
			email.send();
	    	
		    //sending message worked, change the password from database
	    	// TODO: dbhandler tarvii metodin
	    	//DBHandler.getInstance().changePassword(aToEmailAddr); 
	    	
	    } catch (Exception e){
	    	Log.write("Failed to send email. " + e);
	    	Log.write(e);
	    }
	}

  	//Methods for creating new password
	private static String randomstring(int lo, int hi) {
		int n = rand(lo, hi);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++)
		b[i] = (byte)rand('a', 'z');
		return new String(b, 0);
	}
	private static int rand(int lo, int hi) {
		int n = hi - lo + 1;
		Random rn = new Random();
		int i = rn.nextInt() % n;
		if (i < 0)
			i = -i;
		return lo + i;
	}
}
