package cn.lzzy.web.client;

import cn.lzzy.dao.UserAuthorityMapper;
import cn.lzzy.dao.UserMapper;
import cn.lzzy.model.domain.UserAuthority;
import cn.lzzy.service.IUserService;
import cn.lzzy.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 梁献红
 * @date 2023/10/29 16:42
 * @description: springboot
 */
@Controller
public class UserController {
    @Autowired
    IUserService iUserService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthorityMapper userAuthorityMapper;

    @GetMapping("/showuser")
    public String Showuser() {
        return "comm/userifo";
    }

    @GetMapping("/toUpdate")
    public String toUpdate() {
        return "client/updateUser";
    }

    @GetMapping("/getuserByContext")
    @ResponseBody
    public String getUser2() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        System.out.println("username: " + principal.getUsername());
        return principal.getUsername();
    }

    @ResponseBody
    @PostMapping(value = "/updateUser")
    public String updateUser(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        System.out.println(username);
        System.out.println(password);
        String csrf_token = request.getParameter("_csrf");
        System.out.println(csrf_token);
        iUserService.updateByUsername(username, getUser2());
        return "用户名修改成功";
    }
    // 新用户注册
    @GetMapping("/toRegister") // http://localhost/toRegister
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserService());
        return "client/registerUser";
    }

    // 用户注册提交处理
    @PostMapping(value = "/registerUser")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("email") String email) {
        // 创建新用户
        UserService user = new UserService();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        if (isUsernameExist(username)) {
            return "comm/user_error";
        } else {
            userMapper.insertUser(user);
            // 获取新插入的用户ID
            Integer userId = userMapper.getNewUserId();
            // 插入用户权限关联信息到t_user_authority表
            UserAuthority userAuthority =new UserAuthority();
            userAuthority.setUserId(userId);
            userAuthority.setAuthorityId(2);
            userAuthorityMapper.insertUser(userAuthority);
            return "comm/user_finish";
        }
    }

    // 判断username是否存在
    public boolean isUsernameExist(String username) {
        List<UserService> users = userMapper.findByUsername(username);
        return !users.isEmpty();
    }

    // 向用户信息删除页跳转
    @GetMapping("/deleteUser")
    public String deleteUser() {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 查询当前用户名id
        int userId = userMapper.getNewUserId();
        userMapper.deleteUser(userId);
        userAuthorityMapper.deleteUserAuthority(userId);
        // 清除认证信息
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        if (authentication1 != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "comm/user_delete_finish";
    }
}
