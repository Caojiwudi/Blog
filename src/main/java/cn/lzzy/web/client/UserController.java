package cn.lzzy.web.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 梁献红
 * @date 2023/10/29 16:42
 * @description: springboot
 */
@Controller
public class UserController {
    @GetMapping("/showuser")
    public String Showuser() {
        return "comm/userifo";
    }
}
