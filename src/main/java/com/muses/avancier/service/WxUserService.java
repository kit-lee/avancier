package com.muses.avancier.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muses.avancier.controller.MainController;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.repository.WxUserRepository;

@Service
public class WxUserService {
	
	//private static final Log logger = LogFactory.getLog(WxUserService.class);

	@Autowired
	private WxUserRepository repository;

	@Transactional
	public boolean saveUser(WxUser user){
		repository.save(user);
		
		return true;
	}
	
	@Transactional
	public List<WxUser> listNotTrans(){
		List<WxUser> l = repository.findAllNotTrans();
		if(l.size()>0){
			for(int i=0;i<l.size();i++){
				WxUser u = l.get(i);
				u.setTrans(true);
				l.set(i, u);
			}
			repository.save(l);
		}
		
		return l;
	}
	
	@Transactional(readOnly=true)
	public List<WxUser> listAll(){
		return repository.findAll();
	}
	
	@Transactional
	public void deleteAll(){
		repository.deleteAll();
	}
	
	@Transactional
	public void deleteByOpenId(String openId){
		WxUser u = repository.findByOpenId(openId);
		repository.delete(u);
	}
	
	/**
	 * 返回所有未审核的微信签到记录
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<WxUser> listNotCheckedWxUser(Pageable pageable){
	    return repository.findByNotChecked(pageable);
	}
	
	/**
	 * 删除多个微信签到
	 * @param ids
	 */
	@Transactional
    public void delete(Long[] ids){
        for(Long id : ids)
            repository.delete(id);
    }
	
	/**
	 * 审核通过指定的微信签到信息
	 * @param ids
	 */
	@Transactional
	public void checkWxUser(Long[] ids){
	    List<WxUser> wxUsers = repository.findByIdIn(ids);
	    for(WxUser user : wxUsers){
            user.setChecked(true);
	    }
	    repository.save(wxUsers);
	}
}
