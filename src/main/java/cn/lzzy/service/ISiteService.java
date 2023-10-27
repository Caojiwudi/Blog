package cn.lzzy.service;

import cn.lzzy.model.ResponseData.StaticticsBo;
import cn.lzzy.model.domain.Article;

import java.util.List;

/**
 * @author 周世雄
 * @date 2023/10/28 0:59
 * 博客： https://blog.csdn.net/m0_60482574?type=blog
 * @description: 网站开发与维护
 */
public interface ISiteService {

    // 获取后台统计数据
    public StaticticsBo getStatistics();

}
