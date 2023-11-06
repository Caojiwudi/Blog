package cn.lzzy.model.domain;

import java.util.Date;

/**
 * @author: 曾寿华
 * @create: 2023-11-04 10:53
 * @Version 1.0
 **/
public class ScheduleEmail {
    private int id;
    private String toaddress;
    private int schedule;
    private String subject;
    private String content;
    private String status;
    private String error;
    private Date createTime;

    public ScheduleEmail(){}
    public ScheduleEmail(String toaddress, int schedule, String subject, String content, String status, String error, Date createTime) {
        this.toaddress = toaddress;
        this.schedule = schedule;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.error = error;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToaddress() {
        return toaddress;
    }

    public void setToaddress(String toaddress) {
        this.toaddress = toaddress;
    }

    public int getSchedule() {
        return schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

