package com.muses.avancier.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.muses.avancier.model.Activity;
import com.muses.avancier.model.WxUser;

public interface WxUserRepository extends PagingAndSortingRepository<WxUser, Long> {
	
	@Query("select u from WxUser u where u.activity=?1 and trans=0 and checked=1")  
	List<WxUser> findAllNotTrans(Activity activity);
	
	@Query("select u from WxUser u where u.activity=?1 and checked=1 order by id desc")  
    List<WxUser> findAllChecked(Activity activity);
	
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
