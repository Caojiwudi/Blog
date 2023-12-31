package cn.lzzy.web.admin;


import cn.lzzy.model.ResponseData.ArticleResponseData;
import cn.lzzy.model.ResponseData.StaticticsBo;
import cn.lzzy.model.domain.Article;
import cn.lzzy.model.domain.Comment;
import cn.lzzy.service.IArticleService;
import cn.lzzy.service.ICommentService;
import cn.lzzy.service.ISiteService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author gaocailing
 * @date 2023/11/1 15:03
 * 博客： https://zhanjq.blog.csdn.net/?type=blog
 * @description:
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ISiteService siteServiceImpl;
    @Autowired
    private IArticleService articleServiceImpl;
    @Autowired
    private ICommentService commentServiceImpl;


    // 管理中心起始页
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        // 获取最新的5篇博客、评论以及统计数据
        List<Article> articles = siteServiceImpl.recentArticles(5);
        List<Comment> comments =siteServiceImpl.recentComments(15);
        StaticticsBo staticticsBo = siteServiceImpl.getStatistics();
        // 向Request域中存储数据
        request.setAttribute("comments",comments);
        request.setAttribute("articles", articles);
        request.setAttribute("statistics", staticticsBo);
        return "back/index";
    }

    // 向文章发表页面跳转
    @GetMapping(value = "/article/toEditPage")
    public String newArticle( ) {
        return "back/article_edit";
    }
    // 发表文章
    @PostMapping(value = "/article/publish")
    @ResponseBody
    public ArticleResponseData publishArticle(Article article) {
        if (StringUtils.isEmpty(article.getCategories())) {
            article.setCategories("默认分类");
        }

        // Check if the content exceeds 200 characters
        if (article.getContent().length() > 210) {
            logger.error("文章内容超过210个字，无法发布");
            return ArticleResponseData.fail("文章内容超过210个字，无法发布");
        }

        try {
            articleServiceImpl.publish(article);
            logger.info("文章发布成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章发布失败，错误信息: " + e.getMessage());
            return ArticleResponseData.fail();
        }
    }
    // 跳转到后台文章列表页面
    @GetMapping(value = "/article")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "count", defaultValue = "10") int count,
                        HttpServletRequest request) {
        PageInfo<Article> pageInfo = articleServiceImpl.selectArticleWithPage(page, count);
        request.setAttribute("articles", pageInfo);
        return "back/article_list";
    }

    // 向文章修改页面跳转
    @GetMapping(value = "/article/{id}")
    public String editArticle(@PathVariable("id") String id, HttpServletRequest request) {
        Article article = articleServiceImpl.selectArticleWithId(Integer.parseInt(id));
        request.setAttribute("contents", article);
        request.setAttribute("categories", article.getCategories());
        return "back/article_edit";
    }

    // 文章修改处理
    @PostMapping(value = "/article/modify")
    @ResponseBody

    public ArticleResponseData modifyArticle(Article article) {
        // Check if the content exceeds 250 characters
        if (article.getContent().length() > 250) {
            logger.error("文章内容超过250个字，无法重新发布");
            return ArticleResponseData.fail("文章内容超过250个字，无法重新发布");
        }

        try {

            articleServiceImpl.updateArticleWithId(article);
            logger.info("文章更新成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章更新失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    // 文章删除
    @PostMapping(value = "/article/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id) {
        try {
            logger.info("文章删除开始");
            articleServiceImpl.deleteArticleWithId(id);
            logger.info("文章删除成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章删除失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    //评论删除
    // 向评论页面跳转
    @RequestMapping(value = "/comments")
    public String pinglun(@RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "count", defaultValue = "10") int count,
                          HttpServletRequest request) {
        PageInfo<Comment> comments = siteServiceImpl.selectCommentWithPage(page, count);
        PageInfo<Article> pageInfo =
                articleServiceImpl.selectArticleWithPage(page, count);
        request.setAttribute("articles", pageInfo);
        request.setAttribute("comments", comments);
        return "back/comment_list";
    }

    // 评论删除
    @RequestMapping(value = "/comment/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id,
                                      HttpServletRequest request) {
        try {
            int a = commentServiceImpl.deleteCommentId(id);
            logger.info("评论删除成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("评论删除失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }
}
