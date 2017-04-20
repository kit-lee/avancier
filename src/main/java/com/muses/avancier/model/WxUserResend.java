package com.muses.avancier.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 弹幕类型活动的弹幕重发任务
 * @author kit@muses.cc
 *
 */
@Entity
@Table(name="wxUserResend")
public class WxUserResend {

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
	 * 留言内容
	 */
	private String message;
	
	/**
	 * 延迟的秒数
	 */
	private int delay;
	
	/**
	 * 是否已重发
	 */
	private boolean resended;

	
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

    @Column(name = "message", length = 200)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "delay")
    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Column(name = "resended", nullable = false)
    public boolean isResended() {
        return resended;
    }

    public void setResended(boolean resended) {
        this.resended = resended;
    }

}
