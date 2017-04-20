package com.muses.avancier.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.muses.avancier.model.Activity;
import com.muses.avancier.model.WxUser;

/**
 * 活动签到/弹幕记录
 * @author kit@muses.cc
 *
 */
public interface WxUserRepository extends PagingAndSortingRepository<WxUser, Long> {
	
    /**
     * 返回所有未传输到前端的记录（不区分客户端）
     * @param activity
     * @return
     */
	@Query("select u from WxUser u where u.activity=?1 and trans=0 and checked=1")  
	List<WxUser> findAllNotTrans(Activity activity);
	
	/**
	 * 返回所有已审核的记录
	 * @param activity
	 * @return
	 */
	@Query("select u from WxUser u where u.activity=?1 and checked=1 order by id desc")  
    List<WxUser> findAllChecked(Activity activity);
	
	/**
	 * 分页返回所有已审核的记录
	 * @param activity
	 * @param pageable
	 * @return
	 */
	@Query("select u from WxUser u where u.activity=?1 and checked=1 order by id desc")  
    Page<WxUser> findAllChecked(Activity activity, Pageable pageable);
	
	/**
	 * 按关键字过滤返回已审核的记录
	 * @param activity
	 * @param keyword
	 * @param pageable
	 * @return
	 */
	@Query("select u from WxUser u where u.activity=?1 and checked=1 and (nickname like %?2% or tags like %?2%) order by id desc")  
    Page<WxUser> findAllChecked(Activity activity, String keyword, Pageable pageable);
	
	/**
	 * 返回未审核的所有记录
	 * @param pageable
	 * @return
	 */
	@Query("select u from WxUser u where checked=0")  
	Page<WxUser> findByNotChecked(Pageable pageable);
	
	/**
	 * 根据ID数组返回列表
	 * @param ids
	 * @return
	 */
    List<WxUser> findByIdIn(Long[] ids);
    
    /**
     * 返回指定openId的一个用户
     * @param act
     * @param openId
     * @return
     */
    long countByActivityAndOpenId(Activity act, String openId);
    
}
