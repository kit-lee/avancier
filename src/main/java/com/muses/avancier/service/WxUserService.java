package com.muses.avancier.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muses.avancier.model.Activity;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.repository.ActivityRepository;
import com.muses.avancier.repository.WxUserRepository;
import com.muses.avancier.type.ActivityType;

/**
 * 处理签到用户信息的服务层spring bean
 * @author kit@muses.cc
 *
 */
@Service
public class WxUserService {
	
	private static final Log logger = LogFactory.getLog(WxUserService.class);

	@Autowired
	private WxUserRepository repository;
	
	@Autowired
	private ActivityRepository activityRepo;
	
	@Autowired
    private CacheManager cacheManager;

	@Transactional
	public boolean saveUser(WxUser user){
	    String type = user.getActivity().getType();
	    if(type==null)
	        type = ActivityType.checkin.name();
	    if(ActivityType.checkin.name().equals(type)){
	        long count = repository.countByActivityAndOpenId(user.getActivity(), user.getOpenId());
	        if(count>0)
	            return false;
	    }
	        
		repository.save(user);
		
		return true;
	}
	
	/**
	 * 返回未传输的签到信息
	 * @param activityId
	 * @return
	 */
	@Transactional
	public List<WxUser> listNotTrans(Long activityId){
	    Activity activity = activityRepo.findOne(activityId); 
		List<WxUser> l = repository.findAllNotTrans(activity);
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
	
	/**
	 * 返回未传输的签到信息
	 * （通过cache判断哪些未传输）
	 * @param activityId
	 * @return
	 */
	@Transactional(readOnly=true)
    public List<WxUser> listNotTransWithCache(Long activityId, String clientId){
        Activity activity = activityRepo.findOne(activityId); 
        List<WxUser> list = repository.findAllChecked(activity);
        
        Cache cache = cacheManager.getCache("trans-data");
        @SuppressWarnings("unchecked")
        Set<WxUser> transedUsers = cache.get(clientId, HashSet.class);
        if(transedUsers==null)
            transedUsers = new HashSet<>();
        List<WxUser> users = new ArrayList<>();
        for(WxUser user : list){
            if(!transedUsers.contains(user)){
                users.add(user);
                transedUsers.add(user);
            }else
                break;
        }
        cache.put(clientId, transedUsers);
        
        return users;
    }
	
	/**
	 * 返回活动所有已审的签到
	 * @param activityId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<WxUser> listAll(Long activityId){
	    Activity activity = activityRepo.findOne(activityId); 
		return repository.findAllChecked(activity);
	}
	
	/**
	 * 分页返回活动所有已审的签到
	 * @param activityId
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
    public Page<WxUser> listAll(Long activityId, Pageable pageable){
        Activity activity = activityRepo.findOne(activityId); 
        return repository.findAllChecked(activity, pageable);
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
	 * @param tags
	 *         自定义标签
	 */
	@Transactional
	public void checkWxUser(Long[] ids, String tags){
	    List<WxUser> wxUsers = repository.findByIdIn(ids);
	    for(WxUser user : wxUsers){
            user.setChecked(true);
            user.setTags(tags);
	    }
	    repository.save(wxUsers);
	}
}
