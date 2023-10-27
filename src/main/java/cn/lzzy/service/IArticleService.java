package cn.lzzy.service;

import com.github.pagehelper.PageInfo;
import cn.lzzy.model.domain.Article;

import java.util.List;

/**
 * @author 周世雄
 * @date 2023/10/27 11:57
 * 博客： https://blog.csdn.net/m0_60482574?type=blog
 * @description: 网站开发与维护
 */
public interface IArticleService {
    // 分页查询文章列表
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count);

    // 统计前15的热度文章信息
    public List<Article> getHeatArticles();
}
