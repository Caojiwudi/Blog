package cn.lzzy.service;
import cn.lzzy.model.domain.ScheduleEmail;

/**
 * @author: 曾寿华
 * @create: 2023-11-01 16:33
 * @Version 1.0
 * 邮件管理的接口类
 **/


public interface IMailService {

    public void saveScheduledEmail(ScheduleEmail email);

}
