package cn.lzzy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Classname 曾寿华
 * @Description 邮件发送工具类
 * @Date 2023-10-29 16:15
 *
 */
@Component
public class MailUtils {
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String mailfrom;

    //发送简单邮件
    public void sendSimpleEmail(String mailto, String title, String content) {
        //定制邮件发送内容
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailfrom);
        message.setTo(mailto);
        message.setSubject(title);
        message.setText(content);
        try {
            //发送邮件
            mailSender.send(message);
            System.out.println("纯文本邮件发送成功");
        } catch (MailException e) {
            System.out.println("纯文本邮件发送失败 " + e.getMessage());
            e.printStackTrace();
        }
    }
}

