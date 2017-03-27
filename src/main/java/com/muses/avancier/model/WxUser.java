package com.muses.avancier.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 签到微信用户
 * @author kit@muses.cc
 *
 */
@Entity
@Table(indexes = { 
        @Index(name="idx_openid", columnList="openId", unique=true)
        })
public class WxUser {

    /**
     * 自增长主键
     */
	private Long id;
	
	/**
	 * 活动ID
	 */
	private long activityId;
	
	/**
	 * 微信openId
	 */
	private String openId;
	
	/**
	 * 微信头像
	 */
	private String headpic;
	
	/**
	 * 创建时间
	 */
	private Date createtime;
	
	/**
	 * 是否已传输到上墙程序
	 */
	private boolean trans;
	
	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "activityId", nullable = false)
	public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
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
	
}
