package fi.helsinki.cs.kohahdus.trainer;




import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import fi.helsinki.cs.kohahdus.DBHandler;

/**
* Simple demonstration of using the javax.mail API.
*
* Run from the command line. Please edit the implementation
* to use correct email addresses and host name.
*/
public final class Emailer {
	
	//CONSTRUCTOR
	public Emailer() {
	}

	
	/** Send a single email. */
	public void sendEmail(String aToEmailAddr){
		//Here, no Authenticator argument is used (it is null).
	    //Authenticators are used to prompt the user for user
	    //name and password.
		Session session = Session.getDefaultInstance( fMailServerConfig, null );
		MimeMessage message = new MimeMessage( session );
		boolean success=true;
	    try {
	    	
	    	//Sender and receiver
	    	String aFromEmailAddr="support@titotrainer.fi";
	    	message.setFrom( new InternetAddress(aFromEmailAddr) );
	    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
	    	
	    	//Generate new password, (change it via DBHandler later if sending message worked)
	    	String aSubject="TitoTrainer lost password";
	    	String aBody="Your new password is: ";
	    	String newPassword=randomstring(6,12); // password 6-12 chars long
	    	aBody+=newPassword;
	      
	      
	    	message.setSubject( aSubject );
	    	message.setText( aBody );
	    	Transport.send( message );
	    	
	    	
	    }
	    catch (MessagingException ex){
	    	success=false;
	    	System.err.println("Cannot send email. " + ex);
	    }
	    //sending message worked, change the password from database
	    if (success) {
	    	DBHandler db=DBHandler.getInstance();
	    	db.changePassword(aToEmailAddr); //TODO: dbhandler tarvii metodin
	    	
	    	
	    }
	    
	    
	}

	/**
	* Allows the config to be refreshed at runtime, instead of
	* requiring a restart.
	*/
	public void refreshConfig() {
		fMailServerConfig.clear();
		fetchConfig();
	}
	// PRIVATE //
	private Properties fMailServerConfig = new Properties();

	{
		fetchConfig();
	}
  
  	//Methods for creating new password
	private String randomstring(int lo, int hi) {
		int n = rand(lo, hi);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++)
		b[i] = (byte)rand('a', 'z');
		return new String(b, 0);
	}
	private int rand(int lo, int hi) {
		int n = hi - lo + 1;
		Random rn = new Random();
		int i = rn.nextInt() % n;
		if (i < 0)
			i = -i;
		return lo + i;
	}

	/**
	* Open a specific text file containing mail server
	* parameters, and populate a corresponding Properties object.
	*/
	private void fetchConfig() {
		InputStream input = null;
		try {
			//If possible, one should try to avoid hard-coding a path in this
			//manner; in a web application, one should place such a file in
			//WEB-INF, and access it using ServletContext.getResourceAsStream.
			//Another alternative is Class.getResourceAsStream.
			//This file contains the javax.mail config properties mentioned above.
			input = new FileInputStream( "C:\\Temp\\MyMailServer.txt" );
			fMailServerConfig.load( input );
		}
		catch ( IOException ex ){
			System.err.println("Cannot open and load mail server properties file.");
		}
		finally {
			try {
				if ( input != null ) input.close();
			}
			catch ( IOException ex ){
				System.err.println( "Cannot close mail server properties file." );
			}
		}
	}
}
