package com.muses.avancier.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = { 
        @Index(name="idx_openid", columnList="openId",unique=true)
        })
public class WxUser {

	@Id
	@GeneratedValue
	private Long id;
	private String openId;
	private String headpic;
	private Date createtime;
	private boolean trans;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getHeadpic() {
		return headpic;
	}
	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public boolean isTrans() {
		return trans;
	}
	public void setTrans(boolean trans) {
		this.trans = trans;
	}
	
}
