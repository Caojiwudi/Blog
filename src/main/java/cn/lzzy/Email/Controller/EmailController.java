package cn.lzzy.Email.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author: 曾寿华
 * @create: 2023-11-02 14:27
 * @Version 1.0
 **/
@Controller
public class EmailController {
    private JavaMailSender emailSender;
    @Autowired
    private JavaMailSenderImpl mailSender; // 自动注入邮件发送器
    @Value("${spring.mail.username}")
    private String form; // 从配置文件中读取发件人邮箱地址

    @GetMapping(value = "/shou")
    public String shou(){
        return "email/index";
    }

    @GetMapping("/toEmail")         //http://localhost/toEmail
    public String toUpload() {
        return "email/sendPlainEmail";
    }

    @GetMapping("/sendEmail")
    public Object addStudentDispose(String recipient, String subject, String text) {

        // 判断目标邮箱地址是否为空
        if (recipient == null || recipient.trim().isEmpty()) {
            System.out.println("请输入邮箱地址");
            ModelAndView mav = new ModelAndView("email/sendPlainEmail");  //假设upload是你的视图名称
            mav.addObject("uploadStatus", "请输入邮箱地址");  //你可以在视图中使用这些数据
            return mav;
        }

        // 判断邮箱格式是否正确
        if (!isValidEmail(recipient)) {
            System.out.println("邮箱格式不正确");
            ModelAndView mav = new ModelAndView("email/sendPlainEmail");  //假设upload是你的视图名称
            mav.addObject("uploadStatus", "邮箱格式不正确");  //你可以在视图中使用这些数据
            return mav;
        }

        // 判断文本内容是否为空
        if (text == null || text.trim().isEmpty()) {
            System.out.println("请输入文本内容");
            ModelAndView mav = new ModelAndView("email/sendPlainEmail");  //假设upload是你的视图名称
            mav.addObject("uploadStatus", "请输入文本内容");  //你可以在视图中使用这些数据
            return mav;
        }

        // 判断文本内容是否超过200字
        if (text.length() > 200) {
            System.out.println("文本内容不能超过200个字");
            ModelAndView mav = new ModelAndView("email/sendPlainEmail");  //假设upload是你的视图名称
            mav.addObject("uploadStatus", "文本内容不能超过200个字");  //你可以在视图中使用这些数据
            return mav;
        }

        System.out.println(recipient);
        System.out.println(subject);
        System.out.println(text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(form); // 设置发件人
        message.setTo(recipient); // 设置收件人
        message.setSubject(subject); // 设置邮件标题
        message.setText(text); // 设置邮件内容
        try {
            // 发送邮件
            mailSender.send(message);
            System.out.println("纯文本邮件发送成功");
            ModelAndView mav = new ModelAndView("email/sendPlainEmail");  //假设upload是你的视图名称
            mav.addObject("uploadStatus", "邮件发送成功");  //你可以在视图中使用这些数据
            return mav;

        } catch (MailException e) {
            System.out.println("纯文本邮件发送失败 " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("邮件发送失败：" + e.getMessage()); // 返回错误信息给前端
        }
    }
    // 判断邮箱格式是否正确
    private boolean isValidEmail(String email) {
        String regex = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
        return email.matches(regex);
    }
}
