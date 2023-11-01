package cn.lzzy.dao;

import cn.lzzy.service.impl.UserService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 梁献红
 * @date 2023/11/2 0:22
 * @description: springboot
 */
@Mapper
public interface UserMapper {
    // 注册新用户
    int insertUser(UserService user);

    // 获取用户名
    //@Select("SELECT * FROM t_user WHERE username = #{username}")
    List<UserService> findByUsername(String username);

    // 获取新用户id
    int getNewUserId();
}
