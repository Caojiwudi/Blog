package cn.lzzy.model.ResponseData;

/**
 * @author 周世雄
 * @date 2023/10/27 11:56
 * 博客： https://blog.csdn.net/m0_60482574?type=blog
 * @description: 网站开发与维护
 */
public class StaticticsBo {
    private Integer articles;
    private Integer comments;

    public Integer getArticles() {
        return articles;
    }

    public void setArticles(Integer articles) {
        this.articles = articles;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "StaticticsBo{" +
                "articles=" + articles +
                ", comments=" + comments +
                '}';
    }
}
