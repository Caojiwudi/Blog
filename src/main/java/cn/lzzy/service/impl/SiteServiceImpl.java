package cn.lzzy.service.impl;

import com.github.pagehelper.PageHelper;
import cn.lzzy.dao.ArticleMapper;

import cn.lzzy.dao.StatisticMapper;
import cn.lzzy.model.ResponseData.StaticticsBo;
import cn.lzzy.model.domain.Article;
import cn.lzzy.model.domain.Comment;
import cn.lzzy.model.domain.Statistic;
import cn.lzzy.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author gaocailing
 * @date 2023/11/1 15:19
 * 博客： https://zhanjq.blog.csdn.net/?type=blog
 * @description:
 */
@Service
@Transactional
public class SiteServiceImpl implements ISiteService{

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public void updateStatistics(Article article) {
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
        statistic.setHits(statistic.getHits()+1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }


    @Override
    public List<Article> recentArticles(int limit) {
        PageHelper.startPage(1, limit>10 || limit<1 ? 10:limit);
        List<Article> list = articleMapper.selectArticleWithPage();
        // 封装文章统计数据
        for (int i = 0; i < list.size(); i++) {
            Article article = list.get(i);
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return list;
    }

    @Override
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo = new StaticticsBo();
        Integer articles = articleMapper.countArticle();

        staticticsBo.setArticles(articles);

        return staticticsBo;
    }
}
