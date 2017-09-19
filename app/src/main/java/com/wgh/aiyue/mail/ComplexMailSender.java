package com.wgh.aiyue.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by   : WGH.
 */
public class ComplexMailSender  {

    public boolean sendTextMail(MailInfo mailInfo, String filePath) {
        // Determine whether authentication is required.
        MyAuthenticator authenticator = null;
        Properties properties = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // If an authentication is required, then create a password verifier.
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // According to the mail session attribute and password verifier to construct a mail session.
        Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
        try {
            // According to the mail session to construct a mail message.
            Message mailMessage = new MimeMessage(sendMailSession);
            // Create mail sender address.
            Address fromAddress = new InternetAddress(mailInfo.getFromAddress());
            // Sets the sender of the mail message.
            mailMessage.setFrom(fromAddress);
            // Creates the recipient address of the message and sets it to the mail message.
            Address toAddress = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, toAddress);
            // Set the subject of the mail message.
            mailMessage.setSubject(mailInfo.getSubject());
            // Set the time when the mail message was sent.
            mailMessage.setSentDate(new Date());
            // Set the main contents of the mail message.
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
            // Add attachments when sending messages.
            MimeBodyPart attachPart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(filePath); // Open the file you want to send.
            attachPart.setDataHandler(new DataHandler(fileDataSource));
            attachPart.setFileName(fileDataSource.getName());
            MimeMultipart allMultipart = new MimeMultipart("mixed"); // Attachment.
            allMultipart.addBodyPart(attachPart);
            mailMessage.setContent(allMultipart);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
