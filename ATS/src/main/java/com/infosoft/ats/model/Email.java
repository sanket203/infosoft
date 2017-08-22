package com.infosoft.ats.model;

import java.util.Date;

public class Email {
	
	private String sender;
	private String subject;
	private String emailBody;
	private String resumeFilePath;
	private Date sentDate;
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public String getResumeFilePath() {
		return resumeFilePath;
	}
	public void setResumeFilePath(String resumeFilePath) {
		this.resumeFilePath = resumeFilePath;
	}
	
	

}
