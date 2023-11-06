package cn.lzzy.service.impl;

import cn.lzzy.service.IMailService;
import cn.lzzy.dao.EmailMapper;
import cn.lzzy.dao.StatisticMapper;
import cn.lzzy.model.domain.ScheduleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.Calendar;
import java.util.List;

/**
 * @author: 曾寿华
 * @create: 2023-11-01 16:35
 * @Version 1.0
 * 邮件管理
 **/

@Service
@Transactional
public class EmailServicelmpl implements IMailService {

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private StatisticMapper statisticMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailfrom;

    //spring 定时管理器
    private ThreadPoolTaskScheduler taskScheduler;
    private final static String[] WEEK_NAMES=new String[]{"MON","TUES","WED","THUR","FRI","SAT","SUN"};

    /**
     * 初始化spring定时管理器，并动态配置邮件定时器
     */
    @PostConstruct
    public void initTaskScheduler(){
        System.out.println("初始化邮件定时管理器....");
        taskScheduler=new ThreadPoolTaskScheduler();
        taskScheduler.initialize();

        //获取当前时间，然后查询当前时间点，以及之前为执行发送的邮件列表
        Calendar cal=Calendar.getInstance();
        int week=cal.get(Calendar.DAY_OF_WEEK);
        if(week==1){
            //如果0，则星期天，需要修改为7，与界面上设置的一致
            week=7;
        }else
            week-=1;
        int hour=cal.get(Calendar.HOUR_OF_DAY);
        int minute=cal.get(Calendar.MINUTE);
        int schedule=Integer.valueOf(week+(hour<10?"0":"")+hour+(minute<10?"0":"")+minute);
        List<ScheduleEmail> scheduledEmailList=emailMapper.queryScheduledEmail(schedule);
        if(scheduledEmailList.size()>0){
            System.out.println("动态配置邮件定时器....");
            for(ScheduleEmail email :scheduledEmailList){
                setupScheduledEmail(email);
            }
        }
    }

    //保存定时邮件
    @Override
    public void saveScheduledEmail(ScheduleEmail email) {
        emailMapper.insertScheduledEmail(email);
        setupScheduledEmail(email);
    }

    //发送定时邮件
    public void sendScheduledEmail(ScheduleEmail email){
        System.out.println("执行定时邮件发送 > "+email.getId()+" > "+email.getSchedule());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailfrom);
        mailMessage.setTo(email.getToaddress());
        mailMessage.setSubject(email.getSubject());

        //mailMessage.setText(email.getContent());

        StringBuffer content = new StringBuffer();
        long totalvisit = statisticMapper.getTotalVisit();
        long totalComment = statisticMapper.getTotalComment();
        content.append("博客系统总访问量为："+totalvisit+"人次").append("\n");
        content.append("博客系统总评论量为："+totalComment+"人次").append("\n");
        mailMessage.setText(content.toString());


        String message=null;
        try {
            //发送邮件
            mailSender.send(mailMessage);
            emailMapper.updateScheduledEmailStatus(email.getId(),"1");
        } catch (MailException e) {
            emailMapper.updateScheduledEmailStatusWithError(email.getId(),"2","发送失败 > "+e.getMessage());
        }
    }

    //动态配置定时器
    public void setupScheduledEmail(ScheduleEmail email){
        String scheduleConfig=email.getSchedule()+"";
        int week=Integer.valueOf(scheduleConfig.substring(0,1));
        int hour=Integer.valueOf(scheduleConfig.substring(1,3));
        int minute=Integer.valueOf(scheduleConfig.substring(3));
        taskScheduler.schedule(new EmailSendTask(email),new CronTrigger("0 "+minute+" "+hour+" ? * "+WEEK_NAMES[week-1]));
    }

    public class EmailSendTask implements  Runnable{

        private ScheduleEmail email;
        public EmailSendTask(ScheduleEmail email){
            this.email=email;
        }

        @Override
        public void run() {
            sendScheduledEmail(email);
        }
    }
}
