package io.naztech.nuxeoclient.service;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sun.mail.util.MailSSLSocketFactory;

import io.naztech.nuxeoclient.model.Template;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttachmentDownloadFromMailService {
	@Autowired
	private DownloadFilesFromGoogleDriveUrl serveGoogleDrive;
	@Autowired
	private DownloadFilesFromOneDriveUrl serveOneDrive;
	@Autowired
	private DownloadFilesFromDropbox serveDropbox;
		
	@Value("${mailcredential.host}")
    private String host; 
	@Value("${mailcredential.port}")
    private String port; 
	@Value("${mailcredential.username}")
    private String userName; 
	@Value("${mailcredential.password}")
    private String password;
	@Value("${mailcredential.savedirectory}")
	private String saveDirectory;


	public void downloadEmailAttachments(String host, String port, String userName, String password) {

		try {
			Properties properties = new Properties();
			/* for dac-mail */
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			properties.put("mail.imap.ssl.trust", "*");// change pop3 with imap for imap
			properties.put("mail.imap.ssl.socketFactory", sf);// change pop3 with imap for imap
			/* for dac-mail */

			properties.put("mail.imap.fetchsize", "819200");
			// server setting
			properties.put("mail.imap.host", host);// change pop3 with imap for imap
			properties.put("mail.imap.port", port);// change pop3 with imap for imap

			// SSL setting
			properties.setProperty("mail.imap.socketFactory.class", // change pop3 with imap for imap
					"javax.net.ssl.SSLSocketFactory");
			properties.setProperty("mail.imap.socketFactory.fallback", "false");// change pop3 with imap for imap
			properties.setProperty("mail.imap.ssl.enable", "true");
			properties.setProperty("mail.imap.socketFactory.port", String.valueOf(port));// change pop3 with imap for
																							// imap

			Session session = Session.getDefaultInstance(properties);

			try {
				// connects to the message store
				Store store = session.getStore("imap");
				store.connect(userName, password);

				// opens the inbox folder
				Folder folderInbox = store.getFolder("INBOX");
				folderInbox.open(Folder.READ_WRITE);
				// create parsed folder if not existed
				Folder newFolder = store.getFolder("Parsed");
				if(!newFolder.exists()) {
					if (newFolder.create(Folder.HOLDS_MESSAGES)) {
						newFolder.setSubscribed(true);
				        log.info("Folder was created successfully");
				    } else {
				    	log.error("Could not create New folder");
				    }
				}

				// fetches new messages from server
				//Message[] arrayMessages = folderInbox.getMessages();
				Message[] arrayMessages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

				if (arrayMessages.length == 0) {
					log.info("No new message");
				} else {

					for (int i = 0; i < arrayMessages.length; i++) {

						Message message = arrayMessages[i];
						message.setFlag(Flags.Flag.SEEN, true);
						try {
							// search for attachment in the mail
							saveAttachment(message);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				// delete processed message
				folderInbox.copyMessages(arrayMessages, newFolder);
				for(int i = 0; i<arrayMessages.length; i++) {
					arrayMessages[i].setFlag(Flags.Flag.DELETED, true);
				}
				//folderInbox.expunge();

				// disconnect
				folderInbox.close(false);
				store.close();
			} catch (NoSuchProviderException ex) {
				log.error("No provider for imap.", ex);

			} catch (MessagingException ex) {
				log.error("Could not connect to the message store", ex);

			}
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveAttachment(Part p) {
		
		Template tmplt = new Template();
		
		try {

			if (p.isMimeType("text/html")) {
				getLinkFromHtml(p);
			} else if (p.isMimeType("multipart/*")) {
				Multipart mp = (Multipart) p.getContent();
				int count = mp.getCount();
				for (int i = 0; i < count; i++)
					saveAttachment(mp.getBodyPart(i));
			}
			// check if the content is a nested message
			else if (p.isMimeType("message/rfc822")) {
				saveAttachment((Part) p.getContent());
			}
			// check if content has pdf
			if (p.isMimeType("application/pdf") || p.isMimeType("application/binary")) {

				String fileName = MimeUtility.decodeText(p.getFileName());
				log.info("File name :" + fileName);

				((MimeBodyPart) p).saveFile(saveDirectory + File.separator +"EMAIL"+ fileName);
				log.info(fileName + " download Successful");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getLinkFromHtml(Part p) {
		// List<String> googleDriveLinks = new ArrayList<String>();
		// List<String> oneDriveLinks = new ArrayList<String>();
		// List<String> dropboxLinks = new ArrayList<String>();
		try {
			Pattern linkPattern = Pattern.compile("href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			String desc = p.getContent().toString();

			Matcher pageMatcher = linkPattern.matcher(desc);

			while (pageMatcher.find()) {
				// Search for google drive link
				if (pageMatcher.toString().contains("drive.google.com"))
					// googleDriveLinks.add(pageMatcher1.group(1));
					serveGoogleDrive.downloadFileFromGoogledrive(pageMatcher.group(1));
				// Search for one drive link
				if (pageMatcher.toString().contains("onedrive.live.com")
						|| pageMatcher.toString().contains("https://1drv.ms/"))
					// oneDriveLinks.add(pageMatcher1.group(1));
					serveOneDrive.downloadFilesFromOneDriveUrl(pageMatcher.group(1));
				// Search for dropbox link
				if (pageMatcher.toString().contains("dropbox.com"))
					// oneDriveLinks.add(pageMatcher1.group(1));
					serveDropbox.downloadFilesFromDropbox(pageMatcher.group(1));
			}

//		for (String temp : oneDriveLinks) {
//			log.info(temp);
//			// NOTE: This class has Autowired with the class DownloadFilesFromOneDriveUrl
//			serveOneDrive.downloadFilesFromOneDriveUrl(temp);
//
//		}
//		for (String temp : googleDriveLinks) {
//			log.info(temp);
//			// NOTE: This class has Autowired with the class DownloadFileFromGoogleDriveUrl
//			servGoogleDrive.downloadFileFromGoogledrive(temp);
//
//		}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void downloadAttachments() {
//		String host = "mail.ntdac.naztech.local";
//		String port = "993";
//		String userName = "einvoice.test@naztech.us.com";
//		String password = "n@ztech.test123";
//
//		String saveDirectory = "C:\\temporary\\";
		
		downloadEmailAttachments(host, port, userName, password);

	}

}