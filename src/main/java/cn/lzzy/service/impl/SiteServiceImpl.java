package cn.lzzy.service.impl;

import cn.lzzy.dao.ArticleMapper;
import cn.lzzy.dao.CommentMapper;
import cn.lzzy.dao.StatisticMapper;
import cn.lzzy.model.ResponseData.StaticticsBo;
import cn.lzzy.model.domain.Article;
import cn.lzzy.model.domain.Comment;
import cn.lzzy.model.domain.Statistic;
import cn.lzzy.service.ISiteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LiuQin
 * @date 2023/11/1 14:15
 * 博客： https://blog.csdn.net/qq_63909948
 * @description:
 */
@Service
@Transactional
public class SiteServiceImpl implements ISiteService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo =new StaticticsBo();
        Integer articles = articleMapper.countArticle();
        Integer comments = commentMapper.countComment();
        staticticsBo.setArticles(articles);
        staticticsBo.setComments(comments);
        return staticticsBo;
    }

    @Override
    public List<Comment> recentComments(int limit) {
        PageHelper.startPage(1,limit>15 || limit<1 ? 15:limit);
        List<Comment> byPage = commentMapper.selectNewComment();
        return byPage;
    }

    @Override
    public PageInfo<Comment> selectCommentWithPage(int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectNewComment();
        PageInfo<Comment> pageInfo=new PageInfo<>(commentList);
        return pageInfo;
    }

    @Override
    public List<Article> recentArticles(int limit) {
        PageHelper.startPage(1,limit>10 || limit<1 ? 10:limit);
        List<Article> list = articleMapper.selectArticleWithPage();
        //封装文章统计数据
        for(int i=0; i<list.size();i++){
            Article article = list.get(i);
            Statistic statistic=
                    statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return list;
    }

    @Override
    public void updateStatistics(Article article) {
        Statistic statistic =
                statisticMapper.selectStatisticWithArticleId(article.getId());
        statistic.setHits(statistic.getHits()+1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }
}
