package cn.lzzy.web.client;

import cn.lzzy.model.ResponseData.ArticleResponseData;
import cn.lzzy.model.domain.Comment;
import cn.lzzy.service.ICommentService;
import cn.lzzy.utils.MyUtils;
import com.vdurmont.emoji.EmojiParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author LiuQin
 * @date 2023/11/1 16:40
 * 博客： https://blog.csdn.net/qq_63909948
 * @description:
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private ICommentService commentServiceImpl;

    // 发表评论操作
    @PostMapping(value = "/publish")
    @ResponseBody
    public ArticleResponseData publishComment(HttpServletRequest request, @RequestParam Integer aid, @RequestParam String text) {
        // 去除js脚本
        text = MyUtils.cleanXSS(text);
        text = EmojiParser.parseToAliases(text);
        // 获取当前登录用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 封装评论信息
        Comment comments = new Comment();
        comments.setArticleId(aid);
        comments.setIp(request.getRemoteAddr());
        comments.setCreated(new Date());
        comments.setAuthor(user.getUsername());
        comments.setContent(text);
        try {
            commentServiceImpl.pushComment(comments);
            logger.info("发布评论成功，对应文章id: " + aid);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("发布评论失败，对应文章id: " + aid + ";错误描述: " + e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable(name = "id") int id,
                       HttpServletRequest request) {
        Comment comment =  commentServiceImpl.selectById(id);
        request.setAttribute("comment", comment);
        return "back/comment_edit";
    }

    @RequestMapping(value = "/comment/edit")
    @ResponseBody
    public ArticleResponseData editsave(@RequestParam int id,@RequestParam String content,
                                        HttpServletRequest request) {
        int a = commentServiceImpl.save(id, content);

        return a>0?ArticleResponseData.ok():ArticleResponseData.fail();
    }


}
