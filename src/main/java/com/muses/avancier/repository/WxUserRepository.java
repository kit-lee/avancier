package com.muses.avancier.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.muses.avancier.model.WxUser;

public interface WxUserRepository extends PagingAndSortingRepository<WxUser, Long> {

    /**
     * 返回所有签到微信用户
     */
	public List<WxUser> findAll();
	
	/**
	 * 根据微信OpenId返回一个微信用户
	 * @param openId
	 * @return
	 */
	public WxUser findByOpenId(String openId);
	
	@Query("select u from WxUser u where trans=0 and checked=1")  
	public List<WxUser> findAllNotTrans();
	
	/**
	 * 返回未审核的所有记录
	 * @param pageable
	 * @return
	 */
	@Query("select u from WxUser u where checked=0")  
	public Page<WxUser> findByNotChecked(Pageable pageable);
	
	/**
	 * 根据ID数组返回列表
	 * @param ids
	 * @return
	 */
    public List<WxUser> findByIdIn(Long[] ids);

}
