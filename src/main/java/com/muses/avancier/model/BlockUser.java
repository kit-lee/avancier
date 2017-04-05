package com.muses.avancier.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 黑名单记录
 * @author kit@muses.cc
 * @since 2017-3-29
 *
 */
@Entity
@Table(name="blockUser")
public class BlockUser {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 微信用户openid
     */
    private String openId;
   
    /**
     * 微信昵称
     */
    private String nickname;
    
    /**
     * 微信头像
     */
    private String headpic;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "openId", nullable = false, length = 45)
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Column(name = "nickname", nullable = false, length = 45)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "headpic", nullable = false, length = 200)
    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }
    
}
