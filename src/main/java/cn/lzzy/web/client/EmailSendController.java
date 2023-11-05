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
    //发送上传附件
    @PostMapping("sendAttachment")
    public String sendAttachment(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws MessagingException {

        String dirName="c:\\upload\\";
        String fileName = file.getOriginalFilename();
        File uploadDir = new File(dirName);
        if (!uploadDir.exists())
            uploadDir.mkdirs();
        String path = dirName + fileName;
        // 新建一个文件
        File tempFile = new File(path);
        try {
            // 将上传的文件写入新建的文件中
            file.transferTo(tempFile);
        } catch (Exception e) {
            request.setAttribute("message","上传文件存储失败 "+e.getMessage());
            e.printStackTrace();
        }

        //-------发送邮件附件
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(emailfrom);
        helper.setTo(request.getParameter("to"));
        helper.setSubject(request.getParameter("subject"));
        helper.setText("");
        helper.addAttachment(fileName,tempFile);
        String result;
        try {
            //发送邮件
            mailSender.send(message);
            result="附件邮件发送成功";
        } catch (MailException e) {
            result="附件邮件发送失败 " + e.getMessage();
            System.out.println(result);
            e.printStackTrace();
        }
        request.setAttribute("message",result);
        return index(request);
    }


}
