package cn.lzzy.service;

import cn.lzzy.model.ResponseData.StaticticsBo;
import cn.lzzy.model.domain.Article;
import cn.lzzy.model.domain.Comment;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 周世雄
 * @date 2023/10/28 0:59
 * 博客： https://blog.csdn.net/m0_60482574?type=blog
 * @description: 网站开发与维护
 */

/**
 * 博客站点统计服务
 */
public interface ISiteService {

    // 获取后台统计数据
    public StaticticsBo getStatistics();
    // 最新发表的文章
    public List<Article> recentArticles(int count);

    // 更新某个文章的统计数据
    public void updateStatistics(Article article);
    //最新收到的评论
    public List<Comment> recentComments(int count) ;

    PageInfo<Comment> selectCommentWithPage(int page, int count);
}
