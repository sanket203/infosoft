package com.infosoft.ats.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infosoft.ats.model.Email;
import com.infosoft.ats.model.UserDetails;
import com.infosoft.ats.service.EmailExtractorService;
import com.infosoft.ats.utils.ResponseData;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Service
public class EmailExtractorServiceImpl implements EmailExtractorService {

	public static final String RESUME_BASE_PATH = "G:\\INFOSOFT\\Resumes\\";

	public ResponseEntity<ResponseData> saveEmail(UserDetails user) {
		List<Email> messages = getEmails(user.getEmailId(), user.getPassword());
		return null;
	}

	private List<Email> getEmails(String userId, String password) {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		Message[] messages = null;
		List<Email> emailList = new ArrayList<Email>();
		try {
			Session session = Session.getInstance(props, null);
			Store store = session.getStore();
			store.connect("imap.gmail.com", userId, password);

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			SearchTerm sender = new FromTerm(new InternetAddress("isresadm1@gmail.com"));

			messages = inbox.search(sender);
			for (int i = 0; i < messages.length; i++) {
				Email email = new Email();
				Message message = messages[i];
				Address[] fromAddress = message.getFrom();
				String from = fromAddress[0].toString();
				email.setSender(from);
				String subject = message.getSubject();
				email.setSubject(subject);
				String sentDate = message.getSentDate().toString();
				String contentType = message.getContentType().toString();
				String messageContent = "";
				if (contentType.contains("multipart")) {
					// content may contain attachments
					MimeMultipart multiPart = (MimeMultipart) message.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						BodyPart part = multiPart.getBodyPart(partCount);
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							String resumeFilePath = saveAttachment(part.getInputStream(), part.getFileName());
							email.setResumeFilePath(resumeFilePath);
						} else {
							messageContent = getText(part);
						System.out.println(messageContent);
							if (StringUtils.isNotEmpty(messageContent)) {
								
								if(StringUtils.isNotEmpty(messageContent)) {
									String fileName =getResumeFileName(messageContent);
									System.out.println(fileName);
									if(StringUtils.isNotEmpty(fileName)) {
										String filePath = toPdf(messageContent, fileName);
										email.setResumeFilePath(filePath);
									}else {
										String filePath = toPdf(messageContent, "Resume_"+i);
										email.setResumeFilePath(filePath);
									}
									
								}
								
							}

						}
					}

				} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
					Object content = message.getContent();
					if (content != null) {
						messageContent = content.toString();
						if(StringUtils.isNotEmpty(messageContent)) {
							String fileName =getResumeFileName(messageContent);
							if(StringUtils.isNotEmpty(fileName)) {
								String filePath = toPdf(messageContent, fileName);
								email.setResumeFilePath(filePath);
							}else {
								String filePath = toPdf(messageContent, "Resume_"+i);
								email.setResumeFilePath(filePath);
							}
							
						}

					}
				}

				emailList.add(email);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return emailList;
	}

	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else if (bp.isMimeType("message/rfc822")) {
					String s = getText(bp);
					if (s != null)
						return s;
				}

				else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}
	
	private String getResumeFileName(String metaData) {
		String fileName = "";
		
		String monsterId = getMonsterId(metaData);
		String name = getCandidateName(metaData);
		if(monsterId!=null && name!=null) {
			fileName = name+"_"+monsterId;
		}
		
		return fileName;
		
	}
	
	private String getCandidateName(String metaData) {
		Pattern pattern = Pattern.compile("(Name:)(.*?)(\\<br\\>)");
		Matcher matcher = pattern.matcher(metaData);
		while (matcher.find()) {
		    System.out.println("group 1: " + matcher.group(1));
		    System.out.println("Name : " + matcher.group(2).replaceAll("\u00A0", ""));
		    System.out.println("group 3: " + matcher.group(3));
		    String name = matcher.group(2).replaceAll("\u00A0", "").trim();
		    System.out.println(name);
		    return name;
		}
		return null;
		
	}
	
	private String getMonsterId(String metaData) {
		Pattern pattern = Pattern.compile("(Monster resume #)(.*?)(\\<br\\>)");
		Matcher matcher = pattern.matcher(metaData);
		while (matcher.find()) {
		    System.out.println("group 1: " + matcher.group(1));
		    System.out.println("group 2: " + matcher.group(2));
		    System.out.println("group 3: " + matcher.group(3));
		    return matcher.group(2).trim();
		}
		return null;
		
	}

	private String toPdf(String htm, String fileName) throws IOException {
		String filePath = RESUME_BASE_PATH +  fileName + ".pdf";
		byte[] xhtml = Jsoup.parse(htm).html().getBytes();
		try {
			OutputStream file = new FileOutputStream(new File(filePath));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);
			document.open();
			InputStream is = new ByteArrayInputStream(xhtml);

			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			document.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	private String saveAttachment(InputStream in, String fileName) {
		String filePath = RESUME_BASE_PATH + fileName;
		FileOutputStream output;
		try {
			output = new FileOutputStream(filePath);
			byte[] buffer = new byte[4096];

			int byteRead;

			while ((byteRead = in.read(buffer)) != -1) {
				output.write(buffer, 0, byteRead);
			}
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filePath;
	}

	public static void main(String[] args) {
		UserDetails user = new UserDetails();
		user.setEmailId("ats.resumeengine@gmail.com");
		user.setPassword("ResumeEngine");

		EmailExtractorServiceImpl obj = new EmailExtractorServiceImpl();
		List<Email> list = obj.getEmails(user.getEmailId(), user.getPassword());

	}

}
