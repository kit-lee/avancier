package com.muses.avancier.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveUser(WxUser user){
		repository.save(user);
		
		return true;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
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
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<WxUser> listAll(){
		return repository.findAll();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteAll(){
		repository.deleteAll();
	}
}
