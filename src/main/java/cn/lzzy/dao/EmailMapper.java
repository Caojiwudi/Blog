package cn.lzzy.dao;

import cn.lzzy.model.domain.ScheduleEmail;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * @author: 曾寿华
 * @create: 2023-11-05 12:02
 * @Version 1.0
 * 邮件管理
 **/
@Mapper
public interface EmailMapper {
    // 创建定时邮件
    @Insert("INSERT INTO t_schedule_email(toaddress, schedule, subject,content,status,error,create_time) VALUES(#{toaddress}, #{schedule}, #{subject},#{content},#{status},#{error},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insertScheduledEmail(ScheduleEmail email);


    @Select("select * from t_schedule_email where status='0' and schedule>=#{schedule}")
    public List<ScheduleEmail> queryScheduledEmail(@Param("schedule") int schedule);

    @Update("update t_schedule_email set status=#{status} where id=#{id}")
    public void updateScheduledEmailStatus(@Param("id") int id,@Param("status") String status);

    @Update("update t_schedule_email set status=#{status},error=#{error} where id=#{id}")
    public void updateScheduledEmailStatusWithError(@Param("id") int id,@Param("status") String status, @Param("error") String error);

}
