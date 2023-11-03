package cn.lzzy.dao;

import cn.lzzy.model.domain.UserAuthority;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 梁献红
 * @date 2023/11/2 0:36
 * @description: springboot
 */
@Mapper
public interface UserAuthorityMapper {
    // 注册新用户权限
    int insertUser(UserAuthority userAuthority);
    // 查看用户权限
    int getAuthoritiesByUserId(int userId);

    // 删除用户信息权限
    int deleteUserAuthority(int userId);

    //根据权限id查找用户权限
    String getAuthorityById(int authorityId);
}
