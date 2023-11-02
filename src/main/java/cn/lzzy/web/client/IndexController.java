package cn.lzzy.web.client;

import cn.lzzy.model.domain.Comment;
import cn.lzzy.service.ICommentService;
import cn.lzzy.service.ISiteService;
import com.github.pagehelper.PageInfo;
import cn.lzzy.model.domain.Article;
import cn.lzzy.service.IArticleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 周世雄
 * @date 2023/10/27 12:06
 * 博客： https://blog.csdn.net/m0_60482574?type=blog
 * @description: 网站开发与维护
 */
@Controller
public class IndexController {
     final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IArticleService articleServiceImpl;
    @Autowired
    private ICommentService commentServiceImpl;
    @Autowired
    private ISiteService siteServiceImpl;


    // 博客首页，会自动跳转到文章页
    @GetMapping(value = "/")
    private String index(HttpServletRequest request) {
        return this.index(request, 1, 5);
    }

    // 文章页
    @GetMapping(value = "/page/{p}")
    public String index(HttpServletRequest request, @PathVariable("p") int page, @RequestParam(value = "count", defaultValue = "5") int count) {
        PageInfo<Article> articles = articleServiceImpl.selectArticleWithPage(page, count);
        // 获取文章热度统计信息
        List<Article> articleList = articleServiceImpl.getHeatArticles();
        request.setAttribute("articles", articles);
        request.setAttribute("articleList", articleList);
        logger.info("分页获取文章信息: 页码 " + page + ",条数 " + count);
        return "client/index";
    }

    // 文章详情查询
    @GetMapping(value = "/article/{id}")
    public String getArticleById(@PathVariable("id") Integer id,
                                 HttpServletRequest request) {
        Article article = articleServiceImpl.selectArticleWithId(id);
        if (article != null) {
            //查询封装评论相关数据
            getArticleComments(request,article);
            //更新文章点击量
            siteServiceImpl.updateStatistics(article);
            request.setAttribute("article", article);
            return "client/articleDetails";
        } else {
            logger.warn("查询文章详情结果为空，查询文章id: " + id);
            //未找到对应文章页面
            return "comm/error_404";
        }
    }
    //查询文章的评论信息，并补充到文章详情里面
    public void getArticleComments(HttpServletRequest request,Article article){
        if (article.getAllowComment()){
            //cp表示评论页码，commentPage
            String  cp = request.getParameter("cp");
            cp = StringUtils.isBlank(cp) ? "1" :cp;
            request.setAttribute("cp",cp);
            PageInfo<Comment> comments =
                    commentServiceImpl.getComment(article.getId(),Integer.parseInt(cp),3);
            request.setAttribute("cp",cp);
            request.setAttribute("comments",comments);
        }
    }

}
