package cn.lzzy.service.impl;

import cn.lzzy.dao.CommentMapper;
import cn.lzzy.dao.StatisticMapper;
import cn.lzzy.model.domain.Article;
import cn.lzzy.model.domain.Comment;
import cn.lzzy.model.domain.Statistic;
import cn.lzzy.service.ICommentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author LiuQin
 * @date 2023/11/1 14:08
 * 博客： https://blog.csdn.net/qq_63909948
 * @description:
 */
@Service
@Transactional
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    //用户发表评论
    @Override
    public void pushComment(Comment comment) {
        commentMapper.pushComment(comment);
        //更新文章评论数据量
        Statistic statistic =
                statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum() + 1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }

    //根据文章id分页查询评论
    @Override
    public PageInfo<Comment> getComment(Integer aid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentPageInfo = new PageInfo<>(commentList);
        return commentPageInfo;
    }

    //删除评论
    @Override
    public int deleteCommentId(int id) {
        return commentMapper.deleteCommentWithId(id);
    }

    //查询
    @Override
    public Comment selectById(int id) {
        return commentMapper.selectId(id);
    }
    //保存
    @Override
    public int save(int id, String content) {
        return commentMapper.update(id,content);
    }

}
