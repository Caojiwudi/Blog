package cn.lzzy.dao;

import cn.lzzy.model.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;
/**
 * @author LiuQin
 * @date 2023/11/1 10:41
 * 博客： https://blog.csdn.net/qq_63909948
 * @description:
 */
@Mapper
public interface CommentMapper {
    // 分页展示某个文章的评论
    @Select("SELECT * FROM t_comment WHERE article_id=#{aid} ORDER BY id DESC")
    public List<Comment> selectCommentWithPage(Integer aid);

    // 后台查询最新几条评论
    @Select("SELECT * FROM t_comment ORDER BY id DESC")
    public List<Comment> selectNewComment();

    // 发表评论
    @Insert("INSERT INTO t_comment (article_id,created,author,ip,content)" +
            " VALUES (#{articleId}, #{created},#{author},#{ip},#{content})")
    public void pushComment(Comment comment);

    // 站点服务统计，统计评论数量
    @Select("SELECT COUNT(1) FROM t_comment")
    public Integer countComment();

    // 通过文章id删除评论信息
    @Delete("DELETE FROM t_comment WHERE id=#{id}")
     int deleteCommentWithId(Integer id);

    //通过id更新评论
    @Update("update t_comment SET content = #{content} where id=#{id}")
    int update(int id,String content);

    //根据id查询评论
    @Select("SELECT * FROM t_comment WHERE id=#{id}")
     Comment selectId(Integer id);


}
