package cn.lzzy.service;

import cn.lzzy.service.impl.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author 梁献红
 * @date 2023/10/30 1:03
 * @description: springboot
 */
public interface IUserService extends JpaRepository<UserService, Integer> {
    UserService findByUsername(String username);
    @Transactional
    @Modifying
    @Query("UPDATE t_user c SET c.username = ?1 WHERE c.username =?2")
    public void updateByUsername(String username, String name);
}
