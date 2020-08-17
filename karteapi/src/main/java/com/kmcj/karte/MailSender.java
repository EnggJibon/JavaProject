/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.properties.KartePropertyService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * メール送信用
 *
 * @author penggd
 * @project mailUtil
 */
@Dependent
public class MailSender {

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private CnfSystemService cnfSystemService;

    private MimeMessage mimeMsg; // MIME邮件对象
    private Session session; // メールセッション
    private Properties props; // システム属性
    private Multipart mp; // メール対象
    private String username;// 送信用のアカウント
    private String password;// 送信用のパスワード
    private String nickname;// 送信者の名称
    private final String CONTENT_TYPE_HTML = "text/html;charset=utf-8";
    private final String CONTENT_TYPE_PLAIN = "text/plain;charset=utf-8";
    private boolean makePlainTextBody = false;
    public static final String MAIL_RETURN_CODE = "\r\n";

    /**
     * メール送信用のSMTPホストを設定
     *
     * @author penggd
     * @param hostName
     * @param port
     */
    private void setSmtpHost(String hostName, String port) {
        if (props == null) {
            props = System.getProperties();
        }
        props.put("mail.smtp.host", hostName);
        props.put("mail.smtp.port", port);

    }

    /**
     * メールObject
     *
     * @author penggd
     */
    private void createMimeMessage() {

        session = Session.getDefaultInstance(props, null);

        // 送信用情報を作成
        mimeMsg = new MimeMessage(session);
        mp = new MimeMultipart();
    }

    /**
     * メール送信権限設定
     *
     * @author penggd
     * @param need
     *
     */
    private void setNeedAuth(boolean need) {
        if (props == null) {
            props = System.getProperties();
        }
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }

    }

    /**
     * メータイトルを設定ル
     *
     * @author penggd
     * @param subject
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private void setSubject(String subject) throws UnsupportedEncodingException,
            MessagingException {
        mimeMsg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));

    }

    /**
     * メール内容を設定
     *
     * @param mailBody
     * @throws MessagingException
     *
     */
    private void setBody(String mailBody) throws MessagingException {
        MimeBodyPart bp = new MimeBodyPart();
        bp.setContent("" + mailBody, this.makePlainTextBody ? CONTENT_TYPE_PLAIN : CONTENT_TYPE_HTML);
        mp.addBodyPart(bp);
    }

    /**
     * 添付ファイルを追加する
     *
     * @author penggd
     * @param filePath
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private void addFileAffix(String filePath) throws MessagingException, UnsupportedEncodingException {
        MimeBodyPart bp = new MimeBodyPart();
        FileDataSource fileds = new FileDataSource(filePath);
        bp.setDataHandler(new DataHandler(fileds));
        bp.setFileName(MimeUtility.encodeText(fileds.getName(), "utf-8", "B"));
        mp.addBodyPart(bp);
    }

    private void addFileAffix(String filePath, String fileName) throws MessagingException, UnsupportedEncodingException {
        MimeBodyPart bp = new MimeBodyPart();
        FileDataSource fileds = new FileDataSource(filePath);
        bp.setDataHandler(new DataHandler(fileds));
        bp.setFileName(MimeUtility.encodeText(fileName, "utf-8", "B"));
        mp.addBodyPart(bp);
    }

    /**
     * 送信メールアドレスを設定する
     *
     * @author penggd
     * @param sender
     * @throws UnsupportedEncodingException
     * @throws AddressException
     * @throws MessagingException
     */
    private void setSender(String sender) throws UnsupportedEncodingException,
            AddressException, MessagingException {
        nickname = MimeUtility.encodeText(nickname, "utf-8", "B");
        mimeMsg.setFrom(new InternetAddress(nickname + " <" + sender + ">"));

    }

    /**
     * 受信メールアドレスを設定する
     *
     *
     * @author penggd
     * @param receiver
     * @throws AddressException
     * @throws MessagingException
     *
     */
    private void setReceiver(String receiver) throws AddressException,
            MessagingException {
        mimeMsg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(receiver));
    }

    /**
     * CCのメールアドレスを設定する
     *
     * @author penggd
     * @param copyto
     * @throws AddressException
     * @throws MessagingException
     *
     */
    private void setCopyTo(String copyto) throws AddressException,
            MessagingException {
        mimeMsg.setRecipients(Message.RecipientType.CC,
                InternetAddress.parse(copyto));
    }

    /**
     * メールを送信する
     *
     * @author penggd
     * @throws MessagingException
     *
     */
    private void sendout() throws MessagingException {
        mimeMsg.setContent(mp);
        mimeMsg.saveChanges();
        Session mailSession = Session.getInstance(props, null);
        Transport transport = mailSession.getTransport("smtp");
        transport.connect((String) props.get("mail.smtp.host"), username,
                password);
        if (null != mimeMsg.getRecipients(Message.RecipientType.TO)) {
            transport.sendMessage(mimeMsg,
                    mimeMsg.getRecipients(Message.RecipientType.TO));
        }

        if (null != mimeMsg.getRecipients(Message.RecipientType.CC)) {
            transport.sendMessage(mimeMsg,
                    mimeMsg.getRecipients(Message.RecipientType.CC));
        }
        transport.close();

    }

    /**
     * 送信者情報を設定する
     *
     * @author penggd
     * @param username
     * @param password
     * @param nickname
     */
    private void setNamePass(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;

    }

    /**
     * リストから、文字列に変換する
     *
     * @author penggd
     * @param list
     *
     * @return String
     */
    private String listToString(List<String> list) {

        if (null == list) {
            return "";
        }

        if (list.size() > 0) {

            StringBuilder strBuilder = new StringBuilder();

            for (int i = 0; i < list.size(); i++) {

                String str = list.get(i);

                if (i > 0) {

                    strBuilder.append(",");

                }

                strBuilder.append(str);
            }

            return strBuilder.toString();

        } else {

            return "";
        }
    }

    /**
     * 送信者名称を取得
     *
     * @author penggd
     * @return String
     */
    private String getMailFromName() {

        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "mail_from_name");

        return cnfSystem.getConfigValue();
    }

    /**
     * 送信メールアドレースを取得
     *
     * @author penggd
     * @return String
     */
    private String getMailFromAddress() {

        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "mail_from_address");

        return cnfSystem.getConfigValue();
    }

    /**
     * メールを送信する
     *
     * @author penggd
     * @param receiverList
     * @param ccList
     * @param subject
     * @param maiBody
     * @throws IOException
     * @throws MessagingException
     *
     */
    public void sendMail(List<String> receiverList, List<String> ccList, String subject, String maiBody)
            throws IOException, MessagingException {

        // メールサーバーを設定する
        setSmtpHost(kartePropertyService.getSmtpHost(), kartePropertyService.getSmtpPort());
        createMimeMessage();
        // メール送信の権限チェック要
        setNeedAuth(true);
        // 送信者情報を設定する
        setNamePass(kartePropertyService.getSmtpUser(), kartePropertyService.getSmtpPassword(), getMailFromName());
        // メータイトルを設定ル
        setSubject(subject);
        // メール内容を設定
        setBody(maiBody);
        // 受信メールアドレスを設定する
        setReceiver(listToString(checkAddress(receiverList)));
        // CCのメールアドレスを設定する
        if (null != ccList && ccList.size() > 0) {
            setCopyTo(listToString(checkAddress(ccList)));
        }
        // 送信メールアドレスを設定する
        setSender(getMailFromAddress());
        // メールを送信する
        sendout();
    }

    /**
     * メールを送信する
     *
     * @author penggd
     * @param receiverList
     * @param ccList
     * @param subject
     * @param maiBody
     * @param filePath
     * @throws IOException
     * @throws MessagingException
     *
     */
    public void sendMailWithAttachment(List<String> receiverList, List<String> ccList, String subject, String maiBody, String filePath)
            throws IOException, MessagingException {

        // メールサーバーを設定する
        setSmtpHost(kartePropertyService.getSmtpHost(), kartePropertyService.getSmtpPort());
        createMimeMessage();
        // メール送信の権限チェック要
        setNeedAuth(true);
        // 送信者情報を設定する
        setNamePass(kartePropertyService.getSmtpUser(), kartePropertyService.getSmtpPassword(), getMailFromName());
        // メータイトルを設定ル
        setSubject(subject);
        // メール内容を設定
        setBody(maiBody);
        // 添付ファイルを設定
        addFileAffix(filePath);
        // 受信メールアドレスを設定する
        setReceiver(listToString(checkAddress(receiverList)));
        // CCのメールアドレスを設定する
        if (null != ccList && ccList.size() > 0) {
            setCopyTo(listToString(checkAddress(ccList)));
        }
        // 送信メールアドレスを設定する
        setSender(getMailFromAddress());
        // メールを送信する
        sendout();
    }

    /**
     * メールを送信する
     *
     * @author cdarvin
     * @param receiverList
     * @param ccList
     * @param subject
     * @param maiBody
     * @param filePath
     * @throws IOException
     * @throws MessagingException
     *
     */
    public void sendMailWithMultiAttachment(List<String> receiverList, List<String> ccList, String subject, String maiBody, List<String> filePath, List<String> fileName)
            throws IOException, MessagingException {

        // メールサーバーを設定する
        setSmtpHost(kartePropertyService.getSmtpHost(), kartePropertyService.getSmtpPort());
        createMimeMessage();
        // メール送信の権限チェック要
        setNeedAuth(true);
        // 送信者情報を設定する
        setNamePass(kartePropertyService.getSmtpUser(), kartePropertyService.getSmtpPassword(), getMailFromName());
        // メータイトルを設定ル
        setSubject(subject);
        // メール内容を設定
        setBody(maiBody);
        // 添付ファイルを設定
        for (int i = 0; i < filePath.size(); i++) {
            addFileAffix(filePath.get(i), fileName.get(i));
        }
        // 受信メールアドレスを設定する
        setReceiver(listToString(checkAddress(receiverList)));
        // CCのメールアドレスを設定する
        if (null != ccList && ccList.size() > 0) {
            setCopyTo(listToString(checkAddress(ccList)));
        }
        // 送信メールアドレスを設定する
        setSender(getMailFromAddress());
        // メールを送信する
        sendout();
    }

    /**
     * メールを送信する(指摘fromNameとfromMailAddress)
     *
     * @author penggd
     * @param sender
     * @param senderName
     * @param receiverList
     * @param ccList
     * @param subject
     * @param maiBody
     * @throws IOException
     * @throws MessagingException
     *
     */
    public void sendMail(String sender, String senderName, List<String> receiverList, List<String> ccList, String subject, String maiBody)
            throws IOException, MessagingException {

        // メールサーバーを設定する
        setSmtpHost(kartePropertyService.getSmtpHost(), kartePropertyService.getSmtpPort());
        createMimeMessage();
        // メール送信の権限チェック要
        setNeedAuth(true);
        // 送信者情報を設定する
        setNamePass(kartePropertyService.getSmtpUser(), kartePropertyService.getSmtpPassword(), senderName);
        // メータイトルを設定ル
        setSubject(subject);
        // メール内容を設定
        setBody(maiBody);
        // 受信メールアドレスを設定する
        setReceiver(listToString(checkAddress(receiverList)));
        // CCのメールアドレスを設定する
        if (null != ccList && ccList.size() > 0) {
            setCopyTo(listToString(checkAddress(ccList)));
        }
        // 送信メールアドレスを設定する
        setSender(sender);
        // メールを送信する
        sendout();
    }

    /**
     * @return the makePlainTextBody
     */
    public boolean isMakePlainTextBody() {
        return makePlainTextBody;
    }

    /**
     * @param makePlainTextBody the makePlainTextBody to set
     */
    public void setMakePlainTextBody(boolean makePlainTextBody) {
        this.makePlainTextBody = makePlainTextBody;
    }

    /**
     * チェックメールアドレス
     *
     * @param address
     * @return
     */
    private List<String> checkAddress(List<String> receiverList) {
        List<String> checkedList = new ArrayList<>();
        if (null == receiverList) {
            return checkedList;
        }

        for (int i = 0; i < receiverList.size(); i++) {
            try {
                InternetAddress internetAddress = new InternetAddress(receiverList.get(i), true);
                checkedList.add(internetAddress.getAddress());
            } catch (AddressException ex) {
                Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return checkedList;
    }

}
