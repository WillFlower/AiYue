package com.wgh.aiyue.mail;


/**
 * Created by   : WGH.
 * 发送邮件需要使用的基本信息
 */

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

public class MailInfo {

    private String mailServerHost;
    private String mailServerPort = "465";
    private String fromAddress;
    private String toAddress;
    private String userName;
    private String password;
    private boolean validate = false;   // Weather need authentication.
    private String subject;
    private String content;
    private String[] attachFileNames;

    /**
     * Get mail session properties.
     */
    public Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", this.mailServerHost);
        props.put("mail.smtp.port", this.mailServerPort);
        props.put("mail.smtp.auth", validate ? true : false);
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        props.put("mail.smtp.ssl.enable", "true"); // SSL verification
        props.put("mail.smtp.ssl.socketFactory", sf);
        props.put("mail.transport.protocol", "smtp"); // Specified protocol
        props.put("mail.debug", "false"); // Debug mode
        return props;
    }


    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] fileNames) {
        this.attachFileNames = fileNames;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }
} 