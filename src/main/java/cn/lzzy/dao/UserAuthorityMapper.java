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
}
