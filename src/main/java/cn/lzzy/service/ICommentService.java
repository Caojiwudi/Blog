package cn.lzzy.service;

/**
 * @author LiuQin
 * @date 2023/11/1 13:58
 * 博客： https://blog.csdn.net/qq_63909948
 * @description:
 */

import cn.lzzy.model.domain.Comment;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;


/**
 * 文章评论业务处理接口
 */
public interface ICommentService {
    //用户发表评论
    public void pushComment(Comment comment);
    //获取文章下的评论
    public PageInfo<Comment> getComment(Integer aid, int page,int count);
    //删除评论
    public int deleteCommentId(int id);
    //查询
    Comment selectById(int id);
    //修改
    int save(int id,String content);
}
