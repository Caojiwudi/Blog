package cn.lzzy.model.domain;

/**
 * @author 梁献红
 * @date 2023/11/2 0:33
 * @description: springboot
 */
public class UserAuthority {
    private Integer id;
    private Integer userId;
    private Integer authorityId;

    public UserAuthority() {
    }

    public UserAuthority(Integer id, Integer userId, Integer authorityId) {
        this.id = id;
        this.userId = userId;
        this.authorityId = authorityId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public String toString() {
        return "UserAuthority{" +
                "id=" + id +
                ", userId=" + userId +
                ", authorityId=" + authorityId +
                '}';
    }
}
