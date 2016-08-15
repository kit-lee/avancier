package com.muses.avancier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.muses.avancier.model.WxUser;

public interface WxUserRepository extends CrudRepository<WxUser, Long> {

	public List<WxUser> findAll();
	
	public WxUser findByOpenId(String openId);
	
	@Query("select u from WxUser u where trans=0")  
	public List<WxUser> findAllNotTrans();
	
}
