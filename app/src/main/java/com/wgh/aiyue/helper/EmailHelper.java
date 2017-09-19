package com.wgh.aiyue.helper;


import com.wgh.aiyue.mail.ComplexMailSender;
import com.wgh.aiyue.mail.MailInfo;
import com.wgh.aiyue.mail.SimpleMailSender;
import com.wgh.aiyue.util.ConstDefine;

/**
 * Created by   : WGH.
 */
public class EmailHelper {

    /**
     * @param mailServerHost   Mail service host, such as smtp.163.com
     * @param mailServerPort   Mail service port, such as 25
     * @param userName         Mail service userName
     * @param password         Mail service password
     * @param fromEamilAddress Mail service address from
     * @param toAddress        Mail service address to
     * @param emailTitle       Mail service title
     * @param emailContent     Mail service content
     */
    public static void send(String mailServerHost,
                            String mailServerPort,
                            String userName,
                            String password,
                            String fromEamilAddress,
                            String toAddress,
                            String emailTitle,
                            String emailContent) {
        // This class is mostly set up mail.
        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(mailServerHost);
        mailInfo.setMailServerPort(mailServerPort);
        mailInfo.setValidate(true);
        mailInfo.setUserName(userName);
        mailInfo.setPassword(password);
        mailInfo.setFromAddress(fromEamilAddress);
        mailInfo.setToAddress(toAddress);
        mailInfo.setSubject(emailTitle);
        mailInfo.setContent(emailContent);
        // This class is mostly send mail.
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);
        sms.sendHtmlMail(mailInfo);
    }

    /**
     * @param toAddress    the addresses you want to send.
     * @param emailTitle
     * @param emailContent
     */
    public static void send(String[] toAddress, String emailTitle, String emailContent) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost("smtp.qq.com");
        mailInfo.setMailServerPort("465");
        mailInfo.setValidate(true);
        mailInfo.setFromAddress(ConstDefine.getMailNum());
        mailInfo.setUserName(ConstDefine.getMailNam());
        mailInfo.setPassword(ConstDefine.getMailPas());

        for (int i = 0; i < toAddress.length; i++) {
            mailInfo.setToAddress(toAddress[i]);
        }

        mailInfo.setSubject(emailTitle);
        mailInfo.setContent(emailContent);
        // This class is mostly send mail.
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);
//        sms.sendHtmlMail(mailInfo);
    }

    /**
     * @param toAddress    the addresses you want to send.
     * @param emailTitle
     * @param emailContent
     */
    public static void sendComplex(String[] toAddress, String emailTitle, String emailContent, String filePath) {
        // In order to set up the mail.
        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost("smtp.qq.com");
        mailInfo.setMailServerPort("465");
        mailInfo.setValidate(true);
        mailInfo.setFromAddress(ConstDefine.getMailNum());
        mailInfo.setUserName(ConstDefine.getMailNam());
        mailInfo.setPassword(ConstDefine.getMailPas());

        for (int i = 0; i < toAddress.length; i++) {
            mailInfo.setToAddress(toAddress[i]);
        }

        mailInfo.setSubject(emailTitle);
        mailInfo.setContent(emailContent);
        // This class is mostly send mail.
        ComplexMailSender complexMailSender = new ComplexMailSender();
        complexMailSender.sendTextMail(mailInfo, filePath);
    }

    public static void sendWith163Default() {
        // This class is mostly set up mail.
        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost("smtp.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName("");
        mailInfo.setPassword("");
        mailInfo.setFromAddress("@163.com");
        mailInfo.setToAddress("@qq.com");
        mailInfo.setSubject("");
        mailInfo.setContent("");
        // This class is mostly send mail.
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);
        sms.sendHtmlMail(mailInfo);
    }
}
