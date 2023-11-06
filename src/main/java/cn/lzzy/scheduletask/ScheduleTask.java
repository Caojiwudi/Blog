package cn.lzzy.scheduletask;


import cn.lzzy.dao.StatisticMapper;

import cn.lzzy.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname 曾寿华
 * @Description 定时任务管理
 * @Date 2023-10-29 16:28
 *
 */
@Component
public class ScheduleTask {

    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private MailUtils emailUtils;

    @Value("${spring.mail.username}")
    private String mailto;

    //定时邮件发送任务，每月1日中午12点整发送邮件
    @Scheduled(cron = "0 0 12 1 * ?")
    public void sendRegularlyEmail() {
        //  定制邮件内容
        long totalvisit = statisticMapper.getTotalVisit();
        long totalComment = statisticMapper.getTotalComment();
        StringBuffer content = new StringBuffer();
        content.append("博客系统总访问量为：" + totalvisit + "人次").append("\n");
        content.append("博客系统总评论量为：" + totalComment + "人次").append("\n");
        emailUtils.sendSimpleEmail(mailto, "个人博客系统流量统计情况", content.toString());
    }

}

