package com.muses.avancier.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 活动信息
 * @author kit@muses.cc
 * @since 2017-3-27
 */
@Entity
@Table(name="activity")
public class Activity {

    /**
     * 主键
     */
    private Long id;
    
    /**
     * 活动名称
     */
    private String name;
    
    /**
     * 开始日期
     */
    private Date start;
    
    /**
     * 结束日期
     */
    private Date end;
    
    /**
     * 活动类型
     * checkin 签到，一活动一微信号一次签到
     * barrage 弹幕，一活动一微信号多次
     */
    private String type;
    
    /**
     * 数据是否需审核
     */
    private boolean needAudit;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start", length = 19)
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end", length = 19)
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Column(name = "actType", length = 7)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "needAudit", nullable = false)
    public boolean isNeedAudit() {
        return needAudit;
    }

    public void setNeedAudit(boolean needAudit) {
        this.needAudit = needAudit;
    }
    
}
