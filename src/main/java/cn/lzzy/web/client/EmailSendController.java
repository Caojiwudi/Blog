package cn.lzzy.web.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;


/**
 * @author: 曾寿华
 * @create: 2023-11-02 14:27
 * @Version 1.0
 **/
@Controller
@RequestMapping("/email")
public class EmailSendController {

    @Autowired
    private JavaMailSenderImpl mailSender; // 自动注入邮件发送器
    @Value("${spring.mail.username}")
    private String emailfrom; // 从配置文件中读取发件人邮箱地址
    //访问首页
    @GetMapping("/index")
    public String index(HttpServletRequest request){

        String page=request.getParameter("page");//页面类型
        if(page!=null)
            request.setAttribute("page",page);

        return "client/email/index";
    }
    //发送纯文本
    @PostMapping("/sendText")
    private String sendTextEmail(@RequestParam("to") String to,
                                 @RequestParam("subject") String subject,
                                 @RequestParam("content") String content,
                                 HttpServletRequest request) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailfrom);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        String message=null;
        try {
            //发送邮件
            mailSender.send(mailMessage);
            message="纯文本邮件发送成功";
        } catch (MailException e) {
            message="纯文本邮件发送失败 " + e.getMessage();
            System.out.println(message);
            e.printStackTrace();
        }

        request.setAttribute("message",message);
        return index(request);
    }


}
