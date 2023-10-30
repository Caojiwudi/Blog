package cn.lzzy.web.client;

import cn.lzzy.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 梁献红
 * @date 2023/10/29 16:42
 * @description: springboot
 */
@Controller
public class UserController {
    @Autowired
    IUserService iUserService;

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
        return "ok";
    }
}
