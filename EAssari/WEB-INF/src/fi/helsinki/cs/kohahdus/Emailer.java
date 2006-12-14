package fi.helsinki.cs.kohahdus;

import java.util.Random;

import org.apache.commons.mail.SimpleEmail;


/**
*
* @author Taro Morimoto 
*/
public class Emailer {
	private static String hostName = "localhost";
	private static int smtpPort = 25;
	
	private Emailer() {}
	
	public static void initialize(String hostName, int smtpPort) {
		if (hostName == null) return;
		Emailer.hostName = hostName;
		Emailer.smtpPort = smtpPort;
	}
	
	/**
	 *  Emails the new password.
	 *  Returns the new password.
	 */
	public static String sendNewPasswordEmail(String toEmailAddr) throws Exception{
	    try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(hostName);
			email.setSmtpPort(smtpPort);
			email.addTo(toEmailAddr);
			email.setFrom("titotrainer@cs.helsinki.fi", "TitoTrainer");
			email.setSubject("New password for TitoTrainer");
			String randomString = randomstring(6,12);
			email.setMsg("Your new password for TitoTrainer is: " +randomString);
			String ret = email.send();
	    	Log.write("Email sent to " +toEmailAddr+ " New password is " +randomString);
	    	Log.write("Result: " +ret);
			return randomString;
	    } catch (Exception e){
	    	Log.write("Failed to send email. " + e);
	    	Log.write(e);
	    	throw e;
	    }
	}

  	//Methods for creating new password
	private static String randomstring(int lo, int hi) {
		int n = rand(lo, hi);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++)
		b[i] = (byte)rand('a', 'z');
		return new String(b);
	}
	private static int rand(int lo, int hi) {
		int n = hi - lo + 1;
		Random rn = new Random();
		int i = rn.nextInt() % n;
		if (i < 0)
			i = -i;
		return lo + i;
	}
	
	public static void main(String[] args) throws Exception{
		Emailer.sendNewPasswordEmail("taro.morimoto@helsinki.fi");
	}
}
