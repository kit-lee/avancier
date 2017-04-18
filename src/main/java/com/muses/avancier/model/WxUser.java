package com.muses.avancier.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 签到微信用户
 * @author kit@muses.cc
 *
 */
@Entity
@Table(indexes = { 
        @Index(name="idx_openid", columnList="openId"),
        @Index(name="idx_tags", columnList="tags")
        })
public class WxUser {

    /**
     * 自增长主键
     */
	private Long id;
	
	/**
	 * 活动ID
	 */
	@JSONField(serialize=false)
	private Activity activity;
	
	/**
	 * 微信openId
	 */
	private String openId;
	
	/**
	 * 微信头像
	 */
	private String headpic;
	
	/**
     * 微信昵称
     */
    private String nickname;
	
	/**
	 * 创建时间
	 */
	private Date createtime;
	
	/**
	 * 是否已传输到上墙程序
	 */
	private boolean trans;
	
	/**
	 * 是否已审核
	 */
	private boolean checked;
	
	/**
	 * 留言内容
	 */
	private String message;
	
	/**
     * 标签
     */
    private String tags;
	
	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="activityId")
	public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Column(name = "openId", nullable = false, length = 45)
	public String getOpenId() {
		return openId;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@Column(name = "headpic", nullable = false, length = 200)
	public String getHeadpic() {
		return headpic;
	}
	
	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}
	
	@Column(name = "nickname", nullable = false, length = 45)
	public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createtime", nullable = false, length = 19)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Column(name = "trans", nullable = false)
	public boolean isTrans() {
		return trans;
	}
	
	public void setTrans(boolean trans) {
		this.trans = trans;
	}

	@Column(name = "checked", nullable = false)
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Column(name = "message", length = 200)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "tags", length = 20)
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
	
    
}
